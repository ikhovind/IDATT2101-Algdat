import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanDecode {
    private static final int ANT_TEGN = 256;

    public static void main(String[] args) throws IOException {
        deCompress("files/huffmanCompressed.txt","files/huffmanDecompressed.txt");
    }

    private static void deCompress(String pathToCompressed, String pathToDecompressed) throws IOException {

        File input = new File(pathToCompressed);
        File output = new File(pathToDecompressed);
        byte[] decoded = Files.readAllBytes(input.toPath());
        int[] frequencyArray = readFrequencyArray(input);
        String encodedBits = "";
        String[] encodings = HuffmanTree.getEncodingArray(frequencyArray);
        //alle tegn i fila som ikke er del av frekvensarrayet
        for(int i = frequencyArray.length; i < decoded.length; i++){
            encodedBits += (Integer.toBinaryString((decoded[i]+ANT_TEGN)%ANT_TEGN)).substring(1);
        }
        //jeg har satt inn encodingen til 256 som en break i den komprimerte fila, denne fjerner break som er først og sist
        encodedBits = encodedBits.substring(encodedBits.indexOf(encodings[ANT_TEGN]) + encodings[ANT_TEGN].length(),encodedBits.lastIndexOf(encodings[ANT_TEGN]));


        new FileOutputStream(output.getPath()).write(getDecodedByteArray(encodedBits,frequencyArray));
    }

    /**
     * Leser frequency array fra en gitt komprimert fil
     */
    private static int[] readFrequencyArray(File file) throws IOException {
        FileReader myReader = new FileReader(file.getPath());
        int[] frequencyArray = new int[ANT_TEGN + 1];
        for (int i = 0; i < frequencyArray.length; i++) {
            frequencyArray[i] += myReader.read();
        }
        myReader.close();
        return frequencyArray;
    }

    /**
     * dekoder-bytene i string-parameteren ved å bruke frekvensarrayet som føres inn
     */
    private static byte[] getDecodedByteArray(String encodedBits, int[] frequencyArray) {
        String[] encodings = HuffmanTree.getEncodingArray(frequencyArray);

        HashMap<String, Integer> encodingsMappedToRealValue = new HashMap<>();
        for (int i = 0; i < frequencyArray.length; i++) {
            if (!encodings[i].equals("")) {
                encodingsMappedToRealValue.put(encodings[i],i);
            }
        }

        ArrayList<Byte> unknownSizeByteArray = new ArrayList<>();
        //går gjennom hele stringen med tegn og finner de reelle verdiene
        for(int i = 0; i < encodedBits.length()+1; i++){
            //dersom vi har kommet til en substring som har en assosiert verdi
            if(encodingsMappedToRealValue.get(encodedBits.substring(0, i))!= null){
                //legger det til det reelle tegnet i arrayet som vi senere skal skrive ut
                unknownSizeByteArray.add(encodingsMappedToRealValue.get(encodedBits.substring(0, i)).byteValue());
                //flytter punkt 0 fram
                encodedBits = encodedBits.substring(i);
                //restarter letingen siden vi fjernet tegnene vi brukte
                i=0;
            }
        }
        byte[] primitiveByteArray = new byte[unknownSizeByteArray.size()];
        for(int i = 0; i < unknownSizeByteArray.size(); i++){
            primitiveByteArray[i] = unknownSizeByteArray.get(i);
        }
        return primitiveByteArray;
    }

}
