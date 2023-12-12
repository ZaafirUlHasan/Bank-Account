package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateValidatorTest {
	CreateValidator createValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		createValidator = new CreateValidator(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void savings_account_is_created_normally() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 12345678 0.6"));
		assertTrue(actual);
	}

	@Test
	public void checking_account_is_created_normally() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 98765432 2.5"));
		assertTrue(actual);
	}

	@Test
	public void cd_account_is_created_normally() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 55555555 2.5 1500"));
		assertTrue(actual);
	}

	@Test
	public void savings_with_huge_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 00000003 2147483647"));
		assertFalse(actual);
	}

	@Test
	public void checking_with_huge_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 00000003 2147483647"));
		assertFalse(actual);
	}

	@Test
	public void cd_with_huge_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000005 2147483647 5000"));
		assertFalse(actual);
	}

	@Test
	public void savings_with_minimum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 00000003 0"));
		assertTrue(actual);
	}

	@Test
	public void checking_with_minimum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 00000003 0"));
		assertTrue(actual);
	}

	@Test
	public void cd_with_minimum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000003 0 1500"));
		assertTrue(actual);
	}

	@Test
	public void savings_with_maximum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 00000003 10"));
		assertTrue(actual);
	}

	@Test
	public void checking_with_maximum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 00000003 10"));
		assertTrue(actual);
	}

	@Test
	public void cd_with_maximum_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000003 10 1500"));
		assertTrue(actual);
	}

	@Test
	public void savings_negative_apr_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 00000003 -5"));
		assertFalse(actual);
	}

	@Test
	public void checking_negative_apr_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 00000003 -2.3"));
		assertFalse(actual);
	}

	@Test
	public void cd_negative_apr_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000003 -6.3 1500"));
		assertFalse(actual);
	}

	@Test
	public void savings_apr_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 12345678 abc"));
		assertFalse(actual);
	}

	@Test
	public void checking_apr_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 12345678 abc"));
		assertFalse(actual);
	}

	@Test
	public void cd_apr_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 abc 1500"));
		assertFalse(actual);
	}

	@Test
	public void invalid_account_type() {
		boolean actual = createValidator.validateCreate(splitCommand("create foobar 00000003 0.6"));
		assertFalse(actual);
	}

	@Test
	public void missing_account_type() {
		boolean actual = createValidator.validateCreate(splitCommand("create 12345678 0.6"));
		assertFalse(actual);
	}

	@Test
	public void typo_or_incorrect_account_type() {
		boolean actual = createValidator.validateCreate(splitCommand("create invalidAccount 12345678 0.6"));
		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_savings_account_id() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 1234567890 0.6"));
		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_checking_account_id() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 1234567890 0.6"));
		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_cd_account_id() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 1234567890 0.6 1500"));
		assertFalse(actual);
	}

	@Test
	public void savings_id_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings abc123de 0.6"));
		assertFalse(actual);
	}

	@Test
	public void checking_id_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking abc123de 0.6"));
		assertFalse(actual);
	}

	@Test
	public void cd_id_is_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd abc123de 0.6 1500"));
		assertFalse(actual);
	}

	@Test
	public void savings_account_with_too_many_arguments() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 12345678 0.6 500"));
		assertFalse(actual);
	}

	@Test
	public void checking_account_with_too_many_arguments() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 12345678 0.6 500"));
		assertFalse(actual);
	}

	@Test
	public void cd_account_with_too_many_arguments() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 0.6 500 1500"));
		assertFalse(actual);
	}

	@Test
	public void savings_with_no_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings 12345678 "));
		assertFalse(actual);
	}

	@Test
	public void checking_with_no_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking 12345678 "));
		assertFalse(actual);
	}

	@Test
	public void cd_with_no_apr_and_balance() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 "));
		assertFalse(actual);
	}

	@Test
	public void savings_with_no_id_and_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create savings"));
		assertFalse(actual);

	}

	@Test
	public void checking_with_no_id_and_apr() {
		boolean actual = createValidator.validateCreate(splitCommand("create checking"));
		assertFalse(actual);
	}

	@Test
	public void cd_missing_balance() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 5"));
		assertFalse(actual);
	}

	@Test
	public void missing_create() {
		boolean actual = createValidator.validateCreate(splitCommand("savings 12345678 3.0"));
		assertFalse(actual);
	}

	@Test
	public void cd_with_no_id_apr_and_balance() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd"));
		assertFalse(actual);
	}

	@Test
	public void no_account_type_id_or_balance() {
		boolean actual = createValidator.validateCreate(splitCommand("create"));
		assertFalse(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_at_end() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create savings 12345678 5                      	"));
		assertFalse(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_at_end() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create checking 12345678 5                      	"));
		assertFalse(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_at_end() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create cd 12345678 5 1500                      	"));
		assertFalse(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_at_start() {
		boolean actual = createValidator
				.validateCreate(splitCommand("                     	create savings 12345678 5"));
		assertFalse(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_at_start() {
		boolean actual = createValidator
				.validateCreate(splitCommand("                     	create checking 12345678 5"));
		assertFalse(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_at_start() {
		boolean actual = createValidator
				.validateCreate(splitCommand("                     	create cd 12345678 5 1500"));
		assertFalse(actual);
	}

	@Test
	public void savings_account_created_with_extra_spaces_in_middle() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create            savings            12345678            4.0"));
		assertFalse(actual);
	}

	@Test
	public void checking_account_created_with_extra_spaces_in_middle() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create            checking            12345678            4.0"));
		assertFalse(actual);
	}

	@Test
	public void cd_account_created_with_extra_spaces_in_middle() {
		boolean actual = createValidator
				.validateCreate(splitCommand("create            cd            12345678            4.0 	 	 1500"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_minimum_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000000 0.0 1000"));
		assertTrue(actual);
	}

	@Test
	public void cd_initial_deposit_maximum_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 99999999 0.0 10000"));
		assertTrue(actual);
	}

	@Test
	public void cd_initial_deposit_maximum_lower_bound_invalid_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000000 0.0 999.99999"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_maximum_upper_bound_invalid_value() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 00000000 0.0 10000.00001"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_is_a_decimal() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 5 1500.50"));
		assertTrue(actual);
	}

	@Test
	public void cd_negative_initial_deposit() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 1.3 -2000"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_too_low() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 55555555 1.2 500"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_too_high() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 55555555 1.2 11000"));
		assertFalse(actual);
	}

	@Test
	public void cd_initial_deposit_not_a_number() {
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 0.5 abc"));
		assertFalse(actual);
	}

	@Test
	public void savings_case_insensitive() {
		boolean actual = createValidator.validateCreate(splitCommand("CrEaTe SaViNgS 12345678 0.4"));
		assertTrue(actual);
	}

	@Test
	public void checking_case_insensitive() {
		boolean actual = createValidator.validateCreate(splitCommand("CREATE CHECKING 12345678 5"));
		assertTrue(actual);
	}

	@Test
	public void cd_case_insensitive() {
		boolean actual = createValidator.validateCreate(splitCommand("create Cd 12345678 5 1500"));
		assertTrue(actual);
	}

	@Test
	public void multiple_savings_accounts_created() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = createValidator.validateCreate(splitCommand("create savings 87654321 0.5"));
		assertTrue(actual);
	}

	@Test
	public void multiple_checking_accounts_created() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = createValidator.validateCreate(splitCommand("create checking 87654321 0.5"));
		assertTrue(actual);
	}

	@Test
	public void multiple_cd_accounts_created() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = createValidator.validateCreate(splitCommand("create cd 87654321 4.0 1500"));
		assertTrue(actual);
	}

	@Test
	public void attempt_to_create_savings_account_with_existing_id() {
		bank.addAccount("12345678", 4.0, false);
		boolean actual = createValidator.validateCreate(splitCommand("create savings 12345678 0.5"));
		assertFalse(actual);
	}

	@Test
	public void attempt_to_create_checking_account_with_existing_id() {
		bank.addAccount("12345678", 4.0, true);
		boolean actual = createValidator.validateCreate(splitCommand("create checking 12345678 0.5"));
		assertFalse(actual);
	}

	@Test
	public void attempt_to_create_cd_account_with_existing_id() {
		bank.addAccount("12345678", 4.0, 1500);
		boolean actual = createValidator.validateCreate(splitCommand("create cd 12345678 1500"));
		assertFalse(actual);
	}
}
