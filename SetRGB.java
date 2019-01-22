import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import javax.imageio.ImageIO;

public class SetRGB
{
    public static void main (String[] args) {
        String imageName = "/home/master/Documents/CS455/455Project/sunset.jpg";
        BufferedImage img = null;
        try {
            //f = new File(imageName);
            img = ImageIO.read(new File(imageName));
        }
        catch (IOException e) {
            System.out.println(e);
        }
        int p = 0;
        String binary = Integer.toBinaryString(-2);
        long l = Long.parseLong(binary, 2);
        System.out.println((int) l);
        //System.out.println(Integer.parseInt("11111111111111111111111111111110", 2));
        for (int i = img.getWidth() - 1; i > img.getWidth() - 3; i-- ) {
            for (int j = 0; j < 2; j++) {
                //System.out.println(img.getRGB(i, j));
                img.setRGB(i, j, (int) Long.parseLong("11111111111111111111111111111110", 2));
                System.out.println(img.getRGB(i, j));
            }
        }
        saveImage(img);
    }

    public static void saveImage (BufferedImage img) {
        File f = null;
        try {
            f = new File("khate.png");
            ImageIO.write(img, "png", f);
            System.out.println("File saved to: " + f.getAbsolutePath());
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}