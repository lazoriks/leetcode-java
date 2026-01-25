import java.util.*;

public class Tasks15{
    
    public static String findNumber(List<Integer> arr, int k) {
        // Проста перевірка через contains
        return arr.contains(k) ? "YES" : "NO";
        
        // Або через цикл (але тут була помилка!)
        /* for (Integer number : arr) {
            if (number == k) { // Виправлено: порівнюємо з k, а не з arr
                return "YES";u6
            }
        }
        return "NO"; */
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

    void heapSort(int[] arr) {
    // Будую купу
        for (int i = arr.length/2-1; i >= 0; i--) {
            heapify(arr, arr.length, i);
        }
        
        // Один за одним витягаю елементи
        for (int i = arr.length-1; i > 0; i--) {
            // Переміщую корінь (найбільший) в кінець
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            
            // Відновлюю купу для решти елементів
            heapify(arr, i, 0);
        }
    }
    
    private void heapify(int[] arr, int length, int i) {
        int largest = i; // Ініціалізую найбільший як корінь
        int left = 2*i + 1; // лівий = 2*i + 1
        int right = 2*i + 2; // правий = 2*i + 2
        
        // Якщо лівий дочірній елемент більший за корінь
        if (left < length && arr[left] > arr[largest]) {
            largest = left;
        }
        
        // Якщо правий дочірній елемент більший за найбільший до цього моменту
        if (right < length && arr[right] > arr[largest]) {
            largest = right;
        }
        
        // Якщо найбільший не корінь
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            
            // Рекурсивно heapify піддерево, яке має найбільший елемент
            heapify(arr, length, largest);
        }
    }

    void bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (arr[j] > arr[j+1]) {
                    // Міняємо місцями
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    void selectionSort(int[] arr) {
        int n = arr.length;
        
        for (int i = 0; i < n-1; i++) {
            // Знаходжу найменший елемент у невідсортованій частині
            int minIndex = i;
            for (int j = i+1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            
            // Міняю місцями з поточним елементом
            int temp = arr[minIndex];
            arr[minIndex] = arr[i];
            arr[i] = temp;
        }
    }

    void insertionSort(int[] arr) {
        int n = arr.length;
        
        for (int i = 1; i < n; i++) {
            int key = arr[i];  // Поточний елемент, який вставляємо
            int j = i - 1;
            
            // Зсуваємо елементи, які більші за key, праворуч
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            
            // Вставляємо key на правильне місце
            arr[j + 1] = key;
        }
    }

    public interface Flyable {
        void fly();
    }

    public static class Bird implements Flyable {
        public void fly() {
            System.out.println("Bird flies");
        }

        void chirp() {
            System.out.println("Bird chirps");
        }
    }
    public static void main(String[] args) {
        // Не потрібно створювати об'єкт, бо методи static!
        // test solution = new test(); - це зайве
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Виклик статичних методів без створення об'єкта
        List<Integer> oddNumbers = oddNumbers(3, 9);
        System.out.println("Odd numbers between 3 and 9: " + oddNumbers);

        String result1 = findNumber(numbers, 3);
        System.out.println("Finding 3 in the list: " + result1);
        String result2 = findNumber(numbers, 6);
        System.out.println("Finding 6 in the list: " + result2);

        Flyable f = new Bird();  // Upcasting до інтерфейсу
        f.fly();
        // f.chirp();            // Чому помилка?
        
        if (f instanceof Bird) {
            Bird b = (Bird) f;   // Downcasting до класу
            b.chirp();           // Тепер можна
        }
        
        // Приведення між інтерфейсами:
        Object obj = new Bird();
        Flyable flyable = (Flyable) obj;  // Безпечно?
        flyable.fly();
    }
}