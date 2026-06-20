import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserCacheService {
    private final Map<Long, CacheEntry> cache = new HashMap<>();
    private final UserRepository userRepository;
    private final int maxSize = 1000;

    public UserCacheService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id) {
        CacheEntry entry = cache.get(id);
        if (entry != null && !entry.isExpired()) {
            return entry.getUser();
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        put(id, user);
        return user;
    }

    public void put(Long id, User user) {
        if (cache.size() >= maxSize) {
            Long oldestKey = cache.entrySet().stream()
                    .min(Comparator.comparingLong(e -> e.getValue().createdAt))
                    .map(Map.Entry::getKey)
                    .orElse(null);
            if (oldestKey != null) {
                cache.remove(oldestKey);
            }
        }
        cache.put(id, new CacheEntry(user));
    }

    public void evictAll() {
        cache.clear();
    }

    public void cleanUp() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
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
        public Optional<User> findById(Long id) {
            // TODO: implement actual database lookup
            return Optional.empty();
        }
    }

    public class User {
        // User fields and methods
    }
}