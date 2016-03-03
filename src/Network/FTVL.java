package Network;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class FTVL {
    Socket socket;
    int port;
    String host;

    String newFolder;

    public FTVL(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void createM3U() {
        try {
            socket = new Socket(host, port);
            System.out.println("createM3U onnected to: " + socket.getInetAddress());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(Command.CREATE);


            socket.close();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> readM3U(File file) {
        String m3uContent = "";
        List<File> fileList = null;
        try {
            Socket socket = new Socket("localhost", 4444);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(Command.READ); // Send command to server
            BufferedReader fileIn = new BufferedReader(new FileReader(file)); // File reader
            String line = fileIn.readLine(); // Actual line in file

            while (line != null) { //Read from line to line, until an empty line
                m3uContent += line + "\n"; // Add line and linebreak to m3uContent string
                line = fileIn.readLine(); // Go to next line

            }
            fileIn.close();

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.print(m3uContent);  // Send string to server
            printWriter.close();

//            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
//            fileList = (List)ois.readObject(); // Hogy csinálok readObjectből listát? Valaki pls?



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
        return fileList;
    }

    public void splitMP3(File file, int parts) {
        try {
            if(!file.exists() || !file.getName().endsWith(".mp3")){
                System.out.println("Unkown file, please choose a real mp3");
                return;}

            socket = new Socket(host, port);
            System.out.println("splitMP3 Connected to: " + socket.getInetAddress());

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(Command.SPLIT);
            sendFile(file, parts);

            if(!makeDirectory(file, parts)){
                System.out.println("Directory already exists");
            }
            for(int i =0;i<parts;i++){
                saveFile();
            }

//            oos.close();
//            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void sendFile(File file,int parts) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.write(parts);
            oos.writeObject(file);

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            while (fis.read(buffer) > 0) {
                dos.write(buffer);
            }
//            oos.close();
//            socket.close();
        } catch (IOException e) {
            System.err.println("sendFile");
        }
    }

    private void saveFile() {
        File file;
        try {
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            file = (File) ois.readObject();

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            FileOutputStream fos = new FileOutputStream(newFolder+File.separator+file.getName());
            byte[] buffer = new byte[4096];
            int read;
            long remaining = file.length();
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                remaining -= read;
                fos.write(buffer, 0, read);
            }
//            fos.close();
//            dis.close();
//            ois.close();
        } catch (Exception e) {
//            System.out.println("File save error");    ///STREAM HEADER-ÖK NEM EGYEZNEK, HOGY TUDOM MEGCSINÁLNI HOGY EGYEZZENEK?
//            e.printStackTrace();
        }
    }

    private boolean makeDirectory(File file, int parts){
        String name = file.getName().substring(0,file.getName().length()-4);
        String dirName = name + "_" + parts;
        newFolder = file.getParent()+"\\"+dirName;
        File dir = new File(newFolder);
        return dir.mkdir();
    }

}

