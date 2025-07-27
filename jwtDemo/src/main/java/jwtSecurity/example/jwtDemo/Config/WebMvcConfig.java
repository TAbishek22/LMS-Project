package jwtSecurity.example.jwtDemo.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Autowired
    private AdminEmailInterceptor adminEmailInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // General interceptor for JWT validation
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login","/api/auth/register");

        // Admin-only routes
        registry.addInterceptor(adminEmailInterceptor)
        .addPathPatterns(
            "/api/books/addBooks",
            "/api/books/bulkUpload",
            "/api/books/{id}",
            "/api/books/deleteAllBooks",
            "/api/books/{id}",

            "/api/members",
            "/api/members/{memberId}",
            "/api/members/email/**",
            "/api/members/phone/**",
            "/api/members/status/**",
            "/api/members/name/**",

            "/api/borrowings/with-books",
            "/api/borrowings/with-member",
            "/api/borrowings/all",

            "/api/fines/**", // as already implemented
            "/api/notifications/**" // as already implemented
        );

    }
}