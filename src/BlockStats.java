package blokdata;

public class BlockStats{
    private static int filesCount;
    private static double averageLatency;
    private static int onlineDevices;

    public static setFilesCount(){
        filesCount++;
    }
    public static removeFilesCount(){
        filesCount--;
    }
    public static getFilesCount(){
        return filesCount;
    }

    public static setAvgLatency(double latency){
        averageLatency=latency;        
    }
    public static getAvgLatency(){
        return averageLatency;
    }

    public static addOnlineDevices(){
        onlineDevices++;
    }
    public static removeOnlineDevices(){
        onlineDevices--;
    }

}
