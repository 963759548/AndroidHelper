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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class FileWriter {
    
    private Logger logger;
    private File _file;
    private java.io.FileWriter _fileWriter;
    private BufferedWriter _bufferedWriter;
    
    public FileWriter () {
        createLogger();
    }
    
    public void WriteFile(String path, String content){
        if(!CreateFile(path))
            return;
        
        if(!OpenFileWriter())
            return;
        
        if(!OpenBufferedWriter()){
            CloseFileWriter();
            CloseFile();
            return;
        }
        
        System.out.println(String.format("Writing data to: %s", _file.getPath()));
        WriteToFile(content);
        
        CloseBufferedWriter();
        CloseFileWriter();
        CloseFile();
    }
    
    private boolean CreateFile(String path){
        _file = new File(path);
        if(_file.exists()){
            logger.log(Level.WARNING, String.format("File already exist. Overwriting file [%s]", _file.getPath()));
            return true;
        }
        
        // Create Folder if does not exist
        File folder = _file.getParentFile();
        if(folder != null && !folder.exists()){
            boolean result = folder.mkdirs();
            if(!result){
                logger.log(Level.WARNING, "Could not create parent folder");
                return false;
            }
        }
        
        // Create File
        try {
            _file.createNewFile();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Could not create file[%s]: %s", path, ex.toString()));
            return false;
        }
        
        return true;
    }
    
    private void CloseFile(){
        _file = null;
    }
    
    private boolean OpenFileWriter(){
        try {
            _fileWriter = new java.io.FileWriter(_file.getAbsoluteFile());
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Could not create File Writer [%s]: %s", _file.getPath(), ex.toString()));
            return false;
        }
        
        return true;
    }
    
    private void CloseFileWriter(){
        if(_fileWriter == null){
            return;
        }
        
        try {
            _fileWriter.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Could not close File Writer: %s", ex.toString()));
        }
        
        _fileWriter = null;
    }
    
    private boolean OpenBufferedWriter(){
        _bufferedWriter = new BufferedWriter(_fileWriter);
        return true;
    }
    
    private boolean WriteToFile(String content){
        try {
            _bufferedWriter.write(content);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Could not write data to file: %s", ex.toString()));
        }
        return true;
    }
    
    private void CloseBufferedWriter(){
        if(_bufferedWriter == null)
            return;
        
        try {
            _bufferedWriter.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Could not close Buffered Writer: %s", ex.toString()));
        }
        
        _bufferedWriter = null;
    }
    
    private void createLogger() {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), FileReader.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
