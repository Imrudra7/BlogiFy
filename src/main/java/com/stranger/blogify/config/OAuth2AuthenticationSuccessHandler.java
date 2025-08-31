package com.stranger.blogify.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // You will need to create this utility class for generating JWTs
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${app.oauth2.redirect-uri.local}")
    private String localRedirectUri;

    @Value("${app.oauth2.redirect-uri.prod}")
    private String prodRedirectUri;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 1. Get authenticated user
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        // 2. Generate JWT
        String token = tokenProvider.generateToken(email);

        // 3. Detect environment
        String serverName = request.getServerName(); // safer than Origin header
        String targetUrl;

        if ("127.0.0.1".equals(serverName) || "localhost".equals(serverName)) {
            targetUrl = localRedirectUri + "?token=" + token;
        } else {
            targetUrl = prodRedirectUri + "?token=" + token;
        }
        System.out.println("Target  URL : "+targetUrl);
        // 4. Clear authentication attributes
        clearAuthenticationAttributes(request);

        // 5. Redirect to frontend
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}