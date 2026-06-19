import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class UserCasheService {
    private final Map<Long, CacheEntry> cache = new HashMap<>();
    private final UserRepository userRepository;
    private final int maxSize = 1000;

    public UserCasheService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id) {
        CacheEntry entry = cache.get(id);
        if (entry != null && !entry.isExpired()) {
            return entry.getUser();
        }

        User user = ((Object) userRepository.findById(id))
                .orElseThrow(() -> new RuntimeException("User not found"));

        put(id, user);
        return user;
    }

    public void put(Long id, User user) {
        if (cache.size() >= maxSize) {
            Long oldestKey = cache.keySet().iterator().next();
            /*
             * Long oldestKey = cache.entrySet().stream()
             * .min(Comparator.comparingLong(e -> e.getValue().createdAt))
             * .map(Map.Entry::getKey)
             * .orElse(null);
             */
            cache.remove(oldestKey);
        }
        cache.put(id, new CacheEntry(user));
    }

    public void evictAll() {
        cache.clear();
    }

    public void cleanUp() {
        for (Map.Entry<Long, CacheEntry> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
            }
        }
        // cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }

    public static class CacheEntry {

        private User user;
        private long createdAt;
        private static final long TTL = 5 * 60 * 1000;

        boolean isExpired() {
            return System.currentTimeMillis() - createdAt > TTL;
        }

        CacheEntry(User user) {
            this.user = user;
            this.createdAt = System.currentTimeMillis();
        }

        public User getUser() {
            return user;
        }

    }

    public class UserRepository {

        public Object findById(Long id) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'findById'");
        }

    }

    public class User {

    }

}
