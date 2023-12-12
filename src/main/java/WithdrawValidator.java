public class WithdrawValidator extends TransferValidator {
	public WithdrawValidator(Bank bank) {
		super(bank);
	}

	public boolean validateWithdraw(String[] parts) {
		boolean validWithdrawal;

		if (parts.length != 3) {
			return false;
		}

		String id = parts[1];
		String withdrawAmount = parts[2];
		boolean validId = (super.checkId(parts[1])) && (accountExistsInBank(parts[1]));
		try {
			validWithdrawal = checkWithdrawal(id, Double.parseDouble(withdrawAmount));
		} catch (NumberFormatException e) {
			validWithdrawal = false;
		}

		return validWithdrawal && validId;
	}

	public boolean checkWithdrawal(String id, Double withdrawAmount) {
		return bank.isValidWithdrawal(id, withdrawAmount);
	}
}
