package edu.lewis.fitness_center;

public class Member extends Entity implements Displayable {
	private final String membershipType;
	private boolean active;
	private double balance;

	public Member(int id, String name, String membershipType) {
		super(id, name);
		this.membershipType = membershipType;
		this.active = true;
		this.balance = 0.0;
	}

	public String getMembershipType() { return membershipType; }
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	public double getBalance() { return balance; }
	public void setBalance(double balance) { this.balance = balance; }

	@Override
	public String toShortString() { return String.format("[%d] %s", id, name); }

	@Override
	public String toDetailedString() {
		String status = active ? "Active" : "Inactive";
		return String.format("[%d] %s (%s, %s) Balance: $%.2f", id, name, membershipType, status, balance);
	}
}

