import java.util.*;

class PageStats {
    int views = 0;
    Set<String> uniqueUsers = new HashSet<>();
}

public class RealTimeAnalytics {

    HashMap<String, PageStats> pageStats = new HashMap<>();
    HashMap<String, Integer> sourceCount = new HashMap<>();

    public void processEvent(String url, String userId, String source) {

        pageStats.putIfAbsent(url, new PageStats());

        PageStats stats = pageStats.get(url);

        stats.views++;
        stats.uniqueUsers.add(userId);

        sourceCount.put(source, sourceCount.getOrDefault(source,0)+1);
    }

    public List<Map.Entry<String,PageStats>> getTopPages(){

        PriorityQueue<Map.Entry<String,PageStats>> heap =
                new PriorityQueue<>(
                        (a,b) -> a.getValue().views - b.getValue().views
                );

        for(Map.Entry<String,PageStats> entry : pageStats.entrySet()){

            heap.offer(entry);

            if(heap.size() > 10)
                heap.poll();
        }

        return new ArrayList<>(heap);
    }

    public void printDashboard(){

        System.out.println("Top Pages:");

        for(Map.Entry<String,PageStats> entry : getTopPages()){

            System.out.println(
                    entry.getKey() +
                            " - " +
                            entry.getValue().views +
                            " views (" +
                            entry.getValue().uniqueUsers.size() +
                            " unique)"
            );
        }

        System.out.println("\nTraffic Sources:");

        int total = sourceCount.values().stream().mapToInt(i->i).sum();

        for(String s : sourceCount.keySet()){

            double percent = (sourceCount.get(s)*100.0)/total;

            System.out.println(s + ": " + percent + "%");
        }
    }

    public static void main(String[] args){

        RealTimeAnalytics analytics = new RealTimeAnalytics();

        analytics.processEvent("/article/breaking-news","user1","google");
        analytics.processEvent("/article/breaking-news","user2","facebook");
        analytics.processEvent("/sports/championship","user3","google");

        analytics.printDashboard();
    }
}