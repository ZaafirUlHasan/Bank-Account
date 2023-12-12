package banking;

public class CommandProcessor {

	public Bank bank;

	public CommandProcessor(Bank bank) {

		this.bank = bank;
	}

	public void process(String command) {
		String[] parts = getParts(command);

		switch (parts[0]) {
			case "create":
				CreateProcessor createProcessor = new CreateProcessor(bank);
				createProcessor.processCreate(parts);
				break;
			case "deposit":
				DepositProcessor depositProcessor = new DepositProcessor(bank);
				depositProcessor.processDeposit(parts);
				break;
			case "withdraw":
				WithdrawProcessor withdrawProcessor = new WithdrawProcessor(bank);
				withdrawProcessor.processWithdrawal(parts);
				break;
			case "transfer":
				TransferProcessor transferProcessor = new TransferProcessor(bank);
				transferProcessor.processTransfer(parts);
				break;
			case "pass":
				PassTimeProcessor passTimeProcessor = new PassTimeProcessor(bank);
				passTimeProcessor.processPassTime(parts);
				break;
			default:
				break;
		}
	}

	private String[] getParts(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}
}
