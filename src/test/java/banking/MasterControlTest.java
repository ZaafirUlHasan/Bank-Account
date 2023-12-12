package banking;

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

		assertEquals(2, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("create checking 12345678 1.0", actual.get(1));

	}

	@Test
	void account_state_inserted() {
		input.add("create checking 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(1, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
	}

	@Test
	void multiple_account_states_inserted() {
		input.add("create checking 12345678 1.0");
		input.add("create cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());
		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("Cd 01234567 1500.00 1.00", actual.get(1));
		assertEquals("Savings 90123456 0.00 1.00", actual.get(2));
	}

	@Test
	void two_invalid_two_valid_commands_from_mixed_commands() {
		input.add("create checking 12345678 1.0");
		input.add("createe cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");
		input.add("create savings 012345649807789 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals(4, actual.size());
	}

	@Test
	void correct_invalid_commands_from_mixed_commands() {
		input.add("create checking 12345678 1.0");
		input.add("createe cd 01234567 1.0 1500");
		input.add("create savings 90123456 1.0");
		input.add("depositt 12345678 1.0");

		List<String> actual = masterControl.start(input);

		assertEquals("Checking 12345678 0.00 1.00", actual.get(0));
		assertEquals("Savings 90123456 0.00 1.00", actual.get(1));
		assertEquals("createe cd 01234567 1.0 1500", actual.get(2));
		assertEquals("depositt 12345678 1.0", actual.get(3));
	}


	@Test
	void account_should_be_deleted_after_0_balance_in_one_month() {
		input.add("create checking 12345678 1.0");
		input.add("create savings 87654321 2.0");


		input.add("deposit 12345678 400");
		input.add("deposit 87654321 2000");

		input.add("transfer 12345678 87654321 400");

		input.add("pass 1");

		List<String> actual = masterControl.start(input);

		assertEquals(3, actual.size());

		assertEquals("Savings 87654321 2404.00 2.00", actual.get(0));
		assertEquals("deposit 87654321 2000", actual.get(1));
		assertEquals("transfer 12345678 87654321 400", actual.get(2));

	}

	@Test
	void deposit_commands_added_to_output() {
		input.add("create checking 12345678 1.0");
		input.add("create savings 87654321 2.0");

		input.add("deposit 87654321 2000");
		input.add("deposit 12345678 400");

		List<String> actual = masterControl.start(input);

		assertEquals("Checking 12345678 400.00 1.00", actual.get(0));
		assertEquals("deposit 12345678 400", actual.get(1));

		assertEquals("Savings 87654321 2000.00 2.00", actual.get(2));
		assertEquals("deposit 87654321 2000", actual.get(3));

	}

	@Test
	void withdraw_commands_added_to_output() {
		input.add("create checking 12345678 1.0");
		input.add("create savings 87654321 2.0");

		input.add("deposit 12345678 400");
		input.add("deposit 87654321 2400");

		input.add("withdraw 12345678 150");
		input.add("withdraw 87654321 1000");

		input.add("withdraw 87654321 300");

		input.add("pass 3");

		input.add("withdraw 87654321 300");

		List<String> actual = masterControl.start(input);

		assertEquals("Checking 12345678 250.63 1.00", actual.get(0));
		assertEquals("deposit 12345678 400", actual.get(1));
		assertEquals("withdraw 12345678 150", actual.get(2));

		assertEquals("Savings 87654321 1107.01 2.00", actual.get(3));
		assertEquals("deposit 87654321 2400", actual.get(4));
		assertEquals("withdraw 87654321 1000", actual.get(5));
		assertEquals("withdraw 87654321 300", actual.get(6));

		assertEquals("withdraw 87654321 300", actual.get(7));
	}


	@Test
	void transactional_commands_added_to_output() {
		input.add("create checking 12345678 1.0");
		input.add("create savings 87654321 2.0");
		input.add("deposit 87654321 2000");
		input.add("deposit 12345678 400");

		input.add("withdraw 87654321 200");
		input.add("withdraw 12345678 200");

		input.add("pass 1");

		input.add("transfer 87654321 12345678 200");
		input.add("transfer 12345678 87654321 100");

		List<String> actual = masterControl.start(input);

		assertEquals("Checking 12345678 300.17 1.00", actual.get(0));
		assertEquals("deposit 12345678 400", actual.get(1));
		assertEquals("withdraw 12345678 200", actual.get(2));
		assertEquals("transfer 87654321 12345678 200", actual.get(3));
		assertEquals("transfer 12345678 87654321 100", actual.get(4));

		assertEquals("Savings 87654321 1703.00 2.00", actual.get(5));
		assertEquals("deposit 87654321 2000", actual.get(6));
		assertEquals("withdraw 87654321 200", actual.get(7));
		assertEquals("transfer 87654321 12345678 200", actual.get(8));
		assertEquals("transfer 12345678 87654321 100", actual.get(9));

	}

	@Test
	void empty_input_should_return_empty_invalid_commands() {
		List<String> actual = masterControl.start(new ArrayList<>());
		assertEquals(0, actual.size());
	}


}
