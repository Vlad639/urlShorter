package com.urlshorter.site.other;

public class CheckPassword {

    private static final int minPasswordLength = 7;
    private static final int maxPasswordLength = 15;
    private static final String specialSymbols = "%*)?@#$~";

    public static CheckPasswordResult checkPassword(String password){
        int passwordLength = password.length();
        char[] charPassword = password.toCharArray();
        char[] charSpecialSymb = specialSymbols.toCharArray();
        boolean containUpperCaseSymb = false;
        boolean containLowerCaseSymb = false;
        boolean containSpecialSymb = false;
        boolean containDigit = false;

        if (passwordLength < minPasswordLength) return new CheckPasswordResult(false, "Пароль слишком короткий, мин.длина " + minPasswordLength + " символов");
        if (passwordLength > maxPasswordLength) return new CheckPasswordResult(false, "Пароль слишком длиный, макс.длина " + maxPasswordLength + " символов");

        for (int i = 0; i < passwordLength; i++) {
            if (!containUpperCaseSymb && Character.isUpperCase(charPassword[i]))
                containUpperCaseSymb = true;

            if (!containLowerCaseSymb && Character.isLowerCase(charPassword[i]))
                containLowerCaseSymb = true;

            if (!containDigit && Character.isDigit(charPassword[i]))
                containDigit = true;
        }

        if (!containUpperCaseSymb) return new CheckPasswordResult(false, "Пароль должен содержать хотя бы одну букву верхнего регистра!");
        if (!containLowerCaseSymb) return new CheckPasswordResult(false, "Пароль должен содержать хотя бы одну букву нижнего регистра!");
        if (!containDigit) return new CheckPasswordResult(false, "Пароль должен содержать хотя бы одну цифру!");

        for (char passChar: charPassword) {
            if (containSpecialSymb) break;
            for (char specSymb: charSpecialSymb) {
                if (passChar == specSymb) {
                    containSpecialSymb = true;
                    break;
                }
            }
        }

        if (!containSpecialSymb) return new CheckPasswordResult(false, "Пароль должен содержать хотя бы один из смволов " + specialSymbols);

        return new CheckPasswordResult(true, "Пароль соответствует требованиям");
    }
}
