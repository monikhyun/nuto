package goorm.nuto.Nuto.Service;

import goorm.nuto.Nuto.Dto.LoginRequestDto;
import goorm.nuto.Nuto.Dto.ReissueRequestDto;
import goorm.nuto.Nuto.Dto.SignUpRequestDto;
import goorm.nuto.Nuto.Dto.VerifiedRequestDto;
import goorm.nuto.Nuto.Entity.Member;
import goorm.nuto.Nuto.jwt.JwtToken;
import org.springframework.stereotype.Service;

@Service
public interface MemberService {
    JwtToken login(LoginRequestDto request);
    JwtToken reissue(ReissueRequestDto request);
    void signup(SignUpRequestDto request);
    void sendCodeToEmail(String toEmail);

    void verifiedCode(VerifiedRequestDto verifiedRequestDto);

    void logout(String email);

    Member findMemberByNickname(String username);

}
