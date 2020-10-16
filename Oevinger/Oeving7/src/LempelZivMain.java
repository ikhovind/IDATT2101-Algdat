import java.io.File;
import java.io.IOException;

public class LempelZivMain {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("files/hei.txt");
        File outputFile = new File("files/hei1.txt");

        LempelZiv lempelZiv = new LempelZiv(inputFile, outputFile);
        lempelZiv.compressFile();
    }
}
