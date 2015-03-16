package proactor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class Demultiplexer implements CompletionHandler<Integer, ByteBuffer> {

	private AsynchronousSocketChannel channel;
	private NioHandleMap handleMap;
	
	public Demultiplexer(AsynchronousSocketChannel channel, NioHandleMap handleMap) {
		this.channel = channel;
		this.handleMap = handleMap;
	}
	
	@Override
	public void completed(Integer result, ByteBuffer buffer) {
	
		// result가 -1이면 접속 종료
		if (result == -1) {
			try {
				channel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (result > 0) {
			// 읽기, 쓰기 모드 전환
			buffer.flip();
			
			String header = new String(buffer.array());
			
			NioEventHandler handler = handleMap.get(header);
			
			ByteBuffer newBuffer = ByteBuffer.allocate(handler.getDataSize());
			
			handler.initialize(channel, newBuffer);
			channel.read(newBuffer, newBuffer, handler);
			
//			String msg = new String(buffer.array());
//			System.out.println("echo: " + msg);
//			
//			// echo하기
//			buffer = ByteBuffer.wrap((msg.getBytes()));
//			
//			// 비동기식 연산의 결과를 나타냄
//			Future<Integer> w = channel.write(buffer);
//			
//			try {
//				w.get(); // 블록 
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//			
//			try {
//				buffer.clear();
//				channel.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

	@Override
	public void failed(Throwable exc, ByteBuffer buffer) {
		// TODO Auto-generated method stub
		
	}

}
