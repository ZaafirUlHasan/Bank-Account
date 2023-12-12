import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawValidatorTest {
	WithdrawValidator withdrawValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		withdrawValidator = new WithdrawValidator(bank);

	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void withdraw_from_checking_normally() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 300"));

		assertTrue(actual);
	}

	@Test
	public void withdraw_from_savings_normally() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 300"));

		assertTrue(actual);
	}

	@Test
	public void withdraw_from_cd_normally() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("deposit 12345678 1500"));

		assertTrue(actual);
	}

	@Test
	public void multiple_withdrawals_savings() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual1 = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 750"));
		boolean actual2 = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 800"));

		assertTrue(actual1 && actual2);
	}

	@Test
	public void multiple_withdrawals_from_checking() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual1 = withdrawValidator.validateWithdraw(splitCommand("deposit 12345678 250"));
		boolean actual2 = withdrawValidator.validateWithdraw(splitCommand("deposit 12345678 150"));

		assertTrue(actual1 && actual2);
	}

	@Test
	public void multiple_withdrawals_from_cd() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual1 = withdrawValidator.validateWithdraw(splitCommand("deposit 12345678 250"));
		boolean actual2 = withdrawValidator.validateWithdraw(splitCommand("deposit 12345678 0"));

		assertTrue(actual1 && actual2);
	}

	@Test
	public void minimum_withdrawal_from_savings() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 0"));

		assertTrue(actual);
	}

	@Test
	public void minimum_withdrawal_from_checking() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 0"));

		assertTrue(actual);
	}

	@Test
	public void minimum_withdrawal_from_cd() {
		bank.addAccount("12345678", 4.0, 1000);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 1000"));

		assertTrue(actual);
	}

	@Test
	public void maximum_withdrawal_from_savings() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 1000"));

		assertTrue(actual);
	}

	@Test
	public void maximum_withdrawal_from_checking() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 400"));

		assertTrue(actual);
	}

	@Test
	public void maximum_withdrawal_from_cd() {
		bank.addAccount("12345678", 4.0, 10000);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 10000"));

		assertTrue(actual);
	}

	@Test
	public void savings_huge_withdrawal() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 2147483647"));

		assertFalse(actual);
	}

	@Test
	public void checking_huge_withdrawal() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 2147483647"));

		assertFalse(actual);
	}

	@Test
	public void cd_huge_withdrawal() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 2147483647"));

		assertTrue(actual);
	}

	@Test
	public void savings_withdraw_maximum_lower_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -0.000001"));

		assertFalse(actual);
	}

	@Test
	public void checking_withdraw_maximum_lower_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -0.000001"));

		assertFalse(actual);
	}

	@Test
	public void cd_withdraw_maximum_lower_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -0.000001"));

		assertFalse(actual);
	}

	@Test
	public void savings_withdraw_minimum_upper_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 1000.000001"));

		assertFalse(actual);
	}

	@Test
	public void checking_withdraw_minimum_upper_bound_invalid_value() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 400.000001"));

		assertFalse(actual);
	}

	@Test
	public void negative_value_withdrawn_from_savings() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -150"));

		assertFalse(actual);
	}

	@Test
	public void negative_value_withdrawn_from_checking() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -150"));

		assertFalse(actual);
	}

	@Test
	public void negative_value_withdrawn_from_cd() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 -1500"));

		assertFalse(actual);
	}

	@Test
	public void missing_withdraw_amount() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678"));

		assertFalse(actual);
	}

	@Test
	public void missing_withdraw_amount_and_id() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw"));

		assertFalse(actual);
	}

	@Test
	public void missing_withdraw_command() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("12345678 150"));

		assertFalse(actual);
	}

	@Test
	public void withdraw_amount_is_not_a_number() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 abc"));

		assertFalse(actual);
	}

	@Test
	public void id_is_not_a_number() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw abc 250"));

		assertFalse(actual);
	}

	@Test
	public void id_is_alphanumeric() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 123abc 250"));

		assertFalse(actual);
	}

	@Test
	public void too_many_arguments() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 250 4.5"));

		assertFalse(actual);
	}

	@Test
	public void savings_withdraw_is_a_decimal() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 250.5"));

		assertTrue(actual);
	}

	@Test
	public void checking_withdraw_is_a_decimal() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = withdrawValidator.validateWithdraw(splitCommand("withdraw 12345678 250.5"));

		assertTrue(actual);
	}

}