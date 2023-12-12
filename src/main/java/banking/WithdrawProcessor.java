package banking;

public class WithdrawProcessor extends CommandProcessor {
	public WithdrawProcessor(Bank bank) {
		super(bank);
	}

	public void processWithdrawal(String[] parts) {
		bank.withdraw(parts[1], Double.parseDouble(parts[2]));
	}
}
