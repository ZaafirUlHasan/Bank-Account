package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PassTimeProcessor extends CommandProcessor {
	public PassTimeProcessor(Bank bank) {
		super(bank);
	}

	public void processPassTime(String[] parts) {
		int months = Integer.parseInt(parts[1]);
		Map<String, Account> accounts = bank.getAccounts();
		List<String> accountsToRemove = new ArrayList<>();

		for (int i = 1; i <= months; i++) {
			for (Map.Entry<String, Account> entry : accounts.entrySet()) {
				Account account = entry.getValue();

				if (account.getBalance() == 0) {
					accountsToRemove.add(account.getId());
					continue;
				}

				if (account.getBalance() < 100) {
					account.withdraw(25); // Update the account balance
				}

				if (account.getAccountType().equals("cd")) {
					for (int j = 1; j <= 4; j++) {
						calculateApr(account);
					}
				} else {
					calculateApr(account);
				}

				account.passMonth();
			}

			// Remove accounts outside the iteration
			for (String accountId : accountsToRemove) {
				bank.removeAccount(accountId);
				accounts.remove(accountId);
			}
		}
	}

	private void calculateApr(Account account) {
		double apr = account.getApr();
		double balance = account.getBalance();

		account.deposit(balance * (apr / 1200));

	}
}
