package application.cache;

import org.springframework.stereotype.Component;

@Component
public class CacheSupplier {
    private final SaveProcess.ResultCache resultCache;

    public CacheSupplier(){
        resultCache = new SaveProcess.ResultCache();
    }

    public SaveProcess.ResultCache get(){
        return resultCache;
    }
}
