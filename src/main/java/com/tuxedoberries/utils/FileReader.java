/*
 * Copyright (C) 2016 Juan Silva <juanssl@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
 * @author Juan Silva
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
