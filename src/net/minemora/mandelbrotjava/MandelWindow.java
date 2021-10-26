package net.minemora.mandelbrotjava;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class MandelWindow extends JFrame {

	private static final long serialVersionUID = -45459919626785909L;
	private JPanel contentPane;
	private JPanel panelRender;
	private JButton btnRender;
	private JProgressBar progressBar;
	private JPanel panelMandel;
	private JTextField textField;

	public MandelWindow(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 883, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		panelRender = new JPanel();
		contentPane.add(panelRender, BorderLayout.SOUTH);
		panelRender.setLayout(new BorderLayout(0, 0));
		
		
		
		btnRender = new JButton("Render");
		
		btnRender.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MandelbrotJava.renderMandelPNG(Integer.parseInt(textField.getText()), 200);
			}
			
		});
		
		panelRender.add(btnRender, BorderLayout.CENTER);
		
		progressBar = new JProgressBar();
		panelRender.add(progressBar, BorderLayout.SOUTH);
		
		textField = new JTextField();
		textField.setText("4096");
		panelRender.add(textField, BorderLayout.WEST);
		textField.setColumns(10);
		
		panelMandel = new JPanel();
		contentPane.add(panelMandel, BorderLayout.CENTER);
		
		Canvas canvas = new MandelCanvas();
		canvas.setSize(2 * MandelbrotJava.size, MandelbrotJava.size);
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				TimerTask task = new TimerTask() {
			        @Override
			        public void run() {
			            Point p = MouseInfo.getPointerInfo().getLocation();
			            int x = p.x - MandelbrotJava.frame.getLocation().x;
			            int y = p.y - MandelbrotJava.frame.getLocation().y - 25;
			            double c_re = (x - MandelbrotJava.size / 2) * 4.0 / MandelbrotJava.size;
						double c_im = (y - MandelbrotJava.size / 2) * 4.0 / MandelbrotJava.size;
						MandelbrotJava.lastRe = c_re;
						MandelbrotJava.lastIm = c_im;			
						MandelbrotJava.renderMandel(MandelbrotJava.mandelRight, c_re, c_im);
						canvas.repaint();
			        }
			    };
			    MandelbrotJava.timer.schedule(task, 80, 80);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				MandelbrotJava.timer.cancel();
				MandelbrotJava.timer = new Timer();
			}
		});
		panelMandel.add(canvas);
		
		
		this.pack();
		this.setMinimumSize(this.getSize());
		this.setResizable(false);
		this.setVisible(true);
		
		
		
	}

	public JPanel getPanelRender() {
		return panelRender;
	}
	public JButton getBtnRender() {
		return btnRender;
	}
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JPanel getPanelMandel() {
		return panelMandel;
	}
	public JTextField getTextField() {
		return textField;
	}
}
