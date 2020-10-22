import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class HuffmanTree {
    private HuffmanTreeNode root;
    private int freqLength;

    public HuffmanTree(int[] frequencyArray) {
        this.freqLength = frequencyArray.length;
        this.root = getHuffmanTree(frequencyArray);
    }

    public HuffmanTreeNode getRoot() {
        return root;
    }

    private HuffmanTreeNode getHuffmanTree(int[] frequencyArray) {
        PriorityQueue<HuffmanTreeNode> nodeQueue = new PriorityQueue<>();
        //legger til alle nodene i køa
        IntStream.range(0,frequencyArray.length).filter(s->frequencyArray[s] != 0).forEach(i->nodeQueue.add(new HuffmanTreeNode(i,frequencyArray[i])));

        while (nodeQueue.size() > 1) {
            //finner de to minste nodene
            HuffmanTreeNode h = nodeQueue.poll();
            HuffmanTreeNode h2 = nodeQueue.poll();
            //setter de sammen i felles tre
            HuffmanTreeNode root = new HuffmanTreeNode(-1, h.weight + h2.weight);
            root.left = h;
            root.right = h2;
            //legger til rota i det treet som vi akkurat satt sammen i køa
            nodeQueue.add(root);
        }
        //returnerer rota
        return nodeQueue.poll();
    }

    /**
     * Returnerer et array med encodings
     * indeks 0 er encoding til byte med verdi 0
     * indeks 1 er encoding til byte med verdi 1
     * indeks 128 er encoding til byte med verdi 128
     *
     */
    public String[] getEncodingArray(){
        String[] encodings = new String[freqLength];
        HuffmanTreeNode root = getRoot();
        for(int i = 0; i < freqLength; i++){
            encodings[i] = encodeValue(root, i, "");
        }
        return encodings;
    }

    /**
     * Finner huffman-enkoding til en gitt verdi
     */
    private String encodeValue(HuffmanTreeNode current, int value, String counter) {
        String answer = "";
        if (current.left != null && current.left.value == value) {
            return (counter + "0");
        }
        if (current.right != null && current.right.value == value) {
            return (counter + "1");
        }

        if (current.left != null) {
            answer = encodeValue(current.left, value, (counter + "0"));
        }
        if (!answer.equals("")) return answer;
        if (current.right != null) {
            answer = encodeValue(current.right, value, (counter + "1"));
        }
        return answer;
    }

    public Integer decodeHuffmanCode(String huffManCode){
        HuffmanTreeNode current = getRoot();
        for(char c : huffManCode.toCharArray()){
            //0 eller 1 er de eneste mulighetene
            current = (c == '0') ? current.left : current.right;
        }
        if(current == null) return null;

        //hvis vi ikke er i en løvnode så returnerer vi null
        if(current.left != null || current.right != null) return null;
        return current.value;
    }

}

class HuffmanTreeNode implements Comparable{
    int value;
    int weight;
    HuffmanTreeNode left;
    HuffmanTreeNode right;

    public HuffmanTreeNode(int value, int weight) {
        this.value = value;
        this.weight = weight;
    }

    //brukes av priorityqueue
    public int compareTo(Object o) {
        return weight - ((HuffmanTreeNode) o).weight;
    }
}