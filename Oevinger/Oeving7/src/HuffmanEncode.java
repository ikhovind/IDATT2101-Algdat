import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class HuffmanEncode {
    static final int ANT_TEGN = 256;
    public static void main(String[] args) throws IOException {
        compress("files/lempelZivCompressed.txt","files/huffmanCompressed.txt");
    }

    private static void compress(String pathToFile, String pathToCompressed) throws IOException {
        File input = new File(pathToFile);
        File output = new File(pathToCompressed);
        int[] frequencyArray = getFrequencyArray(input);
        String[] encodings = HuffmanTree.getEncodingArray(frequencyArray);

        byte[] inputFile = Files.readAllBytes(input.toPath());

        String fileToString = "";
        FileWriter myWriter = new FileWriter(output.getPath());
        for (int i = 0; i < frequencyArray.length; i++) {
            myWriter.write(frequencyArray[i]);
        }

        myWriter.close();
        FileOutputStream fileOutputStream = new FileOutputStream(output.getPath(), true);
        fileToString += encodings[256];
        for (byte b : inputFile) {
            fileToString += encodings[(b + ANT_TEGN) % ANT_TEGN];
            //slipper å behandle hele filen som en string, tenkt som et forsøk på å håndtere store filer
            if (fileToString.length() % 64 == 0 && fileToString.length() > 0){
                fileOutputStream.write(getEncodedByteArray(fileToString));
                fileToString = "";
            }
        }
        fileToString += encodings[ANT_TEGN];
        //sparer til 8 bits
        while((fileToString).length() % 8 != 0) fileToString += "0";
        fileOutputStream.write(getEncodedByteArray(fileToString));
        fileOutputStream.close();
    }

    private static byte[] getEncodedByteArray(String fileToString) {
        ArrayList<Byte> encodedBytes = new ArrayList<>();

        while(fileToString.length() > 0){
            //når man decoder så må man simpelthen ignorerer det første sifferet i hver byte som man leser
            encodedBytes.add((byte) Integer.parseInt((fileToString.length() >= 7 ? '1' + fileToString.substring(0,7) : '1' + fileToString),2));
            //dersom lengden er over 7 så lages det ny substring der. Hvis lengden er under 7 så har vi lagt til hele og stringen blir tom
            fileToString = fileToString.substring(Math.min(fileToString.length(), 7));
        }

        byte[] answer = new byte[encodedBytes.size()];
        IntStream.range(0,encodedBytes.size()).forEach(i->answer[i] = encodedBytes.get(i));

        return answer;
    }

    private static int[] getFrequencyArray(File file) throws IOException {

        byte[] fileContent = Files.readAllBytes(file.toPath());
        int[] frequencyArray = new int[ANT_TEGN + 1];

        //gjør om negative bytes til positive int, med modulo 256 så slipper man å sjekke om byten er positiv, da
        //(x + 256) mod 256 gir naturligvis x dersom x er et positivt tall
        for (int j = 0; j < file.length(); j++) {
            frequencyArray[(fileContent[j] + ANT_TEGN) % ANT_TEGN]++;
        }
        frequencyArray[frequencyArray.length - 1] = 1;
        return frequencyArray;
    }
}