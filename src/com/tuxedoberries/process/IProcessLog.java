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
public interface IProcessLog {
    
    /**
     * Get the process log.
     * @return log string
     */
    public String getLog ();
    
    /**
     * Subscribe to receive each new line of the Process Output.
     * @param listener 
     */
    public void subscribeOutput(IProcessOutputListener listener);
    
    /**
     * Unsubscribe to receive each new line of the Process Output.
     * @param listener 
     */
    public void unsubscribeOutput(IProcessOutputListener listener);
}
