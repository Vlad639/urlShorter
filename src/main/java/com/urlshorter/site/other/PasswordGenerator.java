package com.urlshorter.site.other;

import java.security.SecureRandom;
import java.util.*;

public class PasswordGenerator {

    private static String getRandomStringFromDiapason(int start, int fin, int stringSize){

        return new SecureRandom().ints(start, fin + 1)
                .limit(stringSize)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private static List<Character> toCharacterList(String str){
        int strLength = str.length();
        List<Character> result = new ArrayList<>();

        for (int i = 0; i < strLength; i++)
            result.add(str.charAt(i));

        return result;
    }

    public static String generatePassword(){

        String specialSymbols = PasswordSettings.specialSymbols;
        StringBuilder specialSymbForPassword = new StringBuilder();
        int specialSymbLength = specialSymbols.length();
        int lowerCaseLettersNumbers = 4;
        int upperCaseLetterNumbers = 4;
        int digitNumbers = 3;
        int specialSymbolsNumbers = 3;

        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < specialSymbolsNumbers; i++) {
            specialSymbForPassword.append(specialSymbols.charAt(secureRandom.nextInt(specialSymbLength)));
        }

        StringBuilder generatedPassword = new StringBuilder(getRandomStringFromDiapason(97, 122, lowerCaseLettersNumbers) +
                getRandomStringFromDiapason(65, 90, upperCaseLetterNumbers) +
                getRandomStringFromDiapason(48, 57, digitNumbers) +
                specialSymbForPassword);

        List<Character> passwordCollectin = toCharacterList(generatedPassword.toString());
        Collections.shuffle(passwordCollectin);

        generatedPassword = new StringBuilder();
        for (Character elem: passwordCollectin)
            generatedPassword.append(elem.toString());

        return generatedPassword.toString();
    }
}
