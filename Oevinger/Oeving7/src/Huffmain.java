import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Huffmain {
    public static void main(String args[]) throws IOException {
        //TODO SE PÅ KODEN HAN HAR LAGT UT PÅ OPPGAVEN OG SE OM DEN HJELPER
        //får problemer med •
        //todo bytte til unicode?
        File input = new File("files/diverse.txt");
        File output = new File("files/output.txt");
        int[] frequencyArray = getFrequencyArray(input.getPath());
        for(int i = 0; i < frequencyArray.length; i++){
            if(frequencyArray[i] > 0){

               System.out.println(((char) i )  + " " + i + ": " + frequencyArray[i]);
            }
        }

        //writeToFile(output, frequencyArray);
        //https://www.ascii-code.com/
    }

    private static void writeToFile(File output, int[] frequencyArray) throws IOException {
        FileWriter myWriter = new FileWriter(output.getPath());
        for (int i = 0; i < frequencyArray.length; i++) {
            myWriter.write(frequencyArray[i]);
        }
        myWriter.close();
    }

    public static int[] getHuffmanTree(){
        return null;
    }
    private static int[] getFrequencyArray(String pathToFile) throws IOException {
        //todo trenger sikkert ikke å lese hele fila på en gang, kan lese i løkka lenger nede
        // gjetter jeg, kanskje bruke outputstream

        byte[] fileContent = Files.readAllBytes(Path.of(pathToFile));
        int[] frequencyArray = new int[fileContent.length];
    /*
        char gir tall fra -128 - 127, men pga hvordan overflow funker så kan man ikke
         konvertere fra negative tall til char, trenger derimot ikke å bruke annen encoding, da
         255 tegn er nok for å få med det norske alfabetet

        dersom man får overflow så må den være over 127, derav første + 128
        når man får overflow så telles det som overflower opp fra -128, så dersom man f.eks
         får -28 så har den 100 overflow pga -128 + 100 = -28.
         -28 + 128 = 100, derav andre + 128
        med modulo 256 så slipper man å sjekke om byten er < 0, da man får riktig svar uansett :)
         (x + 256) mod 256 gir naturligvis x dersom x er et positivt tall

     */

        for(int j = 0; j < fileContent.length; j++){
            frequencyArray[j] = (fileContent[j] + 256)%256;
        }
        writeToFile(new File("files/output.txt"), frequencyArray);
        return frequencyArray;
    }

}
