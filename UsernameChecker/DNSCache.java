import java.util.*;

class DNSEntry {

    String domain;
    String ipAddress;
    long expiryTime;

    DNSEntry(String domain, String ipAddress, long ttlSeconds){

        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + ttlSeconds * 1000;

    }

    boolean isExpired(){

        return System.currentTimeMillis() > expiryTime;

    }
}

public class DNSCache {

    private LinkedHashMap<String,DNSEntry> cache;
    private int capacity;

    private int hits = 0;
    private int misses = 0;

    DNSCache(int capacity){

        this.capacity = capacity;

        cache = new LinkedHashMap<>(capacity,0.75f,true){
            protected boolean removeEldestEntry(Map.Entry<String,DNSEntry> eldest){
                return size() > DNSCache.this.capacity;
            }
        };

    }

    public String resolve(String domain){

        if(cache.containsKey(domain)){

            DNSEntry entry = cache.get(domain);

            if(!entry.isExpired()){

                hits++;
                return "Cache HIT → "+entry.ipAddress;

            }

            cache.remove(domain);

        }

        misses++;

        String ip = queryUpstreamDNS(domain);

        cache.put(domain,new DNSEntry(domain,ip,300));

        return "Cache MISS → "+ip;

    }

    private String queryUpstreamDNS(String domain){

        return "172.217.14."+new Random().nextInt(255);

    }

    public void getCacheStats(){

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits*100.0)/total;

        System.out.println("Hit Rate: "+hitRate+"%");
    }

    public static void main(String[] args){

        DNSCache dns = new DNSCache(5);

        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));

        dns.getCacheStats();

    }
}