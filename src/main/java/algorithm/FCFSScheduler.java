package algorithm;

import data.Process;
import data.Result;
import data.TaskResult;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FCFSScheduler implements Scheduler {
    @Override
    public Result run(List<Process> processes, int quantum) {
        int size = processes.size();
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        AtomicInteger currentTime = new AtomicInteger();
        List<TaskResult> taskResultList = processes.stream().map(process -> {
                    String processId = "P" + process.getProcessId();
                    List<Task> tasks = new ArrayList<>();
                    tasks.add(new Task(processId, new SimpleTimePeriod(currentTime.get(), currentTime.get() + process.getBurstTime())));
                    return new TaskResult(
                            process.getProcessId(),
                            tasks,
                            currentTime.get(),
                            currentTime.addAndGet(process.getBurstTime())
                    );
                }
        ).collect(Collectors.toList());
        return new Result(
                taskResultList,
                taskResultList.stream().map(taskResult -> taskResult.getWaitTime()).reduce(Integer::sum).get() / size,
                taskResultList.stream().map(taskResult -> taskResult.getCompleteTime()).reduce(Integer::sum).get() / size
        );
    }
}
