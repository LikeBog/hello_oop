package edu.lewis.fitness_center;

import java.util.List;

public class Main {

    static DataStore ds = new DataStore();

    public static void addMember() {
        System.out.println("\n=== Add New Member ===");
        String name = Console.readNonempty("Name: ");
        System.out.println("Membership types: student, faculty, community");
        String membershipType = Console.readLine("Membership type: ").trim().toLowerCase();
        if (!membershipType.equals("student") && !membershipType.equals("faculty") && !membershipType.equals("community")) {
            System.out.println("Unknown membership type. Defaulting to 'community'.");
            membershipType = "community";
        }
        Member member = new Member(ds.nextMemberId(), name, membershipType);
        ds.members.add(member);
        System.out.println("Member added with id " + member.getId());
    }

    public static void listMembers(boolean showDetails) {
        System.out.println("\n=== Members ===");
        if (ds.members.isEmpty()) { System.out.println("No members found."); return; }
        for (Member m : ds.members) System.out.println(showDetails ? m.toDetailedString() : m.toShortString());
        System.out.println();
    }

    public static void deactivateMember() {
        System.out.println("\n=== Deactivate Member ===");
        listMembers(false);
        int mid = Console.readInt("Enter member id to deactivate: ", null, null);
        Member member = ds.findMemberById(mid);
        if (member == null) { System.out.println("Member not found."); return; }
        if (!member.isActive()) { System.out.println("Member is already inactive."); return; }
        member.setActive(false);
        System.out.println("Member " + member.getName() + " is now inactive.");
    }

