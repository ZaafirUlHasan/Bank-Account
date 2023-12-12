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

	@Test
	void sample_make_sure_this_passes_unchanged() {
		input.add("Create savings 12345678 0.6");
		input.add("Deposit 12345678 700");
		input.add("Deposit 12345678 5000");
		input.add("creAte cHecKing 98765432 0.01");
		input.add("Deposit 98765432 300");
		input.add("Transfer 98765432 12345678 300");
		input.add("Pass 1");
		input.add("Create cd 23456789 1.2 2000");
		List<String> actual = masterControl.start(input);

		assertEquals(5, actual.size());
		assertEquals("Savings 12345678 1000.50 0.60", actual.get(0));
		assertEquals("Deposit 12345678 700", actual.get(1));
		assertEquals("Transfer 98765432 12345678 300", actual.get(2));
		assertEquals("Cd 23456789 2000.00 1.20", actual.get(3));
		assertEquals("Deposit 12345678 5000", actual.get(4));
	}

	@Test
	void mixed_all_commands_tested() {

		// valid creates:
		input.add("create savings 12345678 2");
		input.add("create checking 11111111 1");
		input.add("create cd 23456789 3 5000");
		input.add("create cd 34567890 3 5000");

		// invalid creates:
		input.add("create sacings 12345678 2");
		input.add("create chacking 11111111 1");
		input.add("create certificate of deposit 23456789 3 5000");

		// valid deposits:
		input.add("deposit 12345678 2000");
		input.add("deposit 11111111 500");

		// invalid deposits:
		input.add("deposit 12345678 3000");
		input.add("deposit 11111111 1500");
		input.add("deposit 12345678 -300");
		input.add("deposit 11111111 -900");
		input.add("deposit 23456789 500");
		input.add("deposit 23456789 -500");

		// invalid withdraws:
		input.add("withdraw 12345678 2000");
		input.add("withdraw 11111111 1000");
		input.add("withdraw 12345678 -400");
		input.add("withdraw 11111111 -400");
		input.add("withdraw 11111111 5000"); // 12 months not passed yet
		input.add("withdraw 34567890 5000"); // 12 months not passed yet
		input.add("withdraw 11111111 3000");
		input.add("withdraw 34567890 -5000");

		// valid withdraws:
		input.add("withdraw 12345678 800");
		input.add("withdraw 11111111 300");

		// invalid withdraws again(due to no pass time):
		input.add("withdraw 12345678 800");

		// invalid pass times:
		input.add("Pass 73");
		input.add("Pass 0");
		input.add("Pass -3");
		input.add("Pass 3000");
		input.add("Pass abc");

		// valid pass times:
		input.add("Pass 1");
		input.add("Pass 60");
		input.add("Pass 30");
		input.add("Pass 9");

		// valid withdraws for cd only:
		input.add("withdraw 23456789 135745.45");
		input.add("withdraw 34567890 20000");

		// valid transfers:
		input.add("transfer 12345678 11111111 1000");
		input.add("transfer 11111111 12345678 400");

		// invalid transfers:
		input.add("transfer 12345678 11111111 -400");
		input.add("transfer 11111111 12345678 -300");
		input.add("transfer 23456789 11111111 8000");
		input.add("transfer 34567890 23456789 8000");

		input.add("pass 1");

		List<String> actual = masterControl.start(input);

		assertEquals("Savings 12345678 818.80 2.00", actual.get(0));
		assertEquals("deposit 12345678 2000", actual.get(1));
		assertEquals("withdraw 12345678 800", actual.get(2));
		assertEquals("transfer 12345678 11111111 1000", actual.get(3));
		assertEquals("transfer 11111111 12345678 400", actual.get(4));
		assertEquals("Checking 11111111 818.05 1.00", actual.get(5));
		assertEquals("deposit 11111111 500", actual.get(6));
		assertEquals("withdraw 11111111 300", actual.get(7));
		assertEquals("transfer 12345678 11111111 1000", actual.get(8));
		assertEquals("transfer 11111111 12345678 400", actual.get(9));
		assertEquals("create sacings 12345678 2", actual.get(10));
		assertEquals("create chacking 11111111 1", actual.get(11));
		assertEquals("create certificate of deposit 23456789 3 5000", actual.get(12));
		assertEquals("deposit 12345678 3000", actual.get(13));
		assertEquals("deposit 11111111 1500", actual.get(14));
		assertEquals("deposit 12345678 -300", actual.get(15));
		assertEquals("deposit 11111111 -900", actual.get(16));
		assertEquals("deposit 23456789 500", actual.get(17));
		assertEquals("deposit 23456789 -500", actual.get(18));
		assertEquals("withdraw 12345678 2000", actual.get(19));
		assertEquals("withdraw 11111111 1000", actual.get(20));
		assertEquals("withdraw 12345678 -400", actual.get(21));
		assertEquals("withdraw 11111111 -400", actual.get(22));
		assertEquals("withdraw 11111111 5000", actual.get(23));
		assertEquals("withdraw 34567890 5000", actual.get(24));
		assertEquals("withdraw 11111111 3000", actual.get(25));
		assertEquals("withdraw 34567890 -5000", actual.get(26));
		assertEquals("withdraw 12345678 800", actual.get(27));
		assertEquals("Pass 73", actual.get(28));
		assertEquals("Pass 0", actual.get(29));
		assertEquals("Pass -3", actual.get(30));
		assertEquals("Pass 3000", actual.get(31));
		assertEquals("Pass abc", actual.get(32));
		assertEquals("transfer 12345678 11111111 -400", actual.get(33));
		assertEquals("transfer 11111111 12345678 -300", actual.get(34));
		assertEquals("transfer 23456789 11111111 8000", actual.get(35));
		assertEquals("transfer 34567890 23456789 8000", actual.get(36));

	}


}
