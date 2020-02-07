package application.model;

public class InputPayload {
    private final String url;
    private final int thread;

    public InputPayload(String url, int thread) {
        this.url = url;
        this.thread = thread;
    }

    public String getUrl() {
        return url;
    }

    public int getThread() {
        return thread;
    }
}
