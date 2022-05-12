package data;

import java.awt.*;
import java.util.Objects;

public class Process {
    private int processId;
    private int arrivalTime;
    private int burstTime;
    private int priority;
    private Color color;
    private int remainTime;

    public Process(int processId, int arrivalTime, int burstTime, int priority, Color color) {
        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainTime = burstTime;
        this.priority = priority;
        this.color = color;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int remainTime) {
        this.remainTime = remainTime;
    }

    @Override
    public String toString() {
        return "Process{" +
                "processId=" + processId +
                ", arrivalTime=" + arrivalTime +
                ", burstTime=" + burstTime +
                ", priority=" + priority +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Process process = (Process) o;
        return Objects.equals(processId, process.processId) && Objects.equals(arrivalTime, process.arrivalTime) && Objects.equals(burstTime, process.burstTime) && Objects.equals(priority, process.priority) && Objects.equals(color, process.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, arrivalTime, burstTime, priority, color);
    }
}
