package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandValidatorTest {

	CommandValidator commandValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		commandValidator = new CommandValidator(bank);

	}

	@Test
	public void savings_account_is_created_normally() {
		boolean actual = commandValidator.validate("create savings 12345678 0.6");
		assertTrue(actual);
	}

	@Test
	public void checking_account_is_created_normally() {
		boolean actual = commandValidator.validate("create checking 98765432 2.5");
		assertTrue(actual);
	}

	@Test
	public void cd_account_is_created_normally() {
		boolean actual = commandValidator.validate("create cd 55555555 2.5 1500");
		assertTrue(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_at_end() {
		boolean actual = commandValidator.validate("create savings 12345678 5                      ");
		assertTrue(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_at_end() {
		boolean actual = commandValidator.validate("create checking 12345678 2.5               ");
		assertTrue(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_at_end() {
		boolean actual = commandValidator.validate("create cd 12345678 2.5 1500           ");
		assertTrue(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_at_start() {
		boolean actual = commandValidator.validate("                     	create savings 12345678 5");
		assertFalse(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_at_start() {
		boolean actual = commandValidator.validate("                     	create checking 12345678 5");
		assertFalse(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_at_start() {
		boolean actual = commandValidator.validate("                     	create cd 12345678 5 1500");
		assertFalse(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_in_middle() {
		boolean actual = commandValidator.validate("create            savings            12345678            4.0");
		assertFalse(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_in_middle() {
		boolean actual = commandValidator.validate("create            checking            12345678            4.0");
		assertFalse(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_in_middle() {
		boolean actual = commandValidator
				.validate("create                  cd            12345678            4.0 	 	 1500");
		assertFalse(actual);
	}

	@Test
	public void savings_case_insensitive() {
		boolean actual = commandValidator.validate("CrEaTe SaViNgS 12345678 0.4");
		assertTrue(actual);
	}

	@Test
	public void checking_case_insensitive() {
		boolean actual = commandValidator.validate("CREATE CHECKING 12345678 5");
		assertTrue(actual);
	}

	@Test
	public void cd_case_insensitive() {
		boolean actual = commandValidator.validate("creAtE Cd 12345678 5 1500");
		assertTrue(actual);
	}

	@Test
	public void typo_in_create() {
		boolean actual = commandValidator.validate("Craete savings 12345678 0.5");
		assertFalse(actual);
	}

	@Test
	public void missing_create() {
		boolean actual = commandValidator.validate("savings 12345678 3.0");
		assertFalse(actual);
	}

	@Test
	public void typo_in_deposit() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = commandValidator.validate("deopsit 12345678 750");

		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_normally() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = commandValidator.validate("deposit 12345678 500");

		assertTrue(actual);
	}

	@Test
	public void deposit_into_savings_normally() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = commandValidator.validate("deposit 12345678 500");

		assertTrue(actual);
	}

	@Test
	public void attempt_to_deposit_into_cd_() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = commandValidator.validate("deposit 12345678 500");

		assertFalse(actual);
	}

	@Test
	public void account_created_and_deposited() {
		boolean actual1 = commandValidator.validate("create savings 12345678 0.6");
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual2 = commandValidator.validate("deposit 12345678 500");

		assertFalse(actual1 && actual2);
	}

	@Test
	public void transfer_normally() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("transfer 12345678 87654321 300");

		assertTrue(actual);
	}

	@Test
	public void transfer_with_extra_spaces_at_start() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("                 transfer 12345678 87654321 300");

		assertFalse(actual);
	}

	@Test
	public void transfer_with_extra_spaces_in_middle() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("transfer     12345678       87654321       300");

		assertFalse(actual);
	}

	@Test
	public void transfer_with_extra_spaces_at_end() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("transfer 12345678 87654321 300            ");

		assertTrue(actual);
	}

	@Test
	public void transfer_case_insensitive() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("tRaNsFeR 12345678 87654321 300");

		assertTrue(actual);
	}

	@Test
	public void typo_in_transfer() {
		bank.addAccount("12345678", 4.0, true);
		bank.addAccount("87654321", 4.0, true);
		boolean actual = commandValidator.validate("twansfer 12345678 87654321 300");

		assertFalse(actual);
	}

}