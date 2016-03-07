/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class FileReader {
    
    private Logger logger;
    
    public FileReader () {
        createLogger();
    }
    
    public boolean fileExist (String path) {
        // Check Path
        if(path == null || path.isEmpty()){
            return false;
        }
        
        // Check File
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        
        return true;
    }
    
    public String readFile(String path) {
        // Check Path
        if(path == null || path.isEmpty()){
            logger.log(Level.WARNING, "Empty path");
            return null;
        }
        
        // Check File
        File file = new File(path);
        if(!file.exists()){
            logger.log(Level.SEVERE, "File does not exist: %s", path);
            return null;
        }
        
        // Read all lines
        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get(path), Charset.defaultCharset());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error Reading File: %s", ex.toString());
        }
        
        // Check null lines
        if(lines == null)
            return null;
        
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<lines.size(); ++i){
            builder.append(lines.get(i));
        }
        
        return builder.toString();
    }
    
    private void createLogger() {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), FileReader.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
