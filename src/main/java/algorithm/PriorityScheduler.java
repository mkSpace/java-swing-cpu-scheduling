package algorithm;

import data.Process;
import data.Result;
import data.TaskResult;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PriorityScheduler implements Scheduler {
    @Override
    public Result run(List<Process> processes, int quantum) {
        if (processes.isEmpty()) return null;
        processes.forEach(process -> process.setRemainTime(process.getBurstTime()));
        int size = processes.size();
        int totalCPUTime = processes.stream().mapToInt(Process::getBurstTime).sum() + processes.stream().mapToInt(Process::getArrivalTime).min().getAsInt();
        int[] timeTable = new int[totalCPUTime];
        AtomicInteger currentTime = new AtomicInteger();
        PriorityQueue<Process> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Process::getPriority));
        while (processes.stream().mapToInt(Process::getRemainTime).sum() != 0) {
            processes.forEach(process -> {
                if (currentTime.get() == process.getArrivalTime()) {
                    priorityQueue.add(process);
                }
            });
            Process selectedProcess = priorityQueue.peek();
            if (selectedProcess == null) {
                timeTable[currentTime.getAndIncrement()] = -1;
            } else {
                selectedProcess.setRemainTime(selectedProcess.getRemainTime() - 1);
                timeTable[currentTime.getAndIncrement()] = selectedProcess.getProcessId();
            }
            priorityQueue.removeIf(process -> process.getRemainTime() <= 0);
        }
        List<TaskResult> taskResultList = processes.stream().map(process -> {
            String processId = "P" + process.getProcessId();
            List<Task> tasks = new ArrayList<>();
            int startTime = -1;
            int completeTime = -1;
            for (int i = 0; i < timeTable.length; i++) {
                if (timeTable[i] == process.getProcessId()) {
                    if (startTime == -1) startTime = i;
                    completeTime = i + 1;
                }
            }
            tasks.add(new Task(processId, new SimpleTimePeriod(startTime, completeTime)));
            return new TaskResult(
                    process.getProcessId(),
                    tasks,
                    completeTime - process.getArrivalTime() - process.getBurstTime(),
                    completeTime - process.getArrivalTime()
            );
        }).collect(Collectors.toList());
        return new Result(
                taskResultList,
                taskResultList.stream().map(taskResult -> taskResult.getWaitTime()).reduce(Integer::sum).get() / size,
                taskResultList.stream().map(taskResult -> taskResult.getCompleteTime()).reduce(Integer::sum).get() / size
        );
    }
}
