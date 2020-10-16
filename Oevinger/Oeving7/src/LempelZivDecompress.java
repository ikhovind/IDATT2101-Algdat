import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class LempelZivDecompress {
    //Fil som leses inn
    private File inputFile;
    //Fil som skal skrives ut til
    private File outputFile;

    //Liste som inneholder bytene som skal dekomprimeres
    private byte[] bytesInputFile;
    //Liste som inneholder bytene som blir dekomprimert
    private byte[] bytesOutputFile;

    //Indexen vi befinner oss i i inputlista
    private int inputIndex;
    //Antall bytes som ikke er dekomprimert enda
    private int bytesLeft;
    //Indexen vi befinner oss i i outputlista
    private int outputIndex;

    public LempelZivDecompress(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.bytesInputFile = new byte[0];
        this.bytesOutputFile = new byte[0];
        this.inputIndex = 0;
        this.outputIndex = 0;

        readInputFile();

        this.bytesLeft = bytesInputFile.length;
    }

    private void readInputFile() throws IOException {
        //henter alle bytene fra inputfilen
        bytesInputFile = Files.readAllBytes(inputFile.toPath());
        //Lager en outputliste med størrelse lik 2 ganger lengden på inputlista. Om dette blir for lite, utvider vi lista
        bytesOutputFile = new byte[bytesInputFile.length * 2];
    }

    public void decompress() throws IOException {
        //Kjører mens det fortsatt finnes bytes som ikke er analysert
        while (bytesLeft != 0){
            //Henter første tallet som står i lista
            //counter vil enten være lengden på antall bytes som ikke er komprimert eller indexen på hvor det som er komprimert skal hente bytes fra
            int counter = bytesInputFile[inputIndex];
            //Øker indexen for dr vi leser i inputlista
            inputIndex ++;

            //Om det tallet som ble lest er negativt, betyr det at det kommer bokstaver som ikke er komprimert
            if(counter < 0){
                //lager en variabel som brukes i loopen slik at i ikke blir større enn antalltegn som ikke er komprimert pluss indexen vi har kommet til i outputlista
                int outputStart = outputIndex;

                //Utvider lista om den er for liten
                if(bytesOutputFile.length <= outputIndex + (-counter)){
                    expandOutputList();
                }

                //kopierer over fra inputlista over til outputlista
                for(int i = outputIndex; i < -counter + outputStart; i ++, outputIndex ++, inputIndex ++){
                    bytesOutputFile[outputIndex] = bytesInputFile[inputIndex];
                }


            } else { //Om tallet ikke er negativt, betyr det at det har skjedd en kompresjon
                //Henter lengden på det som skal hentes
                int compressedContentLength = bytesInputFile[inputIndex];
                inputIndex++;

                //Utvider lista om den er for liten
                if(bytesOutputFile.length <= outputIndex + compressedContentLength){
                    expandOutputList();
                }

                //henter så dataen fra det stedet i lista det er referert til og legger det inn i outputlista
                for (int i = counter; i < compressedContentLength + counter; i++, outputIndex++) {
                    bytesOutputFile[outputIndex] = bytesInputFile[i];
                }
            }
            //Oppdaterer hvor mange bytes som er igjen
            updateBytesLeft();
        }

        trimOutputList();
        writeToFile();
    }

    private void expandOutputList(){
        //Utvider lista
        byte[] tempByteList = new byte[bytesOutputFile.length + bytesOutputFile.length];
        for(int i = 0; i < outputIndex; i ++){
            tempByteList[i] = bytesOutputFile[i];
        }
        bytesOutputFile = tempByteList;
    }

    private void updateBytesLeft(){
        bytesLeft = bytesInputFile.length - inputIndex;
    }

    private void trimOutputList(){
        byte[] tempByteList = new byte[outputIndex];

        for(int i = 0; i < outputIndex; i ++){
            tempByteList[i] = bytesOutputFile[i];
        }

        bytesOutputFile = tempByteList;
    }

    private void writeToFile() throws IOException {
        String[] words = new String[outputIndex];
        for(int i = 0; i < outputIndex; i ++){
            words[i] = String.valueOf(bytesOutputFile[i]);
        }

        BufferedWriter outputWriter = new BufferedWriter(new FileWriter(outputFile.getPath()));
        for (int i = 0; i < words.length; i++) {
            outputWriter.write(words[i]);
            //outputWriter.newLine();
        }
        outputWriter.flush();
        outputWriter.close();
    }
}
