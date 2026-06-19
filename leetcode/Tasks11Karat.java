import java.util.HashMap;
import java.util.Map;

public class Tasks11Karat {
    String topStudentOf = topStudent(new String[]{
        "Alice,Math,90",
        "Bob,Math,80",
        "Alice,Science,95",
        "Bob,Science,85"
    });
    
}

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
    String top = null; 
    double best = Double.NEGATIVE_INFINITY;

    for (var e : averages(records).entrySet())
        if (e.getValue() > best) { 
            best = e.getValue(); top = e.getKey(); 
        }
    
    return top;
}