package banking;

public class TransferValidator extends CommandValidator {
	public TransferValidator(Bank bank) {
		super(bank);
	}

	public boolean validateTransfer(String[] parts) {
		boolean validIds;

		if (parts.length != 4){
			return false;
		}
		String fromID = parts[1];
		String toID = parts[2];
		String transferAmount = parts[3];

		validIds = checkTransferID(fromID , toID) && accountExistsInBank(fromID) && accountExistsInBank(toID);


		boolean validAmount = false;
		if (validIds) {
			try {
				validAmount = validateTransferAmount(fromID, toID, Double.parseDouble(transferAmount));
			} catch (NumberFormatException e) {
				return false;
			}
		}

		return validIds && validAmount;
	}

	private boolean validateTransferAmount(String fromID, String toID, double transferAmount) {
		boolean validWithdrawal = bank.isValidWithdrawal(fromID, transferAmount);
		boolean validDeposit = bank.isValidDeposit(toID, transferAmount);

		return validWithdrawal && validDeposit;
	}

	private boolean checkTransferID(String fromID , String toID) {
		boolean validAccountTypes = false;



		if (super.checkId(fromID) && super.checkId(toID) && !fromID.equals(toID)) {
			if (accountExistsInBank(toID) && accountExistsInBank(fromID)) {
				validAccountTypes = validateAccountType(fromID) && validateAccountType(toID);
			}
		} else {
			return false;
		}

		return validAccountTypes;



	}

	private boolean validateAccountType(String id) {
		String accountType = bank.getAccountType(id);
		return accountType.equals("checking") || accountType.equals("savings");
	}
}
