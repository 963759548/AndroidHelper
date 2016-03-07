/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.utils;

/**
 *
 * @author jsilva
 */
public class ExceptionHelper {
    
    public static String getTrace(Exception e) {
        StringBuilder builder = new StringBuilder();
        if(e == null)
            return null;
        
        StackTraceElement[] elements = e.getStackTrace();
        for(int i=0; i<elements.length; ++i) {
            builder.append(elements[i].toString());
            builder.append("\n");
        }
        
        return builder.toString();
    }
}
