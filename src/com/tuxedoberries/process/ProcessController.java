/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.process;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class ProcessController implements IExecute {
    
    private final ProcessExecutor executor;
    private Logger logger;
    private Thread inputThread;
    private Thread errorInputThread;
    private Thread commandQueueThread;
    private RunnableInputStream runnableInput;
    private RunnableInputStream runnableErrorInput;
    private CommandQueue commandQueue;
    
    public ProcessController () {
        executor = new ProcessExecutor();
        createLogger();
        
        startInputThread ();
        startErrorInputThread();
        startQueueThread();
    }
    
    public void enqueueCommand(String command) {
        enqueueCommand(command, null);
    }
    
    public void enqueueCommand(String command, String[] env) {
        CommandData data = new CommandData();
        data.command = command;
        data.environmental = env;
        enqueueCommand(data);
    }
    
    public void enqueueCommand(CommandData data) {
        commandQueue.enqueue(data);
    }
    
    @Override
    public void execute(String command) {
        execute(command, null);
    }
    
    @Override
    public void execute(String command, String[] env) {
        CommandData data = new CommandData();
        data.command = command;
        data.environmental = env;
        execute(data);
    }
    
    @Override
    public void execute(CommandData data) {
        if(executor.isRunning()) {
            logger.log(Level.WARNING, "Current process is still runnning");
            return;
        }
        
        // Execute Command
        executor.executeProcess(data.command, data.environmental);
        
        // Attach Input
        if(inputThread == null) {
            startInputThread();
        }
        runnableInput.setInputStream(executor.getInput());
        
        
        // Attach Error Input
        if(errorInputThread == null) {
            startErrorInputThread();
        }
        runnableErrorInput.setInputStream(executor.getErrorInput());
        
    }
    
    private void startInputThread () {
        if(inputThread == null) {
            runnableInput = new RunnableInputStream();
            runnableInput.setProcessStats(executor);
            inputThread = new Thread(runnableInput);
            inputThread.start();
        }
    }
    
    private void startErrorInputThread () {
        if(errorInputThread == null) {
            runnableErrorInput = new RunnableInputStream();
            runnableErrorInput.setProcessStats(executor);
            errorInputThread = new Thread(runnableErrorInput);
            errorInputThread.start();   
        }
    }
    
    private void startQueueThread () {
        if(commandQueue == null) {
            commandQueue = new CommandQueue();
            commandQueue.setCommandReceiver(this);
            commandQueueThread = new Thread(commandQueue);
            commandQueueThread.start();
        }
    }
    
    @Override
    public boolean isRunning () {
        return executor.isRunning();
    }
    
    public void stop () {
        // Close input thread
        if(inputThread != null) {
            inputThread.interrupt();
            inputThread = null;
        }
        // Close error input thread
        if(errorInputThread != null) {
            errorInputThread.interrupt();
            errorInputThread = null;
        }
        
        // Stop Process
        if(executor != null) {
            executor.stop();
        }
    }
    
    public IProcessLog getLogger () {
        return runnableInput;
    }
    
    public IProcessLog getErrorLogger () {
        return runnableErrorInput;
    }
    
    public IProcessStats getStats () {
        return executor;
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
