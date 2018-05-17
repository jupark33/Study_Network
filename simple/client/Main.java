import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	private Socket cSocket;
	private String ip;
	private int port;
	private int timeOut;
	private static Logger logger;
	
	public Main(String ip, int port, int timeOut) {
		PropertyConfigurator.configureAndWatch(StringUtil.getWorkingDir() + "/log4j/log4j.properties", 1000);
		logger = Logger.getLogger(this.getClass());
		
		this.ip = ip;
		this.port = port;
		this.timeOut = timeOut;
	}

	private void print() {
		Client client = connect(ip, port, timeOut);
		if (client != null) client.send_req_print();
	}
	
	private Client connect(String ip, int port, int timeOut) {
		Client client = null;
		
		InetSocketAddress is = new InetSocketAddress(ip, port);
		cSocket = new Socket();
		try {
			cSocket.connect(is, timeOut * 1000);
			cSocket.setSoTimeout(timeOut * 1000);
			
			if (cSocket.isConnected()) {
				client = new Client(cSocket);
				client.start();
				
				InetAddress inetAddr = cSocket.getInetAddress();
				logger.debug("[서버에 연결됨] : " + inetAddr);
			}
		} catch (IOException e) {
			logger.debug(e);
		}
		
		return client;
	}
	
	public static void main(String[] args) {
		new Main("127.0.0.1", 80, 5).print();
	}
}
