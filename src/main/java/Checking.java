public class Checking extends Account {

	public Checking(String id, double apr) {
		super(id, apr, "checking");
	}

	@Override
	public void withdraw(double withdrawAmount) {
		double balance = getBalance();
		boolean withdrawnMoreThanBalance = withdrawAmount > balance;
		if (withdrawnMoreThanBalance) {
			if (balance > 400) {
				setBalance(balance - 400);
			} else {
				setBalance(0);
			}
		} else {
			setBalance(balance - withdrawAmount);
		}
	}

	@Override
	public boolean isValidWithdrawal(double withdrawalAmount) {
		return withdrawalAmount >= 0 && withdrawalAmount <= 400;
	}

	@Override
	public boolean isValidDeposit(double depositAmount) {
		return depositAmount >= 0 && depositAmount <= 1000;
	}

	@Override
	public void passMonth() {
	}
}
