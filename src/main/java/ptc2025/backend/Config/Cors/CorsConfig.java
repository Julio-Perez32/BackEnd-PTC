package ptc2025.backend.Config.Cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 🔐 Si usas cookies, debe ser true y NO usar "*" como origen
        config.setAllowCredentials(true);

        // 👇 Orígenes que SÍ usas (Capacitor/WebView y tu web)
        config.setAllowedOriginPatterns(Arrays.asList(
                "https://localhost",        // WebView de Capacitor
                "http://localhost:*",       // dev web con puerto
                "https://localhost:*",      // dev web https con puerto
                "http://10.0.2.2:*",        // emulador Android hacia host
                "capacitor://localhost",    // Capacitor
                "ionic://localhost",        // Ionic (si aplica)
                "https://sapientiae-web.vercel.app",
                "https://*.vercel.app"
        ));

        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        config.setAllowedHeaders(Arrays.asList(
                "Origin","Content-Type","Accept","Authorization","X-Requested-With"
        ));
        config.setExposedHeaders(Arrays.asList(
                "Authorization","Location","Set-Cookie"
        ));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a todo
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

