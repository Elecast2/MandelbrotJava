package net.minemora.mandelbrotjava;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

public class MandelCanvas extends Canvas {

	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) { 
        for(int i = 0; i < MandelbrotJava.size; i++) {
        	for(int j = 0; j < MandelbrotJava.size; j++) {
        		g.setColor(new Color(MandelbrotJava.iter[i][j]));
        		g.fillRect(i, j, i, j);
        	}
        }
        for(int i = 0; i < MandelbrotJava.size; i++) {
        	for(int j = 0; j < MandelbrotJava.size; j++) {
        		g.setColor(new Color(MandelbrotJava.mandelRight[i][j]));
        		g.fillRect(i+MandelbrotJava.size, j, i+MandelbrotJava.size, j);
        	}
        }
    }
	
}
