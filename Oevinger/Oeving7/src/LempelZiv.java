import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class LempelZiv {
    //Filen som sendes inn
    private File inputFile;
    //Filen som det komprimerte innholdet skal sendes tilbake til
    private File outputFile;

    //Bytes som ligger i filen som blir sendt inn
    private byte[] bytesFromInputFile;
    //Listen av bytes som det komprimerte innholdet skal legges inn i
    private byte[] compressed;

    //Setter to variabler som holder på grensene for hvor langt man kan gå tilbake i teksten og hvor langt ordet må være for at det skal komprimeres
    private final int MAX_DISTANCE_BACK = 127;
    private final int MIN_WORD_LENGTH = 4;

    //En variabel som holder styr på hvor mange bytes som ikke ennå er lagt inn i den komprimerte listen
    private int bytesLeft;
    //En byteliste som inneholder de bytene som det er mulig ????
    private byte[] block;
    //Holder styr på hvor i byteFromInputFile vi befinner oss
    private int start;

    public LempelZiv(File inputFile, File outputFile) throws IOException {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.bytesFromInputFile = new byte[0];
        this.compressed = new byte[0];
        this.bytesLeft = 0;
        this.block = new byte[0];
        this.start = 0;

        readInputFile();
    }

    private void readInputFile() throws IOException {
        //Henter alle bytene fra inputfilen
        bytesFromInputFile = Files.readAllBytes(inputFile.toPath());
        //Lager en liste som skal inneholde alle de komprimerte bytene. Denne skal ikke bli lenger enn lengden på den originale filen
        compressed = new byte[bytesFromInputFile.length];
    }

    public void compressFile() throws IOException {
        //Setter antall bytes som er igjen til lengden av bytesFromInputFile siden vi ikke har analysert noe enda
        bytesLeft = bytesFromInputFile.length;

        //Setter telleren til den komprimerte lista til 0
        int indexCompressedList = 0;

        //Kjører loopen helt til antall bytes som er igjen er 0, altså at alle bytene er analysert
        while (!(bytesLeft == 0)){
            //Henter en ny blokk for analyse
            getNewByteBlock();

            //En teller for hvor mange bytes som er ferdiganalysert
            int bytesDone = 0;
            //Sier hvilken index komprinsjonen skal starte på
            int indexCompress = -1;

            //Går gjennom alle bytene i input lista
            for(int i = 0; i < bytesFromInputFile.length; i ++) {
                //Lager en ArrayList som inneholder de bytene som skal analyseres
                ArrayList<Byte> currentBytes = new ArrayList<>();

                //Denne variabelen holder styr på om en mulighet for komprimering er funnet eller ikke. Til å starte med, er det ikke funnet noe
                boolean compressFound = false;

                //Har en teller som holder på verdien til antall bytes som skal komprimeres
                int compressLength = -1;
                //indexen til stedet hvor man finner en match for kompresjon
                int indexCompressMatch = 0;

                //Går gjennom alle elementene som er etter den byten vi undersøker fra før av
                for (int j = i; j < bytesFromInputFile.length;  j ++) {
                    //Legger den byten inn i lista over de vi undersøker
                    currentBytes.add(bytesFromInputFile[j]);

                    //Lista må ha en størrelse som er større eller lik enn den minste ordlengden
                    if (currentBytes.size() >= MIN_WORD_LENGTH) {
                        //Finner den beste matchen for hvilket sted bytene kan komprimeres
                        int compressPlace = findBestMatch(currentBytes, i);

                        //Hvis komprimeringsstedet er funnet
                        if (compressPlace >= 0) {
                            //Sier at det er funnet en komprimeringsmulighet
                            compressFound = true;
                            //Setter starten av kompresjonen lik i
                            indexCompress = i;
                            //Setter matchen lik den beste matchen vi fant tidligere
                            indexCompressMatch = compressPlace;
                            //Setter lengden på det som skal komprimeres lik størrelsen på bytene vi sjekker
                            compressLength = currentBytes.size();
                        } else {
                            break;
                        }
                    }
                }

                //Kjører hvis det er funnet en kompresjonsmulighet
                if (compressFound) {
                    //Finner først antallet på de bytene det ikke var mulig å komprimere
                    int notCompressed = indexCompress - bytesDone;

                    //legger til en byte som teller hvor mange bytes som kommer som ikke er komprimert. Denne vil ha minus foran seg
                    //Øker deretter hvilken index vi er på i den komprimerte lista
                    if(!(compressed.length <= indexCompressedList)){
                        compressed[indexCompressedList] = (byte) -notCompressed;
                        indexCompressedList++;
                    }


                    //Legger inn de bytene som det ikke var mulig å komprimere
                    for (int j = bytesDone; j < indexCompress; j ++) {
                        if(compressed.length <= indexCompressedList || bytesFromInputFile.length <= j){
                            break;
                        }
                        compressed[indexCompressedList] = bytesFromInputFile[j];
                        indexCompressedList++;
                    }

                    //finner indexen til det stedet komprimeringen skal hente data fra
                    int back = indexCompress - indexCompressMatch;

                    //Legger så til dette i den komprimerte lista i tillegg til lengden av det som skal hentes
                    if(!(compressed.length <= indexCompressedList)){
                        compressed[indexCompressedList] = (byte) back;
                        indexCompressedList++;
                        compressed[indexCompressedList] = (byte) compressLength;
                        indexCompressedList++;
                    }

                    //Endrer fullførte bytes til der komprimeringen starter pluss lengden på den
                    bytesDone = indexCompress + compressLength;

                    //Setter deretter i lik denne slik at vi fortsetter søket etter den fullførte komprimeringen
                    i += compressLength;
                }
            }

            //Variabel som holder styr på hvor mange bytes som ikke er komprimert
            int notCompressed = bytesFromInputFile.length - bytesDone;

            //Legger til de som ikke er komprimert, om det er noen
            if(indexCompressedList < compressed.length){
                compressed[indexCompressedList] = (byte) -notCompressed;
                indexCompressedList++;
            }

            for(int j = bytesDone; j < bytesFromInputFile.length; j ++){
                if(indexCompressedList >= compressed.length){
                    break;
                }
                compressed[indexCompressedList] = bytesFromInputFile[j];
                indexCompressedList ++;
            }
        }

        //lager en liste som skal sendes videre til fixEmptyBufferBytes slik at de tomme plassene fjernes
        byte[] buffer = compressed;
        fixEmptyBufferBytes(buffer, indexCompressedList);

        //Skriver deretter den komprimerte lista til fil
        writeFile();
    }

    private void getNewByteBlock(){
        //Sjekker om det er mulig å ta et steg som er like langt som den lengste steglengden
        if(bytesLeft < MAX_DISTANCE_BACK){
            //lager en liste som er like stor som bytene som er igjen
            block = new byte[bytesLeft];

            //kopierer fra inputlista til blocklista
            for(int i = 0; i < bytesLeft; i ++, start ++){
                block[i] = bytesFromInputFile[start];
            }

            //Setter deretter antall bytes som er igjen til 0
            bytesLeft = 0;
        }else {
            //lager en liste som er like stor som det største steget som er lov
            block = new byte[MAX_DISTANCE_BACK];
            //kopierer fra inputlista til blocklista
            for(int i = 0; i < MAX_DISTANCE_BACK; i ++, start ++){
                block[i] = bytesFromInputFile[start];
            }
            //Trekker fra det steget vi tok fra antall bytes som er igjen
            bytesLeft -= MAX_DISTANCE_BACK;
        }
    }

    private int findBestMatch(ArrayList<Byte> currentBytes, int indexStart){
        //Finner indexen til det elementet det er mulig å gå lengst tilbake for å hente
        int indexFurthestBack = start - MAX_DISTANCE_BACK;
        int distanceBack = MAX_DISTANCE_BACK;

        //Om indexen blir negativ, setter vi den lik null og distansen man kan gå tilbake lik startindexen
        if(indexFurthestBack < 0){
            indexFurthestBack = 0;
            distanceBack = indexStart;
        }

        //Går gjennom alle elementene som ligger mellom indexstarten og opp til distansen man kan gå minus den ordlengden man må være over
        for(int i = indexFurthestBack; i < distanceBack - MIN_WORD_LENGTH; i ++){
            //Setter at en match er funnet
            boolean found = true;

            //Setter indexen lik i
            int index = i;
            //går gjennom lista over bytene som skal analyseres
            for(int j = 0; j < currentBytes.size(); j ++){
                if(block.length <= j || currentBytes.size() <= index){
                    break;
                }
                //om den finner noe som ikke er likt, vil den gå ut av loopen og sette funnet til false siden hele ordet ikke matcher
                if(block[j] != currentBytes.get(index)){
                    found = false;
                    break;
                }
                index ++;
            }

            //hvis funnet er sant, vil den sende tilbake startindexen på hvor matchen finnes
            if(found){
                return i;
            }
        }

        //Retunerer negativt om den ikke finner en match
        return -1;
    }

    private void fixEmptyBufferBytes(byte[] buffer, int bufferLength){
        //Gjør om compressed til en ny liste med lengden på antall bytes som lå i den gamle
        compressed = new byte[bufferLength];

        //Kopierer over
        for(int i = 0; i < bufferLength; i ++){
            compressed[i] = buffer[i];
        }
    }

    private void writeFile() throws IOException {
        //Skriver til fil
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile.getPath())));
        dataOutputStream.write(compressed);
        dataOutputStream.close();
    }
}
