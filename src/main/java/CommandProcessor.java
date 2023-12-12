public class CommandProcessor {

	public Bank bank;

	public CommandProcessor(Bank bank) {
		this.bank = bank;
	}

	public void process(String command) {
		command = command.toLowerCase();
		String[] parts = command.split(" ");

		String commandType = parts[0];

		if (commandType.equals("create")) {
			CreateProcessor createProcessor;
			createProcessor = new CreateProcessor(bank);

			createProcessor.processCreate(parts);
		} else if (commandType.equals("deposit")) {
			DepositProcessor depositProcessor;
			depositProcessor = new DepositProcessor(bank);

			depositProcessor.processDeposit(parts);
		} else if (commandType.equals("withdraw")) {
			WithdrawProcessor withdrawProcessor;
			withdrawProcessor = new WithdrawProcessor(bank);

			withdrawProcessor.processWithdrawal(parts);
		}

	}
}
