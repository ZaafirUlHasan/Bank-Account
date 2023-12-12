public class TransferValidator extends CommandValidator {
	public TransferValidator(Bank bank) {
		super(bank);
	}

	public boolean validateTransfer(String[] parts) {

		if (parts.length != 4) {
			return false;
		}
		String fromID = parts[1];
		String toID = parts[2];
		String transferAmount = parts[3];

		if (fromID.equals(toID)) {
			return false;
		}

		boolean validFromId;
		boolean validToId;
		boolean accountsExist = accountExistsInBank(fromID) && accountExistsInBank(toID);

		if (accountsExist && super.checkId(fromID) && super.checkId(toID)) {
			validFromId = checkTransferID(fromID);
			validToId = checkTransferID(toID);
		} else {
			return false;
		}
		boolean validAmount;
		try {
			validAmount = validateTransferAmount(fromID, toID, Double.parseDouble(transferAmount));
		} catch (NumberFormatException e) {
			return false;
		}

		return validFromId && validToId && validAmount;
	}

	private boolean validateTransferAmount(String fromID, String toID, double transferAmount) {
		boolean validWithdrawal = bank.isValidWithdrawal(fromID, transferAmount);
		boolean validDeposit = bank.isValidDeposit(toID, transferAmount);

		return validWithdrawal && validDeposit;
	}

	private boolean checkTransferID(String id) {
		boolean accountExists = accountExistsInBank(id);
		boolean validAccountType = validateAccountType(id);

		return accountExists && validAccountType;
	}

	private boolean validateAccountType(String id) {
		String accountType = bank.getAccountType(id);
		return accountType.equals("checking") || accountType.equals("savings");
	}
}
