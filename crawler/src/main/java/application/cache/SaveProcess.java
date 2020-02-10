package application.cache;

import application.job.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SaveProcess {
    private ResultCache resultCache;

    public void save (Job job){
        resultCache.add(job.getId(), job);
    }

    public static class ResultCache {
        private final ConcurrentHashMap<UUID, Job> map = new ConcurrentHashMap<>();

        public void add(UUID key, Job job) {
            map.put(key, job);
        }

        public Job get(UUID key) {
            return map.get(key);
        }

        public boolean has(UUID key) {
            return map.containsKey(key);
        }
    }
}
