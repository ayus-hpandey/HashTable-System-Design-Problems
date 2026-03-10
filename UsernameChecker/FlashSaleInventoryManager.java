import java.util.*;

public class FlashSaleInventoryManager {

    static HashMap<String,Integer> stockMap = new HashMap<>();
    static LinkedHashMap<Integer,Integer> waitingList = new LinkedHashMap<>();

    public static int checkStock(String productId){

        return stockMap.getOrDefault(productId,0);

    }

    public synchronized static String purchaseItem(String productId, int userId){

        int stock = stockMap.getOrDefault(productId,0);

        if(stock > 0){

            stockMap.put(productId, stock-1);

            return "Success, "+(stock-1)+" units remaining";

        } else {

            waitingList.put(waitingList.size()+1, userId);

            return "Added to waiting list, position #"+waitingList.size();

        }

    }

    public static void main(String[] args) {

        stockMap.put("IPHONE15_256GB",100);

        System.out.println("Stock: "+checkStock("IPHONE15_256GB"));

        System.out.println(purchaseItem("IPHONE15_256GB",12345));
        System.out.println(purchaseItem("IPHONE15_256GB",67890));

    }
}