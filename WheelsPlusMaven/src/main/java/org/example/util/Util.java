package org.example.util;

public class Util {
    public  boolean conferirEmail(String email){
        if(email.isEmpty()){
            return false;
        }
        if(!email.contains("@")){
            return false;
        }
        if(!email.contains(".")){
            return false;
        }
        return true;
    }
}
