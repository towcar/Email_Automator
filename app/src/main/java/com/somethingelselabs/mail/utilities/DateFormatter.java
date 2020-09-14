package com.somethingelselabs.mail.utilities;

/**
 * Created by Carson on 8/18/2020.
 */
public class DateFormatter {

    public static String remove_parenthesis(String input_string, String parenthesis_symbol){
        // removing parenthesis and everything inside them, works for (),[] and {}
        if(parenthesis_symbol.contains("[]")){
            return input_string.replaceAll("\\s*\\[[^\\]]*\\]\\s*", " ");
        }else if(parenthesis_symbol.contains("{}")){
            return input_string.replaceAll("\\s*\\{[^\\}]*\\}\\s*", " ");
        }else{
            return input_string.replaceAll("\\s*\\([^\\)]*\\)\\s*", " ");
        }
    }
}
