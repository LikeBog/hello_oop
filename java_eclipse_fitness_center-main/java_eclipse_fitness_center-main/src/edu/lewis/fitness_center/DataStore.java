package edu.lewis.fitness_center;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public final List<Member> members = new ArrayList<>();
    public final List<FitnessClass> fitnessClasses = new ArrayList<>();
    public final List<Trainer> trainers = new ArrayList<>();

    private int nextMemberId = 1;
    private int nextClassId = 1;
    private int nextTrainerId = 1;

    public int nextMemberId() { return nextMemberId++; }
    public int nextClassId() { return nextClassId++; }
    public int nextTrainerId() { return nextTrainerId++; }

    public Member findMemberById(int id) {
        for (Member m : members) if (m.getId() == id) return m;
        return null;
    }

    public FitnessClass findClassById(int id) {
        for (FitnessClass c : fitnessClasses) if (c.getId() == id) return c;
        return null;
    }

    public Trainer findTrainerById(int id) {
        for (Trainer t : trainers) if (t.getId() == id) return t;
        return null;
    }
}
