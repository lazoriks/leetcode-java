import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class test {

    public double findAverage(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("Array is null or empty");
        }
        double sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        return sum / numbers.length;
    }

    // write function for coin change problem
    public int coinChange(int[] coins, int amount) {
        if (amount < 0) {
            return -1;
        }
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        for (int i = 1; i <= amount; i++) {
            dp[i] = max;
        }
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i - coin] + 1);
            }
        }
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // two sum problem
    public int[] twoSum(int[] nums, int target) {     
        int[] result = new int[2];
        final Map<Integer, Integer> map = new java.util.HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int difference = target - nums[i];
            if (map.containsKey(difference)) {
                result[0] = i;
                result[1] = map.get(difference);
                break;
            }
            map.put(nums[i], i);
        }
        return result;
    }

    //parkingSystem
    class ParkingSystem {
        private final int[] capacity;

        public ParkingSystem(int big, int medium, int small) {
            capacity = new int[]{big, medium, small};
        }

        public boolean addCar(int carType) {
            if (capacity[carType - 1] > 0) {
                capacity[carType - 1]--;
                return true;
            }
            return false;
        }
    }

    //pattern Singleton
    public static class Singleton { 
        private static Singleton instance;

        private Singleton() {
            // private constructor to prevent instantiation
        }

        public static Singleton getInstance() {
            if (instance == null) {
                instance = new Singleton();
            }
            return instance;
        }
    }

    //pattern Prototype
    public static class Prototype implements Cloneable {    
        private String field;

        public Prototype(String field) {
            this.field = field;
        }

        public String getField() {
            return field;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    //threeSum
    public List<List<Integer>> threeSum(int[] nums) {   
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicates
            int left = i + 1, right = nums.length - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left + 1]) left++; // skip duplicates
                    while (left < right && nums[right] == nums[right - 1]) right--; // skip duplicates
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    public Map<String, User> getUsers(List<User> users) {
        return users.stream()
                    .collect(Collectors.toMap(
                        u -> u.getEmail(), // ключ — email
                        u -> u             // значення — об'єкт User
                    ));
}

    public static class User {
        private String name;
        private String email;

        public User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    public static void main(String[] args) {
        test solution = new test();

        // Example usage of findAverage
        int[] numbers = {1, 2, 3, 4, 5};
        double average = solution.findAverage(numbers);
        System.out.println("Average: " + average);

        // Example usage of coinChange
        int[] coins = {1, 2, 5};
        int amount = 11;
        int minCoins = solution.coinChange(coins, amount);
        System.out.println("Minimum coins needed: " + minCoins);

        // Example usage of twoSum
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] indices = solution.twoSum(nums, target);
        System.out.println("Indices: " + Arrays.toString(indices));
    }

    public boolean ContainsOnce(List<User> users, User targetUser) {
        long count = users.stream()
                          .filter(user -> user.getEmail().equals(targetUser.getEmail()))
                          .count();
        return count == 1;
    }
    
}
