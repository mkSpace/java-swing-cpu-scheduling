package algorithm;

import data.Algorithm;

public class SchedulerFactory {

    public static Scheduler createScheduler(Algorithm algorithm) {
        if(algorithm == Algorithm.FCFS) {
            return new FCFSScheduler();
        } else if(algorithm == Algorithm.SJF){
            return new SJFScheduler();
        } else if(algorithm == Algorithm.PRIORITY){
            return new PriorityScheduler();
        } else {
            return new RRScheduler();
        }
    }

}
