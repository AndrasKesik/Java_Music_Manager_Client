package m3u_mp3split_client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FTVL {

    private enum Command {
        CREATE,
        READ,
        SPLIT
    }

    public File createM3U(List<File> fileList) {

        return null;
    }

    public List<File> readM3U(File file) {
        String m3uContent = "";
        try {
            Socket socket = new Socket("localhost", 1234);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(Command.READ); // Send command to server
            BufferedReader fileIn = new BufferedReader(new FileReader(file)); // File reader
            String line = fileIn.readLine(); // Actual line in file
            while (line != null) { //Read from line to line, until an empty line
                m3uContent += line + "\n"; // Add line and linebreak to m3uContent string
                line = fileIn.readLine(); // Go to next line
            }
            fileIn.close();

            oos.writeBytes(m3uContent);  // Send string to server (?) Not sure.

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ois.readObject();
            // Hogy csinálok readObjectből listát? Valaki pls?


            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        / küldje át a szerverhez a read enumot - PIPA
        / megkapja a fájlt - PIPA
        / beolvassa Stringnek - PIPA
        / átküldi a szervernek - PIPA

        / serverből listát kap - PIPA
        / kiolvassa - PIPA
        / visszaadja
        */
        return null;
    }

    public void splitMP3() {


    }

}
