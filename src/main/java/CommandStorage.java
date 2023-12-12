import java.util.ArrayList;
import java.util.List;

public class CommandStorage {

	private final List<String> invalidCommands;
	private final List<String> validCommands;

	public CommandStorage() {
		invalidCommands = new ArrayList<>();
		validCommands = new ArrayList<>();
	}

	public void addValidCommand(String command) {
		validCommands.add(command);
	}

	public List<String> getValidCommands() {
		return validCommands;
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

	public List<String> getInvalidCommands() {
		return invalidCommands;
	}
}