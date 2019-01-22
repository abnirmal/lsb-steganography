import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class AES {

	static File file = null;
	static KeyGenerator keyGenerator;
	static Key key; 
	static byte[] encrypted; 

	public AES(File imageFile) {
		this.file = imageFile;
	}
	
	public AES()
	{
		
	}

	public void encryptFile() throws NoSuchAlgorithmException, IOException {
		InputStream is = null;
		//System.out.println(file.getName());
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		byte[] content = null;
		try {
			content = new byte[is.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			is.read(content);
		} catch (IOException e) {
			e.printStackTrace();
		}

		keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);
		key = keyGenerator.generateKey();
		//System.out.println(key);
		
		
		//Store key in a file.
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("KeyFile.xx"));
        out.writeObject(key);
        out.close();

		Cipher cipher;
		encrypted = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypted = cipher.doFinal(content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileOutputStream fos = new FileOutputStream("output-encrypted.png");
		fos.write(encrypted);
		fos.close();
		
	}
	
	public void decryptFile(String fileName, Key receivedKey) throws IOException
	{
		InputStream is = null;
		//Convert encrypted file created by the server
		//into array of bytes
		File fileEnc = new File(fileName); 
		try {
			is = new FileInputStream(fileEnc);
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}

		byte[] contentEnc = null;
		try {
			contentEnc = new byte[is.available()];
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			is.read(contentEnc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, receivedKey);
            decrypted = cipher.doFinal(contentEnc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        FileOutputStream fos = new FileOutputStream("output-decrypted.png");
		fos.write(decrypted);
		fos.close();
		
		
	}
}