    public static void addChargeToMember() {
        System.out.println("\n=== Add Charge to Member ===");
        listMembers(false);
        int mid = Console.readInt("Enter member id to charge: ", null, null);
        Member member = ds.findMemberById(mid);
        if (member == null) { System.out.println("Member not found."); return; }
        String line = Console.readLine("Charge amount: $");
        double amount;
        try {
            amount = Double.parseDouble(line.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        member.setBalance(member.getBalance() + amount);
        System.out.printf("Added $%.2f to %s's balance.%n", amount, member.getName());
    }

    public static void applyPaymentFromMember() {
        System.out.println("\n=== Record Payment ===");
        listMembers(false);
        int mid = Console.readInt("Enter member id: ", null, null);
        Member member = ds.findMemberById(mid);
        if (member == null) { System.out.println("Member not found."); return; }
        String line = Console.readLine("Payment amount: $");
        double amount;
        try {
            amount = Double.parseDouble(line.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        member.setBalance(member.getBalance() - amount);
        System.out.printf("Recorded payment of $%.2f from %s.%n", amount, member.getName());
    }

    public static void createClass() {
        System.out.println("\n=== Create Fitness Class ===");
        String name = Console.readNonempty("Class name: ");
        String difficulty = Console.readLine("Difficulty (beginner/intermediate/advanced): ").trim().toLowerCase();
        if (!difficulty.equals("beginner") && !difficulty.equals("intermediate") && !difficulty.equals("advanced")) difficulty = "beginner";
        int capacity = Console.readInt("Capacity: ", 1, null);
        FitnessClass fc = new FitnessClass(ds.nextClassId(), name, difficulty, capacity);
        ds.fitnessClasses.add(fc);
        System.out.println("Fitness class created with id " + fc.getId());
    }

    public static void listClasses(boolean showEnrollment) {
        System.out.println("\n=== Fitness Classes ===");
        if (ds.fitnessClasses.isEmpty()) { System.out.println("No classes found."); return; }
        for (FitnessClass c : ds.fitnessClasses) System.out.println(showEnrollment ? c.toDetailedString() : c.toShortString());
        System.out.println();
    }

    public static void enrollMemberInClass() {
        System.out.println("\n=== Enroll Member in Class ===");
        listMembers(false);
        int mid = Console.readInt("Enter member id: ", null, null);
        Member member = ds.findMemberById(mid);
        if (member == null) { System.out.println("Member not found."); return; }
        if (!member.isActive()) { System.out.println("Cannot enroll an inactive member."); return; }
        listClasses(false);
        int cid = Console.readInt("Enter class id: ", null, null);
        FitnessClass fclass = ds.findClassById(cid);
        if (fclass == null) { System.out.println("Class not found."); return; }
        if (fclass.isFull()) { System.out.println("Class is full."); return; }
        if (fclass.isEnrolled(mid)) { System.out.println("Member is already enrolled in this class."); return; }
        fclass.enroll(mid);
        System.out.println("Enrolled " + member.getName() + " in " + fclass.getName() + ".");

        double baseFee = 10.0;
        String membershipType = member.getMembershipType();
        double discountedFee = switch (membershipType) {
            case "student" -> baseFee * 0.5;
            case "faculty" -> baseFee * 0.75;
            default -> baseFee;
        };

        member.setBalance(member.getBalance() + discountedFee);
        System.out.printf("Charged $%.2f for class (base $%.2f, type: %s).%n", discountedFee, baseFee, membershipType);
    }

    public static void listClassRoster() {
        System.out.println("\n=== Class Roster ===");
        listClasses(false);
        int cid = Console.readInt("Enter class id: ", null, null);
        FitnessClass fclass = ds.findClassById(cid);
        if (fclass == null) { System.out.println("Class not found."); return; }
        System.out.println("\nRoster for " + fclass.getName() + ":");
        List<Integer> enrolledIds = fclass.getEnrolledIds();
        if (enrolledIds.isEmpty()) { System.out.println("No members enrolled."); return; }
        for (int mid : enrolledIds) { Member m = ds.findMemberById(mid); if (m != null) System.out.println("- " + m.getName() + " (" + m.getMembershipType() + ")"); }
    }

    public static void addTrainer() {
        System.out.println("\n=== Add Trainer ===");
        String name = Console.readNonempty("Trainer name: ");
        String specialty = Console.readNonempty("Specialty (e.g., yoga, strength, cardio): ");
        Trainer t = new Trainer(ds.nextTrainerId(), name, specialty);
        System.out.println("Enter trainer availability (e.g., Mon 9-11). Leave blank to stop.");
        while (true) { String slot = Console.readLine("Availability: ").trim(); if (slot.isEmpty()) break; t.getSchedule().add(slot); }
        ds.trainers.add(t);
        System.out.println("Trainer added with id " + t.getId());
    }

    public static void listTrainers(boolean showSchedule) {
        System.out.println("\n=== Trainers ===");
        if (ds.trainers.isEmpty()) { System.out.println("No trainers found."); return; }
        for (Trainer t : ds.trainers) {
            System.out.println(showSchedule ? t.toDetailedString() : t.toShortString());
        }
        System.out.println();
    }

    public static void updateTrainerSchedule() {
        System.out.println("\n=== Update Trainer Schedule ===");
        listTrainers(false);
        int tid = Console.readInt("Enter trainer id: ", null, null);
        Trainer trainer = ds.findTrainerById(tid);
        if (trainer == null) { System.out.println("Trainer not found."); return; }
        System.out.println("Current schedule:"); for (String s : trainer.getSchedule()) System.out.println("- " + s);
        System.out.println("1. Replace schedule"); System.out.println("2. Add to schedule"); int choice = Console.readInt("Choice: ", 1, 2);
        if (choice == 1) { trainer.getSchedule().clear(); System.out.println("Enter new availability (blank to finish):"); while (true) { String slot = Console.readLine("Availability: ").trim(); if (slot.isEmpty()) break; trainer.getSchedule().add(slot); } }
        else { System.out.println("Enter additional availability (blank to finish):"); while (true) { String slot = Console.readLine("Availability: ").trim(); if (slot.isEmpty()) break; trainer.getSchedule().add(slot); } }
        System.out.println("Updated schedule:"); for (String s : trainer.getSchedule()) System.out.println("- " + s);
    }

    public static void showSummaryReport() {
        System.out.println("\n=== Summary Report ===");
        int totalMembers = ds.members.size(); int activeMembers = 0; double totalBalance = 0.0;
        for (Member m : ds.members) { if (m.isActive()) activeMembers++; totalBalance += m.getBalance(); }
        System.out.println("Total members: " + totalMembers);
        System.out.println("Active members: " + activeMembers);
        System.out.printf("Total outstanding balance: $%.2f%n", totalBalance);
        System.out.println("\nClasses:"); for (FitnessClass c : ds.fitnessClasses) { System.out.printf("- %s (%s): %d/%d enrolled%n", c.getName(), c.getDifficulty(), c.getEnrolledIds().size(), c.getCapacity()); }
        System.out.println("\nTrainers:"); for (Trainer t : ds.trainers) { System.out.printf("- %s (%s)%n", t.getName(), t.getSpecialty()); }
        System.out.println();
    }

    public static void memberMenu() {
        while (true) {
            System.out.println("\n=== Member Menu ===");
            System.out.println("1. Add member"); System.out.println("2. List members"); System.out.println("3. Deactivate member");
            System.out.println("4. Add charge to member"); System.out.println("5. Record payment from member"); System.out.println("6. Back to main menu");
            int choice = Console.readInt("Choice: ", 1, 6);
            switch (choice) {
                case 1 -> addMember();
                case 2 -> listMembers(true);
                case 3 -> deactivateMember();
                case 4 -> addChargeToMember();
                case 5 -> applyPaymentFromMember();
                case 6 -> { return; }
            }
            Console.pause();
        }
    }

    public static void classesMenu() {
        while (true) {
            System.out.println("\n=== Classes Menu ===");
            System.out.println("1. Create fitness class"); System.out.println("2. List fitness classes"); System.out.println("3. Enroll member in class");
            System.out.println("4. Show class roster"); System.out.println("5. Back to main menu");
            int choice = Console.readInt("Choice: ", 1, 5);
            switch (choice) {
                case 1 -> createClass();
                case 2 -> listClasses(true);
                case 3 -> enrollMemberInClass();
                case 4 -> listClassRoster();
                case 5 -> { return; }
            }
            Console.pause();
        }
    }

    public static void trainerMenu() {
        while (true) {
            System.out.println("\n=== Trainer Menu ===");
            System.out.println("1. Add trainer"); System.out.println("2. List trainers"); System.out.println("3. Update trainer schedule"); System.out.println("4. Back to main menu");
            int choice = Console.readInt("Choice: ", 1, 4);
            switch (choice) {
                case 1 -> addTrainer();
                case 2 -> listTrainers(true);
                case 3 -> updateTrainerSchedule();
                case 4 -> { return; }
            }
            Console.pause();
        }
    }

    public static void mainMenu() {
        while (true) {
            System.out.println("\n=== Campus Fitness Center Management ===");
            System.out.println("1. Manage members"); System.out.println("2. Manage classes"); System.out.println("3. Manage trainers"); System.out.println("4. Show summary report"); System.out.println("5. Exit");
            int choice = Console.readInt("Choice: ", 1, 5);
            switch (choice) {
                case 1 -> memberMenu();
                case 2 -> classesMenu();
                case 3 -> trainerMenu();
                case 4 -> {
                    showSummaryReport();
                    Console.pause();
                }
                case 5 -> {
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
            }
        }
    }

    public static void main(String[] args) { mainMenu(); }

}
