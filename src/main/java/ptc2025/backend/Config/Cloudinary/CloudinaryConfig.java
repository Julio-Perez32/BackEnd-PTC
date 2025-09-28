package ptc2025.backend.Config.Cloudinary;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean(name = "cloudinaryImg")
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME"));
        config.put("api_key", System.getenv("CLOUDINARY_API_KEY"));
        config.put("api_secret", System.getenv("CLOUDINARY_API_SECRET"));

        return new Cloudinary(config);
    }

    @Bean(name = "cloudinaryDocs")
    public Cloudinary cloudinary2(){
        Map<String, String> config = new HashMap<>();

        config.put("cloud_name", System.getenv("CLOUDINARY_CLOUD_NAME_2"));
        config.put("api_key", System.getenv("CLOUDINARY_API_KEY_2"));
        config.put("api_secret", System.getenv("CLOUDINARY_API_SECRET_2"));

        return new Cloudinary(config);
    }
}
