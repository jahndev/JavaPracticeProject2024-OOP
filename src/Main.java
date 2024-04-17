import java.util.*;
import java.util.stream.Collectors;

public class Main {

    Map<Long, Long> count4(Map<String, UserStats>... visits) {
        return Arrays.stream(visits)
                .filter(visit -> visit != null && !visit.isEmpty())
                .map(visit -> {
                    try {
                        Long key = Long.parseLong(visit.keySet().iterator().next());
                        Long visitCount = visit.values().iterator().next().getVisitCount().orElse(0L);
                        return Map.entry(key, visitCount);
                    } catch (Exception ignored) {
                        return null;
                    }
                })
                .filter(entry -> entry != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum));
    }

    Map<Long, Long> count3(Map<String, UserStats>... visits) {
        Map<Long, Long> map = new HashMap<>();

        for (Map<String, UserStats> visit : visits) {
            if (visit != null && !visit.isEmpty()) {
                try {
                    Long key = Long.parseLong(visit.keySet().iterator().next());
                    Long visitCount = visit.values().iterator().next().getVisitCount().orElse(0L);
                    map.put(key, map.getOrDefault(key, 0L) + visitCount);
                } catch (Exception ignored) {}
            }
        }

        return map;
    }

    /**
     *
     * @param visits
     * @return
     */
    public static Map<Long, Long> count2(Map<String, UserStats>... visits) {
            Map<Long, Long> map = new HashMap<>();

        if (visits != null) {
            for (Map<String, UserStats> visit : visits) {
                if (visit != null) {
                    for (Map.Entry<String, UserStats> entry : visit.entrySet()) {
                        String keyString = entry.getKey();
                        UserStats userStats = entry.getValue();

                        if (keyString != null && userStats != null && keyString.isEmpty()) {
                            try {
                                Long key = Long.parseLong(keyString);
                                Long visitCount = userStats.getVisitCount().orElse(0L);

                                map.merge(key, visitCount, Long::sum);
                            } catch (NumberFormatException ignored) {
                                // Handle if the key cannot be parsed as Long
                            }
                        }
                    }
                }
            }
        }

            return map;
    }

    public static Map<Long, Long> count5() {
        Map<Long, Long> map = Collections.synchronizedMap(new HashMap<>());
        return map;
    }
    public Map<Long, Long> count(Map<String, UserStats>... visits) {
        return Arrays.stream(visits)
                .filter( v -> v == null || v.isEmpty() || v.values().stream().filter(Objects::nonNull).findAny().isEmpty())
                .flatMap(map -> map.entrySet().stream())
                .filter(entry -> isConvertibleToLong(entry.getKey()))
                .collect(Collectors.groupingBy(
                        entry -> Long.parseLong(entry.getKey()),
                        Collectors.summingLong(entry -> entry.getValue().getVisitCount().orElse(0L))
                ));
    }

    private boolean isConvertibleToLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        Main calculator = new Main();

        // Example visit maps
        Map<String, UserStats> visitMap1 = new HashMap<>();
        visitMap1.put("1", new UserStats(Optional.of(3L)));
        visitMap1.put("2", new UserStats(Optional.of(5L)));
        visitMap1.put("invalid", new UserStats(Optional.of(2L)));

        Map<String, UserStats> visitMap2 = new HashMap<>();
        visitMap2.put("1", new UserStats(Optional.of(2L)));
        visitMap2.put("3", new UserStats(Optional.of(4L)));

        Map<String, UserStats> visitMap3 = new HashMap<>();

        Map<String, UserStats> visitMap4 = new HashMap<>();
        visitMap4.put("4", null);
        visitMap4.put("3", new UserStats(Optional.empty()));
        visitMap4.put("1", new UserStats(Optional.of(1L)));

        Map<Long, Long> result = calculator.count2(visitMap1, visitMap2, visitMap3, visitMap4);
        System.out.println(result); // Output: {1=5, 2=5, 3=4}

        Map<String, UserStats>[] nullVisits = null;
        Map<Long, Long> result3 = count2(nullVisits);
// Expected Output: {}

    }
}
