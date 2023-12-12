public class CD extends Account {

	int monthsSinceCreation;

	public CD(String id, double apr, double balance) {
		super(id, apr, "cd");
		deposit(balance);
		monthsSinceCreation = 0;
	}

	@Override
	public void withdraw(double withdrawAmount) {
		setBalance(0);
	}

	@Override
	public boolean isValidDeposit(double depositAmount) {
		return false;
	}

	@Override
	public boolean isValidWithdrawal(double withdrawalAmount) {
		double balance = getBalance();
		return withdrawalAmount >= balance && yearPassed();
	}

	private boolean yearPassed() {
		return monthsSinceCreation >= 12;
	}

	@Override
	public void passMonth() {
		monthsSinceCreation++;
	}
}
