package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.List;

public class Trainer extends Entity implements Displayable {
	private final String specialty;
	private final List<String> schedule = new ArrayList<>();

	public Trainer(int id, String name, String specialty) {
		super(id, name);
		this.specialty = specialty;
	}

	public String getSpecialty() { return specialty; }
	public List<String> getSchedule() { return schedule; }

	@Override
	public String toShortString() { return String.format("[%d] %s", id, name); }

	@Override
	public String toDetailedString() {
		String scheduleStr = schedule.isEmpty() ? "No availability" : String.join(", ", schedule);
		return String.format("[%d] %s - %s (Schedule: %s)", id, name, specialty, scheduleStr);
	}
}

