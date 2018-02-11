import java.nio.charset.*;
import java.util.*;

public class fileMetaData{
    String fileName;
    String fileID;
    long fileSize;
    String fileType;
    String mimeType;

    fileMetaData(){
        this.fileName="test";
        this.fileID="";
        this.fileSize=-1;
        this.fileType="null";
        this.mimeType="null";
    }
    fileMetaData(String fileName,String fileID,long fileSize,String fileType,String mimeType){
        this.fileName=fileName;
        this.fileID=fileID;
        this.fileSize=fileSize;
        this.fileType=fileType;
        this.mimeType=mimeType;
    }
    fileMetaData(String fileName,String fileID,long fileSize,String fileType){
        this.fileName=fileName;
        this.fileID=fileID;
        this.fileSize=fileSize;
        this.fileType=fileType;
        this.mimeType="null";
    }
    fileMetaData(String fileName,String fileID,long fileSize){
        this.fileName=fileName;
        this.fileID=fileID;
        this.fileSize=fileSize;
        this.fileType=fileName.substring(fileName.indexOf("."),fileName.length());
        this.mimeType="null";
    }
    String getFileName(){
        return this.fileName;
    }
    String getFileID(){
        return this.fileID;
    }
    long getFileSize(){
        return this.fileSize;
    }

    public static String RandomString() {
        byte[] array = new byte[7]; // length is bounded by 7
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        return generatedString;
    }
}
