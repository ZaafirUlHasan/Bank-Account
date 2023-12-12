package banking;

public class CommandProcessor {

	public Bank bank;

	public CommandProcessor(Bank bank) {

		this.bank = bank;
	}

	public void process(String command) {
		command = command.toLowerCase();
		String[] parts = command.split(" ");

		String commandType = parts[0];

		switch (commandType) {
		case "create":
			CreateProcessor createProcessor;
			createProcessor = new CreateProcessor(bank);

			createProcessor.processCreate(parts);
			break;
		case "deposit":
			DepositProcessor depositProcessor;
			depositProcessor = new DepositProcessor(bank);

			depositProcessor.processDeposit(parts);
			break;
		case "withdraw":
			WithdrawProcessor withdrawProcessor;
			withdrawProcessor = new WithdrawProcessor(bank);

			withdrawProcessor.processWithdrawal(parts);
			break;
		case "transfer":
			TransferProcessor transferProcessor;
			transferProcessor = new TransferProcessor(bank);

			transferProcessor.processTransfer(parts);
			break;
		case "pass":
			PassTimeProcessor passTimeProcessor;
			passTimeProcessor = new PassTimeProcessor(bank);

			passTimeProcessor.processPassTime(parts);
			break;
		}

	}
}
