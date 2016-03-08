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

import com.tuxedoberries.utils.ExceptionHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class RunnableInputStream implements Runnable, IProcessLog {
    
    private InputStream inputStream;
    private BufferedReader reader;
    private Logger logger;
    private StringBuilder historyBuilder;
    private HashSet<IProcessOutputListener> listeners;
    private IProcessStats stats;
    public boolean saveHistory;
    public boolean printLog;
    
    public RunnableInputStream () {
        this(null, true, false);
    }
    
    public RunnableInputStream (InputStream stream) {
        this(stream, true, false);
    }
    
    public RunnableInputStream (InputStream stream, boolean history, boolean print) {
        saveHistory = history;
        printLog = print;
        listeners = new HashSet<>();
        inputStream = stream;
        updateReader (stream);
    }
    
    public void setProcessStats (IProcessStats stats) {
        this.stats = stats;
    }
    
    public void enableLog(boolean enable) {
        printLog = enable;
    }
    
    public void setInputStream (InputStream stream) {
        inputStream = stream;
        updateReader (stream);
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
        }catch(IOException e){
            // Input closed
            reader = null;
            inputStream = null;
        }

        return line;
    }
    
    @Override
    public void run () {
        createLogger();
        historyBuilder = new StringBuilder();
        String line;
        try {
            while(true) {
                if(stats == null || !stats.isRunning()) {
                    sleepMe();
                    continue;
                }
                if(inputStream == null) {
                    sleepMe();
                    continue;
                }
                if(reader == null){
                    sleepMe();
                    continue;
                }
                
                line = readLine();
                if(line == null){
                    sleepMe();
                    continue;
                }
                
                if(saveHistory){
                    appendHistory (line);
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
    
    @Override
    public synchronized String getLog () {
        if(historyBuilder != null) {
            return historyBuilder.toString();
        }
        return null;
    }
    
    @Override
    public void subscribeOutput(IProcessOutputListener listener) {
        if(listeners.contains(listener))
            return;
        listeners.add(listener);
    }
    
    @Override
    public void unsubscribeOutput(IProcessOutputListener listener) {
        if(!listeners.contains(listener))
            return;
        listeners.remove(listener);
    }
    
    private synchronized void appendHistory (String line) {
        historyBuilder.append(line);
        historyBuilder.append("\n");
    }
    
    private void raiseOutputEvent (String line) {
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
