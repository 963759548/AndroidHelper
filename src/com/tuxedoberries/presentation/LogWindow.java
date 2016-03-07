/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tuxedoberries.presentation;

import com.tuxedoberries.process.IProcessOutputListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JScrollBar;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.WindowConstants;

/**
 *
 * @author jsilva
 */
public class LogWindow extends JFrame implements IProcessOutputListener {
    
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final int SCROLL_DELTA = 100;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;
    private JScrollBar verticalBar;
    private boolean autoScrollDown = true;
    private boolean allowClose = true;
    
    public LogWindow (boolean allowClose, boolean autoScroll) {
        this.allowClose = allowClose;
        this.autoScrollDown = autoScroll;
        
        initComponents();
        reposition();
    }
    
    private void initComponents () {
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        verticalBar = jScrollPane1.getVerticalScrollBar();
        
        if(allowClose)
            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        else
            setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        jScrollPane1.setBackground(Color.BLACK);
        jTextArea1.setBackground(Color.BLACK);
        jTextArea1.setForeground(Color.WHITE);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEditable(false);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, WINDOW_WIDTH, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, WINDOW_HEIGHT, Short.MAX_VALUE)
        );

        pack();
    }
    
    private void reposition () {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int posX = dim.width - WINDOW_WIDTH;
        int posY = 0;
        setLocation(posX, posY);
    }
    
    @Override
    public void onNewLine(String line) {
        this.jTextArea1.append(line);
        this.jTextArea1.append("\n");
        
        if(autoScrollDown) {
            verticalBar.setValue(verticalBar.getMaximum());
        }
    }
    
    public void clear () {
        this.jTextArea1.setText("");
    }
}
