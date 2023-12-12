package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositValidatorTest {
	DepositValidator depositValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		depositValidator = new DepositValidator(bank);

	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void deposit_into_checking_normally() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));

		assertTrue(actual);
	}

	@Test
	public void deposit_into_savings_normally() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));

		assertTrue(actual);
	}

	@Test
	public void attempt_to_deposit_into_cd_() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));

		assertFalse(actual);
	}

	@Test
	public void multiple_deposits_into_savings() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual1 = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));
		boolean actual2 = depositValidator.validateDeposit(splitCommand("deposit 12345678 1500"));

		assertTrue(actual1 && actual2);
	}

	@Test
	public void multiple_deposits_into_checking() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual1 = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));
		boolean actual2 = depositValidator.validateDeposit(splitCommand("deposit 12345678 750"));

		assertTrue(actual1 && actual2);
	}

	@Test
	public void attempt_to_deposit_multiple_times_into_cd_() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual1 = depositValidator.validateDeposit(splitCommand("deposit 12345678 500"));
		boolean actual2 = depositValidator.validateDeposit(splitCommand("deposit 12345678 1500"));

		assertFalse(actual1 || actual2);
	}

	@Test
	public void savings_deposit_minimum_value() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 0"));

		assertTrue(actual);
	}

	@Test
	public void checking_deposit_minimum_value() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 0"));

		assertTrue(actual);
	}

	@Test
	public void savings_deposit_maximum_value() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 2500"));

		assertTrue(actual);
	}

	@Test
	public void checking_deposit_maximum_value() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 1000"));

		assertTrue(actual);
	}

	@Test
	public void savings_huge_deposit() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 2147483647"));

		assertFalse(actual);

	}

	@Test
	public void checking_huge_deposit() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 2147483647"));

		assertFalse(actual);

	}

	@Test
	public void savings_deposit_minimum_upper_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 2500.000001"));

		assertFalse(actual);
	}

	@Test
	public void checking_deposit_minimum_upper_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 1000.0001"));

		assertFalse(actual);

	}

	@Test
	public void savings_deposit_case_insensitive() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("dEpOsIt 12345678 750"));

		assertTrue(actual);

	}

	@Test
	public void checking_deposit_case_insensitive() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("DEPOSIT 12345678 750"));

		assertTrue(actual);
	}

	@Test
	public void deposit_into_savings_with_extra_spaces_at_end() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750          "));

		assertTrue(actual);
	}

	@Test
	public void deposit_into_checking_with_extra_spaces_at_end() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750       "));

		assertTrue(actual);
	}

	@Test
	public void deposit_into_savings_account_with_extra_spaces_at_start() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("                     	deposit 12345678 750"));
		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_account_with_extra_spaces_at_start() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("                     	deposit 12345678 750"));
		assertFalse(actual);
	}

	@Test
	public void deposit_into_savings_account_with_extra_spaces_in_middle() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit            12345678            750"));
		assertFalse(actual);
	}

	@Test
	public void deposit_into_checking_account_with_extra_spaces_in_middle() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit            12345678            750"));
		assertFalse(actual);
	}

	@Test
	public void deposit_into_nonexistant_account() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 98709876 750"));

		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_account_id() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 123456789 750"));

		assertFalse(actual);
	}

	@Test
	public void incorrect_account_id() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 87654321 750"));

		assertFalse(actual);
	}

	@Test
	public void negative_value_deposited_into_savings() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 -750"));

		assertFalse(actual);
	}

	@Test
	public void missing_deposit_amount() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678"));

		assertFalse(actual);
	}

	@Test
	public void missing_deposit_amount_and_account_id() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit"));
		assertFalse(actual);
	}

	@Test
	public void missing_deposit() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("12345678 750"));

		assertFalse(actual);
	}

	@Test
	public void deposit_amount_is_not_a_number() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 abc"));

		assertFalse(actual);
	}

	@Test
	public void deposit_amount_is_alphanumeric() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 7a5b0c"));

		assertFalse(actual);
	}

	@Test
	public void deposit_id_is_not_a_number() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit abc 750"));

		assertFalse(actual);
	}

	@Test
	public void savings_deposit_too_many_arguments() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750 extraArgument"));

		assertFalse(actual);
	}

	@Test
	public void checking_deposit_too_many_arguments() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750 extraArgument"));

		assertFalse(actual);
	}

	@Test
	public void savings_deposit_is_an_decimal() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750.50"));

		assertTrue(actual);
	}

	@Test
	public void checking_deposit_is_an_decimal() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 750.50"));

		assertTrue(actual);
	}

	@Test
	public void savings_deposit_too_high() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 2600"));

		assertFalse(actual);
	}

	@Test
	public void checking_deposit_too_high() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 12345678 1100"));

		assertFalse(actual);
	}

	@Test
	public void cannot_deposit_into_account_that_does_not_exist() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = depositValidator.validateDeposit(splitCommand("deposit 01234567 350"));

		assertFalse(actual);
	}
}