import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

public class Main {
    public static void main(String args[]) throws IOException {
        File input = new File("files/input.txt");
        File output = new File("files/output.txt");
        FileInputStream inputStream = new FileInputStream(input.getPath());
        byte[] fileContent = Files.readAllBytes(input.toPath());
        new FileOutputStream(input.getPath()).write('a');
        //char gir tall fra -128 - 127, men pga hvordan overflow funker så kan man ikke
        // konvertere fra negative tall til char, trenger derimot ikke å bruke annen encoding, da
        // 255 tegn er nok for å få med det norske alfabetet
        if(fileContent[0] < 0){

            //dersom man får overflow så må den være over 127, derav første + 128
            //når man får overflow så telles det som overflower opp fra -128, så dersom man f.eks
            // får -28 så har den 100 overflow pga -128 + 100 = -28.
            // -28 + 128 = 100, derav andre + 128
            System.out.println((char)(fileContent[0] + 128*2));
        }else{
            System.out.println((char) fileContent[0]);
        }
        //https://www.ascii-code.com/
    }
}
