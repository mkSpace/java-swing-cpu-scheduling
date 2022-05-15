package data;

import java.util.List;

public class Result {
    private final double averageWaitTime;
    private final double averageTurnaroundTime;
    private List<TaskResult> result;

    public Result(List<TaskResult> result, double averageWaitTime, double averageTurnaroundTime) {
        this.result = result;
        this.averageWaitTime = averageWaitTime;
        this.averageTurnaroundTime = averageTurnaroundTime;
    }

    public List<TaskResult> getResult() {
        return result;
    }

    public void setResult(List<TaskResult> result) {
        this.result = result;
    }

    public double getAverageWaitTime() {
        return averageWaitTime;
    }

    public double getAverageTurnaroundTime() {
        return averageTurnaroundTime;
    }
}
