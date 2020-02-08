package application.model;

public class InputPayload {
    private final String url;
    private final int threadCount;

    public InputPayload(String url, int threadCount) {
        this.url = url;
        this.threadCount = threadCount;
    }

    public String getUrl() {
        return url;
    }

    public int getThreadCount() {
        return threadCount;
    }
}
