import java.util.*;
import java.nio.charset.StandardCharsets;

public class StringToBinary
{
    public static void main (String[] args) {
        String msg = "khate hos ta";
        //ArrayList<Integer> msgBinary = new ArrayList<>();
        String msgBinary ="";
        int counter = 0;

        for (byte b : msg.getBytes(StandardCharsets.UTF_8)) {

            int val = b;

            for (int i = 0; i < 8; i++) {
                if ((val & 128) == 0) {
                    msgBinary+='0';
                }
                else {
                    msgBinary+='1';
                }
                val <<= 1;
                counter++;
            }
        }
        System.out.println(msgBinary.toString());
        System.out.println(counter);
        decode(msgBinary);
    }

    public static void decode (String msgBinary) {
        String binaryMsg = msgBinary.substring(0, 8);//.toString();
        int charCode = Integer.parseInt(binaryMsg, 2);
        System.out.println(binaryMsg);
        System.out.println(Character.toString((char) charCode));
    }

    // public static void decode (ArrayList<Integer> msgBinary) {
    //     int a = 0;
    //     String finalString = "";
    //     String binaryString = "";

    //     for (int i = 0; i < msgBinary.size(); i++ ) {
    //         binaryString += msgBinary.get(i);
            
    //         if (a == 7) {
    //             int decimal = Integer.parseInt(binaryString, 2);
    //             System.out.println(decimal);
    //             finalString += Character.toString((char) decimal);
    //             binaryString = "";
    //             a = 0;
    //         }
    //         else {
    //             a++;
    //         }
            
    //     }
    //     System.out.println(finalString);
    // }

}