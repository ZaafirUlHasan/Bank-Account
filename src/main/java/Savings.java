public class Savings extends Account {
	boolean withdrawnThisMonth;

	public Savings(String id, double apr) {

		super(id, apr, "savings");
		withdrawnThisMonth = false;
	}

	@Override
	public void withdraw(double withdrawAmount) {
		double balance = getBalance();
		boolean withdrawnMoreThanBalance = withdrawAmount > balance;

		if (withdrawnMoreThanBalance) {
			if (balance > 1000) {
				setBalance(balance - 1000);
			} else {
				setBalance(0);
			}
		} else {
			setBalance(balance - withdrawAmount);
		}
		withdrawnThisMonth = true;
	}

	@Override
	public boolean isValidDeposit(double depositAmount) {
		return depositAmount >= 0 && depositAmount <= 2500;
	}

	@Override
	public boolean isValidWithdrawal(double withdrawalAmount) {
		return withdrawalAmount >= 0 && withdrawalAmount <= 1000;
	}

	@Override
	public void passMonth() {
		withdrawnThisMonth = false;
	}
}
