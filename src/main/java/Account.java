public abstract class Account {

	private String id;
	private double apr;
	private double balance;
	private String accountType;

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

	public double getApr() {
		return apr;
	}

	public void deposit(double depositAmount) {
		balance += depositAmount;
	}

	public void withdraw(double withdrawAmount) {
		boolean withdrawnMoreThanBalance = withdrawAmount > balance;
		if (withdrawnMoreThanBalance) {
			if (getAccountType().equals("checking")) {
				if (balance > 400) {
					balance -= 400;
				} else {
					balance = 0;
				}
			} else if (getAccountType().equals("savings")) {
				if (balance > 1000) {
					balance -= 1000;
				} else {
					balance = 0;
				}
			} else if (getAccountType().equals("cd")) {
				balance = 0;
			}
		} else {
			balance -= withdrawAmount;
		}
	}

	public String getId() {
		return id;
	}

	public boolean isValidDeposit(double depositAmount) {
		if (depositAmount < 0) {
			return false;
		}
		if (accountType.equals("checking") && depositAmount <= 1000) {
			return true;
		} else {
			return accountType.equals("savings") && depositAmount <= 2500;
		}

	}

	public boolean isValidWithdrawal(double withdrawalAmount) {
		if (withdrawalAmount < 0) {
			return false;
		}
		if (accountType.equals("checking") && withdrawalAmount <= 400) {
			return true;
		} else if (accountType.equals("savings") && withdrawalAmount <= 1000) {
			return true;
		} else {
			return accountType.equals("cd") && (withdrawalAmount >= getBalance() && withdrawalAmount >= 1000);
		}
	}
}
