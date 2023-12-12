public class CommandValidator {

	protected Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String command) {

		command = command.toLowerCase();
		String[] parts = command.split(" ");

		if (parts[0].equals("create")) {
			CreateValidator createValidator;
			createValidator = new CreateValidator(bank);

			return createValidator.validateCreate(parts);
		} else if (parts[0].equals("deposit")) {
			DepositValidator depositValidator;
			depositValidator = new DepositValidator(bank);

			return depositValidator.validateDeposit(parts);
		} else if (parts[0].equals("transfer")) {
			TransferValidator transferValidator;
			transferValidator = new TransferValidator(bank);

			return transferValidator.validateTransfer(parts);
		} else {
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
