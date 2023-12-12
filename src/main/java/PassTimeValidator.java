public class PassTimeValidator extends CommandValidator {
	public PassTimeValidator(Bank bank) {
		super(bank);
	}

	public boolean validatePassTime(String[] parts) {
		int months;

		if (parts.length == 2) {
			try {
				months = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				return false;
			}
		} else {
			return false;
		}

		return months >= 1 && months <= 60;
	}
}
