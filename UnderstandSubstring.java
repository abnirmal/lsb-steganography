import java.io.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Color;
import javax.imageio.ImageIO;

public class UnderstandSubstring
{
    public static void main (String[] args) {
        String aString = "Hello World";
        System.out.println(aString.substring(0, 2) + aString.substring(9, 11));
        BufferedImage img = null;
        String imageName = "/home/master/Documents/CS455/455Project/sunset.jpg";
        try {
            img = ImageIO.read(new File(imageName));
        }
        catch (IOException e) {
            System.out.println(e);
        }
        System.out.println(Integer.toBinaryString(img.getRGB(0, 0)));
    }
}