import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandStorageTest {

	CommandStorage commandStorage;

	@BeforeEach
	public void setUp() {
		commandStorage = new CommandStorage();
	}

	@Test
	public void invalid_command_stored_normally() {
		commandStorage.addInvalidCommand("creat checking 12345678 4.0");
		List<String> invalidCommands = commandStorage.getInvalidCommands();
		String actual = invalidCommands.get(0);

		assertEquals("creat checking 12345678 4.0", actual);

	}

	@Test
	public void two_invalid_commands_stored_normally() {
		commandStorage.addInvalidCommand("creat checking 12345678 4.0");
		commandStorage.addInvalidCommand("creat savings 12345678 4.0");
		List<String> invalidCommands = commandStorage.getInvalidCommands();

		List<String> actual = Arrays.asList("creat checking 12345678 4.0", "creat savings 12345678 4.0");
		assertEquals(invalidCommands, actual);

	}

	@Test
	public void multiple_invalid_commands_stored_normally() {
		commandStorage.addInvalidCommand("creat checking 12345678 4.0");
		commandStorage.addInvalidCommand("creat savings 12345678 4.0");
		commandStorage.addInvalidCommand("crat cd 12345678 4");
		commandStorage.addInvalidCommand("cret cd 123456789 4");

		List<String> invalidCommands = commandStorage.getInvalidCommands();

		List<String> actual = Arrays.asList("creat checking 12345678 4.0", "creat savings 12345678 4.0",
				"crat cd 12345678 4", "cret cd 123456789 4");
		assertEquals(invalidCommands, actual);

	}

	@Test
	public void invalid_command_with_numbers_only() {
		commandStorage.addInvalidCommand("89213650193651065301138056310356105632591326519");
		List<String> invalidCommands = commandStorage.getInvalidCommands();
		String actual = invalidCommands.get(0);

		assertEquals("89213650193651065301138056310356105632591326519", actual);
	}

	@Test
	public void invalid_command_with_special_characters() {
		commandStorage.addInvalidCommand("create! checking? #12345678 &4.0");
		List<String> invalidCommands = commandStorage.getInvalidCommands();
		String actual = invalidCommands.get(0);

		assertEquals("create! checking? #12345678 &4.0", actual);
	}

	@Test
	public void testEmptyInvalidCommandsList() {
		List<String> invalidCommands = commandStorage.getInvalidCommands();
		assertEquals(0, invalidCommands.size());
	}

	@Test
	public void testNullCommand() {
		commandStorage.addInvalidCommand(null);
		List<String> invalidCommands = commandStorage.getInvalidCommands();

		assertNull(invalidCommands.get(0));
	}

}
