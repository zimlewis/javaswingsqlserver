package com.zimlewis;

import java.util.regex.Pattern;

public class InputValidation {
    
    public static boolean isNumeric(String str) { 
        try {  
            Double.parseDouble(str);  
            return true;
        } catch(NumberFormatException e){  
            return false;  
        }  
    }

    public static boolean isNameValid(String name){
        String nameRegex = "^[a-zA-Z]+(?:\\s[a-zA-Z]+)+$";;
                              
        Pattern pat = Pattern.compile(nameRegex);
        if (name == null)
            return false;
        return pat.matcher(name).matches();
        
    }

    public static boolean isEmailValid(String email){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                            "[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                            "A-Z]{2,7}$";
                              
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
        
    }

    public static boolean isPhoneValid(String n){
        String Regex = "^0\\d{9}$";
                              
        Pattern pat = Pattern.compile(Regex);
        if (n == null)
            return false;
        return pat.matcher(n).matches();
        
    }   
}
