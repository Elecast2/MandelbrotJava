package net.minemora.mandelbrotjava;

import java.awt.Graphics;

import javax.swing.JPanel;

public class MandelPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public void paint(Graphics g) { 
        g.drawImage(MandelbrotJava.mandelLeft, 0, 0, null);
        g.drawImage(MandelbrotJava.mandelRight, MandelbrotJava.size, 0, null);
	}
	
}
