import java.util.*;

public class CollectionsTask {
    
    public List<String> getMessages(List<String> texts) {
        if (texts == null || texts.isEmpty()) {
            return new ArrayList<>();
        }

        // Map для підрахунку частоти хештегів
        Map<String, Integer> hashtagFrequency = new HashMap<>();

        // Проходимо по всіх повідомленнях
        for (String text : texts) {
            // Розбиваємо текст на слова
            String[] words = text.split("\\s+");

            // Перевіряємо кожне слово
            for (String word : words) {
                // Перевіряємо, чи слово починається з #
                if (word.startsWith("#")) {
                    // Видаляємо символ # і залишаємо тільки слово
                    String hashtag = word.substring(1);

                    // Додаткова перевірка: хештег повинен містити тільки букви та цифри
                    if (isValidHashtag(hashtag)) {
                        hashtagFrequency.put(hashtag, hashtagFrequency.getOrDefault(hashtag, 0) + 1);
                    }
                }
            }
        }

        // Створюємо список з унікальних хештегів
        List<String> uniqueHashtags = new ArrayList<>(hashtagFrequency.keySet());

        // Сортуємо за частотою (від більшої до меншої)
        // Якщо частота однакова, сортуємо за алфавітом
        uniqueHashtags.sort((h1, h2) -> {
            int freqCompare = hashtagFrequency.get(h2).compareTo(hashtagFrequency.get(h1));
            if (freqCompare != 0) {
                return freqCompare;
            }
            return h1.compareTo(h2);
        });

        return uniqueHashtags;
    }

    // Допоміжний метод для перевірки валідності хештегу
    private boolean isValidHashtag(String hashtag) {
        if (hashtag == null || hashtag.isEmpty()) {
            return false;
        }

        // Перевіряємо, що всі символи - букви або цифри
        for (char c : hashtag.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Приклад використання
    public static void main(String[] args) {
        CollectionsTask analyzer = new CollectionsTask();

        List<String> texts = new ArrayList<>();
        texts.add("We have a list of Strings containing Twitter messages. #Bella #Sova");
        texts.add("Task: Implement a method, which returns the List of unique hashtags #Bella");
        texts.add("Result: hashtags should be sorted by usage frequency #Sova #loganitive");
        texts.add("these are left on the DB Investment in Africa and Lightbend #development");
        texts.add("this one left on the DB Investment in integrated platforms #development #Bella");

        List<String> result = analyzer.getMessages(texts);
        System.out.println(result); // Виведе: [Bella, development, Sova, loganitive]
    }
}
