package blokdata;

public class BlockStats{
    private static int filesCount;
    private static double averageLatency;
    private static int onlineDevices;

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

    public static void addOnlineDevices(){
        onlineDevices++;
    }
    public static void removeOnlineDevices(){
        onlineDevices--;
    }

}
