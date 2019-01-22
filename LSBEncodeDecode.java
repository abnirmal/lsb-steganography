import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.Color;
import javax.imageio.ImageIO;
import java.nio.charset.StandardCharsets;

public class LSBEncodeDecode
{
    /**
     * empty constructor to act as an insantiation when receiving file
     */
    public LSBEncodeDecode() {

    }

    /**
     * Encode image using LSB steganography
     * @param message message to be encoded
     * @param imageName path to image
     * @return
     */
    public String encode (String message, String imageName) {
        BufferedImage i = null;
        BufferedImage img = null;
        Scanner in = new Scanner (System.in);
        String msg = encodeUTF8(message);
        //System.out.println(msg.length()/8);

        msg += "00001010";
        //System.out.println(msg.length()/8);
        
        try {
            i = ImageIO.read(new File(imageName));
            img = new BufferedImage(i.getWidth(), i.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D graphics = img.createGraphics();
            graphics.drawRenderedImage(i, null);
            graphics.dispose();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        System.out.println("Image type: " + i.getType());
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int currentRow = 0;
        int currentColumn = 0;
        int counter = 0;

        System.out.println("Max message length = " + ((imgWidth * imgHeight)/64 - 1) + " characters.");

        if (msg.length() * 8 > (imgWidth * imgHeight)) {
            System.out.println("Message length " + msg.length() * 8 + " > image " + imgWidth * imgHeight + " pixels.");
            return null;
        }
        
        while (counter < msg.length()) {
            //System.out.println(currentRow + ", " + currentColumn);
            int rgb = img.getRGB(currentRow, currentColumn);
            String rgbOld = Integer.toBinaryString(rgb);
                        
            String rgbNew = rgbOld.substring(0, rgbOld.length() - 1) + msg.charAt(counter);            
            
            int newRGB = (int) Long.parseLong(rgbNew, 2);
            img.setRGB(currentRow, currentColumn, newRGB);

            if (currentColumn < imgHeight - 1) {
                currentColumn++;
            }
            else {
                currentRow++;
                currentColumn = 0;
            }
            counter++;
        }
        return saveImage(img);
    }

    /**
     * Encode string into its corresponding UTF-8 binary values
     * @param message actual message
     * @return UTF-8 binary equivalent of each character in message
     */
    public static String encodeUTF8(String message) {
        String msgBinary = "";

        for (byte b : message.getBytes(StandardCharsets.UTF_8)) {

            int val = b;

            for (int i = 0; i < 8; i++) {
                if ((val & 128) == 0) {
                    msgBinary += '0';
                }
                else {
                    msgBinary += '1';
                }
                val <<= 1;
            }
        }
        return msgBinary;
    }


    /**
     * Save image into an output.png file
     * @param img modified image buffer to save
     * @return name or path to encoded image
     */
    public String saveImage (BufferedImage img) {
        File f = null;
        String fileName = "output.png";
        try {
            f = new File(fileName);
            ImageIO.write(img, "png", f);
            System.out.println("File saved to: " + f.getAbsolutePath());
            return f.getAbsolutePath();
        }
        catch (IOException e) {
            System.out.println(e);
            return "\n";
        }
    }

    /**
     * Decode stego-image
     * @param imageName name or path to encoded image
     * @return hidden message
     */
    public String decodeImage (String imageName) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imageName));
        }
        catch (IOException e) {
            System.out.println(e);
        }

        boolean done = false;
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int currentRow = 0;
        int currentColumn = 0;
        int counter = 0;
        String binaryMsg = "";
        String msg = "";
        String finalmsg = "";
        
        while (!done) {
            //System.out.println(currentRow + ", " + currentColumn);

            int rgb = img.getRGB(currentRow, currentColumn);
            
            String binaryRGB = Long.toBinaryString( Integer.toUnsignedLong(rgb) | 0x100000000L ).substring(1);

            binaryMsg += binaryRGB.charAt(binaryRGB.length() - 1);
                
            if (counter == 7) {
                msg = Character.toString((char) Integer.parseInt(binaryMsg, 2));
                finalmsg += msg;
                //System.out.print(msg);
                if (msg.equals("\n"))
                    done = true;
                counter = 0;
                binaryMsg = "";
            }
            else {
                counter++;
            }

            if (currentColumn < imgHeight - 1) {
                currentColumn++;
            }
            else {
                currentRow++;
                currentColumn = 0;
            }
        }
        return finalmsg;
    
    }
    
}