package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.List;

public class FitnessClass implements Displayable {
	private final int id;
	private final String name;
	private final String difficulty;
	private final int capacity;
	private final List<Integer> enrolledIds = new ArrayList<>();

	public FitnessClass(int id, String name, String difficulty, int capacity) {
		this.id = id;
		this.name = name;
		this.difficulty = difficulty;
		this.capacity = capacity;
	}

	public int getId() { return id; }
	public String getName() { return name; }
	public String getDifficulty() { return difficulty; }
	public int getCapacity() { return capacity; }
	public List<Integer> getEnrolledIds() { return enrolledIds; }

	public boolean isFull() { return enrolledIds.size() >= capacity; }
	public boolean isEnrolled(int memberId) { return enrolledIds.contains(memberId); }
	public void enroll(int memberId) { if (!isEnrolled(memberId) && !isFull()) enrolledIds.add(memberId); }

	@Override
	public String toShortString() { return String.format("[%d] %s", id, name); }

	@Override
	public String toDetailedString() {
		return String.format("[%d] %s (%s, capacity %d, enrolled %d)", id, name, difficulty, capacity, enrolledIds.size());
	}
}

