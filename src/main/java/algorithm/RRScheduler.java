package algorithm;

import data.Process;
import data.Result;
import data.TaskResult;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class RRScheduler implements Scheduler {
    @Override
    public Result run(List<Process> processes, int quantum) {
        if (processes.isEmpty()) return null;
        processes.forEach(process -> process.setRemainTime(process.getBurstTime()));
        int size = processes.size();
        int index = 0;
        AtomicInteger currentTime = new AtomicInteger();
        List<Process> processList = new ArrayList<>();
        int totalCPUTime = processes.stream().mapToInt(Process::getBurstTime).sum() + processes.stream().mapToInt(Process::getArrivalTime).min().getAsInt();
        int[] timeTable = new int[totalCPUTime];
        while (processes.stream().mapToInt(Process::getRemainTime).sum() > 0) {
            processes.forEach(process -> {
                if (currentTime.get() >= process.getArrivalTime() && !processList.contains(process)) {
                    processList.add(process);
                }
            });
            if (processList.isEmpty()) {
                timeTable[currentTime.getAndIncrement()] = -1;
                continue;
            }
            Process selectedProcess = processList.get(index % processList.size());
            if (selectedProcess == null) {
                timeTable[currentTime.getAndIncrement()] = -1;
                index = 0;
            } else {
                if (selectedProcess.getRemainTime() >= quantum) {
                    for (int i = 0; i < quantum; i++) {
                        timeTable[currentTime.get() + i] = selectedProcess.getProcessId();
                    }
                    selectedProcess.setRemainTime(selectedProcess.getRemainTime() - quantum);
                    currentTime.addAndGet(quantum);
                } else {
                    int remainTime = selectedProcess.getRemainTime();
                    for (int i = 0; i < remainTime; i++) {
                        timeTable[currentTime.get() + i] = selectedProcess.getProcessId();
                    }
                    selectedProcess.setRemainTime(0);
                    currentTime.addAndGet(remainTime);
                }
                index++;
            }
            processList.removeIf(process -> process.getRemainTime() <= 0);
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
