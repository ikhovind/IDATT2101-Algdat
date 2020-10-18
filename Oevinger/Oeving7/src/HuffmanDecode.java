import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class HuffmanDecode {
    private static final int ANT_TEGN = 256;

    public static void main(String[] args) throws IOException {
        deCompress("files/encoded.txt","files/decoded.txt");
    }

    private static void deCompress(String pathToCompressed, String pathToDecompressed) throws IOException {

        File input = new File(pathToCompressed);
        File output = new File(pathToDecompressed);
        byte[] decoded = Files.readAllBytes(input.toPath());
        int[] frequencyArray = readFrequencyArray(input);
        String kanskje = "";
        String[] encodings = HuffmanTree.getEncodingArray(frequencyArray);
        //alle tegn i fila som ikke er del av frekvensarrayet
        for(int i = frequencyArray.length; i < decoded.length; i++){
            kanskje += (Integer.toBinaryString((decoded[i]+ANT_TEGN)%ANT_TEGN)).substring(1);
        }

        //jeg har satt inn encodingen til 256 som en break i den komprimerte fila, denne fjerner break som er først og sist
        kanskje = kanskje.substring(0,kanskje.lastIndexOf(encodings[ANT_TEGN]));
        //TODO kan optimalisere ved å ikke skrive hele stringen på en gang, skriv litt og litt inne i for-løkka over
        new FileOutputStream(output.getPath()).write(getDecodedByteArray(kanskje,frequencyArray));
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

    private static byte[] getDecodedByteArray(String kanskje, int[] frequencyArray) {
        String[] encodings = HuffmanTree.getEncodingArray(frequencyArray);
        //TODO helge underkjenner om vi bruker hashmap, spør om det går greit
        HashMap<String, Integer> encodedValues = new HashMap<>();
        for (int i = 0; i < frequencyArray.length; i++) {
            if (!encodings[i].equals("")) {
                encodedValues.put(encodings[i],i);
            }
        }

        ArrayList<Byte> unknownSizeByteArray = new ArrayList<>();
        //går gjennom hele stringen med tegn og finner de reelle verdiene
        for(int i = 0; i < kanskje.length()+1; i++){
            //dersom vi har kommet til en substring som har en assosiert verdi
            if(encodedValues.get(kanskje.substring(0, i))!= null){
                //legger det til det reelle tegnet i arrayet som vi senere skal skrive ut
                unknownSizeByteArray.add(encodedValues.get(kanskje.substring(0, i)).byteValue());
                //flytter punkt 0 fram
                kanskje = kanskje.substring(i);
                //restarter letingen siden vi fjernet tegnene vi brukte
                i=0;
            }
        }
        byte[] test = new byte[unknownSizeByteArray.size()];
        for(int i = 0; i < unknownSizeByteArray.size(); i++){
            test[i] = unknownSizeByteArray.get(i);
        }
        return test;
    }

}
