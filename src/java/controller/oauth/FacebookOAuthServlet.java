package controller.oauth;

import config.OAuthConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet("/oauth/facebook")
public class FacebookOAuthServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Tạo URL đăng nhập Facebook với các tham số cần thiết
        String redirectUri = OAuthConfig.FACEBOOK_REDIRECT_URI;
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        
        String authUrl = String.format("https://www.facebook.com/v18.0/dialog/oauth" +
                "?client_id=%s" +
                "&redirect_uri=%s" +
                "&scope=email,public_profile",
                OAuthConfig.FACEBOOK_APP_ID,
                encodedRedirectUri);
        
        // Chuyển hướng người dùng đến trang đăng nhập Facebook
        response.sendRedirect(authUrl);
    }
}