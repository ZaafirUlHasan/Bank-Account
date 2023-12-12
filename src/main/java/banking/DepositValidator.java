package banking;

public class DepositValidator extends CommandValidator {

	public DepositValidator(Bank bank) {
		super(bank);
	}

	public boolean validateDeposit(String[] parts) {
		boolean validDeposit = false;

		if (parts.length != 3) {
			return false;
		}

		String id = parts[1];
		String depositAmount = parts[2];

		boolean validId = (super.checkId(id) && accountExistsInBank(id));
		if (validId) {
			validDeposit = checkDepositAmount(id , depositAmount);
		}else {
			return false;
		}

		return validDeposit;

	}

	private boolean checkDepositAmount(String id, String depositAmount) {
		try {
			return bank.isValidDeposit(id, Double.parseDouble(depositAmount));
		} catch (NumberFormatException ignored) {
			return false;
		}
	}

}
