package TanqueGame;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Metralha implements ImageObserver {
	private double x,y;
	private double angulo;
	private double velocidadeBala;
	
	public Metralha (double x, double y, double angulo, Color cor){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.velocidadeBala = 70;
	}   


	public double getAngulo() {
		return angulo;
	}


	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public void mover(){
		x = x + Math.sin(Math.toRadians(angulo)) * velocidadeBala;
		y = y - Math.cos(Math.toRadians(angulo)) * velocidadeBala;
		
		
	}
	
	public void draw(Graphics2D g2d) throws IOException{
		BufferedImage bala = ImageIO.read(new File("bala.png"));

		
		//Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();
		//sistema de coordenadas para o tanque
		AffineTransform depois = new AffineTransform();  
		depois.translate(x, y);
		depois.rotate(Math.toRadians(angulo));
		g2d.transform(depois); //Aplicamos o sistema de coordenadas.
		
		g2d.drawImage(bala, 5, 5, 20, 20, this);

		
		g2d.setTransform(antes);
		
	}
	
	public Shape getRectEnvolvente(){
		AffineTransform at = new AffineTransform();
		at.translate(x,y);
		at.rotate(Math.toRadians(angulo));
		Rectangle rect = new Rectangle(-24,-32,48,55);
		return at.createTransformedShape(rect);
	}


	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
}

