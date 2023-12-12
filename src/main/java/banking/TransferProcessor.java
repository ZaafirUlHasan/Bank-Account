package banking;

public class TransferProcessor extends CommandProcessor {
	public TransferProcessor(Bank bank) {
		super(bank);
	}

	public void processTransfer(String[] parts) {
		String fromID = parts[1];
		String toID = parts[2];
		double amount = Double.parseDouble(parts[3]);
		double maxWithdrawal = bank.maxWithdrawal(fromID);
		if (maxWithdrawal < amount) {
			amount = maxWithdrawal;
		}

		bank.withdraw(fromID, amount);
		bank.deposit(toID, amount);

	}
}
