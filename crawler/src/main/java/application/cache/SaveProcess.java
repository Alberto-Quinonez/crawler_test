package application.cache;

import application.job.Job;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SaveProcess {
    public ResultCache resultCache;

    public Job save (Job job){
        resultCache.add(job.getId(), job);
        return job;
    }

    public static class ResultCache {
        private final ConcurrentHashMap<UUID, Job> map = new ConcurrentHashMap<>();

        public ResultCache(){}

        public void add(UUID key, Job job) {
            map.put(key, job);
        }

        public Job get(UUID key) {
            return map.get(key);
        }
    }
}
