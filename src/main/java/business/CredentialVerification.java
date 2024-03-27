package business;

import java.util.regex.Pattern;

public class CredentialVerification {
    private static final String passwordRegExpression= "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=*/])"
            + ".{8,20}$";

    /**
     * Validates the password, password must contain at least 8 characters and at most 20 characters.
     * must contain at least one digit.
     * must contain at least one upper case alphabet.
     * must contain at least one lower case alphabet.
     * must contain at least one special character which includes.
     * @param password, the password to be validated
     *  @return true if email is valid and false for otherwise
     * **/
    public static boolean checkPassword(String password){
        return Pattern.compile(passwordRegExpression).matcher(password).matches();
    }

}
