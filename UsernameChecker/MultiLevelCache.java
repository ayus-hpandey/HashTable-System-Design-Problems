import java.util.*;

class Video {

    String id;
    String data;

    Video(String id,String data){
        this.id=id;
        this.data=data;
    }
}

class L1Cache extends LinkedHashMap<String,Video>{

    int capacity;

    L1Cache(int capacity){
        super(capacity,0.75f,true);
        this.capacity=capacity;
    }

    protected boolean removeEldestEntry(Map.Entry<String,Video> eldest){
        return size() > capacity;
    }
}

public class MultiLevelCache {

    L1Cache L1 = new L1Cache(10000);

    HashMap<String,String> L2 = new HashMap<>();

    HashMap<String,Integer> accessCount = new HashMap<>();

    int l1Hits=0;
    int l2Hits=0;
    int dbHits=0;

    public Video getVideo(String videoId){

        if(L1.containsKey(videoId)){

            l1Hits++;
            return L1.get(videoId);

        }

        if(L2.containsKey(videoId)){

            l2Hits++;

            Video v = loadFromSSD(L2.get(videoId));

            promote(videoId,v);

            return v;
        }

        dbHits++;

        Video v = loadFromDatabase(videoId);

        L2.put(videoId,"/ssd/"+videoId);

        updateAccess(videoId);

        return v;
    }

    private void promote(String videoId, Video v){

        updateAccess(videoId);

        if(accessCount.get(videoId) > 5){

            L1.put(videoId,v);
        }
    }

    private void updateAccess(String id){

        accessCount.put(id,
                accessCount.getOrDefault(id,0)+1);
    }

    private Video loadFromSSD(String path){

        return new Video(path,"SSD Video Data");
    }

    private Video loadFromDatabase(String id){

        return new Video(id,"DB Video Data");
    }

    public void printStats(){

        int total = l1Hits+l2Hits+dbHits;

        System.out.println("L1 Hit Rate: "+(l1Hits*100.0/total)+"%");
        System.out.println("L2 Hit Rate: "+(l2Hits*100.0/total)+"%");
        System.out.println("DB Hit Rate: "+(dbHits*100.0/total)+"%");
    }

    public static void main(String[] args){

        MultiLevelCache cache = new MultiLevelCache();

        cache.getVideo("video_1");
        cache.getVideo("video_1");
        cache.getVideo("video_2");

        cache.printStats();
    }
}