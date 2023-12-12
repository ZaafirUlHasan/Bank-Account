package banking;

public class DepositProcessor extends CommandProcessor {

	public DepositProcessor(Bank bank) {
		super(bank);
	}

	public void processDeposit(String[] parts) {
		bank.deposit(parts[1], Double.parseDouble(parts[2]));
	}
}
