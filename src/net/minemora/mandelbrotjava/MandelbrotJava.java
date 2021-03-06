package net.minemora.mandelbrotjava;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;

import com.aparapi.Kernel;
import com.aparapi.Range;

public class MandelbrotJava {

	public static Timer timer = new Timer();
	public static MandelWindow frame;

	public static int size = 500;
	public static int unit = size / 4;
	public static int iterations = 65;

	public static BufferedImage mandelLeft = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

	public static BufferedImage mandelRight = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

	public static double lastRe;
	public static double lastIm;

	public static boolean render = false;

	public static int totalRow = 0;

	public static void main(String[] args) {
		frame = new MandelWindow("Mandelbrot Elecast");
		renderMandel(mandelLeft);
		renderMandel(mandelRight, 0, 0);
	}

	public static void renderMandel(BufferedImage image) {
		renderMandel(image, size, iterations, 0, 0, false, false, null);
		frame.getPanel().repaint();
	}

	public static void renderMandel(BufferedImage image, double re, double im) {
		renderMandel(image, size, iterations, re, im, true, false, null);
		frame.getPanel().repaint();
	}

	public static void renderMandelPNG(int size, int iterations) {
		frame.getProgressBar().setMaximum(size);
		frame.getProgressBar().setMinimum(0);
		frame.getProgressBar().setValue(0);
		frame.getProgressBar().setString("");
		frame.getProgressBar().update(frame.getProgressBar().getGraphics());
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
		renderMandel(image, size, iterations, lastRe, lastIm, true, true, frame.getProgressBar());
	}

	public static void renderPng(BufferedImage image) {
		try {
			ImageIO.write(image, "png", new File("mandel.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		frame.getProgressBar().setValue(0);
		frame.getProgressBar().setString("Done!");
		frame.getProgressBar().update(frame.getProgressBar().getGraphics());
	}

	public static void renderMandel(BufferedImage image, int size, int iterations, double re, double im, boolean staticC, boolean toPng, JProgressBar bar) {
		int[] colors = new int[iterations];
		for (int i = 0; i < iterations; i++) {
			colors[i] = Color.HSBtoRGB(i / 256f, 1, i / (i + 8f));
		}
		Kernel kernel = new Kernel() {
			@Override
			public void run() {
				int col = getGlobalId()%size;
				int row = getGlobalId()/size;
				double c_re = (col - size / 2) * 4.0 / size;
				double c_im = (row - size / 2) * 4.0 / size;
				double x = 0, y = 0;
				if (staticC) {
					double x_new2 = x * x - y * y + c_re;
					y = 2 * x * y + c_im;
					x = x_new2;
				}
				int iteration = 0;
				if (staticC) {
					while (x * x + y * y < 4 && iteration < iterations) {
						double x_new = x * x - y * y + re;
						y = 2 * x * y + im;
						x = x_new;
						iteration++;
					}
				} else {
					while (x * x + y * y < 4 && iteration < iterations) {
						double x_new = x * x - y * y + c_re;
						y = 2 * x * y + c_im;
						x = x_new;
						iteration++;
					}
				}
				
				if (iteration < iterations)
					image.setRGB(col, row, colors[iteration]);
				else
					image.setRGB(col, row, 0);

				if (toPng && getGlobalId()%size == 0) {
					totalRow++;
					bar.setValue(totalRow);
					bar.update(frame.getProgressBar().getGraphics());
				}
				if (toPng && getGlobalId() == size*size -1) {
					render = true;
				}
			}
		};
		Range range = Range.create(size*size);
		kernel.execute(range);
		if (toPng) {
			while (!render) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			renderPng(image);
			render = false;
			totalRow = 0;
		}
	}
}
