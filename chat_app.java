import java.io.*;
import java.net.Socket;
import java.net.SocketException;


public class chat_app {
	private Socket socket = null;
	private InputStream input = null;
	private OutputStream output = null;

	public chat_app() {
		socket = this.socket;
		input = this.input;
		output = this.output;
	}

	public void createSocket() {
		try {
			socket = new Socket("localHost", 3339);
			System.out.println("Connected Successfully :::");
			input = socket.getInputStream();
			output = socket.getOutputStream();
			createReadThread();
			createWriteThread();
		} catch (Exception u) {
			u.printStackTrace();
		}
	}

	public void createReadThread() {
		Thread readThread = new Thread() {
			public void run() {
				while (socket.isConnected()) {

					try {
						byte[] readBuffer = new byte[200];
						int num = input.read(readBuffer);

						if (num > 0) {
							byte[] arrayBytes = new byte[num];
							System.arraycopy(readBuffer, 0, arrayBytes, 0, num);
							String recvedMessage = new String(arrayBytes, "UTF-8");
							System.out.println("Received message :::::" + recvedMessage);
						};
					} catch (SocketException se) {
						System.exit(0);

					} catch (IOException e) {
						e.printStackTrace();
					} 

				}
			}
		};
		readThread.setPriority(Thread.MAX_PRIORITY);
		readThread.start();
	}

	public void createWriteThread() {
		Thread writeThread = new Thread() {
			public void run() {
				while (socket.isConnected()) {

					try {
						BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
						sleep(200);
						String typedMessage = inputReader.readLine();
						if (typedMessage != null && typedMessage.length() > 0) {
							synchronized (socket) {
								output.write(typedMessage.getBytes("UTF-8"));
								sleep(100);
							}
						}
						;

					} catch (IOException i) {
						i.printStackTrace();
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
				}
			}
		};
		writeThread.setPriority(Thread.MAX_PRIORITY);
		writeThread.start();
	}

	public static void main(String[] args) throws Exception {
		chat_app redis = new chat_app();
		redis.createSocket();
		
	}
}