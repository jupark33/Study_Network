import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

/**
 * 1대1 연결만 제공하는 서버 소켓
 */
public class ListenThread implements Runnable {

	private int port;
	private ServerSocket sSocket;
	private int timeOut;
	private boolean stopped;
	private Socket instanceSocket;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	
	private static Logger logger = Logger.getLogger(ListenThread.class);

	public ListenThread(int port, int timeOut) {
		
		this.port = port;
		this.timeOut = timeOut;
	}
	
	public void run() {
		try {
			sSocket = new ServerSocket(port);
//			sSocket.setSoTimeout(timeOut * 1000);
			
			while (!stopped) {
				
				if (sSocket.isClosed()) break;
				
				logger.debug("run(), sSocket.accept(), waiting for client");
				instanceSocket = sSocket.accept();
				logger.debug("run(), client connected : " + instanceSocket.getRemoteSocketAddress());
				bis = new BufferedInputStream(instanceSocket.getInputStream());
				bos = new BufferedOutputStream(instanceSocket.getOutputStream());
				
				InstanceThread instance = new InstanceThread(instanceSocket, bis, bos);
				Thread th = new Thread(instance);
				th.start();
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

}
