package dynamic_best_3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music extends Thread {
	
	private Player player; //jl���̺귯�� => mp3������ java���� �����ϱ� ���� ���̺귯��
	private boolean isLoop; //���� ����Ǵ� ������ ���ѹݺ����� �����ϰ� �� ����
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	
	public Music(String name,boolean isLoop) { //���� ����, ���� �ݺ� ����
		try {
			this.isLoop = isLoop;
			file = new File(Main.class.getResource("../music/"+name).toURI());
			fis = new FileInputStream(file); //�ش� ���������� �ҷ��´�
			bis = new BufferedInputStream(fis); // �ҷ��� ���������� ���ۿ� ��´�
			player = new Player(bis);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}//music
	
	public int getTime() {//������ ����Ǵ� ��ġ
		if(player == null) {
			return 0;
		}
		return player.getPosition();
	}//getTime
	
	public void close() { //������ ��� ��ġ���� ����Ǵ��� ����� �� �ֵ���
		isLoop = false;
		player.close();
		this.interrupt(); //�ش罺���带 ����
	}//close

	@Override
	public void run() {
		try {
			do {
				player.play();
				fis = new FileInputStream(file); // �ش� ���� ������ �ҷ��´�
				bis = new BufferedInputStream(fis); // �ҷ��� ���� ������ ���ۿ� ��´�
				player = new Player(bis);
			}while(isLoop); // isLoop�� true���̶�� ���ѹݺ�
		} catch (Exception e) {
			System.out.println(e.getMessage()); //���� �޼��� ����
		}
	}//run
}

















