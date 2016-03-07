/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.process;

import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class CommandQueue implements Runnable {
    
    private Queue<CommandData> commandQueue;
    private IExecute executor;
    private Logger logger;
    
    public CommandQueue () {
        commandQueue = new LinkedList<>();
        createLogger();
    }
    
    public synchronized void enqueue (CommandData data) {
        commandQueue.add(data);
    }
    
    public void setCommandReceiver (IExecute executor) {
        this.executor = executor;
    }
    
    private synchronized CommandData dequeue () {
        return commandQueue.remove();
    }
    
    private synchronized int queueSize () {
        return commandQueue.size();
    }
    
    @Override
    public void run () {
        while(true) {
            if(Thread.interrupted())
                return;
            
            if(executor == null) {
                sleepMe();
                continue;
            }
            
            if(executor.isRunning()) {
                sleepMe();
                continue;
            }
            
            if(queueSize() <= 0) {
                sleepMe();
                continue;
            }
            
            CommandData data = dequeue();
            executor.execute(data);
        }
    }
    
    private void sleepMe () {
        try {
            Thread.sleep(33);
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, "Threas is interrupted", ex);
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
