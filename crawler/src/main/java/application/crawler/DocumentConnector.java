package application.crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class DocumentConnector {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";
    private static final String REFERRER = "http://www.google.com";
    private static final int TIMEOUT = 10 * 1000;

    Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .timeout(TIMEOUT)
                .get();
    }
}
