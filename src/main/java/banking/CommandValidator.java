package banking;

public class CommandValidator {

	protected Bank bank;

	public CommandValidator(Bank bank) {
		this.bank = bank;
	}

	public boolean validate(String command) {
		String[] parts = getParts(command);
		boolean valid = false;

		switch (parts[0]) {
		case "create":
			CreateValidator createValidator;
			createValidator = new CreateValidator(bank);

			valid = createValidator.validateCreate(parts);
			break;
		case "deposit":
			DepositValidator depositValidator;
			depositValidator = new DepositValidator(bank);

			valid = depositValidator.validateDeposit(parts);
			break;
		case "withdraw":
			WithdrawValidator withdrawValidator;
			withdrawValidator = new WithdrawValidator(bank);

			valid = withdrawValidator.validateWithdraw(parts);
			break;
		case "transfer":
			TransferValidator transferValidator;
			transferValidator = new TransferValidator(bank);

			valid = transferValidator.validateTransfer(parts);
			break;
		case "pass":
			PassTimeValidator passTimeValidator;
			passTimeValidator = new PassTimeValidator(bank);

			valid = passTimeValidator.validatePassTime(parts);
			break;
		default:
			// left empty on purpose
		}

		return valid;
	}

	public boolean accountExistsInBank(String id) {
		return bank.accountExistsById(id);
	}

	public boolean checkId(String id) {
		boolean validLength = id.length() == 8;
		boolean digitsOnly = id.matches("\\d{8}");

		return (validLength && digitsOnly);
	}

	private String[] getParts(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

}
