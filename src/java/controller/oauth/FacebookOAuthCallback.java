package controller.oauth;

import config.OAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import entity.User;
import dal.UserDAO_Long;
import entity.Role;
import entity.Status;
import java.security.SecureRandom;

@WebServlet("/login/oauth2/code/facebook")
public class FacebookOAuthCallback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null) {
            try {
                // 1. Exchange code for access token
                String tokenResponse = getAccessToken(code);
                JSONObject tokenJson = new JSONObject(tokenResponse);
                String accessToken = tokenJson.getString("access_token");

                // 2. Get user info from Facebook
                String userInfo = getUserInfo(accessToken);
                JSONObject userJson = new JSONObject(userInfo);

                // 3. Get or create user
                String email = userJson.getString("email");
                String name = userJson.getString("name");

                UserDAO_Long userDao = new UserDAO_Long();
                User user = userDao.getUserByEmail(email);

                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    user.setFullName(name);
                    user.setRole(Role.TRAVELER);
                    user.setStatus(Status.ACTIVE);
                    user.setPasswordHash(generateRandomPassword(12));

                    boolean created = userDao.createUser(user);
                    if (!created) {
                        throw new ServletException("Could not create new user");
                    }
                }

                // 4. Set session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("authenticated", true);

                // 5. Redirect to home
                response.sendRedirect(request.getContextPath() + "/home");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/login?error=oauth_failed");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/login?error=no_code");
        }
    }

    private String getAccessToken(String code) throws IOException {
        String tokenUrl = String.format("https://graph.facebook.com/v18.0/oauth/access_token"
                + "?client_id=%s"
                + "&client_secret=%s"
                + "&redirect_uri=%s"
                + "&code=%s",
                OAuthConfig.FACEBOOK_APP_ID,
                OAuthConfig.FACEBOOK_APP_SECRET,
                OAuthConfig.FACEBOOK_REDIRECT_URI,
                code);

        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private String getUserInfo(String accessToken) throws IOException {
        String userInfoUrl = String.format("https://graph.facebook.com/me"
                + "?fields=id,name,email"
                + "&access_token=%s",
                accessToken);

        URL url = new URL(userInfoUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }
}
