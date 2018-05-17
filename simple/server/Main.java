import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {

	private ListenThread listen;
	private Logger logger;

	public Main() {
		PropertyConfigurator.configureAndWatch(StringUtil.getWorkingDir() + "/log4j/log4j.properties", 1000);
		logger = Logger.getLogger(this.getClass());
	}
	
	private void listen() {
		logger.debug("listening...");
		listen = new ListenThread(80, 5);
		Thread th = new Thread(listen);
		th.start();
	}
	
	public static void main(String[] args) {
		new Main().listen();
	}
}
