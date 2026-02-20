import java.util.*;
import java.util.stream.Collectors;

public class HashtagAnalyzer {

    public boolean isValidBrackets(String s) {
        if (s == null || s.length() % 2 != 0) {
            return false;
        }

        Map<Character, Character> bracketPairs = new HashMap<>();
        bracketPairs.put(')', '(');
        //bracketPairs.put('}', '{');
        //bracketPairs.put(']', '[');

        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (bracketPairs.containsValue(c)) {
                stack.push(c);
            } else if (bracketPairs.containsKey(c)) {
                if (stack.isEmpty() || stack.pop() != bracketPairs.get(c)) {
                    return false;
                }
            }
        }

        return stack.isEmpty();
    }   


    public List<String> getMessages(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return new ArrayList<>();
        }

        // Збираємо всі хештеги та підраховуємо частоту
        Map<String, Long> hashtagFrequency = texts.stream()
                .flatMap(text -> Arrays.stream(text.split("\\s+")))
                .filter(word -> word.startsWith("#") && word.length() > 1)
                .map(word -> word.substring(1))
                .collect(Collectors.groupingBy(
                        hashtag -> hashtag,
                        Collectors.counting()));

        // Сортуємо тільки за частотою (спадання)
        return hashtagFrequency.entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        HashtagAnalyzer analyzer = new HashtagAnalyzer();

        List<String> texts = Arrays.asList(
                "We have a list of Strings containing Twitter messages. #Bella #Sova",
                "Task: Implement a method, which returns the List of unique hashtags #Bella",
                "Result: hashtags should be sorted by usage frequency #Sova #loganitive",
                "these are left on the DB Investment in Africa and Lightbend #development",
                "this one left on the DB Investment in integrated platforms #development #Bella",
                "Special characters in hashtags: #hello! #world? #test123");

        List<String> result = analyzer.getMessages(texts);
        System.out.println(result);
        // Можливий результат: [Bella, development, Sova, loganitive, hello!, world?,
        // test123]
        String brackets = "[[[())]}";
        System.out.println(analyzer.isValidBrackets(brackets)); // false

    }
}