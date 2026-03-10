import java.util.*;

class Transaction {

    int id;
    int amount;
    String merchant;
    String account;
    long timestamp;

    public Transaction(int id, int amount, String merchant, String account, long timestamp) {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.timestamp = timestamp;
    }
}

public class FinancialTransactionAnalyzer {

    // -----------------------------
    // Classic Two Sum
    // -----------------------------
    public static List<int[]> findTwoSum(List<Transaction> transactions, int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {
                result.add(new int[]{map.get(complement).id, t.id});
            }

            map.put(t.amount, t);
        }

        return result;
    }

    // -----------------------------
    // Two Sum within Time Window
    // -----------------------------
    public static List<int[]> findTwoSumWithTime(List<Transaction> transactions, int target, long windowMillis) {

        HashMap<Integer, List<Transaction>> map = new HashMap<>();
        List<int[]> result = new ArrayList<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                for (Transaction prev : map.get(complement)) {

                    if (Math.abs(t.timestamp - prev.timestamp) <= windowMillis) {
                        result.add(new int[]{prev.id, t.id});
                    }
                }
            }

            map.putIfAbsent(t.amount, new ArrayList<>());
            map.get(t.amount).add(t);
        }

        return result;
    }

    // -----------------------------
    // Duplicate Transaction Detection
    // -----------------------------
    public static Map<String, List<Transaction>> detectDuplicates(List<Transaction> transactions) {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "_" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        Map<String, List<Transaction>> duplicates = new HashMap<>();

        for (String key : map.keySet()) {

            Set<String> accounts = new HashSet<>();

            for (Transaction t : map.get(key))
                accounts.add(t.account);

            if (accounts.size() > 1)
                duplicates.put(key, map.get(key));
        }

        return duplicates;
    }

    // -----------------------------
    // K-Sum Implementation
    // -----------------------------
    public static List<List<Integer>> kSum(int[] nums, int target, int k) {

        Arrays.sort(nums);
        return kSumHelper(nums, target, k, 0);
    }

    private static List<List<Integer>> kSumHelper(int[] nums, int target, int k, int start) {

        List<List<Integer>> result = new ArrayList<>();

        if (k == 2) {

            int left = start;
            int right = nums.length - 1;

            while (left < right) {

                int sum = nums[left] + nums[right];

                if (sum == target) {

                    result.add(Arrays.asList(nums[left], nums[right]));
                    left++;
                    right--;

                } else if (sum < target)
                    left++;
                else
                    right--;
            }

            return result;
        }

        for (int i = start; i < nums.length; i++) {

            for (List<Integer> subset :
                    kSumHelper(nums, target - nums[i], k - 1, i + 1)) {

                List<Integer> temp = new ArrayList<>();
                temp.add(nums[i]);
                temp.addAll(subset);
                result.add(temp);
            }
        }

        return result;
    }

    // -----------------------------
    // Test Example
    // -----------------------------
    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();

        long now = System.currentTimeMillis();

        transactions.add(new Transaction(1, 500, "StoreA", "acc1", now));
        transactions.add(new Transaction(2, 300, "StoreB", "acc2", now + 1000));
        transactions.add(new Transaction(3, 200, "StoreC", "acc3", now + 2000));
        transactions.add(new Transaction(4, 500, "StoreA", "acc4", now + 3000));

        System.out.println("Two Sum Results:");

        for (int[] pair : findTwoSum(transactions, 500))
            System.out.println(pair[0] + " , " + pair[1]);

        System.out.println("\nTwo Sum with Time Window:");

        for (int[] pair : findTwoSumWithTime(transactions, 500, 3600000))
            System.out.println(pair[0] + " , " + pair[1]);

        System.out.println("\nDuplicate Detection:");

        Map<String, List<Transaction>> dup = detectDuplicates(transactions);

        for (String key : dup.keySet()) {
            System.out.println("Duplicate: " + key + " -> " + dup.get(key).size() + " transactions");
        }

        System.out.println("\nK-Sum Example:");

        int[] nums = {500, 300, 200, 100, 400};

        List<List<Integer>> res = kSum(nums, 1000, 3);

        for (List<Integer> list : res)
            System.out.println(list);
    }
}