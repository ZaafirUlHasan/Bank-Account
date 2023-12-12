import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MasterControlTest {
	MasterControl masterControl;
	List<String> input;

	@BeforeEach
	void setUp() {
		input = new ArrayList<>();
		Bank bank = new Bank();
		masterControl = new MasterControl(bank, new CommandValidator(bank), new CommandProcessor(bank),
				new CommandStorage());
	}

	private void assertSingleCommand(String command, List<String> actual) {
		assertEquals(1, actual.size());
		assertEquals(command, actual.get(0));

	}

	@Test
	void typo_in_create_command() {
		input.add("creat checking 12345678 1.0");

		List<String> actual = masterControl.start(input);
		assertSingleCommand("creat checking 12345678 1.0", actual);

	}

	@Test
	void typo_in_deposit_is_invalid() {
		input.add("depositt 12345678 1.0");

		List<String> actual = masterControl.start(input);
		assertSingleCommand("depositt 12345678 1.0", actual);
	}

	@Test
	void two_typo_commands_both_invalid() {
		input.add("creat checking 12345678 1.0");
		input.add("depositt 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(2, actual.size());
		assertEquals("creat checking 12345678 1.0", actual.get(0));
		assertEquals("depositt 12345678 1.0", actual.get(1));

	}

	@Test
	void invalid_to_create_two_accounts_with_same_ID() {
		input.add("create checking 12345678 1.0");
		input.add("create checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertSingleCommand("create checking 12345678 1.0", actual);

	}

	@Test
	void valid_command_not_inserted_into_invalid_commands() {
		input.add("create checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(0, actual.size());
	}

	@Test
	void multiple_valid_commands_not_inserted_into_invalid_commands() {
		input.add("create checking 12345678 1.0");
		input.add("create cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(0, actual.size());
	}

	@Test
	void two_invalid_commands_from_mixed_commands() {
		input.add("create checking 12345678 1.0");
		input.add("createe cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");
		input.add("create savings 012345649807789 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(2, actual.size());
	}

	@Test
	void correct_invalid_commands_from_mixed_commands() {
		input.add("create checking 12345678 1.0");
		input.add("createe cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");
		input.add("depositt 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals("createe cd 01234567 1.0 1500", actual.get(0));
		assertEquals("depositt 12345678 1.0", actual.get(1));
	}

	@Test
	void empty_input_should_return_empty_invalid_commands() {
		List<String> actual = masterControl.start(new ArrayList<>());
		assertEquals(0, actual.size());
	}

	@Test
	void do_not_process_invalid_commands() {
		input.add("create checking 12345678 1.0");
		input.add("createe cd 01234567 1.0 1500");

		List<String> actual = masterControl.start(input);

		assertEquals(1, actual.size());
		assertEquals("createe cd 01234567 1.0 1500", actual.get(0));
	}

}
