package reactor;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


public class ServerInitializer {

	public void startServer() {
		int port = 5000;
		System.out.println("Server On :" + port);
		
		Reactor reactor = new Reactor(port);
		
		// SimpleFramework가 제공하는 기능: xml파일을 객체로 생성 
		try {
			Serializer serializer = new Persister();
			File source = new File("HandlerList.xml");
			
			ServerListData serverList = serializer.read(ServerListData.class, source);
			
			// server 정보 가져옴
			for (HandlerListData handlerListData : serverList.getServer()) {
				
				// server1, server2 중 server1이면
				if ("server1".equals(handlerListData.getName())) {
					
					// server1의 handler들을 받아옴
					List<HandlerData> handlerList = handlerListData.getHandler();
					
					for (HandlerData handler : handlerList) {
						try {
							// HandlerMap에 클래스 등록        "0x5001"                                         StreamSayHelloEventHandler
							reactor.registerHandler(handler.getHeader(), (EventHandler) Class.forName(handler.getHandler()).newInstance());
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
//		
//		reactor.registerHandler(new StreamSayHelloEventHandler());
//		reactor.registerHandler(new StreamUpdateProfileEventHandler());
		
		reactor.startServer();
	}
}
