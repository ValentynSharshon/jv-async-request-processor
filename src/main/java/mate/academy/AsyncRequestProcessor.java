package mate.academy;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class AsyncRequestProcessor {
    private static final String USER_DETAILS = "Details for ";

    private final Executor executor;
    private final Map<String, UserData> cache = new ConcurrentHashMap<>();

    public AsyncRequestProcessor(Executor executor) {
        this.executor = executor;
    }

    public CompletableFuture<UserData> processRequest(String userId) {
        return CompletableFuture.supplyAsync(() -> getUserData(userId), executor);
    }

    private UserData getUserData(String userId) {
        try {
            long timeout = ThreadLocalRandom.current().nextLong(1000);
            TimeUnit.MILLISECONDS.sleep(timeout);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        UserData cachedUserData = cache.get(userId);
        if (cachedUserData != null) {
            return cachedUserData;
        }
        UserData userData = new UserData(userId, USER_DETAILS + userId);
        cache.put(userId, userData);
        return userData;
    }
}
