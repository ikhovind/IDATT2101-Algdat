import java.io.File;
import java.io.IOException;

public class LempelZivDecompressMain {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("files/hei1.txt");
        File outputFile = new File("files/hei2.txt");

        LempelZivDecompress lempelZivDecompress = new LempelZivDecompress(inputFile, outputFile);
        lempelZivDecompress.decompress();
    }
}
