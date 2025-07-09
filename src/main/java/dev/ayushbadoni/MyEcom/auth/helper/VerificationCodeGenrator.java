package dev.ayushbadoni.MyEcom.auth.helper;

import java.util.Random;

public class VerificationCodeGenrator {
    public static String generateCode(){
        Random random = new Random();
        int code = 10000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
