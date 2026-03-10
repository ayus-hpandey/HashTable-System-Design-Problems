import java.util.*;

class UsernameChecker{

    private HashMap<String, Integer> usernameMap = new HashMap<>();
    private HashMap<String, Integer> attemptCount = new HashMap<>();

    public boolean checkAvailability(String username) {

        attemptCount.put(username, attemptCount.getOrDefault(username, 0) + 1);

        return !usernameMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        for(int i=1;i<=5;i++){

            String newName = username + i;

            if(!usernameMap.containsKey(newName)){
                suggestions.add(newName);
            }

        }

        suggestions.add(username.replace("_","."));

        return suggestions;
    }

    public String getMostAttempted(){

        String maxUser = "";
        int maxCount = 0;

        for(Map.Entry<String,Integer> entry : attemptCount.entrySet()){

            if(entry.getValue() > maxCount){

                maxCount = entry.getValue();
                maxUser = entry.getKey();

            }

        }

        return maxUser;
    }

    public void registerUser(String username, int userId){

        usernameMap.put(username, userId);

    }

    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        checker.registerUser("john_doe",101);

        System.out.println(checker.checkAvailability("john_doe"));
        System.out.println(checker.checkAvailability("jane_smith"));

        System.out.println(checker.suggestAlternatives("john_doe"));

        System.out.println(checker.getMostAttempted());
    }
}