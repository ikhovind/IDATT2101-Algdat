import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Huffmain {
    static final int ANT_TEGN = 256;
    //TODO output blir større en input xoxox, tror det blir sånn når det er mange forskjellige
    // tegn, kanskje huffmantreet mitt er litt mindre enn optimalt?
    public static void main(String[] args) throws IOException {
        compress("files/diverse.txt", "files/encoded.txt");
        deCompress("files/encoded.txt","files/decoded.txt");
    }

    public static void compress(String pathToFile, String pathToCompressedFile) throws IOException {
        File input = new File(pathToFile);
        File output = new File(pathToCompressedFile);
        int[] frequencyArray = getFrequencyArray(input);

        String[] encodings = getEncodingArray(frequencyArray);



        byte[] inputFile = Files.readAllBytes(input.toPath());
        String fileToString = "";
      //  fileToString += encodings[ANT_TEGN];
        for(int i = 0; i < inputFile.length; i++){
            fileToString += encodings[(inputFile[i] + ANT_TEGN)%ANT_TEGN];
        }
        fileToString += encodings[ANT_TEGN];
        while ( fileToString.length() % 8 != 0) {
            //må spare til 8 bits
            fileToString += "0";
        }
        System.out.println(fileToString);

/*
        FileWriter clearFile = new FileWriter(output.getPath());
        for (int i = 0; i < frequencyArray.length; i++) {
            clearFile.write(frequencyArray[i]);
        }
        clearFile.close();
*/


        FileWriter appendFile = new FileWriter(output.getPath());
        int[] encodedIntArray = getEncodedByteArray(fileToString);
        for (int i = 0; i < encodedIntArray.length; i++) {
            appendFile.write(encodedIntArray[i]);
            System.out.print(Integer.toBinaryString(encodedIntArray[i]).substring(1));
        }
        System.out.println();
        appendFile.close();
    }

    private static String[] getEncodingArray(int[] frequencyArray){
        String[] encodings = new String[frequencyArray.length];
        HuffmanTreeNode root = getHuffmanTree(frequencyArray);
        for(int i = 0; i < frequencyArray.length; i++){
            encodings[i] = getEncodings(root,i, "");
        }
        return encodings;
    }

    private static int[] getEncodedByteArray(String fileToString) {
        ArrayList<Integer> encodedIntegers = new ArrayList<>();

        //31 bits i hver byte
        while(fileToString.length() > 0){
            //når man decoder så må man simpelthen ignorerer det første sifferet i hver int som man leser
            encodedIntegers.add(Integer.parseInt((fileToString.length() >= 30 ? '1' + fileToString.substring(0,30) : '1' + fileToString),2));
            //dersom lengden er over 31 så lages det ny substring der, hvis lengden er under 31 så har vi lagt til hele og stringen blir tom
            fileToString = fileToString.substring(Math.min(fileToString.length(), 30));
        }

        int[] answer = new int[encodedIntegers.size()];
        IntStream.range(0,encodedIntegers.size()).forEach(i->answer[i] = encodedIntegers.get(i));

        return answer;
    }

    public static void deCompress(String pathToCompressedFile, String pathToDecompressed) throws IOException {
        File input = new File(pathToCompressedFile);
        File output = new File(pathToDecompressed);
        byte[] temparr = Files.readAllBytes(input.toPath());
        int counter = 0;
        for (int i = 0; i < temparr.length; i++) {
            byte b = temparr[i];
            if(i % 8 == 0){
                System.out.print(Integer.toBinaryString((((int) b)+256) %256).substring(1));
            }
            else{
                System.out.print(Integer.toBinaryString((((int) b)+256) %256));
            }
        }
        System.out.println();
        // int[] frequencyArray = readFrequencyArray(input);
        //String[] encodings = getEncodingArray(frequencyArray);

        String kanskje = "";
        FileReader test = new FileReader(input);
        int readInt;
        while((readInt = test.read()) != -1){
            kanskje += Integer.toBinaryString(readInt).substring(1);
        }
        System.out.println( kanskje);
        // new FileOutputStream(output.getPath()).write(getDecodedByteArray(kanskje,frequencyArray));
    }

    private static int[] readFrequencyArray(File file) throws IOException {
        FileReader myReader = new FileReader(file.getPath());
        int[] frequencyArray = new int[ANT_TEGN + 1];
        for (int i = 0; i < frequencyArray.length; i++) {
            frequencyArray[i] += myReader.read();
        }
        myReader.close();
        return frequencyArray;
    }

    private static byte[] getDecodedByteArray(String encodedInputString, int[] frequencyArray) {
        String[] encodings = getEncodingArray(frequencyArray);
        //TODO helge underkjenner om vi bruker hashmap
        HashMap<String, Integer> encodedValues = new HashMap<>();
        for (int i = 0; i < frequencyArray.length; i++) {
            if (!encodings[i].equals("")) {
                encodedValues.put(encodings[i],i);
            }
        }

        ArrayList<Byte> unknownSizeByteArray = new ArrayList<>();
        //går gjennom hele stringen med tegn og finner de reelle verdiene
        for(int i = 0; i < encodedInputString.length()+1; i++){
            //dersom vi har kommet til en substring som har en assosiert verdi
            if(encodedValues.get(encodedInputString.substring(0, i))!= null){
                //legger det til det reelle tegnet i arrayet som vi senere skal skrive ut
                unknownSizeByteArray.add(encodedValues.get(encodedInputString.substring(0, i)).byteValue());
                //flytter punkt 0 fram
                encodedInputString = encodedInputString.substring(i);
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


    /**
     * finner verdien til en verdi value basert på huffmantreet med rot i root, counter skal være
     * "", brukes rekursivt
     */
    public static String getEncodings(HuffmanTreeNode root, int value, String counter) {
        String answer = "";
        if (root.left != null && root.left.value == value) {
            return (counter + "0");
        }
        if (root.right != null && root.right.value == value) {
            return (counter + "1");
        }

        if (root.left != null) {
            answer = getEncodings(root.left, value, (counter + "0"));
        }
        if (!answer.equals("")) return answer;
        if (root.right != null) {
            answer = getEncodings(root.right, value, (counter + "1"));
        }
        return answer;
    }


    public static HuffmanTreeNode getHuffmanTree(int[] frequencyArray) {
        int noCharacters = 0;
        for (int i = 0; i < frequencyArray.length; i++) {
            if (frequencyArray[i] != 0) {
                noCharacters++;
            }
        }
        int noOfNodes = 0;
        HuffmanTreeNode[] huffmanNodes = new HuffmanTreeNode[noCharacters];
        for (int i = 0; i < frequencyArray.length; i++) {
            if (frequencyArray[i] != 0) {
                huffmanNodes[noOfNodes] = new HuffmanTreeNode(i, frequencyArray[i]);
                noOfNodes++;
            }
        }

        int length = huffmanNodes.length;
        Heap.buildHeap(huffmanNodes, length);
        while (length > 0) {

            HuffmanTreeNode h = Heap.getMin(huffmanNodes, length);
            Heap.buildHeap(huffmanNodes, length - 1);
            HuffmanTreeNode h2 = Heap.getMin(huffmanNodes, length - 1);
            Heap.buildHeap(huffmanNodes, length - 2);
            HuffmanTreeNode root = new HuffmanTreeNode(-1, h.weight + h2.weight);
            root.left = h;
            root.right = h2;
            length -= 2;
            Heap.insertOnto(huffmanNodes, root, length);
            length++;
            if (length == 1) {
                return root;
            }
        }
        return null;
    }

    /**
     * Generer et frequency array av bytes fra en gitt fil
     * <p>
     * Denne lagrer antall gitte bytes inn i et gitt array, tror det er derfor man får rare
     * karakterer fordi enkelte karakterer lagres som mer enn én byte
     * Har sjekket med https://stackoverflow.com/questions/13173223/huffman-coding-dealing-with-unicode
     * og denne får akkurat samme svar for alle testfilene
     */
    private static int[] getFrequencyArray(File file) throws IOException {

        byte[] fileContent = Files.readAllBytes(file.toPath());
        int[] frequencyArray = new int[ANT_TEGN + 1];
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

        for (int j = 0; j < file.length(); j++) {
            frequencyArray[(fileContent[j] + ANT_TEGN) % ANT_TEGN]++;
        }
        frequencyArray[frequencyArray.length - 1] = 1;
        return frequencyArray;
    }
}

class HuffmanTreeNode{
    int value;
    int weight;
    HuffmanTreeNode left;
    HuffmanTreeNode right;

    public HuffmanTreeNode(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }
}

class Heap{
    static void heapify(HuffmanTreeNode arr[], int n, int i) {
        int min = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l].weight < arr[min].weight)
            min = l;

        if (r < n && arr[r].weight < arr[min].weight)
            min = r;

        if (min != i) {
            HuffmanTreeNode swap = arr[i];
            arr[i] = arr[min];
            arr[min] = swap;

            heapify(arr, n, min);
        }
    }
    static HuffmanTreeNode getMin(HuffmanTreeNode heap[], int n){
        HuffmanTreeNode temp = heap[0];
        heap[0] = heap[n-1];

        return temp;
    }
    static void buildHeap(HuffmanTreeNode arr[], int n)
    {
        int startIdx = (n / 2) - 1;

        for (int i = startIdx; i >= 0; i--) {
            heapify(arr, n, i);
        }
    }
    static void insertOnto(HuffmanTreeNode arr[], HuffmanTreeNode h, int n){
        arr[n] = h;
        n++;
        buildHeap(arr,n);
    }
}
//TODO slett denne når jeg er sikker på at det funker
 class BinaryTreePrinter {

    private HuffmanTreeNode tree;

    public BinaryTreePrinter(HuffmanTreeNode tree) {
        this.tree = tree;
    }

    private String traversePreOrder(HuffmanTreeNode root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.weight +" " +  (char)root.value);

        String pointerRight = "└──";
        String pointerLeft = (root.right != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.left, root.right != null);
        traverseNodes(sb, "", pointerRight, root.right, false);

        return sb.toString();
    }

    private void traverseNodes(StringBuilder sb, String padding, String pointer, HuffmanTreeNode node,
                               boolean hasRightSibling) {

        if (node != null) {

            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.weight  +" " + (char) node.value);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.right != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.left, node.right != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.right, false);

        }

    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(tree));
    }

}
