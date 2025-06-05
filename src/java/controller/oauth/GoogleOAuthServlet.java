package controller.oauth;

import config.OAuthConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/oauth/google")
public class GoogleOAuthServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String authURL = "https://accounts.google.com/o/oauth2/v2/auth?" +
            "client_id=" + OAuthConfig.GOOGLE_CLIENT_ID +
            "&redirect_uri=" + OAuthConfig.GOOGLE_REDIRECT_URI +
            "&response_type=code" +
            "&scope=email profile";
        
        response.sendRedirect(authURL);
    }
}