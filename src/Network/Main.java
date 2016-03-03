package Network;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<File> fileList = new ArrayList<>();
        fileList.add(new File("c:\\new\\Soilwork_The_Thrill.mp3"));
        fileList.add(new File("c:\\new\\Soilwork_The_Thrill.mp3"));
        fileList.add(new File("c:\\new\\Soilwork_The_Thrill.mp3"));

        FTVL ftvl= new FTVL("localhost", 4444);
        ftvl.createM3U(fileList, "c:\\new\\m3uDir\\result.m3u");
//        ftvl.splitMP3(new File("c:\\new\\Soilwork_The_Thrill.mp3"),3);
        ftvl.readM3U(new File("c:\\new\\Tankcsapda - Punk & Roll (Remastered 2013) (1990).m3u"));
//        ftvl.splitMP3(new File("c:\\new\\Soilwork_The_Thrill.mp3"),5);
        ftvl.disconnectClient();
    }
}
