import java.util.*;

public static String findNumber(List<Integer> arr, int k) {
        //return arr.contains(k) ? "YES" : "NO";
        for (Integer number : arr) {
            if (number.equals(arr)) {
                return "YES";
            }
        }
        return "NO";
    }

public static List<Integer> oddNumbers(int l, int r) {
        List<Integer> oddNumbersList = new ArrayList<>();
        for (int i = l; i <= r; i++) {
            if (i % 2 != 0) {
                oddNumbersList.add(i);
            }
        }
        return oddNumbersList;
        
    }
    

public static void main(String[] args) {
        test solution = new test();

        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
       
}

