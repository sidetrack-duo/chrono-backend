package com.chrono.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EmailTemplate {
    public static String verification(String code){
        String html = loadTemplate("mail/templates/verification.html");
        return html.replace("{{CODE}}", code);
    }

    private static String loadTemplate(String path){
        try {
            ClassPathResource resource = new ClassPathResource(path);
            return StreamUtils.copyToString(
                    resource.getInputStream(),
                    StandardCharsets.UTF_8
            );
        }catch (IOException e){
            throw new RuntimeException("이메일 템플릿 로드 실패", e);
        }
    }
}
