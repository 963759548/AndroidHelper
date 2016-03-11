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

import com.tuxedoberries.process.interfaces.IExecute;
import com.tuxedoberries.process.interfaces.IProcessStats;
import com.tuxedoberries.process.interfaces.IProcessObserver;
import com.tuxedoberries.process.interfaces.IEnqueue;
import com.tuxedoberries.mainloop.MainLoop;
import com.tuxedoberries.process.interfaces.ICommandQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.tuxedoberries.process.interfaces.IProcessEventLog;
import com.tuxedoberries.process.interfaces.IProcessLog;

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
    private ProcessLog processLog;
    
    public ProcessController () {
        executor = new ProcessExecutor();
        createLogger();
        startThreads();
    }
    
    private void startThreads () {
        startInputThread ();
        startErrorInputThread();
        startQueueThread();
        startDetectorThread();
        registerLog();
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
        
        startThreads ();
        // Add to log
        processLog.startCommand(data.command);
        
        // Execute Command
        executor.executeProcess(data.command, data.environmental);
        
        // Attach Input
        runnableInput.enableLog(data.enableLog);
        runnableInput.setInputStream(executor.getInput());
        
        // Attach Error Input
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
    
    private void registerLog () {
        if(processLog == null) {
            processLog = new ProcessLog();
            runnableInput.subscribeOutput(processLog);
            runnableErrorInput.subscribeOutput(processLog);
        }
    }
    
    @Override
    public boolean isRunning () {
        return executor.isRunning() || runnableInput.isReading() || runnableErrorInput.isReading();
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
    
    public ICommandQueue getQueue () {
        return commandQueue;
    }
    
    public IProcessEventLog getLogger () {
        return runnableInput;
    }
    
    public IProcessEventLog getErrorLogger () {
        return runnableErrorInput;
    }
    
    public IProcessStats getStats () {
        return executor;
    }
    
    public IProcessObserver getObserver () {
        return observer;
    }
    
    public IProcessLog getProcessLog () {
        return processLog;
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
