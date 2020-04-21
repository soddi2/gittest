package dynamic_best_3;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class Music extends Thread {
	
	private Player player; //jl라이브러리 => mp3파일을 java에서 실행하기 위한 라이브러리
	private boolean isLoop; //현재 재생되는 음악이 무한반복될지 설정하게 될 변수
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;
	
	public Music(String name,boolean isLoop) { //곡의 제목, 무한 반복 여부
		try {
			this.isLoop = isLoop;
			file = new File(Main.class.getResource("../music/"+name).toURI());
			fis = new FileInputStream(file); //해당 음악파일을 불러온다
			bis = new BufferedInputStream(fis); // 불러온 음악파일을 버퍼에 담는다
			player = new Player(bis);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}//music
	
	public int getTime() {//음악이 실행되는 위치
		if(player == null) {
			return 0;
		}
		return player.getPosition();
	}//getTime
	
	public void close() { //음악이 어느 위치에서 실행되더라도 종료될 수 있도록
		isLoop = false;
		player.close();
		this.interrupt(); //해당스레드를 종료
	}//close

	@Override
	public void run() {
		try {
			do {
				player.play();
				fis = new FileInputStream(file); // 해당 음악 파일을 불러온다
				bis = new BufferedInputStream(fis); // 불러온 음악 파일을 버퍼에 담는다
				player = new Player(bis);
			}while(isLoop); // isLoop이 true값이라면 무한반복
		} catch (Exception e) {
			System.out.println(e.getMessage()); //오류 메세지 전송
		}
	}//run
}

















