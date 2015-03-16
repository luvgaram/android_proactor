package reactor;
import java.io.InputStream;


public interface EventHandler {
	// Handler의 고유 키값을 반환 => HandlerMap에 키값 등록 용도
	public String getHandler();
	
	// 데이터 받아서 처리
	public void handleEvent(InputStream inputStream);
}
