package goorm.nuto.Nuto.Dto;

import goorm.nuto.Nuto.Entity.Member;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Member member;

    // 권한 반환 (ex: ROLE_USER, ROLE_ADMIN)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    // 패스워드 반환 (Spring Security 로그인 시 비교용)
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // 사용자 이름 반환 → 로그인에 사용할 유일 식별자 (userid 사용)
    @Override
    public String getUsername() {
        return member.getUserid();  // 또는 member.getEmail() 사용 가능
    }

    //  계정 만료 여부 (true = 만료되지 않음)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //  계정 잠김 여부 (true = 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //  비밀번호 만료 여부 (true = 만료되지 않음)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //  계정 활성화 여부 (true = 활성화 상태)
    @Override
    public boolean isEnabled() {
        return true;  // 이메일 인증 완료 여부 등을 조건으로 설정 가능
    }
}