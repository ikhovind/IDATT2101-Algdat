import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class HuffmanTree {
    public static HuffmanTreeNode getHuffmanTree(int[] frequencyArray) {
        int noCharacters = 0;
        for (int i = 0; i < frequencyArray.length; i++) {
            if (frequencyArray[i] != 0) {
                noCharacters++;
            }

        }
        int j = 0;
        HuffmanTreeNode[] huffmanNodes = new HuffmanTreeNode[noCharacters];
        for (int i = 0; i < frequencyArray.length; i++) {
            if (frequencyArray[i] != 0) {
                huffmanNodes[j] = new HuffmanTreeNode(i, frequencyArray[i]);
                j++;
            }

        }
        //TODO priorityqueue
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
    public static String[] getEncodingArray(int[] frequencyArray){
        String[] encodings = new String[frequencyArray.length];
        HuffmanTreeNode root = getHuffmanTree(frequencyArray);
        for(int i = 0; i < frequencyArray.length; i++){
            encodings[i] = getEncodings(root,i, "");
        }
        return encodings;
    }
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