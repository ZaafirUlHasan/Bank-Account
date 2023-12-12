package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Output {
	Bank bank;
	CommandStorage commandStorage;

	public Output(Bank bank, CommandStorage commandStorage) {
		this.bank = bank;
		this.commandStorage = commandStorage;
	}

	public List<String> output() {

		List<String> validCommands = commandStorage.getValidCommands();
		ArrayList<String> accountStates = getAccountStates(validCommands);

		ArrayList<String> finalOutput = new ArrayList<>();

		for (String state : accountStates) {
			finalOutput.add(state);
			String[] parts = state.split(" ");
			String accountId = parts[1];
			ArrayList<String> transactionHistory = getTransactionalCommandsForAccount(accountId, validCommands);

			finalOutput.addAll(transactionHistory);
		}

		return finalOutput;
	}

	private ArrayList<String> getTransactionalCommandsForAccount(String accountId, List<String> validCommands) {
		ArrayList<String> transactionHistory = new ArrayList<>();
		for (String command : validCommands) {
			String[] parts = command.split(" ");
			if (parts[0].equalsIgnoreCase("transfer")) {
				if (parts[1].equals(accountId) || parts[2].equals(accountId)) {
					transactionHistory.add(command);
				}
			} else if (parts[0].equalsIgnoreCase("deposit") || parts[0].equalsIgnoreCase("withdraw")) {
				if (parts[1].equals(accountId)) {
					transactionHistory.add(command);
				}
			}
		}

		return transactionHistory;
	}

	private ArrayList<String> getAccountStates(List<String> validCommands) {
		ArrayList<String> accountStates = new ArrayList<>();
		Map<String, Account> accounts = bank.getAccounts();

		for (String command : validCommands) {
			String[] parts = command.split(" ");
			if (parts[0].equalsIgnoreCase("create")) {
				if (bank.accountExistsById(parts[2])) {
					Account account = accounts.get(parts[2]);
					String formattedAccountState = String.format("%s %s %.2f %.2f", account.getAccountType(),
							account.getId(), account.getBalance(), account.getApr());

					formattedAccountState = capitalizeAccountState(formattedAccountState);
					accountStates.add(formattedAccountState);
				}
			}
		}

		return accountStates;

	}

	private String capitalizeAccountState(String formattedAccountState) {
		// Capitalize the first letter and convert the rest of the string to lowercase
		String firstLetter = formattedAccountState.substring(0, 1).toUpperCase();
		String restOfTheString = formattedAccountState.substring(1).toLowerCase();

		return firstLetter + restOfTheString;
	}

}
