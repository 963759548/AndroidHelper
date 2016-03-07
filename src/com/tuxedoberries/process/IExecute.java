/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.process;

/**
 *
 * @author jsilva
 */
public interface IExecute {
    
    /**
     * Execute the given command
     * @param command 
     */
    public void execute(String command);
    
    /**
     * Execute the given command with the given environmental variables
     * @param command
     * @param env 
     */
    public void execute(String command, String[] env);
    
    /**
     * Execute the given command
     * @param data 
     */
    public void execute(CommandData data);
    
    /**
     * Determines if the current process is running.
     * @return true if is running, false otherwise.
     */
    public boolean isRunning();
}
