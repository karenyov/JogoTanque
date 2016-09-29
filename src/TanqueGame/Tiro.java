package TanqueGame;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;


public class Tiro {
	private double x,y;
	private double angulo;
	private double velocidadeBala;
	private Color cor;
	
	public Tiro (double x, double y, double angulo, Color cor){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.cor = cor;
		this.velocidadeBala = 7;
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


	public void mover(){
		x = x + Math.sin(Math.toRadians(angulo)) * velocidadeBala;
		y = y - Math.cos(Math.toRadians(angulo)) * velocidadeBala;
		
		
	}
	
	public void draw(Graphics2D g2d){
		//Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();
		//sistema de coordenadas para o tanque
		AffineTransform depois = new AffineTransform();  
		depois.translate(x, y);
		depois.rotate(Math.toRadians(angulo));
		g2d.transform(depois); //Aplicamos o sistema de coordenadas.
		
		g2d.setColor(cor); //Desenhando a bala
		g2d.drawOval(-3, -50, 7, 7);
		g2d.setColor(cor);
		g2d.fillOval(-3, -50, 7, 7);
		g2d.setColor(cor);
		g2d.setTransform(antes);
		
	}
	
	public Shape getRectEnvolvente(){
		AffineTransform at = new AffineTransform();
		at.translate(x,y);
		at.rotate(Math.toRadians(angulo));
		Rectangle rect = new Rectangle(-24,-32,48,55);
		return at.createTransformedShape(rect);
	}
}

