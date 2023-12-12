package banking;

public class CommandValidator {

	protected Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String command) {

		command = command.toLowerCase();
		String[] parts = command.split(" ");

		String commandType = parts[0];

		switch (commandType) {
		case "create":
			CreateValidator createValidator;
			createValidator = new CreateValidator(bank);

			return createValidator.validateCreate(parts);
		case "deposit":
			DepositValidator depositValidator;
			depositValidator = new DepositValidator(bank);

			return depositValidator.validateDeposit(parts);
		case "withdraw":
			WithdrawValidator withdrawValidator;
			withdrawValidator = new WithdrawValidator(bank);

			return withdrawValidator.validateWithdraw(parts);
		case "transfer":
			TransferValidator transferValidator;
			transferValidator = new TransferValidator(bank);

			return transferValidator.validateTransfer(parts);
		case "pass":
			PassTimeValidator passTimeValidator;
			passTimeValidator = new PassTimeValidator(bank);

			return passTimeValidator.validatePassTime(parts);
		default:
			return false;
		}
	}

	public boolean accountExistsInBank(String id) {
		return bank.accountExistsById(id);
	}

	public boolean checkId(String id) {
		boolean validLength = id.length() == 8;
		boolean digitsOnly = id.matches("\\d{8}");

		return (validLength && digitsOnly);
	}

}
