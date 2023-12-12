public class CreateProcessor extends CommandProcessor {

	public CreateProcessor(Bank bank) {
		super(bank);
	}

	public void processCreate(String[] parts) {

		if (parts[1].equals("savings")) {
			bank.addAccount(parts[2], Double.parseDouble(parts[3]), false);
		} else if (parts[1].equals("checking")) {
			bank.addAccount(parts[2], Double.parseDouble(parts[3]), true);
		} else {
			bank.addAccount(parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
		}
	}
}
