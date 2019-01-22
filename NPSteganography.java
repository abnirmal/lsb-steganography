import java.util.Scanner;
import java.util.ArrayList;
import java.net.*;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

public class NPSteganography
{
    public static void main (String[] args) {
        if (args.length == 0) {
            System.err.println("Please provide path to image");
            System.exit(-1);
        }
        String imageName = args[0];
        Scanner in = new Scanner(System.in);
        String msg = in.nextLine();

        BufferedImage img = null;
        File f = null;
        try {
            f = new File(imageName);
            img = ImageIO.read(f);
        }
        catch (IOException e) {
            System.out.println(e);
        }
        
        LSBEncodeDecode lsb = new LSBEncodeDecode();
        String encodedFile = lsb.encode(msg, imageName);
        if (encodedFile == null) {
            System.err.println("Message size too long");
        }
        else if (encodedFile.equals("\n")) {
            System.out.println(encodedFile);
        }
        else {
            System.out.println(lsb.decodeImage(encodedFile));
        }
    }

}