package com.chrono.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class CryptoUtil {
    private static final String ALGORITHM = "AES";

    @Value("${crypto.aes-key}")
    private String aesKey;

    //암호화
    public String encrypt(String plainText){
        try{
            SecretKey secretKey = new SecretKeySpec(aesKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        }catch (Exception e){
            throw new RuntimeException("암호화 실패", e);
        }
    }

    //복호화
    public String decrypt(String cipherText) {
        try{
            SecretKeySpec secretKey = new SecretKeySpec(aesKey.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decoded = Base64.getDecoder().decode(cipherText);
            byte[] decrypted  = cipher.doFinal(decoded);

            return new String(decrypted);
        }catch (Exception e){
            throw new RuntimeException("복호화 중 오류 발생");
        }
    }

}
