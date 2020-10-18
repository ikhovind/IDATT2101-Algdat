import java.io.File;
import java.io.IOException;

public class LempelZivDecompressMain {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("files/lempelZivCompressed.txt");
        String outFile = "files/lempelZivDecompressed.txt";
        File outputFile = new File(outFile);
        if(outputFile.exists()){
            outputFile.delete();
            outputFile = new File(outFile);
        }

        LempelZivDecompress lempelZivDecompress = new LempelZivDecompress(inputFile, outputFile);
        lempelZivDecompress.decompress();
    }
}
