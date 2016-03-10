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
package com.tuxedoberries.process;

import com.tuxedoberries.process.interfaces.IProcessStats;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class ProcessExecutor implements IProcessStats {
    
    private Logger logger;
    private Process process;
    private InputStream inputStream;
    private InputStream errorInputStream;
    private OutputStream outputStream;
    private String currentProcess;
    private final Stack<String> lastProcess;
    
    public ProcessExecutor () {
        lastProcess = new Stack<>();
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
            lastProcess.add(command);
            currentProcess = command;
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
    
    @Override
    public String getCurrentProcess() {
        if(process == null || !process.isAlive())
            return null;
        return currentProcess;
    }
    
    @Override
    public String getLastProcess() {
        return lastProcess.peek();
    }
    
    private void stopCurrentProcess () {
        if(process != null){
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, ex.toString());
            }
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
