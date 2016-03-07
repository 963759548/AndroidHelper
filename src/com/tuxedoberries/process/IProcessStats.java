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
public interface IProcessStats {
    
    /**
     * Determines if the current process is running.
     * @return true if is running, false otherwise.
     */
    public boolean isRunning();
}
