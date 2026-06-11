import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FindDuplicates {
    public static void main(String[] args) {

        List<Integer> nums = List.of(4, 3, 2, 7, 8, 2, 3, 1);
        System.out.println(nums);

        Set<Integer> duplicates = nums.stream()
                .filter(n -> Collections.frequency(nums, n) > 1)
                .collect(Collectors.toSet());
        System.out.println(duplicates);

    }

    public String reveStringManual(String s) {
        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char temp = chars[left];
            chars[left] = chars[right];
            chars[right] = temp;
            left++;
            right--;
        }
        return new String(chars);
    }

    public String reverseString(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public boolean isPalindrome(String s) {
        String clean = s.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String reversed = new StringBuilder(clean).reverse().toString();
        return clean.equals(reversed);
    }

    public int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[] { i, j };
                }
            }
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public int[] twoSumOptimized(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[] { map.get(complement), i };
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    public List<String> fizzBuzz(int n) {
        return IntStream.rangeClosed(1, n)
                .mapToObj(i -> {
                    if (i % 15 == 0)
                        return "FizzBuzz";
                    else if (i % 3 == 0)
                        return "Fizz";
                    else if (i % 5 == 0)
                        return "Buzz";
                    else
                        return String.valueOf(i);
                })
                .collect(Collectors.toList());
    }

    public int findMaxManual(int[] nums) {
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    public int findMaxStream(int[] nums) {
        return IntStream.of(nums).max().orElseThrow();
    }

    // Завдання 1: Find the first non-repeating character in a string (5 хв)
    public char firstNonRepeating(String s) {
        Map<Character, Integer> count = new LinkedHashMap<>();
        for (char c : s.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            if (entry.getValue() == 1)
                return entry.getKey();
        }
        return '\0';
    }

    // Завдання 2: Merge two sorted arrays (5 хв)
    public int[] merge(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length) {
            result[k++] = (a[i] <= b[j]) ? a[i++] : b[j++];
        }
        while (i < a.length)
            result[k++] = a[i++];
        while (j < b.length)
            result[k++] = b[j++];
        return result;
    }

    // Завдання 3: Group employees by department with average salary (10 хв)
    /* public Map<String, Double> averageSalaryByDept(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(
                        Employee::getDepartment,
                        Collectors.averagingDouble(Employee::getSalary)));
    } */

    // Завдання 4: LRU Cache (10 хв)
    class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // Завдання 5: REST endpoint для створення замовлення (10 хв)
    /* @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        Order order = orderService.createOrder(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(OrderResponse.from(order));
    } */
}
