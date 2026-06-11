public class TryFinallyReturn {
    public static void main(String[] args) {
        System.out.println("Результат: " + test());
    }

    public static int test() {
        try {
            System.out.println("try block");
            /* return 1; */
        } finally {
            System.out.println("finally block");
            //return 2;
        }
        return 0;
    }
}
