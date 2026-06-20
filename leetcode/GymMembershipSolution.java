import java.util.*;

/**
 * Повне робоче рішення gym-таску з твого Karat-інтерв'ю.
 * Реконструйовано за описом інтерв'юера. Містить:
 *   1) Виправлений баг підрахунку платних учасників (SILVER + GOLD, не лише GOLD)
 *   2) Новий метод addWorkout(memberId, workout)
 *   3) Новий метод getAverageWorkoutDurations()
 *   4) Прості тести в main(), щоб запустити й побачити, що все працює.
 *
 * Запуск:  javac GymMembershipSolution.java && java GymMembershipSolution
 */
public class GymMembershipSolution {

    // BRONZE — безкоштовний дефолт; SILVER та GOLD — платні
    enum MembershipStatus { BRONZE, SILVER, GOLD }

    // ---- Member ----
    static class Member {
        int memberId;
        String name;
        MembershipStatus status;
        List<Workout> workouts = new ArrayList<>();   // тренування цього учасника

        Member(int memberId, String name) {
            this(memberId, name, MembershipStatus.BRONZE); // дефолт — BRONZE
        }
        Member(int memberId, String name, MembershipStatus status) {
            this.memberId = memberId;
            this.name = name;
            this.status = status;
        }
        @Override public String toString() {
            return "Member{" + memberId + ", " + name + ", " + status + "}";
        }
    }

    // ---- Workout ----
    static class Workout {
        int id;
        int startTime;   // у хвилинах від початку дня
        int endTime;     // у хвилинах від початку дня

        Workout(int id, int startTime, int endTime) {
            this.id = id;
            this.startTime = startTime;
            this.endTime = endTime;
        }
        int duration() { return endTime - startTime; } // тривалість у хвилинах
    }

    // ---- Statistics (повертається з getMembershipStatistics) ----
    static class MembershipStatistics {
        int total;
        int totalPaid;
        double conversionRate;
        MembershipStatistics(int total, int totalPaid, double conversionRate) {
            this.total = total;
            this.totalPaid = totalPaid;
            this.conversionRate = conversionRate;
        }
    }

    // ---- Membership (основний клас з операціями) ----
    static class Membership {
        List<Member> members = new ArrayList<>();

        void addMember(Member m) { members.add(m); }

        boolean updateMemberStatus(int memberId, MembershipStatus newStatus) {
            for (Member m : members) {
                if (m.memberId == memberId) { m.status = newStatus; return true; }
            }
            return false;
        }

        // helper: чи платний учасник
        private boolean isPaid(Member m) {
            return m.status == MembershipStatus.SILVER || m.status == MembershipStatus.GOLD;
        }

        MembershipStatistics getMembershipStatistics() {
            int total = members.size();
            int totalPaid = 0;
            for (Member m : members) {
                // ===== ВИПРАВЛЕНИЙ БАГ =====
                // Було: рахувався лише GOLD -> тест давав "should be 4, was 1".
                // Стало: рахуємо і SILVER, і GOLD.
                if (isPaid(m)) totalPaid++;
            }
            double conversionRate = (total == 0) ? 0.0 : (double) totalPaid / total;
            return new MembershipStatistics(total, totalPaid, conversionRate);
        }

        // ===== НОВИЙ МЕТОД 1 =====
        // Прив'язує workout до учасника за memberId. true — якщо учасник існує,
        // false — якщо учасника з таким ID немає (тоді workout ігнорується).
        public boolean addWorkout(int memberId, Workout workout) {
            for (Member m : members) {
                if (m.memberId == memberId) {
                    m.workouts.add(workout);   // асоціюємо тренування з учасником
                    return true;
                }
            }
            return false;                       // учасника не знайдено
        }

        // ===== НОВИЙ МЕТОД 2 =====
        // Повертає мапу memberId -> середня тривалість тренувань (хв).
        // Запис є для КОЖНОГО учасника. Якщо тренувань немає -> значення null.
        public Map<Integer, Double> getAverageWorkoutDurations() {
            Map<Integer, Double> result = new HashMap<>();
            for (Member m : members) {
                if (m.workouts.isEmpty()) {
                    result.put(m.memberId, null);        // немає тренувань -> null
                } else {
                    long sum = 0;
                    for (Workout w : m.workouts) 
                    {
                        sum += w.duration();
                    }
                    double avg = (double) sum / m.workouts.size();
                    result.put(m.memberId, avg);
                }
            }
            return result;
        }
    }

    // =======================================================================
    //  Тести (аналог unit-тестів з інтерв'ю). Запусти й переконайся: усе зелене.
    // =======================================================================
    public static void main(String[] args) {
        testMember();
        testMembership();
        testAddWorkout();
        testAverageWorkoutDurations();
        System.out.println("\nУсі тести пройдено ✅");
    }

    static void testMember() {
        Member m = new Member(1, "Alice", MembershipStatus.SILVER);
        assertEqual(1, m.memberId, "memberId");
        assertEqual("Alice", m.name, "name");
        assertEqual(MembershipStatus.SILVER, m.status, "status");
        System.out.println("testMember ok");
    }

    static void testMembership() {
        Membership ms = new Membership();
        ms.addMember(new Member(1, "A", MembershipStatus.BRONZE)); // не платний
        ms.addMember(new Member(2, "B", MembershipStatus.SILVER)); // платний
        ms.addMember(new Member(3, "C", MembershipStatus.SILVER)); // платний
        ms.addMember(new Member(4, "D", MembershipStatus.GOLD));   // платний
        ms.addMember(new Member(5, "E", MembershipStatus.GOLD));   // платний

        MembershipStatistics stats = ms.getMembershipStatistics();
        assertEqual(5, stats.total, "total");
        assertEqual(4, stats.totalPaid, "totalPaid (should be 4)"); // саме той баг
        System.out.println("testMembership ok");
    }

    static void testAddWorkout() {
        Membership ms = new Membership();
        ms.addMember(new Member(12, "Bob", MembershipStatus.GOLD));

        boolean added = ms.addWorkout(12, new Workout(100, 600, 660)); // 60 хв
        assertEqual(true, added, "addWorkout existing member -> true");

        boolean missing = ms.addWorkout(999, new Workout(101, 600, 660)); // немає такого ID
        assertEqual(false, missing, "addWorkout missing member -> false");
        System.out.println("testAddWorkout ok");
    }

    static void testAverageWorkoutDurations() {
        Membership ms = new Membership();
        ms.addMember(new Member(1, "A", MembershipStatus.GOLD));
        ms.addMember(new Member(2, "B", MembershipStatus.SILVER)); // без тренувань -> null

        ms.addWorkout(1, new Workout(10, 600, 660)); // 60 хв
        ms.addWorkout(1, new Workout(11, 700, 740)); // 40 хв -> середнє 50

        Map<Integer, Double> avg = ms.getAverageWorkoutDurations();
        assertEqual(50.0, avg.get(1), "avg member 1");
        assertEqual(null, avg.get(2), "member 2 has no workouts -> null");
        assertEqual(true, avg.containsKey(2), "map contains entry for every member");
        System.out.println("testAverageWorkoutDurations ok");
    }

    // мінімальний assert-хелпер
    static void assertEqual(Object expected, Object actual, String label) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("FAIL [" + label + "]: expected=" + expected + ", actual=" + actual);
        }
    }
}