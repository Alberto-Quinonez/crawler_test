package application.crawler;


import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@NoArgsConstructor
@Slf4j
public class DocumentConnector {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";
    private static final String REFERRER = "http://www.google.com";
    private static final int TIMEOUT = 10 * 1000;

    Optional<Document> getDocument(String url) {
        try {
            return Optional.of(Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .referrer(REFERRER)
                    .timeout(TIMEOUT)
                    .get());
        } catch (IOException e) {
            log.info(String.format("Something went wrong trying to fetch the contents of the URL: %s", url), e);
            return Optional.empty();
          } catch (IllegalArgumentException e) {
            log.info(String.format("Bad URL: %s", url), e);
            return Optional.empty();
        }
    }
}
