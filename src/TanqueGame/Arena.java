package TanqueGame;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

@SuppressWarnings("serial")
public class Arena extends JComponent implements KeyListener, MouseListener, ActionListener {

	private int largura, altura;
	private HashSet<Tanque> tanques;
	private List<Tiro> tiros;
	private Timer contador;
	private List<Canhao> canhoes;
	private List<Metralha> metralhadoras;
	private List<Mina> minas;
	private List<Morto> mortos;
	private List<Laser> lasers;
	private List<TheBoss> chefao;
	private List<TiroTheBoss> tiroBoss;
	//private HashSet<TheBoss> chefao;

	public Arena(int largura, int altura) throws IOException {

		this.largura = largura;
		this.altura = altura;
		
		//chefao = new HashSet<TheBoss>();
		lasers = new ArrayList<Laser>();
		minas = new ArrayList<Mina>();
		metralhadoras = new ArrayList<Metralha>();
		canhoes = new ArrayList<Canhao>();
		tiros = new ArrayList<Tiro>();
		mortos = new ArrayList<Morto>();
		tanques = new HashSet<Tanque>();
		chefao = new ArrayList<TheBoss>();
		tiroBoss = new ArrayList<TiroTheBoss>();

		addMouseListener(this);
		addKeyListener(this);

		contador = new Timer(40, this);
		contador.start();
	}

	//public void adicionaChefao(TheBoss c) {
	//	chefao.add(c);
	//}

	public void adicionaTanque(Tanque t) {
		tanques.add(t);
	}
	
	public void adicionaChefao(TheBoss c) {
		chefao.add(c);
	}

	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	public Dimension getPreferredSize() {
		return new Dimension(largura, altura);
	}

	BufferedImage imagem = ImageIO.read(new File("fundo3.jpg"));

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	
		g2d.drawImage(imagem, getX(), getY(), 800, 600, this);
		
		
		for (Tanque t : tanques)
			t.draw(g2d);
		
		for(TheBoss c: chefao)
			try {
				c.draw(g2d);
				atirarChefao(c);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		for (TiroTheBoss tiros : tiroBoss)
			try {
				tiros.draw(g2d);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		
		for (Tiro tiro : tiros)
			tiro.draw(g2d);
		
		for(Canhao canhao : canhoes)
			try {
				canhao.draw(g2d);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
		for(Metralha metralhas: metralhadoras )
			try {
				metralhas.draw(g2d);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		for(Mina m: minas)
			try {
				m.draw(g2d);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
		for(Laser l: lasers)
			try {
				l.draw(g2d);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		

		for (Morto morto : mortos)
			try {
				morto.draw(g2d);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}

	public void mouseClicked(MouseEvent e) {
		for (Tanque t : tanques)
			t.setEstaAtivo(false);

		for (Tanque t : tanques) {
			boolean clicado = t.getRectEnvolvente()
					.contains(e.getX(), e.getY());
			if (clicado) {
				t.setEstaAtivo(true);

				switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					t.girarAntiHorario(25);
					break;
				case MouseEvent.BUTTON2:
					t.aumentarVelocidade();
					break;
				case MouseEvent.BUTTON3:
					t.girarHorario(25);
					break;
				}

				break;
			}

			repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// Método para movimentar pelo teclado
	public void keyPressed(KeyEvent e) {
		for (Tanque t : tanques) {

			if (t.isEstaAtivo()) {

				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					t.girarAntiHorario(10);
					break;
				case KeyEvent.VK_RIGHT:
					t.girarHorario(10);
					break;
				case KeyEvent.VK_UP:
					t.aumentarVelocidade();
					break;
				case KeyEvent.VK_DOWN:
					t.diminuirVelecidade();
					break;
				case KeyEvent.VK_SPACE:
					atirar(t);
					break;
				case KeyEvent.VK_C:
					atirarCanhao(t);
					break;
				case KeyEvent.VK_Z:
					atirarMetralha(t);
					break;
				case KeyEvent.VK_X:
					soltarMina(t);
					break;
				case KeyEvent.VK_V:
					atirarLaser(t);
					break;
				}
			}
		}
	}

	// Chama o movimento das balas
	public void actionPerformed(ActionEvent e) {
		for (Tanque t : tanques)
			t.mover();
		colisao();
		
		//for (TheBoss c : chefao)
			//c.mover();
		
		for (Tiro tiro : tiros)
			tiro.mover();
		
		for (TiroTheBoss tiros : tiroBoss)
			tiros.mover();
		
		if (tanques.size() == 1) {
			colisaoChefao();
			colisaoChefao2();
			colisaoChefao3();
			colisaoChefao4();
			colisaoChefao5();
		}
		
		
		for (Metralha metralhas: metralhadoras)
			metralhas.mover();
		
		colisaoMetralha();
		
		
		for(Canhao canhao: canhoes)
			canhao.mover();
		colisaoCanhao();
		
		for(Mina m: minas)
			colisaoMina();
		
		
		for(Laser l: lasers)
			l.mover();
		
		for(TheBoss c: chefao)
			c.mover();
			
		
		colisaoLaser();
		
		repaint();
		
		
	}

	// Pega os valores de X e Y junto com o angulo
	public void atirar(Tanque tiro) {
		tiros.add(new Tiro(tiro.getX(), tiro.getY(), tiro.getAngulo(), tiro.getCor()));
	}
	public void atirarChefao(TheBoss c) {
		tiroBoss.add(new TiroTheBoss(c.getX(), c.getY(), c.getAngulo(), c.getCor()));
		
	}
	
	public void atirarCanhao(Tanque canhao) {
		canhoes.add(new Canhao(canhao.getX(), canhao.getY(), canhao.getAngulo(), canhao.getCor()));
	}
	
	public void atirarMetralha(Tanque metralhas) {
		metralhadoras.add(new Metralha(metralhas.getX(), metralhas.getY(), metralhas.getAngulo(), metralhas.getCor()));
	}
	
	public void soltarMina(Tanque m){
		minas.add(new Mina(m.getX(), m.getY(), m.getAngulo(), null));
	
	}
	
	public void atirarLaser(Tanque l){
		lasers.add(new Laser(l.getX(), l.getY(), l.getAngulo(), null));
	
	}
	
	

	public void colisao() {
		for (Tiro tiro : tiros) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(tiro.getX() - t.getX(), 2)+ Math.pow(tiro.getY() - t.getY(), 2));
				if ((distancia <= 30) && (t.isEstaAtivo() == false)) {
					morrer(t);
					musicTiro();
					tanques.remove(t);
					break;
				}
			}
		}
	}
	
	public void colisaoCanhao() {
		for (Canhao canhao : canhoes) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(canhao.getX() - t.getX(), 2)+ Math.pow(canhao.getY() - t.getY(), 2));
		     	if ((distancia <= 50) && (t.isEstaAtivo() == false)) {
					morrer(t);
					musicExplodir();
					tanques.remove(t);
					break;
				}
			}
		}
	}
	
