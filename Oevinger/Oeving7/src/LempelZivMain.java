import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LempelZivMain {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("files/diverse.txt");
        String outFile = "files/lempelZivCompressed.txt";
        File outputFile = new File(outFile);
        if(outputFile.exists()){
            outputFile.delete();
            outputFile = new File(outFile);
        }

        LempelZiv lempelZiv = new LempelZiv(inputFile, outputFile);
        lempelZiv.compressFile();
    }
}
