package algorithm;

import data.Process;
import data.Result;

import java.util.List;

public interface Scheduler {
    Result run(List<Process> processes, int quantum);
}
