import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;

public class Huffmain {
    public static void main(String args[]) throws IOException, InterruptedException {
        File input = new File("files/diverse.pdf");
        File output = new File("files/output.txt");
        int[] frequencyArray = getFrequencyArray(input);
        writeFreqArray(output.getPath(), frequencyArray);
        //leser det vi akkurat skrev til fil

        HuffmanTreeNode root = getHuffmanTree(frequencyArray);
        byte[] inputFile = Files.readAllBytes(input.toPath());
        String fileToString = "";
        System.out.println(inputFile.length);
        Thread.sleep(5000);
        for(int i = 0; i < inputFile.length; i++){
            if(i % 10000 == 0){
                System.out.println(i);
            }
            fileToString += getEncodings(root, inputFile[i], "");
        }
        //TODO skriv frekvensarrayet til filen også og finn en måte og skille dette slik at man
        // kan dekrompimere filen
        System.out.println(fileToString.length()%8);
        for(int i = 0; i < fileToString.length()%8; i++){
            //må spare til 8 bits
            //TODO finn en bedre måte, helst ikke bare legg til 0, fucker sikkert med tegnet
            fileToString+="0";
        }
        byte[] data = decodeBinary(fileToString);
        java.nio.file.Files.write(output.toPath(), data);
    }

    /**
     * Leser frequency array fra en gitt fil
     */
    private static int[] readFrequencyArray(File file) throws IOException {
        FileReader myReader = new FileReader(file.getPath());
        int[] frequencyArray = new int[256];
        for(int i = 0; i < frequencyArray.length; i++){
            frequencyArray[i] += myReader.read();
        }
        return frequencyArray;
    }

    /**
     * finner verdien til en verdi value basert på huffmantreet med rot i root, counter skal være
     * 0, brukes rekursivt
     */
    public static String getEncodings(HuffmanTreeNode root, int value, String counter){
        String answer = "";
        if(root.left != null && root.left.value == value){
            return (counter + "0");
        }
        if(root.right != null && root.right.value == value){
            return (counter + "1");
        }

        if(root.left != null){
            answer = getEncodings(root.left, value, (counter +"0"));
        }
        if(!answer.equals("")) return answer;
        if(root.right != null){
            answer = getEncodings(root.right, value, (counter+"1") );
        }
        return answer;
    }

    private static void writeFreqArray(String pathToFile, int[] frequencyArray) throws IOException {
        FileWriter myWriter = new FileWriter(pathToFile);
        for (int i = 0; i < frequencyArray.length; i++) {
            myWriter.write(frequencyArray[i]);
        }
        myWriter.close();
    }

    public static HuffmanTreeNode getHuffmanTree(int[] frequencyArray){
        int noCharacters = 0;
        for(int i = 0; i < frequencyArray.length; i++){
            if(frequencyArray[i] != 0){
                noCharacters++;
            }

        }
        int j = 0;
        HuffmanTreeNode[] huffmanNodes = new HuffmanTreeNode[noCharacters];
        for(int i = 0; i < frequencyArray.length; i++){
            if(frequencyArray[i] != 0){
                huffmanNodes[j] = new HuffmanTreeNode(i ,frequencyArray[i]);
                j++;
            }

        }

        int length = huffmanNodes.length;
        Heap.buildHeap(huffmanNodes,length);
        while(length > 0){

            HuffmanTreeNode h = Heap.getMin(huffmanNodes, length);
            Heap.buildHeap(huffmanNodes,length-1);
            HuffmanTreeNode h2 = Heap.getMin(huffmanNodes, length-1);
            Heap.buildHeap(huffmanNodes,length-2);
            HuffmanTreeNode root = new HuffmanTreeNode(-1, h.weight + h2.weight);
            root.left = h;
            root.right = h2;
            length -= 2;
            Heap.insertOnto(huffmanNodes,root, length);
            length++;
            if(length == 1){
                return root;
            }
        }
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
    private static int[] getFrequencyArray(File file) throws IOException {
        //todo trenger sikkert ikke å lese hele fila på en gang, kan lese i løkka lenger nede
        // gjetter jeg, kanskje bruke outputstream

        byte[] fileContent = Files.readAllBytes(file.toPath());
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

    static byte[] decodeBinary(String s) {
        if (s.length() % 8 != 0) throw new IllegalArgumentException(
            "Binary data length must be multiple of 8");
        byte[] data = new byte[s.length() / 8];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '1') {
                data[i >> 3] |= 0x80 >> (i & 0x7);
            } else if (c != '0') {
                throw new IllegalArgumentException("Invalid char in binary string");
            }
        }
        return data;
    }}

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
        sb.append(root.weight +" " + (char) root.value);

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