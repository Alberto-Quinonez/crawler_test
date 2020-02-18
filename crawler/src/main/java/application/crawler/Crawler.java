package application.crawler;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toSet;

@Slf4j
@RequiredArgsConstructor
class Crawler {
    private final DocumentConnector documentConnector;

    CompletableFuture<Void> crawl(
            final String startingUrl,
            final int depth,
            ConcurrentHashMap<String, Boolean> visited,
            ConcurrentHashMap<String, Boolean> results,
            ThreadPoolExecutor executor) {

        if (depth <= 0) {
            return completedFuture();
        }

        final CompletableFuture<Void> allDoneFuture = supplyAsync(getContent(startingUrl), executor)
                .thenApply(getURLs(visited, results))
                .thenApply(doForEach(depth, visited, results, executor))
                .thenApply(futures -> futures.toArray(CompletableFuture[]::new))
                .thenAccept(CompletableFuture::allOf);

        allDoneFuture.join();

        return allDoneFuture;
    }

    private CompletableFuture<Void> completedFuture() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        future.complete(null);
        return future;
    }

    private Supplier<Optional<Document>> getContent(final String url) throws IllegalArgumentException{
        return () -> documentConnector.getDocument(url);
    }

    private Function<Optional<Document>, Set<String>> getURLs(
            ConcurrentHashMap<String, Boolean> visited,
            ConcurrentHashMap<String, Boolean> results) {
        return doc -> {
            if(!shouldVisit(doc, visited)){
                return Sets.newHashSet();
            }
            String url = doc.get().baseUri();

            visited.put(url, true);

            doc.get().select("img").forEach(ie -> {
                log.info(String.format("adding this img: %s", ie.attr("abs:src")));
                results.put(ie.attr("abs:src"), true);
            });

            log.info("Getting URLs for document: {}.", url);
            return doc.get().select("a[href]")
                    .stream()
                    .map(link -> link.attr("abs:href"))
                    .collect(toSet());
        };
    }

    private Function<Set<String>, Stream<CompletableFuture<Void>>> doForEach(
            final int depth,
            ConcurrentHashMap<String, Boolean> visited,
            ConcurrentHashMap<String, Boolean> results,
            ThreadPoolExecutor executor) {
        return urls -> urls.stream().map(url -> crawl(url, depth - 1, visited, results, executor));
    }

    private boolean shouldVisit(Optional<Document> document, ConcurrentHashMap<String, Boolean> visited) {
        if(!document.isPresent()){
            return false;
        }
        if(document.get().baseUri().isEmpty()){
           return false;
        }
        if(visited.containsKey(document.get().baseUri())){
            return false;
        }
        return true;
    }
}
