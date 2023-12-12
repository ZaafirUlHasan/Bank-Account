package banking;

import java.util.List;

public class MasterControl {

	private final Bank bank;
	private final CommandValidator commandValidator;
	private final CommandProcessor commandProcessor;
	private final CommandStorage commandStorage;

	public MasterControl(Bank bank, CommandValidator commandValidator, CommandProcessor commandProcessor,
			CommandStorage commandStorage) {
		this.bank = bank;
		this.commandValidator = commandValidator;
		this.commandProcessor = commandProcessor;
		this.commandStorage = commandStorage;

	}

	public List<String> start(List<String> input) {
		for (String command : input) {
			if (commandValidator.validate(command)) {
				commandProcessor.process(command);

				commandStorage.addValidCommand(command);
			} else {
				commandStorage.addInvalidCommand(command);
			}
		}

		Output output = new Output(bank, commandStorage);
		List<String> finalOutput = output.output();

		finalOutput.addAll(commandStorage.getInvalidCommands());
		return commandStorage.getInvalidCommands();
	}
}
