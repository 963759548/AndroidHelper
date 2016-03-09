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

import com.tuxedoberries.mainloop.MainLoop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public class ProcessController implements IExecute, IEnqueue {
    
    private final ProcessExecutor executor;
    private Logger logger;
    private Thread inputThread;
    private Thread errorInputThread;
    private RunnableInputStream runnableInput;
    private RunnableInputStream runnableErrorInput;
    private CommandQueue commandQueue;
    private ProcessObserver observer;
    
    public ProcessController () {
        executor = new ProcessExecutor();
        createLogger();
        
        startInputThread ();
        startErrorInputThread();
        startQueueThread();
        startDetectorThread();
        MainLoop.start();
    }
    
    @Override
    public void enqueueCommand(String command) {
        enqueueCommand(command, null);
    }
    
    @Override
    public void enqueueCommand(String command, String[] env) {
        CommandData data = new CommandData();
        data.command = command;
        data.environmental = env;
        enqueueCommand(data);
    }
    
    @Override
    public void enqueueCommand(CommandData data) {
        commandQueue.enqueue(data);
        startQueueThread();
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
        runnableInput.enableLog(data.enableLog);
        runnableInput.setInputStream(executor.getInput());
        
        
        // Attach Error Input
        if(errorInputThread == null) {
            startErrorInputThread();
        }
        runnableErrorInput.enableLog(data.enableLog);
        runnableErrorInput.setInputStream(executor.getErrorInput());
    }
    
    private void startInputThread () {
        if(runnableInput == null) {
            runnableInput = new RunnableInputStream();
            runnableInput.setProcessStats(executor);
        }
        if(inputThread == null) {
            inputThread = new Thread(runnableInput);
            inputThread.start();
        }
    }
    
    private void startErrorInputThread () {
        if(runnableErrorInput == null) {
            runnableErrorInput = new RunnableInputStream();
            runnableErrorInput.setProcessStats(executor);
        }
        if(errorInputThread == null) {
            errorInputThread = new Thread(runnableErrorInput);
            errorInputThread.start();   
        }
    }
    
    private void startQueueThread () {
        if(commandQueue == null) {
            commandQueue = new CommandQueue();
            commandQueue.setCommandReceiver(this);
        }
        MainLoop.getLooper().subscribe(commandQueue);
        MainLoop.start();
    }
    
    private void startDetectorThread () {
        if(observer == null) {
            observer = new ProcessObserver();
            observer.setProcess(executor);
        }
        MainLoop.getLooperNoSleep().subscribe(observer);
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
        
        // Clear Queue
        MainLoop.getLooper().unsubscribe(commandQueue);
        commandQueue.clear();
    }
    
    public void clearQueue () {
        commandQueue.clear();
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
    
    public IProcessObserver getObserver () {
        return observer;
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
