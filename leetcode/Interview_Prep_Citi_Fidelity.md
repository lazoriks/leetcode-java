# Підготовка до інтерв'ю — Zinkworks (Citibank / Fidelity)

> Робочий файл для самопідготовки. Citi = технічна розмова + **Karat** кодинг-тест.
> Fidelity = розмова з директором + технічне інтерв'ю (cloud migration / COBOL).
> Усі Java-рішення нижче скомпільовані й перевірені на коректність.

---

## Зміст
1. [Karat — формат і стратегія](#1-karat--формат-і-стратегія)
2. [Як говорити під час кодингу (фреймворк)](#2-як-говорити-під-час-кодингу)
3. [Задачі Karat з рішеннями](#3-задачі-karat-з-рішеннями)
4. [Шпаргалка по складності](#4-шпаргалка-по-складності)
5. [Типові помилки в Java на інтерв'ю](#5-типові-помилки-в-java)
6. [Fidelity — проєкт і питання](#6-fidelity--проєкт-і-питання)
7. [Фінальний чеклист](#7-фінальний-чеклист)

---

## 1. Karat — формат і стратегія

Karat — це сторонній сервіс, який проводить технічні скринінги **за Citibank**. Що треба знати:

- Інтерв'юер — **інженер Karat**, не з Citi. Сесія **записується** (відео + код).
- Структура (~50-60 хв):
  - 5-10 хв — **поведінкові/досвідні питання** ("найскладніший баг", "де юзав конкурентність").
  - ~35 хв — **2 кодинг-задачі** у спільному редакторі.
  - кілька хв — твої питання.
- Задачі — **практичні, medium**, часто **багатоетапні** (Part 1 простий → Part 2 ускладнює те саме).
- Оцінюють **і код, і як ти думаєш уголос**. Мовчазне правильне рішення = слабка оцінка.

**Головні правила:**
- Проговорюй підхід **до** того, як писати.
- Завжди сам називай **edge-cases** (порожній вхід, null, дублікати, переповнення).
- Назви **складність** (час/пам'ять) у кінці.
- Якщо застряг — думай уголос, інтерв'юер може підказати. Тиша = провал.
- Пиши чисто: змінні з іменами, маленькі методи.

---

## 2. Як говорити під час кодингу

Запам'ятай 5 кроків (англійською, бо інтерв'ю англійською):

1. **Repeat / clarify** — *"Let me make sure I understand. The input is... and I need to return..."*
2. **Examples & edge cases** — *"What if the input is empty? Can there be duplicates? Can numbers be negative?"*
3. **Approach** — *"I will use a hash map to count... This gives O(n) time."*
4. **Code while narrating** — *"I loop through the list. For each item I..."*
5. **Test & complexity** — *"Let me trace one example. Time is O(n), space is O(k)."*

Корисні фрази:
- *"Let me think out loud for a second."*
- *"A brute force would be O(n²), but I can do better with a hash map."*
- *"Let me handle the edge case where the list is empty first."*
- *"I will use `long` here to avoid integer overflow with money."*

---

## 3. Задачі Karat з рішеннями

### Задача 1 — Середні бали (warm-up, hash map)

> Дано записи `"Name,Subject,Score"`. Part 1: середній бал кожного студента. Part 2: студент із найвищим середнім.

```java
public static Map<String, Double> averages(String[] records) {
    Map<String, int[]> agg = new HashMap<>(); // name -> [sum, count]
    for (String rec : records) {
        String[] parts = rec.split(",");
        String name = parts[0].trim();
        int score = Integer.parseInt(parts[2].trim());
        agg.putIfAbsent(name, new int[2]);
        agg.get(name)[0] += score;
        agg.get(name)[1] += 1;
    }
    Map<String, Double> result = new HashMap<>();
    for (var e : agg.entrySet())
        result.put(e.getKey(), (double) e.getValue()[0] / e.getValue()[1]);
    return result;
}

public static String topStudent(String[] records) {
    String top = null; double best = Double.NEGATIVE_INFINITY;
    for (var e : averages(records).entrySet())
        if (e.getValue() > best) { best = e.getValue(); top = e.getKey(); }
    return top;
}
```

**Сказати:** "Hash map: ключ — ім'я, значення — пара [сума, кількість]. Один прохід O(n). Для Part 2 — прохід по середніх, шукаю максимум."
**Edge-cases:** порожній вхід, нечисловий бал, нічия в Part 2.

---

### Задача 2 — Баланси рахунків + овердрафт (running state, під фінсектор)

> Транзакції `"accountId,type,amount"` (type = DEBIT/CREDIT). Part 1: фінальний баланс. Part 2: рахунки, що **йшли в мінус** будь-коли (порядок важливий).

```java
public static Map<String, Long> balances(List<String> txns) {
    Map<String, Long> bal = new HashMap<>();
    for (String t : txns) {
        String[] p = t.split(",");
        long amount = Long.parseLong(p[2].trim());
        long delta = p[1].trim().equals("CREDIT") ? amount : -amount;
        bal.merge(p[0].trim(), delta, Long::sum);
    }
    return bal;
}

public static Set<String> wentNegative(List<String> txns) {
    Map<String, Long> running = new HashMap<>();
    Set<String> flagged = new LinkedHashSet<>();
    for (String t : txns) {
        String[] p = t.split(",");
        long amount = Long.parseLong(p[2].trim());
        long delta = p[1].trim().equals("CREDIT") ? amount : -amount;
        long nb = running.merge(p[0].trim(), delta, Long::sum);
        if (nb < 0) flagged.add(p[0].trim());
    }
    return flagged;
}
```

**Сказати (сильний сигнал!):** "Гроші тримаю в `long` (центи), а не `double` — щоб не було помилок округлення. Ключове в Part 2 — різниця між фінальним і проміжним балансом: рахунок може закінчити в плюсі, але провалитись усередині, тому перевіряю знак після кожної транзакції."

---

### Задача 3 — Пара з заданою сумою (hash map, класика)

> Дано масив чисел і `target`. Поверни індекси двох чисел, що дають у сумі target.

```java
public static int[] twoSum(int[] nums, int target) {
    Map<Integer,Integer> seen = new HashMap<>(); // value -> index
    for (int i = 0; i < nums.length; i++) {
        int need = target - nums[i];
        if (seen.containsKey(need)) return new int[]{seen.get(need), i};
        seen.put(nums[i], i);
    }
    return new int[]{-1, -1};
}
```

**Сказати:** "Наївно — два цикли O(n²). Краще — hash map: для кожного числа шукаю `target - число` за O(1). Один прохід, O(n) час, O(n) пам'ять."
**Edge-cases:** немає пари, дублікати, від'ємні числа.

---

### Задача 4 — Валідні дужки (stack)

> Рядок із `()[]{}`. Перевір, чи всі дужки правильно закриті.

```java
public static boolean isValid(String s) {
    Deque<Character> stack = new ArrayDeque<>();
    Map<Character,Character> match = Map.of(')','(', ']','[', '}','{');
    for (char c : s.toCharArray()) {
        if (c=='('||c=='['||c=='{') stack.push(c);
        else if (match.containsKey(c)) {
            if (stack.isEmpty() || stack.pop() != match.get(c)) return false;
        }
    }
    return stack.isEmpty();
}
```

**Сказати:** "Stack: відкриваючі кладу, закриваючі — звіряю з вершиною. Якщо не збігається або стек порожній — невалідно. У кінці стек має бути порожній. O(n) час і пам'ять."
**Edge-cases:** порожній рядок (валідний), тільки закриваючі, незакриті в кінці.

---

### Задача 5 — Об'єднати інтервали (sort + merge)

> Дано інтервали `[[1,3],[2,6],[8,10]]`. Об'єднай ті, що перетинаються → `[[1,6],[8,10]]`.

```java
public static int[][] merge(int[][] intervals) {
    if (intervals.length == 0) return new int[0][];
    Arrays.sort(intervals, (a,b) -> Integer.compare(a[0], b[0]));
    List<int[]> result = new ArrayList<>();
    int[] cur = intervals[0].clone();
    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] <= cur[1])
            cur[1] = Math.max(cur[1], intervals[i][1]);   // перетин — розширюю
        else { result.add(cur); cur = intervals[i].clone(); }
    }
    result.add(cur);
    return result.toArray(new int[0][]);
}
```

**Сказати:** "Спершу сортую за початком. Потім іду по порядку: якщо наступний починається ≤ поточного кінця — вони перетинаються, розширюю кінець. Інакше — закриваю поточний. O(n log n) через сортування."
**Edge-cases:** порожній вхід, один інтервал, повністю вкладені.

---

### Задача 6 — Обробка логів: топ користувачів (hash map + sort)

> Дано лог-рядки `"user action"`. Поверни користувачів за спаданням активності (нічия — за алфавітом).

```java
public static List<String> topUsers(List<String> logs) {
    Map<String,Integer> count = new HashMap<>();
    for (String log : logs)
        count.merge(log.split(" ")[0], 1, Integer::sum);
    return count.entrySet().stream()
        .sorted((a,b) -> !b.getValue().equals(a.getValue())
            ? b.getValue() - a.getValue()        // за кількістю спадно
            : a.getKey().compareTo(b.getKey()))  // нічия — алфавіт
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}
```

**Сказати:** "Рахую активність у hash map, потім сортую entry: спершу за кількістю спадно, при нічиї — за іменем. Логи — дуже типова тема для Karat."
**Edge-cases:** однакові кількості, порожній лог, кривий формат рядка.

---

### Задача 7 — Найдовший підрядок без повторів (sliding window)

> Дано рядок. Знайди довжину найдовшого підрядка без повторюваних символів. `"abcabcbb"` → 3.

```java
public static int longestUnique(String s) {
    Map<Character,Integer> last = new HashMap<>(); // символ -> останній індекс
    int start = 0, best = 0;
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        if (last.containsKey(c) && last.get(c) >= start)
            start = last.get(c) + 1;          // зсуваю вікно за дублікат
        last.put(c, i);
        best = Math.max(best, i - start + 1);
    }
    return best;
}
```

**Сказати:** "Sliding window: тримаю вікно [start..i] без повторів. Якщо символ уже був усередині вікна — зсуваю start за нього. Запам'ятовую останній індекс кожного символа. O(n) час."
**Edge-cases:** порожній рядок, усі однакові, усі різні.

---

### Задача 8 — Badge access (підпис Karat, багатоетапна)

> Записи `[name, "enter"/"exit"]`. Знайди: 1) хто **вийшов, не зайшовши**; 2) хто **зайшов і не вийшов** (або зайшов двічі поспіль).

```java
public static Map<String,List<String>> badgeViolations(List<String[]> records) {
    Set<String> inside = new HashSet<>();
    Set<String> enteredNoExit = new LinkedHashSet<>();
    Set<String> exitedNoEnter = new LinkedHashSet<>();
    for (String[] r : records) {
        String name = r[0], action = r[1];
        if (action.equals("enter")) {
            if (inside.contains(name)) enteredNoExit.add(name); // подвійний вхід
            inside.add(name);
        } else { // exit
            if (!inside.contains(name)) exitedNoEnter.add(name);
            else inside.remove(name);
        }
    }
    enteredNoExit.addAll(inside); // лишилися всередині в кінці
    Map<String,List<String>> result = new HashMap<>();
    result.put("enteredWithoutExit", new ArrayList<>(enteredNoExit));
    result.put("exitedWithoutEnter", new ArrayList<>(exitedNoEnter));
    return result;
}
```

**Сказати:** "Тримаю множину `inside` — хто зараз у будівлі. На enter: якщо вже всередині — це порушення (подвійний вхід). На exit: якщо його немає всередині — вийшов без входу. У кінці всі, хто лишився всередині, — це 'зайшов без виходу'. Це класична Karat-задача."
**Edge-cases:** подвійний вхід, подвійний вихід, порожній вхід.

---

### Задача 9 — Кількість островів (BFS/DFS на матриці)

> Сітка з `'1'` (земля) і `'0'` (вода). Порахуй острови (зв'язані по горизонталі/вертикалі).

```java
public static int numIslands(char[][] grid) {
    if (grid == null || grid.length == 0) return 0;
    int count = 0;
    for (int r = 0; r < grid.length; r++)
        for (int c = 0; c < grid[0].length; c++)
            if (grid[r][c] == '1') { count++; sink(grid, r, c); }
    return count;
}

private static void sink(char[][] g, int r, int c) {
    if (r<0 || c<0 || r>=g.length || c>=g[0].length || g[r][c]!='1') return;
    g[r][c] = '0';                       // "топлю" відвідану землю
    sink(g,r+1,c); sink(g,r-1,c); sink(g,r,c+1); sink(g,r,c-1);
}
```

**Сказати:** "Іду по сітці. Кожна нова '1' — новий острів, і я через DFS 'топлю' всю зв'язану землю в '0', щоб не рахувати двічі. O(rows × cols)."
**Edge-cases:** порожня сітка, уся вода, уся земля.

---

### Задача 10 — Групування анаграм (hash map зі сортованим ключем)

> Згрупуй слова-анаграми. `["eat","tea","tan","ate"]` → `[[eat,tea,ate],[tan]]`.

```java
public static List<List<String>> groupAnagrams(String[] words) {
    Map<String,List<String>> map = new HashMap<>();
    for (String w : words) {
        char[] ch = w.toCharArray();
        Arrays.sort(ch);
        String key = new String(ch);     // відсортовані літери = спільний ключ
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(w);
    }
    return new ArrayList<>(map.values());
}
```

**Сказати:** "Ключ анаграми — відсортовані літери слова. Усі анаграми дають однаковий ключ. `computeIfAbsent` створює список при першій появі. O(n × k log k), де k — довжина слова."

---

### Задача 11 — Перший унікальний символ (LinkedHashMap)

> Знайди перший символ, що не повторюється. `"swiss"` → `'w'`.

```java
public static Character firstUnique(String s) {
    Map<Character,Integer> count = new LinkedHashMap<>(); // зберігає порядок
    for (char c : s.toCharArray()) count.merge(c, 1, Integer::sum);
    for (var e : count.entrySet())
        if (e.getValue() == 1) return e.getKey();
    return null;
}
```

**Сказати:** "`LinkedHashMap` зберігає порядок вставки. Перший прохід рахує символи, другий — повертає перший із кількістю 1. O(n)."

---

## 4. Шпаргалка по складності

| Структура / дія | Час |
|---|---|
| HashMap get/put | O(1) середнє |
| Сортування (`Arrays.sort`) | O(n log n) |
| Прохід по масиву | O(n) |
| Вкладені цикли | O(n²) |
| BFS/DFS на графі | O(V + E) |
| BFS/DFS на сітці | O(rows × cols) |
| Stack/Queue push/pop | O(1) |

**Правило:** якщо бачиш "знайди пару / порахуй / згрупуй" → думай **hash map** (O(n) замість O(n²)).

---

## 5. Типові помилки в Java

- **Цілочисельне переповнення:** для грошей і великих сум — `long`, не `int`.
- **`double` для грошей:** ніколи. Тільки цілі (центи) або `BigDecimal`.
- **`==` для рядків:** використовуй `.equals()`. (`==` порівнює посилання.)
- **`Integer` autoboxing у sort:** `b.getValue() - a.getValue()` ок для малих int, але для безпеки — `Integer.compare()`.
- **`split` без trim:** дані з пробілами → `.trim()` після split.
- **`NullPointerException`:** перевіряй null/порожній вхід **першим рядком**.
- **Модифікація колекції під час ітерації:** збереже окремий список або використовуй iterator.

---

## 6. Fidelity — проєкт і питання

### Контекст
**Fidelity Investments** — один із найбільших управителів активів (пенсії 401k, фонди, брокеридж). Дані = гроші клієнтів → максимальні вимоги до точності й регуляторики.

**Проєкт (зі слів рекрутера):** **cloud migration старої COBOL-системи** + **data reconciliation**. Тобто легасі на мейнфреймі переносять у хмару (імовірно Java/Spring мікросервіси), а reconciliation звіряє, що дані після міграції **точно** збігаються зі старими.

**Чому такі проєкти існують:** мейнфрейми дорогі, COBOL-розробників майже нема, бізнес хоче швидше й масштабованіше. Але міграція ризикована — не можна втратити жоден запис.

### Формат інтерв'ю
1. **Розмова з директором** — огляд проєкту, культура, fit. Слухай, постав 1-2 розумні питання.
2. **Технічне з Fidelity** — high-level дизайн + Java/Spring + хмара + розуміння міграції.

### Питання з відповідями (концепт + готова EN-фраза)

**Q: What is a cloud migration? What strategies exist?**
"6 R's", головні: **Rehost** (lift-and-shift), **Replatform** (дрібні зміни, managed-БД), **Refactor** (переписати під хмару).
> *"Cloud migration means moving a system to the cloud. Rehost is lift-and-shift — you move it as it is. Replatform makes small changes, like a managed database. Refactor means you rewrite the system, for example COBOL to Java microservices. For an old COBOL system, refactor gives the most value but the most risk."*

**Q: How do you migrate without breaking the live system?**
Strangler fig pattern — переносиш по шматочку, старе й нове працюють паралельно.
> *"I would use the strangler fig pattern. Instead of rewriting everything at once, you build the new system around the old one and move one feature at a time. Old and new run together until the old system is fully replaced. This lowers the risk."*

**Q: What is data reconciliation and why is it critical here?**
Звірка джерела й цілі: кількість записів, суми, контрольні суми.
> *"Data reconciliation means checking that the data in the new system matches the old one exactly. I compare record counts, totals, and checksums between source and target. In finance even one wrong balance is a serious problem, so I automate these checks and run them after every migration batch."*

**Q: How do you make sure no data is lost during migration?**
Батчі з контрольними точками, ідемпотентність, dry-run, rollback-план.
> *"I move data in batches with checkpoints, so I can restart safely if something fails. Each step is idempotent — running it twice gives the same result. After each batch I reconcile the data. I also do a dry run first and keep a rollback plan."*

**Q: How would the new Java system talk to the old COBOL system during migration?**
Anti-corruption layer — адаптер, що перекладає старі формати в чисту модель нового сервісу.
> *"I would put an adapter between them — an anti-corruption layer. It translates the old mainframe formats into the clean model of the new service, so legacy details stay out of the new code. They can talk through a REST facade or a message queue."*

**Q: How do you handle correctness with financial data?**
Гроші в цілих (центи), ідемпотентність, транзакційність, аудит-лог, reconciliation.
> *"I store money as integers, in cents, to avoid rounding errors. I make operations idempotent and transactional, and I keep an audit log. I recently built a Spring Batch job that processes transactions exactly this way."*

**Q (behavioural): Why this kind of work?**
> *"I like systems where correctness really matters. Financial data must be exact and reliable. Modernising a legacy system is a hard but valuable problem, and I enjoy that challenge."*

### Питання директору (показує зрілість)
- At what stage is the migration right now?
- How well is the legacy COBOL code documented?
- What part is already in the cloud?
- How is the Zinkworks team organised inside the Fidelity project?

---

## 7. Фінальний чеклист

**Перед Karat (Citi):**
- [ ] Прорешати всі 11 задач вище **уголос**, із таймером (~20 хв на задачу).
- [ ] Прогнати ще 20-30 medium на LeetCode: hashmap, two pointers, sliding window, stack, intervals, BFS/DFS.
- [ ] Підготувати 2-3 досвідні історії (складний баг, конкурентність, оптимізація).
- [ ] Тренувати **проговорювання англійською** — не тільки код.

**Перед Fidelity:**
- [ ] Вивчити концепти: 6 R's, strangler fig, anti-corruption layer, reconciliation.
- [ ] Підняти власний Spring Batch проєкт локально — щоб говорити з досвіду.
- [ ] Підготувати питання директору.

**Спільне:**
- [ ] Узгодити історію досвіду — без суперечностей із CV.
- [ ] Чесно: якщо чогось не знаєш — скажи, як шукав би відповідь.
- [ ] Говорити повільно, короткими реченнями. Думати вголос — це плюс.

---

*Усі Java-рішення в цьому файлі перевірені компіляцією та запуском.*
