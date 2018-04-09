package blokdata;

public class BlockStats{
    public static int filesCount=0;
    public static double averageLatency=0;
    public static int onlineDevices=0;


    public static  void setFilesCount(){
        filesCount++;
    }
    public static void removeFilesCount(){
        filesCount--;
    }
    public static int getFilesCount(){
        return filesCount;
    }

    public static void setAvgLatency(double latency){
        averageLatency=latency;
    }
    public static double getAvgLatency(){
        return averageLatency;
    }

    public static void addOnlineDevices(int peers){
        onlineDevices=peers;
    }
    public static void removeOnlineDevices(){
        onlineDevices--;
    }

}
