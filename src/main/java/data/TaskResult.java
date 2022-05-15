package data;

import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;

import java.util.List;

public class TaskResult {
    private final int processId;
    private final int waitTime;
    private final int completeTime;
    private TaskSeries series;

    public TaskResult(int processId, List<Task> taskList, int waitTime, int completeTime) {
        this.processId = processId;
        this.series = new TaskSeries("P" + processId);
        taskList.forEach(task -> {
            series.add(task);
        });
        this.waitTime = waitTime;
        this.completeTime = completeTime;
    }

    public void addTask(Task task) {
        series.add(task);
    }

    public TaskSeries getSeries() {
        return series;
    }

    public void setSeries(TaskSeries series) {
        this.series = series;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getCompleteTime() {
        return completeTime;
    }

    public int getProcessId() {
        return processId;
    }

    @Override
    public String toString() {
        return "TaskResult{" +
                "series=" + series.getTasks() +
                ", processId=" + processId +
                ", waitTime=" + waitTime +
                ", completeTime=" + completeTime +
                '}';
    }
}
