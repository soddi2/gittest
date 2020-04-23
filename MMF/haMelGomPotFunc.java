package MMF;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.text.StyleContext.SmallAttributeSet;

import com.mysql.cj.x.protobuf.MysqlxConnection.Close;
import com.mysql.cj.x.protobuf.MysqlxCrud.Limit;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.decoder.*;

public class haMelGomPotFunc implements Runnable {
	haMelGomPotDB hadb = new haMelGomPotDB();
	
	static class Sample{
		private short[] buffer;
		private int length;
		
		public Sample(short[] buf, int s) {
			buffer = buf.clone();
			length = s;
		}
		
		public short[] GetBuffer() {
			return buffer;
		}
		
		public int GetLength() {
			return length;
		}
	}
	
//1. 중간에 javax부분은 볼륨조절용 클래스입니다. 볼륨조절을 안만드시는 분은 빼셔도 됩니다. sample클래스는 디코딩을 위해 javazoom이 제공하는 클래스를 오버라이딩 한 것 입니다.
	
	public static final int BUFFER_SIZE = 10000;
	
	private Decoder decoder;
	private AudioDevice out;
	private ArrayList<Sample> playes;
	private int length;
	
	public boolean isinvaild() {
		return (decoder == null || out == null || playes == null || !out.isOpen());
	}
	
	protected boolean Getplayes(String path) {
		if(isinvaild()) 
			return false;
			try {
				Header header;
				SampleBuffer pb;
				FileInputStream in = new FileInputStream(path);
				Bitstream bitstream = new Bitstream(in);
				
				if((header = bitstream.readFrame())==null) 
					return false;
					
					while(length < BUFFER_SIZE && header !=null) {
						
						pb = (SampleBuffer)decoder.decodeFrame(header,bitstream);
						playes.add(new Sample(pb.getBuffer(), pb.getBufferLength()));
						length++;
						bitstream.closeFrame();
						header = bitstream.readFrame();
						
					}
			}
			 catch (Exception e) {
				return false;
			}
			return true;
	
	} //2. 스트림을 통해 샘플링하여 버퍼에 집어넣습니다.
	
	public boolean Open(String path) {
		try {
			decoder = new Decoder();
			out = FactoryRegistry.systemRegistry().createAudioDevice();
			playes = new ArrayList<Sample>(BUFFER_SIZE);
			length = 0;
			out.open(decoder);
			Getplayes(path);
		} catch (JavaLayerException e) {
			decoder = null;
			out = null;
			playes = null;
			return false;
		}
		return true;
	}//3. db부분에서 불러온 파일주소를 불러오는 메소드
	
	
	private Thread thisThread;
	
	final static int STATE_INIT = 0;
	final static int STATE_STARTED = 1;
	final static int STATE_SUSPENDED = 2;
	final static int STATE_STOPPED = 3;
	
	static int stateCode = STATE_INIT;
	
	public void start(){
		synchronized (this) {
			thisThread = new Thread(this);
			thisThread.start();
			stateCode = STATE_STARTED;
		}
	}
	
	@Override
	public void run() {
		while (true ) {
			if(stateCode == STATE_STOPPED) {
				break;
			}
			play();
		}
		
	}//4. 여러 기능들에서 쓸 상태코드 선언과 play를 위한 start, run메소드 오버라이딩
	
	@SuppressWarnings("static - access")
	public void stop() {
		synchronized (this) {
			this.stateCode = STATE_STOPPED;
			
			try {
				thisThread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("정지");
		}
	}//5. 정지기능을 위한 상태코드(STATE_STOPPED)지정과 정지 작동시 쓰레드를 잠시 멈춤
	
	@SuppressWarnings("static - access")
	public void play() {
		if(isinvaild()) {
			return;
		}
		System.out.println(hadb.Name()+" : 실행 중");
	
		try {
			for(int i=0; i<length; i++) {
				out.write(playes.get(i).GetBuffer(), 0, playes.get(i).GetLength());
				if(stateCode == STATE_STOPPED) {
					Close();
				}
				if(stateCode == STATE_SUSPENDED) {
					System.out.println("일시 정지");
					while(true) {
					try {
						thisThread.sleep(100);
					
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(stateCode == STATE_STARTED || stateCode == STATE_STOPPED) {
						break;
						}
					}
				}
			}
		}catch (JavaLayerException e) { }
			Close();
	}//6. 버퍼에 저장된 데이터를 꺼내서 노래를 실행, 실행 중 다른 상태코드가 진입시 기능 수행(if문)
	
	public void Close() {
		if((out != null ) && !out.isOpen()){
			out.close();
		}
		length = 0;
		playes = null;
		out = null;
		decoder = null;
	}//7. 버퍼에 저장된 데이터를 완전 초기화 시켜서 노래가 끈어지는 역할을 하는 정지 도우미

	public void suspend() {
		if(stateCode == STATE_SUSPENDED) {
			return;
		}else if(stateCode == STATE_INIT) {
			System.out.println("실행중이 아닙니다");
		}else if(stateCode==STATE_STOPPED) {
			System.out.println("정지상태 입니다.");
		}else {
			System.out.println("일시 정지");
			stateCode = STATE_SUSPENDED;
		}
	}
	
	public void resume() {
		if(stateCode == STATE_STARTED || stateCode == STATE_INIT) {
			System.out.println("실행중이 아닙니다.");
			return;
		}
		if(stateCode == STATE_STOPPED) 
			System.out.println("정지상태 입니다.");
			stateCode = STATE_STARTED;
			System.out.println("되돌리기");
	}//8.일시정지와 되돌리기
}
//	static LinkedList<Line> speakers = new LinkedList<Line>();
//
//	final static void findSpeakers() {
//		Mixer.Info[] imxers = AudioSystem.getMixerInfo();
//		
//		for(Mixer.Info mixerInfo : mixers) {
//			if(!mixerInfo.getName().equals("java sound Audio Engine")) continue;
//			
//			Mixer mixer = AudioSystem.getMixer(mixerInfo);
//			Line.Info[] lines = mixer.getSourceLineInfo();
//			
//			for(Line.Info info : lines) {
//				try {
//					Line line = mixer.getLine(info);
//					speakers.add(line);
//					
//				} catch (LineUnavailableException e) {
//					e.printStackTrace(); 
//				} catch (IllegalAccessError e) {
//					// TODO: handle exception
//				}
//			}
//			
//		}
//	}
//	static(findSpeakers());
//
//	public void setVolune(float level) {
//		System.out.println("setting volume to "+level);
//		for(Line line : speakers) {
//			try {
//				line.open();
//				FloatControl control = (FloatControl)line.getControl(FloatControl.Type.MASTER_GAIN);
//				control.setValue(limit(control,level));
//				
//			} catch (LineUnavailableException e) {
//				e.printStackTrace(); 
//			} catch (IllegalAccessError e) {
//				// TODO: handle exception
//			}
//			private static float limit(FloatControl control,float level) {
//				return Math.min(control.getMaximum(), Math.max(control.getMinimum(), level));
//		}
//		
//	}// 9. 볼륨 조절
//}













































