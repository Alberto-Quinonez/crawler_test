package application.model;

public class Progress {
    private final String completed;
    private final String inProgress;

    public Progress(String completed, String inProgress) {
        this.completed = completed;
        this.inProgress = inProgress;
    }

    public String getCompleted() {
        return completed;
    }

    public String getInProgress() {
        return inProgress;
    }
}
