package banking;

import java.util.HashMap;
import java.util.Map;

public class Bank {
	private final Map<String, Account> accounts;

	public Bank() {
		accounts = new HashMap<>();
	}

	public Map<String, Account> getAccounts() {
		return accounts;
	}

	public void addAccount(String id, double apr, Boolean checking) {
		Account newAccount;
		if (checking) {
			newAccount = new Checking(id, apr);
		} else {
			newAccount = new Savings(id, apr);
		}
		accounts.put(id, newAccount);
	}

	public void addAccount(String id, double apr, double balance) {
		Account newAccount;
		newAccount = new CD(id, apr, balance);
		accounts.put(id, newAccount);
	}

	public Account getAccountByID(String id) {
		return accounts.get(id);
	}

	public void deposit(String id, double amountToBeDeposited) {
		Account account = getAccountByID(id);
		account.deposit(amountToBeDeposited);
	}

	public void withdraw(String id, double amountToBeWithdrawn) {
		Account account = getAccountByID(id);
		account.withdraw(amountToBeWithdrawn);
	}

	public boolean accountExistsById(String id) {
		return accounts.get(id) != null;
	}

	public boolean isValidDeposit(String id, double depositAmount) {
		Account account;
		account = accounts.get(id);

		return account.isValidDeposit(depositAmount);
	}

	public boolean isValidWithdrawal(String id, double withdrawalAmount) {
		Account account;
		account = accounts.get(id);
		return account.isValidWithdrawal(withdrawalAmount);
	}

	public String getAccountType(String id) {
		Account account = getAccountByID(id);
		return account.getAccountType();
	}

	public double maxWithdrawal(String id) {
		Account account = getAccountByID(id);
		return account.getBalance();
	}

	public void removeAccount(String accountId) {
		accounts.remove(accountId);
	}
}
