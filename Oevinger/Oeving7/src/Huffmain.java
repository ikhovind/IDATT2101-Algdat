import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Huffmain {
    public static void main(String args[]) throws IOException {
        File input = new File("files/diverse.txt");
        File output = new File("files/output.txt");
        int[] frequencyArray = getFrequencyArray(input.getPath());
        writeToFile(output.getPath(), frequencyArray);
        int[] readFrequencyArray = readFrequencyArray(input.getPath());

    }

    /**
     * Leser frequency array fra en gitt fil
     */
    private static int[] readFrequencyArray(String pathToFile) throws IOException {
        FileReader myReader = new FileReader(pathToFile);
        int[] frequencyArray = new int[256];
        for(int i = 0; i < frequencyArray.length; i++){
            frequencyArray[i] += myReader.read();
        }
        return frequencyArray;
    }
    private static void writeToFile(String pathToFile, int[] frequencyArray) throws IOException {
        FileWriter myWriter = new FileWriter(pathToFile);
        for (int i = 0; i < frequencyArray.length; i++) {
            myWriter.write(frequencyArray[i]);
        }
        myWriter.close();
    }

    public static int[] getHuffmanTree(int[] frequencyArray){
        
        int[] huffmanTree = new int[256];
        return null;
    }

    /**
     * Generer et frequency array av bytes fra en gitt fil
     *
     * Denne lagrer antall gitte bytes inn i et gitt array, tror det er derfor man får rare
     * karakterer fordi enkelte karakterer lagres som mer enn én byte
     * Har sjekket med https://stackoverflow.com/questions/13173223/huffman-coding-dealing-with-unicode
     * og denne får akkurat samme svar for alle testfilene
     */
    private static int[] getFrequencyArray(String pathToFile) throws IOException {
        //todo trenger sikkert ikke å lese hele fila på en gang, kan lese i løkka lenger nede
        // gjetter jeg, kanskje bruke outputstream

        byte[] fileContent = Files.readAllBytes(Path.of(pathToFile));
        int[] frequencyArray = new int[256];
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

        får rare bokstaver dersom man prøver å konvertere byte og byte til char, dette er fordi
        enkelte karakterer slik som å må ha mer enn én byte, men vi lagrer jo begge bytsene så
        det går jo bra
     */

        for(int j = 0; j < fileContent.length; j++){
            frequencyArray[(fileContent[j] + 256)%256]++;
        }
        return frequencyArray;
    }
}
