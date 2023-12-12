import java.util.ArrayList;
import java.util.List;

public class CommandStorage {

	private final List<String> invalidCommands;

	public CommandStorage() {
		invalidCommands = new ArrayList<>();
	}

	public void addInvalidCommand(String command) {
		invalidCommands.add(command);
	}

	public List<String> getInvalidCommands() {
		return invalidCommands;
	}
}