/*
 * Copyright (C) 2016 jsilva
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
package com.tuxedoberries.mainloop;

import com.tuxedoberries.process.ProcessController;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsilva
 */
public class CoreLoop implements Runnable, ILooper {
    
    private static final long TARGET_SLEEP = 42;
    private final HashSet<IUpdate> subscribers;
    private Logger logger;
    
    public CoreLoop () {
        subscribers = new HashSet<>();
        createLogger();
    }
    
    @Override
    public void run () {
        while(true){
            // Update
            triggerUpdate ();
            sleepMe();
        }
    }
    
    private synchronized void triggerUpdate () {
        for (IUpdate subscriber : subscribers) {
            if(subscriber == null)
                continue;
            subscriber.Update();
        }
    }
    
    @Override
    public synchronized void subscribe(IUpdate updater) {
        if(subscribers.contains(updater))
            return;
        subscribers.add(updater);
    }
    
    @Override
    public synchronized void unsubscribe(IUpdate updater) {
        if(!subscribers.contains(updater))
            return;
        subscribers.remove(updater);
    }
    
    public synchronized void clearSubscribers () {
        subscribers.clear();
    }
    
    private void sleepMe () {
        try {
            Thread.sleep(TARGET_SLEEP);
        } catch (InterruptedException ex) {
            logger.log(Level.INFO, "Threas is interrupted", ex);
        }
    }
    
    private void createLogger () {
        if(logger == null) {
            String loggerName = String.format("[%d]%s", this.hashCode(), ProcessController.class.getName());
            logger = Logger.getLogger(loggerName);
        }
    }
}
