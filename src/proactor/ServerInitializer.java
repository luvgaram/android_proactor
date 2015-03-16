package proactor;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServerInitializer {
	
	private static int PORT = 5000;
	private static int threadPoolSize = 8;
	private static int initialSize = 4;
		
	// backlog = queue 일종
	private static int backlog = 50;
	
	public void startServer() {
		System.out.println("SEVER START!");
		
		NioHandleMap handleMap = new NioHandleMap();
		
		NioEventHandler sayHelloHandler = new NioSayHelloEventHandler();
		NioEventHandler sayUpdateProfileHandler = new NioUpdateProfileEventHandler();
		
		handleMap.put(sayHelloHandler.getHandle(), sayHelloHandler);
		handleMap.put(sayUpdateProfileHandler.getHandle(), sayUpdateProfileHandler);
		
		// 고정 스레드 풀 생성 threadPoolSize 만큼의 스레드만 사용
		ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
		
		// 캐시 스레드 풀 생성 - 초기에 스레드를 만들고 새 스레드는 필요한 만큼만 생성, 이전 스레드 재사용 가능
		try {
			// 비동기 채널 사용											스레드풀 적용
			AsynchronousChannelGroup group = AsynchronousChannelGroup.withCachedThreadPool(executor, initialSize);
			
			// 스트림 지향의 리스닝 소켓을 위한 비동기 채널
			AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
			
			// 포트 지정해서 바인드
			listener.bind(new InetSocketAddress(PORT), backlog);
			
			// 비동기 IO 작업에 콜백 등록해두고 완료/실패시 CompletionHandler 호출해서 접속 결과 받음
			listener.accept(listener, new Dispatcher(handleMap));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
