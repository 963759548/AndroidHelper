/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.androidhelper;

import com.tuxedoberries.presentation.MainWindow;

/**
 *
 * @author jsilva
 */
public class AndroidHelper {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Open Main Window
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
}
