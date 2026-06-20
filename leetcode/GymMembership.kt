// Kotlin-версія gym-таску. Та сама логіка, що й у Java-рішенні.
// Запуск:  kotlinc GymMembership.kt -include-runtime -d gym.jar && java -jar gym.jar

enum class MembershipStatus { BRONZE, SILVER, GOLD }   // BRONZE — безкоштовний дефолт

// data class автоматично дає equals/hashCode/toString/copy
data class Workout(val id: Int, val startTime: Int, val endTime: Int) {
    val duration: Int get() = endTime - startTime       // обчислювана властивість
}

data class Member(
    val memberId: Int,
    val name: String,
    var status: MembershipStatus = MembershipStatus.BRONZE,   // дефолтне значення прямо в сигнатурі
    val workouts: MutableList<Workout> = mutableListOf()
)

data class MembershipStatistics(val total: Int, val totalPaid: Int, val conversionRate: Double)

class Membership {
    val members = mutableListOf<Member>()

    fun addMember(m: Member) { members.add(m) }

    fun updateMemberStatus(memberId: Int, newStatus: MembershipStatus): Boolean {
        // find повертає null, якщо не знайдено -> елвіс-оператор ?: одразу виходить
        val m = members.find { it.memberId == memberId } ?: return false
        m.status = newStatus
        return true
    }

    private fun isPaid(m: Member) =
        m.status == MembershipStatus.SILVER || m.status == MembershipStatus.GOLD

    fun getMembershipStatistics(): MembershipStatistics {
        val total = members.size
        val totalPaid = members.count { isPaid(it) }    // ВИПРАВЛЕНИЙ БАГ: SILVER + GOLD
        val conversionRate = if (total == 0) 0.0 else totalPaid.toDouble() / total
        return MembershipStatistics(total, totalPaid, conversionRate)
    }

    // НОВИЙ МЕТОД 1
    fun addWorkout(memberId: Int, workout: Workout): Boolean {
        val m = members.find { it.memberId == memberId } ?: return false
        m.workouts.add(workout)
        return true
    }

    // НОВИЙ МЕТОД 2: memberId -> середня тривалість (null якщо тренувань немає)
    fun getAverageWorkoutDurations(): Map<Int, Double?> =
        members.associate { m ->
            val avg: Double? =
                if (m.workouts.isEmpty()) null
                else m.workouts.map { it.duration }.sum().toDouble() / m.workouts.size
            m.memberId to avg
        }
}

// ---- Тести ----
fun <T> assertEqual(expected: T, actual: T, label: String) {
    check(expected == actual) { "FAIL [$label]: expected=$expected, actual=$actual" }
}

fun main() {
    // testMembership: 4 платних з 5
    val ms = Membership()
    ms.addMember(Member(1, "A", MembershipStatus.BRONZE))
    ms.addMember(Member(2, "B", MembershipStatus.SILVER))
    ms.addMember(Member(3, "C", MembershipStatus.SILVER))
    ms.addMember(Member(4, "D", MembershipStatus.GOLD))
    ms.addMember(Member(5, "E", MembershipStatus.GOLD))
    val stats = ms.getMembershipStatistics()
    assertEqual(5, stats.total, "total")
    assertEqual(4, stats.totalPaid, "totalPaid")
    println("testMembership ok")

    // testAddWorkout
    assertEqual(true, ms.addWorkout(1, Workout(100, 600, 660)), "addWorkout existing")
    assertEqual(false, ms.addWorkout(999, Workout(101, 600, 660)), "addWorkout missing")
    println("testAddWorkout ok")

    // testAverageWorkoutDurations
    ms.addWorkout(1, Workout(11, 700, 740))      // у учасника 1 тепер 60 і 40 -> середнє 50
    val avg = ms.getAverageWorkoutDurations()
    assertEqual(50.0, avg[1], "avg member 1")
    assertEqual(null, avg[2], "member 2 no workouts -> null")
    assertEqual(true, avg.containsKey(2), "entry for every member")
    println("testAverageWorkoutDurations ok")

    println("Усі тести пройдено ✅")
}
