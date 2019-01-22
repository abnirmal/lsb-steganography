
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.StandardOpenOption;
import java.security.Key;

public class ReceiveFile implements Runnable {
	private static Socket socket1 = null;
	private static Socket socket2 = null;

	private DataInputStream inStream1 = null;
	private DataOutputStream outStream1 = null;

	private DataInputStream inStream2 = null;
	private DataOutputStream outStream2 = null;

	private FileOutputStream fileOut1;
	private FileOutputStream fileOut2;

	public ReceiveFile(Socket socket2) {

	}

	/* Creating five sockets for each thread to receive file */
	public void createSocket1() {
		try {

			socket1 = new Socket("150.243.155.117", 3339);
			inStream1 = new DataInputStream(socket1.getInputStream());
			outStream1 = new DataOutputStream(socket1.getOutputStream());

		} catch (UnknownHostException u) {
			u.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	/*
	 * Creating five "receiveFile" class for each thread to receive the file from
	 * server and download it.
	 */
	private void receiveFile1() {
		try {
			int fileSize = inStream1.readInt();
			byte data[] = new byte[fileSize];
			fileOut1 = new FileOutputStream("received-encrypted-image.png");
			int count = 0, totalBytes = 0;
			while (true) {
				count = inStream1.read(data, 0, fileSize);
				byte[] arrayBytes = new byte[count];
				System.arraycopy(data, 0, arrayBytes, 0, count);
				totalBytes = totalBytes + count;
				if (count > 0) {
					fileOut1.write(arrayBytes);
					fileOut1.flush();
				}
				if (totalBytes == fileSize)
					break;
			}

			socket1.close();
			inStream1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createSocket2() {
		try {
			socket2 = new Socket("150.243.155.117", 3340);
			inStream2 = new DataInputStream(socket2.getInputStream());
			outStream2 = new DataOutputStream(socket2.getOutputStream());

		} catch (UnknownHostException u) {
			u.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	private void receiveFile2() {
		try {
			int fileSize = inStream2.readInt();
			byte data[] = new byte[fileSize];
			fileOut2 = new FileOutputStream("received-KeyFile.xx");
			int count = 0, totalBytes = 0;
			while (true) {
				count = inStream2.read(data, 0, fileSize);
				byte[] arrayBytes = new byte[count];
				System.arraycopy(data, 0, arrayBytes, 0, count);
				totalBytes = totalBytes + count;
				if (count > 0) {
					fileOut2.write(arrayBytes);
					fileOut2.flush();
				}
				if (totalBytes == fileSize)
					break;
			}

			socket2.close();
			inStream2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {

		ReceiveFile fileClient1 = new ReceiveFile(socket1);
		ReceiveFile fileClient2 = new ReceiveFile(socket2);

		Thread t1 = new Thread(new ReceiveFile(socket1)) {
			public void run() {
				fileClient1.createSocket1();
				fileClient1.receiveFile1();
			}
		};

		Thread t2 = new Thread(new ReceiveFile(socket2)) {
			public void run() {
				fileClient2.createSocket2();
				fileClient2.receiveFile2();

			}
		};

		/*
		 * Start the thread, print the status and start time of the thread and let each
		 * thread finish first by using .join()
		 */
		t1.start();
		t2.start();

		t1.join();
		t2.join();

		ObjectInputStream in = new ObjectInputStream(new FileInputStream("received-KeyFile.xx"));
		Key receivedKey = (Key) in.readObject();
		in.close();

		// Implement aes-256 decryption of the encrypted image file containing hidden message
		System.out.println("Client is decrypting the received image file...");
		AES receive = new AES();
		receive.decryptFile("received-encrypted-image.png", receivedKey);
		System.out.println("Successfully decrypted");
		System.out.println();

		//Decode the image to retrieve hidden message
		System.out.println("Decoding the decrypted image file to retrieve hidden message...");
		LSBEncodeDecode retrieve = new LSBEncodeDecode();
		String decodedString = retrieve.decodeImage("output-decrypted.png");
		System.out.println("The hidden text in the image is: " + decodedString);

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}
}