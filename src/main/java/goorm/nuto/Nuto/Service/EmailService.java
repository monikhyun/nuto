package goorm.nuto.Nuto.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String configEmail;

    private static final String AUTH_CODE_PREFIX = "AuthCode:";

    // 인증 코드 생성
    private String createdCode() {
        int leftLimit = 48; // '0'
        int rightLimit = 122; // 'z'
        int targetLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // 숫자 + 알파벳
                .limit(targetLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    // 템플릿에 코드 삽입
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    // 메일 본문 생성 및 Redis 저장
    private MimeMessage createEmailForm(String email, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);
        message.setSubject("안녕하세요 Nuto 회원가입 인증번호입니다.");
        message.setFrom(configEmail);
        message.setText(setContext(authCode), "utf-8", "html");
        return message;
    }

    // 이메일 전송
    public String sendEmail(String toEmail) throws MessagingException {
        if (redisService.getValues("AuthCode:" + toEmail).isPresent()) {
            redisService.deleteValues("AuthCode:" + toEmail);
        }

        String authCode = createdCode(); // 영문+숫자 조합

        MimeMessage emailForm = createEmailForm(toEmail, authCode);
        mailSender.send(emailForm);

        return authCode; // 👈 생성한 인증 코드를 반환
    }

    // 인증 코드 검증
    public Boolean verifyEmailCode(String email, String code) {
        String storedCode = redisService.getValues(AUTH_CODE_PREFIX + email).orElse(null);
        return storedCode != null && storedCode.equals(code);
    }

    // SHA-256 해시 기반 ID 생성
    public String makeMemberId(String email) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(email.getBytes());
        md.update(LocalDateTime.now().toString().getBytes());
        return Arrays.toString(md.digest());
    }
}