package reactor;
import java.util.List;

import org.simpleframework.xml.ElementList;


public class ServerListData {

	// ElementList로 server를 지정
	@ElementList(entry="server", inline=true)
	private List<HandlerListData> server;
	
	public List<HandlerListData> getServer() {
		return server;
	}
}
