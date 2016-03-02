package m3u_mp3split_client;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class FTVL {
    Socket socket;
    private enum Command{
        CREATE,
        READ,
        SPLIT
    }

    public FTVL(String host, int port){
//        socket = new Socket(host, port);
        try {
            socket = new Socket("localhost", 1234);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File createM3U(List<File> fileList){

        return null;
    }

    public List<File> readM3U(File file){

        return null;
    }

    public void splitMP3(File file, int parts){


        sendFile(file);

    }


    public static void main(String[] args) {
        FTVL ftvl = new FTVL("loc",1234);
        ftvl.splitMP3(new File("c:\\new\\Soilwork_The_Thrill.mp3"),1);
    }





    private void sendFile(File file){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(file);
            sendContent(file);
            oos.close();
//        socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendContent(File file) throws IOException{
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[4096];

        while (fis.read(buffer) > 0) {
            dos.write(buffer);
        }
        fis.close();
        dos.close();
    }
}
