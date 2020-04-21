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
	
	public DynamicBest() {	//생성자 생성
		setTitle("Dynamic Beat");
		setSize(Main.SCREEN_WIDTH,Main.SCREEN_HEIGTH); //화면크기 설정
		setResizable(false); //화면크기 변경 x
//		setLocationRelativeTo(null); //창의 위치를 화면의 중심으로 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //창을 종료 했을떄 프로그램이 종료 되도록 설정
		Dimension d = getSize();
		setVisible(true); //화면 표시
		
		/* 메인클래스 위치를 기반으로 이미지를 초기화 */
		introBackground = new ImageIcon(Main.class.getResource("2.jpg")).getImage();
		
		/* 게임이 실행됨과 동시에 인트로 뮤직이 무한 반복 재생 */
		Music introMusic = new Music("introMusic.mp3",true);
		introMusic.start();
		
	}
		public void paint(Graphics g) {
			// JFrame을 상속받는 처음에 화면을 그려주는 메소드
			screenImage = createImage(Main.SCREEN_WIDTH,Main.SCREEN_HEIGTH);
			screenGraphics = screenImage.getGraphics();
			screenDraw(screenGraphics);
			g.drawImage(screenImage, 0, 0, null);
		}//paint
		
		public void screenDraw(Graphics g) {
			g.drawImage(introBackground, 0, 0, null);
			this.repaint(); //다시 paint메소드 호출
		}//screenDraw
	}






















