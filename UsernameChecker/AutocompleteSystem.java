import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    boolean isWord = false;
    String query;
}

public class AutocompleteSystem {

    TrieNode root = new TrieNode();
    HashMap<String,Integer> frequency = new HashMap<>();

    public void insert(String query){

        TrieNode node = root;

        for(char c : query.toCharArray()){

            node.children.putIfAbsent(c,new TrieNode());
            node = node.children.get(c);

        }

        node.isWord = true;
        node.query = query;

        frequency.put(query,frequency.getOrDefault(query,0)+1);
    }

    public List<String> search(String prefix){

        TrieNode node = root;

        for(char c : prefix.toCharArray()){

            if(!node.children.containsKey(c))
                return new ArrayList<>();

            node = node.children.get(c);
        }

        List<String> results = new ArrayList<>();

        dfs(node,results);

        PriorityQueue<String> heap =
                new PriorityQueue<>(
                        (a,b) -> frequency.get(a) - frequency.get(b)
                );

        for(String q : results){

            heap.offer(q);

            if(heap.size() > 10)
                heap.poll();
        }

        return new ArrayList<>(heap);
    }

    private void dfs(TrieNode node, List<String> results){

        if(node.isWord)
            results.add(node.query);

        for(TrieNode child : node.children.values())
            dfs(child,results);
    }

    public static void main(String[] args){

        AutocompleteSystem system = new AutocompleteSystem();

        system.insert("java tutorial");
        system.insert("javascript");
        system.insert("java download");

        System.out.println(system.search("jav"));
    }
}