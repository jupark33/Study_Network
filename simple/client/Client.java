import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class Client extends Thread {

	private static Logger logger;
	private Socket socket;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;
	
	public Client(Socket socket) {
		logger = Logger.getLogger(this.getClass());
		this.socket = socket;
		
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			bos = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void run() {
		try {
			if (!socket.isClosed()) readData(bis);
		} catch (Exception e) {
			logger.debug("run() Exception");
			logger.error(e);
		}
	}
	
	private void readData(InputStream is) throws IOException {
		int cReaded;
		byte[] readBuff = new byte[1024];
		
		logger.debug("--readData() start--" + StringUtil.lineFeed);
		while((cReaded = is.read(readBuff)) > 0) {
			String strBuf = new String(readBuff, 0, cReaded);
			logger.debug(strBuf);
		}
		logger.debug("--readData() end--" + StringUtil.lineFeed);
	}
	
	/**
	 * 서버로 print 요청 메시지 전송
	 */
	public void send_req_print() {
		String msg = "req_print";
		sendMsg(msg);
	}
	
	private void sendMsg(String msg) {
		logger.debug("sendMsg msg : " + msg);
		
		if (bos == null) return;
		try {
			bos.write(msg.getBytes());
			bos.flush();
		} catch (IOException e) {
			logger.debug(e);
		} catch (Exception e) {
			logger.debug(e);
		}
	}
}
