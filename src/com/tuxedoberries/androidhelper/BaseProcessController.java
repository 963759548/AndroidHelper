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
package com.tuxedoberries.androidhelper;

import com.tuxedoberries.configuration.ADBConfiguration;
import com.tuxedoberries.presentation.LogWindow;
import com.tuxedoberries.process.ProcessController;
import com.tuxedoberries.process.interfaces.IProcessStartListener;
import com.tuxedoberries.process.interfaces.IProcessStopListener;
import com.tuxedoberries.utils.FileWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Silva
 */
public abstract class BaseProcessController implements IProcessStartListener, IProcessStopListener {
    
    private final ProcessController processController;
    protected Logger logger;
    protected LogWindow logWindow;
    protected final FileWriter fileWriter;
    protected boolean showWindow = true;
    
    public BaseProcessController () {
        fileWriter  = new FileWriter();
        processController = new ProcessController();
        createLogger();
    }
    
    public final boolean isRunning () {
        if(processController == null)
            return false;
        return processController.isRunning();
    }
    
    public final int queueCount () {
        if(processController == null)
            return 0;
        return processController.getQueue().queueSize();
    }
    
    public ProcessController getProcess () {
        return processController;
    }
    
    public final void showLogWindow (boolean show) {
        showWindow = show;
        if(logWindow != null){
            logWindow.setVisible(showWindow);
        }
    }
    
    public final void startProcess() {
        if(isRunning()){
            logger.log(Level.INFO, "Process is already running");
            return;
        }
        
        // Show Window or not
        logWindow = new LogWindow(false, true);
        logWindow.setTitle(getWindowTitle());
        logWindow.setVisible(showWindow);
        processLogWindow (logWindow);

        // Subscribe Window
        processController.getLogger().subscribeOutput(logWindow);  
        processController.getErrorLogger().subscribeOutput(logWindow);
        
        // Subscribe Events
        processController.getObserver().subscribeOnStart(this);
        processController.getObserver().subscribeOnStop(this);
        
        // Do the rest
        doStartProcess();
    }
    
    public final void stopProcess() {
        if(!isRunning()){
            //logger.log(Level.INFO, "Logcat is already stopped");
            return;
        }
        
        // Do the rest
        doStopProcess();
    }
    
    @Override
    public final void onProcessStarted(String process) {
        processStartedProcess(process);
    }
    
    @Override
    public final void onProcessStopped(String process) {
        processStoppedProcess(process);
        if(processController.getQueue().queueSize() <= 0){
            // Unsubscribe Window
            processController.getLogger().unsubscribeOutput(logWindow);  
            processController.getErrorLogger().unsubscribeOutput(logWindow);
            
            // Try to wait until the log is finished
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex){
                
            }
            // Save Log
            saveLog( getLogFileName() );

            // Dispose Window
            if(logWindow != null){
                logWindow.dispose();
                logWindow = null;
            }
        }
    }
    
    /**
     * Return this instance process controller
     * @return 
     */
    protected ProcessController getProcessController () {
        return processController;
    }
    
    /**
     * Save the log of the process into the given file
     * @param logfile 
     */
    protected void saveLog (String logfile) {
        String log = processController.getProcessLog().getLog();
        fileWriter.WriteFile(ADBConfiguration.DEFAULT_DESTINATION_FOLDER.concat(logfile), log);
        processController.getProcessLog().clearLog();
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), this.getClass().getName());
            logger = Logger.getLogger(loggerName);
        }
    }
    
    //------------------
    // Virtual
    //------------------
    protected void processStartedProcess (String process) {
        // Nothing to do here
    }
    
    protected void processStoppedProcess (String process) {
        // Nothing to do here
    }
    
    protected void processLogWindow (LogWindow logWindow) {
        // Nothing to do here
    }

    //------------------
    // Abstract
    //------------------
    
    /**
     * Get the corresponding window title
     * @return 
     */
    protected abstract String getWindowTitle ();
    
    /**
     * Get the filename of the log to be saved
     * @return 
     */
    protected abstract String getLogFileName ();
    
    /**
     * Execute the start of the process
     */
    protected abstract void doStartProcess ();
    
    /**
     * Execute the stop of the process
     */
    protected abstract void doStopProcess ();

}
