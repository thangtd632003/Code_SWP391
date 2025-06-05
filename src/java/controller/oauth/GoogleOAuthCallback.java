package controller.oauth;

import config.OAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import entity.User;
import dal.UserDAO_Long;
import entity.Role;
import entity.Status;
import java.security.SecureRandom;

@WebServlet("/login/oauth2/code/google")
public class GoogleOAuthCallback extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null) {
            try {
                // 1. Đổi code lấy token
                String tokenResponse = getTokenFromGoogle(code);
                JSONObject tokenJson = new JSONObject(tokenResponse);
                String accessToken = tokenJson.getString("access_token");

                // 2. Lấy thông tin user từ Google
                String userInfo = getUserInfo(accessToken);
                JSONObject userJson = new JSONObject(userInfo);

                // 3. Tạo hoặc cập nhật user trong database
                String email = userJson.getString("email");
                String name = userJson.getString("name");
                String picture = userJson.getString("picture");

                UserDAO_Long userDao = new UserDAO_Long();
                User user = userDao.getUserByEmail(email);

                if (user == null) {
                    // Tạo user mới với password ngẫu nhiên
                    String randomPassword = generateRandomPassword(12);
                    
                    user = new User();
                    user.setEmail(email);
                    user.setFullName(name);
                    user.setRole(Role.TRAVELER);
                    user.setStatus(Status.ACTIVE);
                    user.setPasswordHash(randomPassword); // Đặt password ngẫu nhiên
                    
                    // Thêm user vào database
                    boolean created = userDao.createUser(user);
                    if (!created) {
                        throw new ServletException("Không thể tạo tài khoản mới");
                    }
                }

                // 4. Tạo session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("authenticated", true); // Add authentication flag

                // 5. Chuyển hướng về trang chủ - Use sendRedirect instead of forward
                response.sendRedirect(request.getContextPath() + "/dasshboard"); // Change to your actual home page URL

            } catch (Exception e) {
                // Log the error properly
                e.printStackTrace();
                // Redirect to login page with error
                if (!response.isCommitted()) {
                    response.sendRedirect(request.getContextPath() + "/login?error=oauth_failed");
                }
            }
        } else {
            // Redirect to login page if no code
            if (!response.isCommitted()) {
                response.sendRedirect(request.getContextPath() + "/login?error=no_code");
            }
        }
    }

    private String getTokenFromGoogle(String code) throws IOException {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        URL url = new URL(tokenUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String params = String.format(
                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
                code,
                OAuthConfig.GOOGLE_CLIENT_ID,
                OAuthConfig.GOOGLE_CLIENT_SECRET,
                OAuthConfig.GOOGLE_REDIRECT_URI);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = params.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }

        return response.toString();
    }

    private String getUserInfo(String accessToken) throws IOException {
    String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";
    URL url = new URL(userInfoUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    
    // Set request method and headers
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Authorization", "Bearer " + accessToken);
    conn.setRequestProperty("Accept", "application/json");
    
    // Read the response
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