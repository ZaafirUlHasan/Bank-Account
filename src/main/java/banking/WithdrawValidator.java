package banking;

public class WithdrawValidator extends TransferValidator {
	public WithdrawValidator(Bank bank) {
		super(bank);
	}

	public boolean validateWithdraw(String[] parts) {
		boolean validWithdrawal = false;

		if (parts.length != 3) {
			return false;
		}

		String id = parts[1];
		String withdrawAmount = parts[2];
		boolean validId = (super.checkId(parts[1])) && (accountExistsInBank(parts[1]));
		if (validId) {
			try {
				validWithdrawal = checkWithdrawal(id, Double.parseDouble(withdrawAmount));
			} catch (NumberFormatException ignored) {
			}
		}else{
			return false;
		}

		return validWithdrawal;
	}

	public boolean checkWithdrawal(String id, Double withdrawAmount) {
		return bank.isValidWithdrawal(id, withdrawAmount);
	}
}
