import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class HuffmanTree {
    private static HuffmanTreeNode getHuffmanTree(int[] frequencyArray) {
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

    public static String[] getEncodingArray(int[] frequencyArray){
        String[] encodings = new String[frequencyArray.length];
        HuffmanTreeNode root = getHuffmanTree(frequencyArray);
        for(int i = 0; i < frequencyArray.length; i++){
            encodings[i] = getEncodings(root,i, "");
        }
        return encodings;
    }

    private static String getEncodings(HuffmanTreeNode root, int value, String counter) {
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