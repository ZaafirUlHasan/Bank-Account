package banking;

public class CreateValidator extends CommandValidator {

	public CreateValidator(Bank bank) {
		super(bank);
	}

	public boolean validateCreate(String[] parts) {

		boolean validApr;
		boolean validId;
		boolean validInitialDeposit = true;

		boolean validAccount = checkAccountTypeAndLength(parts, parts.length);

		if (validAccount) {
			validId = super.checkId(parts[2]);
			if (accountExistsInBank(parts[2])) {
				validId = false;
			}

			try {
				validApr = checkApr(Double.parseDouble(parts[3]));
			} catch (NumberFormatException e) {
				validApr = false;
			}

			if (parts.length == 5) { // If it is a cd account, check the initial deposit:
				try {
					validInitialDeposit = checkCdInitialDeposit(Double.parseDouble(parts[4]));
				} catch (NumberFormatException e) {
					validInitialDeposit = false;
				}
			}
		} else {
			return false;
		}
		return validApr && validId && validInitialDeposit;

	}

	public boolean checkAccountTypeAndLength(String[] command, int length) {
		String accountType;

		if (length >= 4) {
			accountType = command[1];
		} else {
			return false;
		}

		if ("savings".equals(accountType) && (length == 4)) {
			return true;
		} else if ("checking".equals(accountType) && (length == 4)) {
			return true;
		} else {
			return ("cd".equals(accountType)) && (length == 5);
		}
	}

	public boolean checkApr(double apr) {
		return ((apr >= 0) && (apr <= 10));

	}

	private boolean checkCdInitialDeposit(double initialDeposit) {
		return (initialDeposit >= 1000) && (initialDeposit <= 10000);
	}

}
