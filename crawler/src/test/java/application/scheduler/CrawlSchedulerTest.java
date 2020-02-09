package application.scheduler;


import application.crawler.Crawler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ThreadFactory;

@DisplayName("CrawlScheduler Test")
class CrawlSchedulerTest {
    private CrawlScheduler crawlScheduler;

    @Mock
    private Crawler crawler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .setDaemon(true)
                .build();
    }

//    @Test
//    void givenStringCrawl() throws InterruptedException {
//        List<String> paths = Lists.newArrayList("1", "2");
//        ConcurrentHashMap<Integer, List<String>> map = new ConcurrentHashMap<>();
//        ExecutorService pool = newFixedThreadPool(3, threadFactory("Scheduler-%d"));
//
//        List<Future<List<String>>> futures = Lists.newArrayList();
//        paths.forEach(x -> futures.add(crawl(x, pool)));
//
//        futures.forEach(f -> {
//            try {
//                map.put(f.hashCode(), f.get()) ;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
//        map.forEach((k,v) -> System.out.println("k: " + k + " v: " + v));
//        pool.shutdown();
//    }
//
//    Future<List<String>> crawl(CrawlJob job, ExecutorService executorService) {
//        CompletableFuture<List<String>> completableFuture = new CompletableFuture<>();
//        executorService.submit(() -> {
//            crawler.getImgs(job);
//            completableFuture.complete(job.getResults());
//            return null;
//        });
//
//        return completableFuture;
//    }
//
//    Observable<List<String>> deferCrawl(String url) {
//        return Observable.defer(() -> Observable.just(crawler.getImgs(url)));
//    }


//    @Override
//    public void onNext(ArrayList<Phrase> phrases) {
//        ArrayList<Integer> phraseIDs = new ArrayList<>();
//        Observable
//                .from(phrases)
//                .collect(phraseIDs, new Action2<ArrayList<Integer>, Phrase>() {
//                    @Override
//                    public void call(ArrayList<Integer> integers, Phrase phrase) {
//                        integers.add(phrase.getId());
//                    }
//                })
//                .subscribe();
//    }
}
