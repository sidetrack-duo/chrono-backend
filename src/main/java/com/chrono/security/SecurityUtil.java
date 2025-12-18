package com.chrono.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    //로그인한 사용자 userId가져오기
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null
                || authentication.getPrincipal() == null
                || authentication.getPrincipal().equals("anonymousUser")){
            throw new RuntimeException("로그인 상태 아님");
        }

        if(authentication.getPrincipal() instanceof  CustomUserPrincipal principal){
            return  principal.getUser().getUserId();
        }

        throw new RuntimeException("인증 정보 타입 오류");

    }
    //이메일로 가져오기
    public static String getCurrentEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication.getPrincipal() == null
                || authentication.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("로그인 상태 아님");
        }

        if (authentication.getPrincipal() instanceof CustomUserPrincipal principal) {
            return principal.getUser().getEmail();
        }

        return authentication.getName();
    }
}
