package dynamic_best_3;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class DynamicBest extends JFrame {  
	
	private Image screenImage;
	private Graphics screenGraphics;
	private Image introBackground;
	
	public DynamicBest() {	//������ ����
		setTitle("Dynamic Beat");
		setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGTH); //ȭ��ũ�� ����
		setResizable(false); //ȭ��ũ�� ���� x
//		setLocationRelativeTo(null); //â�� ��ġ�� ȭ���� �߽����� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //â�� ���� ������ ���α׷��� ���� �ǵ��� ����
		Dimension d = getSize();
		setVisible(true); //ȭ�� ǥ��
		
		/* ����Ŭ���� ��ġ�� ������� �̹����� �ʱ�ȭ */
		introBackground = new ImageIcon(Main.class.getResource("2.jpg")).getImage();
		
		/* ������ ����ʰ� ���ÿ� ��Ʈ�� ������ ���� �ݺ� ��� */
		Music introMusic = new Music("introMusic.mp3",true);
		introMusic.start();
		
	}
		public void paint(Graphics g) {
			// JFrame�� ��ӹ޴� ó���� ȭ���� �׷��ִ� �޼ҵ�
			screenImage = createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGTH);
			screenGraphics = screenImage.getGraphics();
			screenDraw(screenGraphics);
			g.drawImage(screenImage, 0, 0, null);
		}//paint
		
		public void screenDraw(Graphics g) {
			g.drawImage(introBackground, 0, 0, null);
			this.repaint(); //�ٽ� paint�޼ҵ� ȣ��
		}//screenDraw
	}






















