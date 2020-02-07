package application.crawler;

import com.google.common.collect.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class Crawler {
    private static final int MAX_DEPTH = 2;
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; de-de) AppleWebKit/523.10.3 (KHTML, like Gecko) Version/3.0.4 Safari/523.10";

    @Autowired
    public Crawler(){}

    public List<String> getImgs(String url){
        List<String> val = crawl(url, 0, Lists.newArrayList());
        return val;
    }

    private List<String> crawl(String URL, int depth, List<String> results) {

        if((depth < MAX_DEPTH)){
            return results;
        }
        System.out.println(">> Depth: " + depth + " [" + URL + "]");
        try {

            Document document = Jsoup
                    .connect(URL)
                    .userAgent(USER_AGENT)
                    .referrer("http://www.google.com")
                    .timeout(10 * 1000)
                    .get();

            document.select("img").forEach(ie -> {
                System.out.println("adding this img: " + ie.attr("abs:src"));
                results.add(ie.attr("abs:src"));
            });

            Elements linksOnPage = document.select("a[href]");

            depth++;

            for(Element ie : linksOnPage) {
                System.out.println("checking this path: " + ie.attr("abs:href"));
                crawl(ie.attr("abs:href"), depth, results);
            }
            return results;
        } catch (IOException e) {
            System.err.println("For '" + URL + "': " + e.getMessage());
            throw new RuntimeException("durp", e);
        }
    }
}

