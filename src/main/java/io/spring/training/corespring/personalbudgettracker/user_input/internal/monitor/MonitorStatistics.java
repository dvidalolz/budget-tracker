package io.spring.training.corespring.personalbudgettracker.user_input.internal.monitor;

public interface MonitorStatistics {

	String getName();

	long getLastCallTime();

	long getCallCount();

	long getAverageCallTime();

	long getTotalCallTime();

	long getMinimumCallTime();

	long getMaximumCallTime();

}
