package reactor;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;


class HandlerListData {

	// ElementList로 handler지정
	@ElementList(entry="handler", inline=true)
	private List<HandlerData> handler;
	
	// 서버 이름 구분 위한 Attribute 설정
	@Attribute
	private String name;
	
	public List<HandlerData> getHandler() {
		return handler;
	}
	
	public String getName() {
		return name;
	}
	
}
