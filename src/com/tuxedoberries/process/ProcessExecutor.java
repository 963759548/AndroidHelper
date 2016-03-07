/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.process;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class ProcessExecutor implements IProcessStats {
    
    private Logger logger;
    private Process process;
    private InputStream inputStream;
    private InputStream errorInputStream;
    private OutputStream outputStream;
    
    public ProcessExecutor () {
        // Nothing here
    }
    
    public void executeProcess (String command) {
        executeProcess(command, null);
    }
    
    public void executeProcess (String command, String[] env) {
        createLogger();
        internalStop();
        try {
            Runtime runtime = Runtime.getRuntime();
            process = runtime.exec(command, env);
            createOutputStream();
            createInputStream();
            createErrorInputStream();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Exception running command: [%s] %s", command, ex.toString()));
        }
    }
    
    public boolean stop () {
        if(process == null){
            logger.log(Level.WARNING, "Process is not running.");
            return false;
        }
        internalStop();
        return true;
    }
    
    private void internalStop() {
        closeOutputStream ();
        closeInputStream();
        closeErrorInputStream();
        stopCurrentProcess();
    }
    
    public InputStream getInput () {
        return inputStream;
    }
    
    public InputStream getErrorInput () {
        return errorInputStream;
    }
    
    public OutputStream getOutput () {
        return outputStream;
    }
    
    @Override
    public boolean isRunning () {
        if(process == null)
            return false;
        return process.isAlive();
    }
    
    private void stopCurrentProcess () {
        if(process != null){
            process.destroy();
            process = null;
        }
    }
    
    private void createOutputStream () {
        if(outputStream == null){
            outputStream = process.getOutputStream();
        }
    }
    
    private void closeOutputStream () {
        if(outputStream != null){
            try {
                outputStream.close();
                outputStream = null;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, String.format("Exception closing Output Stream: %s", ex.toString()));
            }
        }
    }
    
    private void createInputStream () {
        if(inputStream == null){
            inputStream = process.getInputStream();
        }
    }
    
    private void createErrorInputStream () {
        if(errorInputStream == null){
            errorInputStream = process.getErrorStream();
        }
    }
    
    private void closeInputStream () {
        if(inputStream != null) {
            try {
                inputStream.close();
                inputStream = null;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, String.format("Exception closing Input Stream: %s", ex.toString()));
            }
        }
    }
    
    private void closeErrorInputStream () {
        if(errorInputStream != null) {
            try {
                errorInputStream.close();
                errorInputStream = null;
            } catch (IOException ex) {
                logger.log(Level.SEVERE, String.format("Exception closing Error Input Stream: %s", ex.toString()));
            }
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessExecutor.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
