package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeValidatorTest {
	PassTimeValidator passTimeValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		passTimeValidator = new PassTimeValidator(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void pass_time_normally() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 1"));
		assertTrue(actual);
	}

	@Test
	public void zero_time_cannot_be_passed() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 0"));
		assertFalse(actual);
	}

	@Test
	public void negative_time_cannot_be_passed() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass -6"));
		assertFalse(actual);
	}

	@Test
	public void more_than_60_months_cannot_be_passed() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 61"));
		assertFalse(actual);
	}

	@Test
	public void sixty_months_can_be_passed() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 60"));
		assertTrue(actual);
	}

	@Test
	public void decimal_months_cannot_be_passed() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 32.5"));
		assertFalse(actual);
	}

	@Test
	public void random_integer_within_range_is_valid() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 32"));
		assertTrue(actual);
	}

	@Test
	public void time_is_not_an_integer() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass abc"));
		assertFalse(actual);
	}

	@Test
	public void time_is_alphanumeric() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 123abc"));
		assertFalse(actual);
	}

	@Test
	public void very_large_time_pass_is_invalid() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 2147483647"));
		assertFalse(actual);
	}

	@Test
	public void very_large_negative_time_pass_is_invalid() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass -2147483647"));
		assertFalse(actual);
	}

	@Test
	public void missing_time() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass"));
		assertFalse(actual);
	}

	@Test
	public void missing_pass() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("12"));
		assertFalse(actual);
	}

	@Test
	public void too_many_arguments() {
		boolean actual = passTimeValidator.validatePassTime(splitCommand("pass 12 45"));
		assertFalse(actual);
	}

}