import java.util.*;

public class PlagiarismDetector {

    HashMap<String, Set<String>> ngramIndex = new HashMap<>();
    int n = 5;

    public List<String> generateNgrams(String text){

        List<String> grams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for(int i=0;i<=words.length-n;i++){

            StringBuilder gram = new StringBuilder();

            for(int j=0;j<n;j++){
                gram.append(words[i+j]).append(" ");
            }

            grams.add(gram.toString().trim());
        }

        return grams;
    }

    public void addDocument(String docId, String text){

        List<String> grams = generateNgrams(text);

        for(String g : grams){

            ngramIndex.putIfAbsent(g,new HashSet<>());

            ngramIndex.get(g).add(docId);

        }
    }

    public Map<String,Integer> analyzeDocument(String docId, String text){

        List<String> grams = generateNgrams(text);

        Map<String,Integer> matchCount = new HashMap<>();

        for(String g : grams){

            if(ngramIndex.containsKey(g)){

                for(String existingDoc : ngramIndex.get(g)){

                    matchCount.put(
                            existingDoc,
                            matchCount.getOrDefault(existingDoc,0)+1
                    );
                }
            }
        }

        return matchCount;
    }

    public static void main(String[] args){

        PlagiarismDetector detector = new PlagiarismDetector();

        detector.addDocument("essay_092",
                "machine learning models require good data for training");

        Map<String,Integer> result =
                detector.analyzeDocument("essay_123",
                        "machine learning models require good data");

        System.out.println(result);

    }
}