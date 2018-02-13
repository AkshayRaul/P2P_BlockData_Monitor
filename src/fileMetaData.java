import java.nio.charset.*;
import java.util.*;

public class fileMetaData{
    String fileName;
    String fileId;
    long fileSize;
    String fileType;
    String mimeType;

    fileMetaData(){
        this.fileName="test";
        this.fileId="";
        this.fileSize=-1;
        this.fileType="null";
        this.mimeType="null";
    }
    fileMetaData(String fileName,String fileId,long fileSize,String fileType,String mimeType){
        this.fileName=fileName;
        this.fileId=fileId;
        this.fileSize=fileSize;
        this.fileType=fileType;
        this.mimeType=mimeType;
    }
    fileMetaData(String fileName,String fileId,long fileSize,String fileType){
        this.fileName=fileName;
        this.fileId=fileId;
        this.fileSize=fileSize;
        this.fileType=fileType;
        this.mimeType="null";
    }
    fileMetaData(String fileName,String fileId,long fileSize){
        this.fileName=fileName;
        this.fileId=fileId;
        this.fileSize=fileSize;
        this.fileType=fileName.substring(fileName.indexOf("."),fileName.length());
        this.mimeType="null";
    }
    String getFileName(){
        return this.fileName;
    }
    String getFileId(){
        return this.fileId;
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
