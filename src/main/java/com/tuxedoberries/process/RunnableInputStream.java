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

import com.tuxedoberries.process.interfaces.IProcessOutputListener;
import com.tuxedoberries.utils.ExceptionHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.tuxedoberries.process.interfaces.IProcessEventLog;

/**
 *
 * @author Juan Silva
 */
public class RunnableInputStream implements Runnable, IProcessEventLog {
    
    private InputStream inputStream;
    private BufferedReader reader;
    private Logger logger;
    private HashSet<IProcessOutputListener> listeners;
    public boolean printLog;
    private boolean reading = false;
    
    public RunnableInputStream () {
        this(null, false);
    }
    
    public RunnableInputStream (InputStream stream) {
        this(stream, false);
    }
    
    public RunnableInputStream (InputStream stream, boolean print) {
        printLog = print;
        listeners = new HashSet<IProcessOutputListener>();
        inputStream = stream;
        updateReader (stream);
    }
    
    public void enableLog(boolean enable) {
        printLog = enable;
    }
    
    public void setInputStream (InputStream stream) {
        inputStream = stream;
        updateReader (stream);
    }
    
    public boolean isReading () {
        return reading;
    }
    
    private synchronized void updateReader (InputStream stream) {
        if(inputStream == null)
            return;
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }
    
    private synchronized String readLine() throws IOException {
        if(reader == null)
            return null;
        
        String line = null;
        try{
            line = reader.readLine();
            reading = true;
        }catch(IOException e){
            // Input closed
            reader = null;
            inputStream = null;
        }
        return line;
    }
    
    public void run () {
        createLogger();
        String line;
        try {
            while(true) {
                if(inputStream == null) {
                    reading = false;
                    sleepMe();
                    continue;
                }
                if(reader == null){
                    reading = false;
                    sleepMe();
                    continue;
                }
                
                line = readLine();
                if(line == null){
                    reading = false;
                    sleepMe();
                    continue;
                }
                
                if(printLog){
                    System.out.println(String.format("%s", line));
                }
                raiseOutputEvent(line);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, String.format("Exception reading input: %s", ex.toString()));
            logger.log(Level.SEVERE, ExceptionHelper.getTrace(ex));
        }
    }
    
    public synchronized void subscribeOutput(IProcessOutputListener listener) {
        if(listeners.contains(listener))
            return;
        listeners.add(listener);
    }
    
    public synchronized void unsubscribeOutput(IProcessOutputListener listener) {
        if(!listeners.contains(listener))
            return;
        listeners.remove(listener);
    }
    
    private synchronized void raiseOutputEvent (String line) {
        for (IProcessOutputListener listener : listeners) {
            listener.onNewLine(line);
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), RunnableInputStream.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
    
    private void sleepMe () {
        try {
            Thread.sleep(33);
        } catch (InterruptedException ex) {
            //logger.log(Level.SEVERE, "Threas is interrupted", ex);
        }
    }
}
