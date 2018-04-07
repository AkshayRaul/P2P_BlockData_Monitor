package blokdata;
import java.nio.charset.*;
import java.util.*;

public class fileMetaData{
    String fileName;
    String fileId;
    long fileSize;
    String fileType;
    String mimeType;
    String owner;
    String peerId;

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
    String getFileType(){
        return this.fileType;
    }
    long getFileSize(){
        return this.fileSize;
    }
    void setPeerId(String peerId){
      this.peerId=peerId;
    }

    public static String RandomString() {
      String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
          StringBuilder salt = new StringBuilder();
          Random rnd = new Random();
          while (salt.length() < 10) { // length of the random string.
              int index = (int) (rnd.nextFloat() * SALTCHARS.length());
              salt.append(SALTCHARS.charAt(index));
          }
          return salt.toString();
    }
}
