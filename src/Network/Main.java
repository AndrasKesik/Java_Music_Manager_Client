package Network;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        FTVL ftvl= new FTVL("localhost", 4444);
        ftvl.splitMP3(new File("c:\\new\\Soilwork_The_Thrill.mp3"),3);
        ftvl.createM3U();
        ftvl.readM3U(new File("D:\\test.m3u"));
        ftvl.splitMP3(new File("c:\\new\\Soilwork_The_Thrill.mp3"),5);
    }
}
