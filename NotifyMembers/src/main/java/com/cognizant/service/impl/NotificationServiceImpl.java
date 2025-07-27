package com.cognizant.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.cognizant.dto.NotificationDTO;
import com.cognizant.entity.Notification;
import com.cognizant.exception.NotificationException;
import com.cognizant.repository.NotificationRepository;
import com.cognizant.service.NotificationService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final NotificationRepository repository;
    private final ModelMapper modelMapper;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendNotification(NotificationDTO dto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(dto.getEmail());
            helper.setSubject("📚 Library Notification");

            // Enhanced HTML email content with styling
            String htmlContent = "<html>"
            	    + "<body style='margin: 0; padding: 0; font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif; background-color: #f4f6f8;'>"
            	    + "<div style='max-width: 600px; margin: 40px auto; background: #ffffff; border-radius: 12px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.1);'>"
            	    + "<div style='background: linear-gradient(to right, #007bff, #00c6ff); padding: 20px 30px;'>"
            	    + "<h2 style='color: #ffffff; margin: 0; text-align: center;'>Library Notification</h2>"
            	    + "</div>"
            	    + "<div style='padding: 30px;'>"
            	    + "<p style='font-size: 16px; color: #333333; line-height: 1.6;'>" + dto.getMessage() + "</p>"
            	    + "<div style='margin: 30px 0; border-top: 1px solid #e0e0e0;'></div>"
            	    + "<p style='text-align: center; font-size: 14px; color: #555;'>If you have any questions, feel free to reach out to us.</p>"
            	    + "<p style='text-align: center; font-weight: 600; font-size: 15px; color: #333;'>"
            	    + "Best regards,<br>Library Management System</p>"
            	    + "</div>"
            	    + "<div style='background-color: #f0f0f0; text-align: center; padding: 10px 20px; font-size: 12px; color: #888;'>"
            	    + "© 2025 Library Management System. All rights reserved."
            	    + "</div>"
            	    + "</div>"
            	    + "</body>"
            	    + "</html>";

            helper.setText(htmlContent, true); // Enable HTML email content

            mailSender.send(message);

            // Save notification record in database
            Notification notification = Notification.builder()
                    .memberId(dto.getMemberId())
                    .email(dto.getEmail())
                    .message(dto.getMessage())
                    .dateSent(LocalDateTime.now())
                    .build();

            repository.save(notification);
            log.info("HTML Notification sent to member {}", dto.getMemberId());
        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", dto.getEmail(), e.getMessage());
            throw new NotificationException("Failed to send email notification.");
        }
    }

    @Override
    public List<NotificationDTO> getNotificationsByMember(Long memberId) {
        return repository.findByMemberId(memberId).stream()
                .map(notification -> modelMapper.map(notification, NotificationDTO.class))
                .collect(Collectors.toList());
    }
}
