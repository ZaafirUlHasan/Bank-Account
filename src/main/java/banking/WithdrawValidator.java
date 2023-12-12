package banking;

public class WithdrawValidator extends TransferValidator {
	public WithdrawValidator(Bank bank) {
		super(bank);
	}

	public boolean validateWithdraw(String[] parts) {
		boolean validWithdrawal = false;
		boolean validLength = parts.length == 3;
		String id;
		String withdrawAmount;

		if (validLength) {
			id = parts[1];
			withdrawAmount = parts[2];
		}else {
			return false;
		}

		boolean validId = (super.checkId(parts[1])) && (accountExistsInBank(parts[1]));
		if (validId) {
			try {
				validWithdrawal = checkWithdrawal(id, Double.parseDouble(withdrawAmount));
			} catch (NumberFormatException ignored) {
				// Left empty on purpose
			}
		}

		return validWithdrawal;
	}

	public boolean checkWithdrawal(String id, Double withdrawAmount) {
		return bank.isValidWithdrawal(id, withdrawAmount);
	}
}
