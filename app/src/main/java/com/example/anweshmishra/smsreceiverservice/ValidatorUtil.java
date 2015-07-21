package com.example.anweshmishra.smsreceiverservice;
import java.util.regex.*;
/**
 * Created by anweshmishra on 27/06/15.
 */
public class ValidatorUtil {
    public static boolean validNumber(String value) {
        if(!value.isEmpty()) {
            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(value);
            return (matcher.find() && matcher.group().length() == value.length());
        }
        return false;
    }
    public static boolean validMobileNumber(String value) {
        if(validNumber(value)) {
            return (value.length()==10 && Integer.parseInt(""+value.charAt(0)) >= 7);
        }
        return false;
    }
}
