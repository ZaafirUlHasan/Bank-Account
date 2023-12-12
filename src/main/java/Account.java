public abstract class Account {

	private final String id;
	private final double apr;
	private final String accountType;
	private double balance;

	public Account(String id, double apr, String accountType) {
		this.id = id;
		this.apr = apr;
		this.balance = 0;
		this.accountType = accountType;
	}

	public String getAccountType() {
		return accountType;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getApr() {
		return apr;
	}

	public void deposit(double depositAmount) {
		balance += depositAmount;
	}

	public String getId() {
		return id;
	}

	public abstract void withdraw(double withdrawAmount);

	public abstract boolean isValidDeposit(double depositAmount);

	public abstract boolean isValidWithdrawal(double withdrawalAmount);

	public abstract void passMonth();
}
