package net.minemora.mandelbrotjava;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;

public class MandelbrotJava {
	
	public static Timer timer = new Timer();
	public static MandelWindow frame;

	public static int size = 250;
	public static int unit = size / 4;
	public static int iterations = 80;

	public static int[][] iter = new int[size][size];

	public static int[][] mandelRight = new int[size][size];
	
	public static double lastRe;
	public static double lastIm;

	public static void main(String[] args) {
		renderMandel(iter);
		renderMandel(mandelRight, 0, 0);
		frame = new MandelWindow("Mandelbrot Elecast");
		
	}

	public static void renderMandel(int[][] mandel) {
		int black = 0;
		int[] colors = new int[iterations];
		for (int i = 0; i < iterations; i++) {
			colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f));
		}
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				double c_re = (col - size / 2) * 4.0 / size;
				double c_im = (row - size / 2) * 4.0 / size;
				double x = 0, y = 0;
				int iteration = 0;
				while (x * x + y * y < 4 && iteration < iterations) {
					double x_new = x * x - y * y + c_re;
					y = 2 * x * y + c_im;
					x = x_new;
					iteration++;
				}
				if (iteration < iterations)
					mandel[col][row] = colors[iteration];
				else
					mandel[col][row] = black;
			}
		}
	}

	public static void renderMandel(int[][] mandel, double re, double im) {
		int black = 0;
		int[] colors = new int[iterations];
		for (int i = 0; i < iterations; i++) {
			colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f));
		}
		for (int row = 0; row < size; row++) {
			for (int col = 0; col < size; col++) {
				double c_re = (col - size / 2) * 4.0 / size;
				double c_im = (row - size / 2) * 4.0 / size;
				double x = 0, y = 0;
				double x_new2 = x * x - y * y + c_re;
				y = 2 * x * y + c_im;
				x = x_new2;
				int iteration = 0;
				while (x * x + y * y < 4 && iteration < iterations) {
					double x_new = x * x - y * y + re;
					y = 2 * x * y + im;
					x = x_new;
					iteration++;
				}
				if (iteration < iterations)
					mandel[col][row] = colors[iteration];
				else
					mandel[col][row] = black;
			}
		}
	}
	
	public static void renderMandelPNG(int size, int iterations) {
		frame.getProgressBar().setMaximum(size);
		frame.getProgressBar().setMinimum(0);
        frame.getProgressBar().setValue(0);
        frame.getProgressBar().setString("");
        frame.getProgressBar().update(frame.getProgressBar().getGraphics());
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        int black = 0;
        int[] colors = new int[iterations]; 
        for (int i = 0; i<iterations; i++) {
            colors[i] = Color.HSBtoRGB(i/256f, 1, i/(i+8f));
        }
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
            	double c_re = (col - size / 2) * 4.0 / size;
				double c_im = (row - size / 2) * 4.0 / size;
				double x = 0, y = 0;
				double x_new2 = x * x - y * y + c_re;
				y = 2 * x * y + c_im;
				x = x_new2;
				int iteration = 0;
				while (x * x + y * y < 4 && iteration < iterations) {
					double x_new = x * x - y * y + lastRe;
					y = 2 * x * y + lastIm;
					x = x_new;
					iteration++;
				}
                if (iteration < iterations) image.setRGB(col, row, colors[iteration]);
                else image.setRGB(col, row, black);
            }
            frame.getProgressBar().setValue(row);
            frame.getProgressBar().update(frame.getProgressBar().getGraphics());
        }
        try {
			ImageIO.write(image, "png", new File("mandel.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        frame.getProgressBar().setValue(0);
        frame.getProgressBar().setString("Done!");
        frame.getProgressBar().update(frame.getProgressBar().getGraphics());
	}
}
