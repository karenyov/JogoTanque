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

public class Morto implements ImageObserver {
	private double x,y;
	private double angulo;
	private Color cor;

	
	public Morto (double x, double y, double angulo, Color cor){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.cor = cor;
	}   
	
	
	public Color getCor() {
		return cor;
	}


	public void setCor(Color cor) {
		this.cor = cor;
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

	
	public void draw(Graphics2D g2d) throws IOException{
		BufferedImage morte = ImageIO.read(new File("morto.png"));


		//Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();
		
		//sistema de coordenadas para o tanque
		AffineTransform depois = new AffineTransform();  
		depois.translate(x, y);
		depois.rotate(Math.toRadians(angulo));
		g2d.transform(depois); 
		
		//Aplicamos o sistema de coordenadas.
		g2d.drawImage(morte, 5, 5, 100, 60, this);

		g2d.setTransform(antes);
		
	}
	
	public Shape getRectEnvolvente(){
		AffineTransform at = new AffineTransform();
		at.translate(x,y);
		at.rotate(Math.toRadians(angulo));
		Rectangle rect = new Rectangle(-34,-42,58,55);
		return at.createTransformedShape(rect);
	}

	@Override
	public boolean imageUpdate(Image arg0, int arg1, int arg2, int arg3,int arg4, int arg5) {
		return false;
	}
}

