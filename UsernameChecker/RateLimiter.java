import java.util.*;

class TokenBucket {

    int tokens;
    long lastRefillTime;
    int maxTokens;
    int refillRate;

    TokenBucket(int maxTokens, int refillRate) {
        this.tokens = maxTokens;
        this.maxTokens = maxTokens;
        this.refillRate = refillRate;
        this.lastRefillTime = System.currentTimeMillis();
    }

    synchronized boolean allowRequest() {

        refill();

        if(tokens > 0){
            tokens--;
            return true;
        }

        return false;
    }

    void refill(){

        long now = System.currentTimeMillis();
        long elapsed = (now - lastRefillTime) / 1000;

        int tokensToAdd = (int)(elapsed * refillRate);

        if(tokensToAdd > 0){

            tokens = Math.min(maxTokens, tokens + tokensToAdd);
            lastRefillTime = now;

        }
    }
}

public class RateLimiter {

    HashMap<String, TokenBucket> clientBuckets = new HashMap<>();

    int maxTokens = 1000;
    int refillRate = 1000 / 3600;

    public boolean checkRateLimit(String clientId){

        clientBuckets.putIfAbsent(clientId,
                new TokenBucket(maxTokens,refillRate));

        TokenBucket bucket = clientBuckets.get(clientId);

        return bucket.allowRequest();
    }

    public static void main(String[] args){

        RateLimiter limiter = new RateLimiter();

        String client = "abc123";

        for(int i=0;i<1005;i++){

            boolean allowed = limiter.checkRateLimit(client);

            if(allowed)
                System.out.println("Allowed");
            else
                System.out.println("Denied");

        }
    }
}