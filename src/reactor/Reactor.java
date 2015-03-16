package reactor;
import java.io.IOException;
import java.net.ServerSocket;

// 연결을 받아 demultiplex하고 HandleMap에 헤더인 key값과 EventHandler를 연결하는 역할
public class Reactor {

	private ServerSocket serverSocket;
	private HandleMap handleMap;
	
	public Reactor(int port) {
		// Reactor에서 관리한 HandleMap 생성 
		handleMap = new HandleMap();
		
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startServer() {
		
		// Dispatcher 선택 가능 
		// Dispatcher dispatcher = new ThreadPerDispatcher();		
		Dispatcher dispatcher = new ThreadPoolDispatcher();
		dispatcher.dispatch(serverSocket, handleMap);
	}
	
	// xml에서 헤더명을 가져와서 등록할 경우를 위한 오버로딩
	public void registerHandler(String header, EventHandler handler) {
		handleMap.put(header,  handler);
	}
	
	public void registerHandler(EventHandler handler) {
		//           핸들러 헤더 등록("0x5001"), 핸들러 등록
		handleMap.put(handler.getHandler(), handler);
	}
	
	public void removeHandler(EventHandler handler) {
		handleMap.remove(handler.getHandler());
	}
}
