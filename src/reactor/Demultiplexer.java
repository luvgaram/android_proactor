package reactor;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


public class Demultiplexer implements Runnable{
	private final int HEADER_SIZE = 6;
	
	private Socket socket;
	private HandleMap handleMap;
	
	public Demultiplexer(Socket socket, HandleMap handleMap) {
		this.socket = socket;
		this.handleMap = handleMap;
	}
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			
			byte[] buffer = new byte[HEADER_SIZE];
			inputStream.read(buffer);
			String header = new String(buffer);
			
			// HandleMap을 받아서 헤더값으로 get한 EventHandler에게 이벤트 발생
			handleMap.get(header).handleEvent(inputStream);
			
			// 데이터 처리 후 접속 종료
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
