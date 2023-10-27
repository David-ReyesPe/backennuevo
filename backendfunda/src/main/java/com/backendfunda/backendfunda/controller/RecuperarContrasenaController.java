package com.backendfunda.backendfunda.controller;

import com.backendfunda.backendfunda.dtos.DtoRegistro;
import com.backendfunda.backendfunda.model.Usuarios;
import com.backendfunda.backendfunda.repository.UsuariosRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Reemplaza con el origen de tu aplicación React
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/auth/recuperacion/")
public class RecuperarContrasenaController {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("enviar")
    public ResponseEntity<String> enviarCorreoRecuperacion(@RequestParam("correo") String correo) {
        // Buscar el usuario por su correo electrónico
        Optional<Usuarios> usuarioOptional = usuariosRepository.findByCorreo(correo);
        if (usuarioOptional.isPresent()) {
            Usuarios usuario = usuarioOptional.get();

            // Generar un token de recuperación de contraseña con una fecha de expiración de 15 minutos
            String token = UUID.randomUUID().toString();
            LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(15); // 15 minutos desde ahora
            usuario.setTokenRecuperacion(token);
            usuario.setTokenExpiracion(expirationTime); // Establece la fecha de expiración

            usuariosRepository.save(usuario);

            // Enviar un correo electrónico con un botón de recuperación de contraseña
            String resetLink = "http://localhost:5173/reset-password?token=" + token;
            String htmlMessage = "<html><body style='text-align: center;'>";
            htmlMessage += "<h1 style='color: #9A1B76;'>Recuperación de Contraseña</h1>";
            htmlMessage += "<p style='color: #9A1B76; font-weight: bold;'>Este token es obligatorio para poder cambiar la contraseña:</p>";
            htmlMessage += "<p style='font-size: 20px; background-color: #EAEAEA; padding: 10px;'>" + token + "</p>";
            htmlMessage += "<h1 style='color: #9A1B76;'>Para restablecer tu contraseña, haz clic en el siguiente botón:</h1>";
            htmlMessage += "<a href=\"" + resetLink + "\">";
            htmlMessage += "<button style=\"background-color: #9A1B76; color: white; padding: 10px 20px; border: none; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\">Restablecer Contraseña</button>";
            htmlMessage += "</a>";
            htmlMessage += "</body></html>";

            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(correo);
                helper.setSubject("Recuperación de Contraseña");
                helper.setText(htmlMessage, true); // Indicar que el contenido es HTML
                System.out.println("Enviando correo...");
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo");
            }

            return ResponseEntity.ok("Correo de recuperación enviado");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetearContrasena(@RequestParam("token") String token, @RequestParam("password") String nuevaContrasena) {
        // Validar datos de entrada
        if (token == null || nuevaContrasena == null || token.isEmpty() || nuevaContrasena.isEmpty()) {
            return ResponseEntity.badRequest().body("Token y nueva contraseña son obligatorios");
        }
        try {
            // Buscar el usuario por el token de recuperación
            Optional<Usuarios> usuarioOptional = usuariosRepository.findByTokenRecuperacion(token);
            if (usuarioOptional.isPresent()) {
                Usuarios usuario = usuarioOptional.get();

                // Verifica si el token ha expirado
                if (usuario.getTokenExpiracion() != null) {
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isBefore(usuario.getTokenExpiracion())) {
                        // El token es válido, procede con el restablecimiento de contraseña
                        // Aquí puedes establecer la nueva contraseña y realizar otras acciones necesarias
                        usuario.setPassword(nuevaContrasena);
                        // Limpia el token de recuperación ya que ya no es necesario
                        usuario.setTokenRecuperacion(null);
                        usuariosRepository.save(usuario);
                        return ResponseEntity.ok("Contraseña restablecida con éxito");
                    } else {
                        // El token ha expirado
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token ha expirado");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El token no tiene fecha de expiración");
                }
            } else {
                // Token de recuperación no válido
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de recuperación no válido");
            }
        } catch (Exception e) {
            // Manejar excepciones apropiadamente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al restablecer la contraseña");
        }
    }
/*
    @PostMapping("enviar")
    public ResponseEntity<String> enviarCorreoRecuperacion(@RequestParam("correo") String correo) {
        // Buscar el usuario por su correo electrónico
        Optional<Usuarios> usuarioOptional = usuariosRepository.findByCorreo(correo);
        if (usuarioOptional.isPresent()) {
            Usuarios usuario = usuarioOptional.get();

            // Generar un token de recuperación de contraseña y guardar en el usuario
            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacion(token);
            usuariosRepository.save(usuario);

            // Enviar un correo electrónico con un botón de recuperación de contraseña
            String resetLink = "http://localhost:5173/reset-password?token=" + token;
            String htmlMessage = "<html><body style='text-align: center;'>";
            htmlMessage += "<h1 style='color: #9A1B76;'>Recuperación de Contraseña</h1>";
            htmlMessage += "<p style='color: #9A1B76; font-weight: bold;'>Este token es obligatorio para poder cambiar la contraseña:</p>";
            htmlMessage += "<p style='font-size: 20px; background-color: #EAEAEA; padding: 10px;'>" + token + "</p>";
            htmlMessage += "<p>Para restablecer tu contraseña, haz clic en el siguiente botón:</p>";
            htmlMessage += "<a href=\"" + resetLink + "\">";
            htmlMessage += "<button style=\"background-color: #9A1B76; color: white; padding: 10px 20px; border: none; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer;\">Restablecer Contraseña</button>";
            htmlMessage += "</a>";
            htmlMessage += "</body></html>";

            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(correo);
                helper.setSubject("Recuperación de Contraseña");
                helper.setText(htmlMessage, true); // Indicar que el contenido es HTML
                System.out.println("Enviando correo...");
                javaMailSender.send(message);
            } catch (MessagingException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo");
            }

            return ResponseEntity.ok("Correo de recuperación enviado");

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @PostMapping("reset-password")
    public ResponseEntity<String> resetearContrasena(@RequestParam("token") String token, @RequestParam("password") String nuevaContrasena) {
        // Validar datos de entrada
        if (token == null || nuevaContrasena == null || token.isEmpty() || nuevaContrasena.isEmpty()) {
            return ResponseEntity.badRequest().body("Token y nueva contraseña son obligatorios");
        }
        try {
            // Buscar el usuario por el token de recuperación
            Optional<Usuarios> usuarioOptional = usuariosRepository.findByTokenRecuperacion(token);
            if (usuarioOptional.isPresent()) {
                Usuarios usuario = usuarioOptional.get();

                // Aplicar políticas de seguridad de contraseñas aquí (por ejemplo, hashing)

                // Actualizar la contraseña y borrar el token de recuperación
                usuario.setPassword(nuevaContrasena);
                usuario.setTokenRecuperacion(null);
                usuariosRepository.save(usuario);

                return ResponseEntity.ok("Contraseña restablecida con éxito");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Token de recuperación no válido");
            }
        } catch (Exception e) {
            // Manejar excepciones apropiadamente
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al restablecer la contraseña");
        }
    }*/
}