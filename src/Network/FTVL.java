package Network;

import common.Command;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class FTVL {
    Socket socket;
    int port;
    String host;

    String newFolder;
    OutputStream socketOut;
    InputStream socketIn;
    ObjectOutputStream oos;

    public FTVL(String host, int port) {
        this.port = port;
        this.host = host;
        try {
            socket = new Socket(host, port);
            socketOut = socket.getOutputStream();
            socketIn = socket.getInputStream();
            oos = new ObjectOutputStream(socketOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createM3U() {
        try {

            oos.write(0);
            oos.writeObject(Command.CREATE);
//            oos.writeObject(fileList);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> readM3U(File file) {
        String m3uContent = "";
        List<File> fileList = null;
        try {
            oos.write(0);
            oos.writeObject(Command.READ); // Send command to server
            BufferedReader fileIn = new BufferedReader(new FileReader(file)); // File reader

            String line = fileIn.readLine(); // Actual line in file
            while (line != null) { //Read from line to line, until an empty line
                m3uContent += line + "\n"; // Add line and linebreak to m3uContent string
                line = fileIn.readLine(); // Go to next line
            }
            fileIn.close();

            oos.writeObject(m3uContent);  // Send string to server (?) Not sure.
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            fileList = (List) ois.readObject(); // Read objects and create a file list.

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileList;
    }

    public void splitMP3(File file, int parts) {
        try {
            if (!file.exists() || !file.getName().endsWith(".mp3")) {
                System.out.println("Unkown file, please choose a real mp3");
                return;
            }

            oos.write(0);
            oos.writeObject(Command.SPLIT);
            sendFile(file, parts);

            if (!makeDirectory(file, parts)) {
                System.out.println("Directory already exists");
            }
            for (int i = 0; i < parts; i++) {
                saveFile();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void sendFile(File file, int parts) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socketOut);
            oos.writeObject(parts);
            oos.writeObject(file);
            oos.writeObject(file.length());

            DataOutputStream dos = new DataOutputStream(socketOut);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int read;
            long remaining = file.length();
            while ((read = fis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                remaining -= read;
                dos.write(buffer, 0, read);
            }

        } catch (IOException e) {
            System.err.println("sendFile");
        }
    }

    private void saveFile() {
        File file;
        try {
            ObjectInputStream ois = new ObjectInputStream(socketIn);
            file = (File) ois.readObject();
            long fileSize = (long) ois.readObject();

            DataInputStream dis = new DataInputStream(socketIn);
            FileOutputStream fos = new FileOutputStream(newFolder + File.separator + file.getName());
            byte[] buffer = new byte[4096];
            int read;
            long remaining = fileSize;
            while ((read = dis.read(buffer, 0, Math.min(buffer.length, (int) remaining))) > 0) {
                remaining -= read;
                fos.write(buffer, 0, read);
            }

        } catch (Exception e) {
            System.out.println("File save error");
            e.printStackTrace();
        }
    }

    private boolean makeDirectory(File file, int parts) {
        String name = file.getName().substring(0, file.getName().length() - 4);
        String dirName = name + "_" + parts;
        newFolder = file.getParent() + "\\" + dirName;
        File dir = new File(newFolder);
        return dir.mkdir();
    }

}

