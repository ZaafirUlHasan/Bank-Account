public class DepositValidator extends CommandValidator {

	public DepositValidator(Bank bank) {
		super(bank);
	}

	public boolean validateDeposit(String[] parts) {
		boolean validDeposit;

		if (parts.length != 3) {
			return false;
		}

		String id = parts[1];
		String depositAmount = parts[2];

		boolean validId = (super.checkId(parts[1])) && (accountExistsInBank(parts[1]));
		try {
			validDeposit = checkDeposit(id, Double.parseDouble(depositAmount));
		} catch (NumberFormatException e) {
			validDeposit = false;
		}

		return validDeposit && validId;

	}

	public boolean checkDeposit(String id, Double depositAmount) {
		return bank.isValidDeposit(id, depositAmount);
	}

}
