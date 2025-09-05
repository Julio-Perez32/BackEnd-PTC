package ptc2025.backend.Config.App;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ptc2025.backend.Utils.JWTUtils;
import ptc2025.backend.Utils.JwtCookieAuthFilter;

@Configuration
public class AppConfig {
    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils) {
        return new JwtCookieAuthFilter(jwtUtils);
    }
}
