package application.cache;

import application.job.Job;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Decided to use in memory storage. For persistance and scalability, we could replace with NOSQL database.
 */
@Component
public class JobsDao {
    private final ConcurrentHashMap<UUID, Job> jobMap = new ConcurrentHashMap<>();

    public void save(Job job) {
        jobMap.put(job.getId(), job);
    }

    public Optional<Job> find(UUID uuid) {
        return Optional.ofNullable(jobMap.get(uuid));
    }
}
