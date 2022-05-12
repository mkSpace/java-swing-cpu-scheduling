package ui;

import algorithm.Scheduler;
import algorithm.SchedulerFactory;
import data.Algorithm;
import data.Process;
import data.Result;
import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.PublishProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainViewModel {

    private final BehaviorProcessor<ArrayList<Process>> _processes = BehaviorProcessor.createDefault(new ArrayList<>());
    private final Flowable<ArrayList<Object[]>> tableModels = _processes.map(processes ->
            new ArrayList<>(processes.stream().map(this::convertProcessToObjectArray).collect(Collectors.toList()))
    );
    private final PublishProcessor<String> _errorMessage = PublishProcessor.create();
    private final Flowable<String> errorMessage = _errorMessage;
    private final PublishProcessor<String> _alertMessage = PublishProcessor.create();
    private final Flowable<String> alertMessage = _alertMessage;
    private final BehaviorProcessor<Integer> _quantum = BehaviorProcessor.createDefault(10);
    private final Flowable<Integer> quantum = _quantum;
    private final BehaviorProcessor<Algorithm> _selectedAlgorithm = BehaviorProcessor.createDefault(Algorithm.FCFS);
    private final Flowable<Algorithm> selectedAlgorithm = _selectedAlgorithm;
    private final BehaviorProcessor<Result> _latestResult = BehaviorProcessor.create();
    private final Flowable<Result> latestResult = _latestResult;
    private int autoIncrementalId = 1;

    public void run() {
        Algorithm selectedAlgorithm = _selectedAlgorithm.getValue();
        Scheduler scheduler = SchedulerFactory.createScheduler(selectedAlgorithm);
        _latestResult.offer(scheduler.run(_processes.getValue(), _quantum.getValue()));
    }

    public void addProcess(int arrivalTime, int burstTime, int priority, Color color) {
        ArrayList<Process> currentProcesses = _processes.getValue();
        currentProcesses.add(new Process(autoIncrementalId++, arrivalTime, burstTime, priority, color));
        _processes.offer(currentProcesses);
    }

    public Process findByProcessId(int processId) {
        ArrayList<Process> currentProcesses = _processes.getValue();
        Objects.requireNonNull(currentProcesses);
        if (currentProcesses.stream().noneMatch(process -> process.getProcessId() == processId)) {
            _errorMessage.offer("해당하는 processId를 찾을 수 없습니다. 다시 시도해주세요.");
            return null;
        }
        return currentProcesses.stream().filter(process -> process.getProcessId() == processId).findFirst().get();
    }

    public void editByProcessId(int processId, int arrivalTime, int burstTime, int priority, Color color) {
        ArrayList<Process> currentProcesses = _processes.getValue();
        Objects.requireNonNull(currentProcesses);
        if (currentProcesses.stream().noneMatch(process -> process.getProcessId() == processId)) {
            _errorMessage.offer("해당하는 processId를 찾을 수 없습니다. 다시 시도해주세요.");
            return;
        }
        currentProcesses
                .removeIf(process -> process.getProcessId() == processId);
        currentProcesses.add(new Process(processId, arrivalTime, burstTime, priority, color));
        _processes.offer(currentProcesses);
    }

    public void deleteByProcessId(int processId) {
        ArrayList<Process> currentProcesses = _processes.getValue();
        Objects.requireNonNull(currentProcesses);
        if (currentProcesses.stream().noneMatch(process -> process.getProcessId() == processId)) {
            _errorMessage.offer("해당하는 processId를 찾을 수 없습니다. 다시 시도해주세요.");
            return;
        }
        currentProcesses
                .removeIf(process -> process.getProcessId() == processId);
        _processes.offer(currentProcesses);
    }

    public Flowable<String> getAlertMessage() {
        return alertMessage;
    }

    public Flowable<String> getErrorMessage() {
        return errorMessage;
    }

    public Flowable<ArrayList<Object[]>> getTableModels() {
        return tableModels;
    }

    public void setAlgorithm(Algorithm algorithm) {
        _selectedAlgorithm.offer(algorithm);
    }

    public Flowable<Algorithm> getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public Integer getQuantum() {
        return _quantum.getValue();
    }

    public void setQuantum(int quantum) {
        _quantum.offer(quantum);
    }

    public Flowable<Result> getLatestResult() {
        return latestResult;
    }

    private Object[] convertProcessToObjectArray(Process process) {
        return new Object[]{
                String.valueOf(process.getProcessId()),
                String.valueOf(process.getBurstTime()),
                String.valueOf(process.getArrivalTime()),
                String.valueOf(process.getPriority()),
                process.getColor()
        };
    }
}
