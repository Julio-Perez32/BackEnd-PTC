package ptc2025.backend.Utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String email, String tempPassword) {
        String subject = "Bienvenido a Sapientiae";
        String body = "Hola " + email + ",\n\n" +
                "Tu cuenta ha sido creada exitosamente.\n" +
                "Tus credenciales son:\n" +
                "Correo: " + to + "\n" +
                "Contraseña: " + tempPassword + "\n\n" +
                "Saludos,\nEquipo de Sapientiae.";

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // `true` si quieres enviar HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de bienvenida a " + to, e);
        }
    }
}
