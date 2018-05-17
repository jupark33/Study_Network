import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import org.apache.log4j.Logger;

public class InstanceThread implements Runnable {

	private boolean stopped;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	private Logger logger;
	private Socket socket;
	
	public InstanceThread(Socket socket, BufferedInputStream bis, BufferedOutputStream bos) {
		this.socket = socket;
		this.bis = bis;
		this.bos = bos;
		logger = Logger.getLogger(this.getClass());
	}

	public void run() {
		readData();
	}
	
	private void readData() {
		int cReaded;
		byte[] readBuff = new byte[1024];
		
		logger.debug("--readData() start--" + StringUtil.lineFeed);
		try {
			while((cReaded = bis.read(readBuff)) > 0) {
				String strBuf = new String(readBuff, 0, cReaded);
				logger.debug("[received from : " + socket.getRemoteSocketAddress() + "] "+ strBuf);
			}
		} catch (SocketException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		logger.debug("--readData() end--" + StringUtil.lineFeed);
	}
	
}
