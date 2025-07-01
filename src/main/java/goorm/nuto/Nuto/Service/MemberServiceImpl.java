package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.*;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.Entity.Role;
import goorm.nuto.Nuto.Exception.*;
import goorm.nuto.Nuto.Repository.MemberRepository;
import goorm.nuto.Nuto.jwt.JwtToken;
import goorm.nuto.Nuto.jwt.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RedisService redisService;
    private final EmailService emailService;

    private static final String AUTH_CODE_PREFIX = "AuthCode:";
    private static final String VERIFIED_EMAIL_PREFIX = "VerifiedEmail:";

    @Value("${spring.mail.auth-code-expiration-millis:180000}") // 기본 3분
    private long authCodeExpirationMillis;

    @Override
    public JwtToken login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUserId(), request.getPassword())
        );
        return jwtTokenProvider.generateToken(authentication);
    }

    @Override
    public JwtToken reissue(ReissueRequestDto request) {
        jwtTokenProvider.validateToken(request.getRefreshToken());
        return jwtTokenProvider.reissueToken(request.getAccessToken(), request.getRefreshToken());
    }

    @Override
    public void signup(SignUpRequestDto request) {
        // 중복 이메일 확인
        if (memberRepository.findByUserid(request.getUserid()).isPresent()) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 이메일 인증 확인
        String verified = redisService.getValues(VERIFIED_EMAIL_PREFIX + request.getUserid())
                .orElseThrow(() -> new NotVerifiedEmailException("이메일 인증이 필요합니다."));

        if (!"true".equals(verified)) {
            throw new NotVerifiedEmailException("이메일 인증이 완료되지 않았습니다.");
        }

        // 회원 생성
        Member member = Member.builder()
                .userid(request.getUserid())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .age(request.getAge())
                .job(request.getJob())
                .role(Role.USER)
                .build();

        memberRepository.save(member);
    }

    @Override
    public void sendCodeToEmail(String toEmail) {
        checkDuplicatedEmail(toEmail);
        try {
            String authCode = emailService.sendEmail(toEmail);
            redisService.setValues("AuthCode:" + toEmail, authCode, Duration.ofMillis(authCodeExpirationMillis)); // 👈 저장
        } catch (Exception e) {
            log.error("이메일 전송 실패", e);
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "이메일 전송에 실패했습니다.");
        }
    }

    @Override
    public void verifiedCode(VerifiedRequestDto dto) {
        String email = dto.getEmail();
        String code = dto.getCode();

        // 코드 조회
        String storedCode = redisService.getValues(AUTH_CODE_PREFIX + email)
                .orElseThrow(() -> new VerificationFailedException("인증코드가 존재하지 않습니다. 다시 요청해주세요."));

        log.info("인증 확인 - 입력 코드: {}", code);
        log.info("인증 확인 - 저장된 코드: {}", storedCode);

        if (!storedCode.equals(code)) {
            throw new VerificationFailedException("인증코드가 일치하지 않습니다.");
        }

        // 인증 완료 처리
        redisService.setValues(VERIFIED_EMAIL_PREFIX + email, "true");
    }

    @Override
    public void logout(String email) {
        Optional<String> refreshToken = redisService.getValues("RT:" + email);
        if (refreshToken.isEmpty()) {
            throw new InvalidTokenException("로그인된 상태가 아닙니다.");
        }
        redisService.deleteValues("RT:" + email);
    }

    @Override
    public Member findMemberByNickname(String name) {
        return memberRepository.findByName(name).orElse(null);
    }

    private void checkDuplicatedEmail(String email) {
        if (memberRepository.findByUserid(email).isPresent()) {
            throw new DuplicateEmailException(email);
        }
    }

}