	public void colisaoChefao() {
		int x = 0;
		for (TiroTheBoss tiros : tiroBoss) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(tiros.getX() - t.getX(), 2)+ Math.pow(tiros.getY() - t.getY(), 2));
				if ((distancia <= 20) && (t.isEstaAtivo() == true)) {
					musicExplodir();
					tanques.remove(t);
					JOptionPane.showMessageDialog(null, "You Lose! :( ");
					musicDerrota();
					x = 1;
					break;
				}
			}
			if (x == 1) {
				break;
			}
		}
	}
	
	public void colisaoChefao2() {
		for (Mina tiros : minas) {
			for (TheBoss t : chefao) {
				double distancia = Math.sqrt(Math.pow(tiros.getX() - t.getX(), 2)+ Math.pow(tiros.getY() - t.getY(), 2));
				if ((distancia <= 30) && (t.isEstaAtivo() == false)) {
					//morrerChefao(t);
					musicExplodir();
					//minas.remove(tiros);
					if (t.getVida() == 1) {
						chefao.remove(t);
						JOptionPane.showMessageDialog(null, "You Win! :P ");
						music();
						break;
					}
					else{
						int vidas = t.getVida() -1;
						t.setVida(vidas);
						break;
					}
					
					
				}
			}
		}
	}
	
	public void colisaoChefao3() {
		for (Metralha tiros : metralhadoras) {
			for (TheBoss t : chefao) {
				double distancia = Math.sqrt(Math.pow(tiros.getX() - t.getX(), 2)+ Math.pow(tiros.getY() - t.getY(), 2));
				if ((distancia <= 30) && (t.isEstaAtivo() == false)) {
					//morrerChefao(t);
					musicMetralha();
					if (t.getVida() == 1) {
						chefao.remove(t);
						JOptionPane.showMessageDialog(null, "You Win! :P ");
						music();
						break;
					}
					else{
						int vidas = t.getVida() -1;
						t.setVida(vidas);
						break;
					}
				}
			}
		}
	}
	
	public void colisaoChefao4() {
		for (Canhao tiros : canhoes) {
			for (TheBoss t : chefao) {
				double distancia = Math.sqrt(Math.pow(tiros.getX() - t.getX(), 2)+ Math.pow(tiros.getY() - t.getY(), 2));
				if ((distancia <= 400) && (t.isEstaAtivo() == false)) {
					//morrerChefao(t);
					musicExplodir();
					if (t.getVida() == 1) {
						chefao.remove(t);
						JOptionPane.showMessageDialog(null, "You Win! :P ");
						music();
						break;
					}
					else{
						int vidas = t.getVida() -1;
						t.setVida(vidas);
						break;
					}
				}
			}
		}
	}
	
	public void colisaoChefao5() {
		for (Laser tiros : lasers) {
			for (TheBoss t : chefao) {
				double distancia = Math.sqrt(Math.pow(tiros.getX() - t.getX(), 2)+ Math.pow(tiros.getY() - t.getY(), 2));
				if ((distancia <= 20) && (t.isEstaAtivo() == false)) {
					//morrerChefao(t);
					musicLaser();
					if (t.getVida() == 1) {
						chefao.remove(t);
						JOptionPane.showMessageDialog(null, "You Win! :P ");
						music();
						break;
					}
					else{
						int vidas = t.getVida() -1;
						t.setVida(vidas);
						break;
					}
				}
			}
		}
	}
	
	private void morrerChefao(TheBoss t) {
		// TODO Auto-generated method stub
		
		
	}

	public void colisaoMetralha() {
		for (Metralha metralha : metralhadoras) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(metralha.getX() - t.getX(), 2)+ Math.pow(metralha.getY() - t.getY(), 2));
				if ((distancia <= 25) && (t.isEstaAtivo() == false)) {
					morrer(t);
					musicMetralha();
					tanques.remove(t);
					break;
				}
			}
		}
	}
	
	public void colisaoMina() {
		int x = 0;
		for (Mina m : minas) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(m.getX() - t.getX(), 2)+ Math.pow(m.getY() - t.getY(), 2));
				if ((distancia <= 20) && (t.isEstaAtivo() == false)) {
					morrer(t);
					musicExplodir();
					tanques.remove(t);
					x = 1;
					break;
				}
			}
			if (x == 1) {
				//minas.remove(m);
				break;
			}
		}
	}
	
	public void colisaoLaser() {
		for (Laser l : lasers) {
			for (Tanque t : tanques) {
				double distancia = Math.sqrt(Math.pow(l.getX() - t.getX(), 2)+ Math.pow(l.getY() - t.getY(), 2));
				if ((distancia <= 30) && (t.isEstaAtivo() == false)) {
					morrer(t);
					musicLaser();
					tanques.remove(t);
					break;
				}
			}
		}
	}
	
	
	
	public void morrer(Tanque morre) {
		mortos.add(new Morto(morre.getX(), morre.getY(), morre.getAngulo(), morre.getCor()));
		int tamanhoLista = mortos.size();
		if (tamanhoLista == 5){
			TheBoss c = new TheBoss(100, 200, 0, Color.BLUE, 5);
			adicionaChefao(c);
			
		}
	}
	
	public static void music() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("som.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }

	public static void musicExplodir() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("boom.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }

	public static void musicMetralha() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("metralha.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }

	public static void musicTiro() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("tiro.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }
	
	public static void musicLaser() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("laser.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }
	
	public static void musicDerrota() {  
        AudioPlayer MGP = AudioPlayer.player;  
        AudioStream BGM = null;
		try {
			BGM = new AudioStream(new FileInputStream("derrota.wav"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        MGP.start(BGM);  
    }



	public static void main(String args[]) throws IOException {

		Arena arena = new Arena(800, 600);
		arena.adicionaTanque(new Tanque(100, 200, 0, Color.BLUE));
		arena.adicionaTanque(new Tanque(200, 200, 45, Color.CYAN));
		arena.adicionaTanque(new Tanque(470, 360, 90, Color.MAGENTA));
		arena.adicionaTanque(new Tanque(450, 50, 157, Color.RED));
		arena.adicionaTanque(new Tanque(300,300,47,Color.ORANGE));
		arena.adicionaTanque(new Tanque(80,307,180,Color.WHITE));

		//arena.adicionaChefao(new TheBoss(80,307,180,Color.WHITE));


		
		JFrame janela = new JFrame("Tanques");

		// Criando um icone para o jogo
		BufferedImage imagem = ImageIO.read(new File("images.jpg"));
		janela.setIconImage(imagem);

		janela.getContentPane().add(arena);
		janela.addKeyListener(arena);
		janela.pack();
		janela.setVisible(true);
		janela.setDefaultCloseOperation(3);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	
}