/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.process;

import com.tuxedoberries.utils.ExceptionHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class RunnableInputStream implements Runnable, IProcessLog {
    
    private InputStream inputStream;
    private BufferedReader reader;
    private Logger logger;
    private StringBuilder historyBuilder;
    private List<IProcessOutputListener> listeners;
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
        printLog = print;
        saveHistory = history;
        listeners = new ArrayList<>();
        inputStream = stream;
        updateReader (stream);
    }
    
    public void setProcessStats (IProcessStats stats) {
        this.stats = stats;
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
                
                if(saveHistory)
                    appendHistory (line);
                if(printLog)
                    System.out.println(String.format("[%d] %s", this.hashCode(), line));
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
        listeners.add(listener);
    }
    
    @Override
    public void unsubscribeOutput(IProcessOutputListener listener) {
        listeners.remove(listener);
    }
    
    private synchronized void appendHistory (String line) {
        historyBuilder.append(line);
        historyBuilder.append("\n");
    }
    
    private void raiseOutputEvent (String line) {
        for(int i=0; i<listeners.size(); ++i){
            listeners.get(i).onNewLine(line);
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
