package banking;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferValidatorTest {
	TransferValidator transferValidator;
	Bank bank;

	@BeforeEach
	void setUp() {
		bank = new Bank();
		transferValidator = new TransferValidator(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void transfer_from_checking_to_checking() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertTrue(actual);
	}

	@Test
	public void transfer_from_checking_to_savings() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertTrue(actual);
	}

	@Test
	public void transfer_from_savings_to_checking() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertTrue(actual);
	}

	@Test
	public void transfer_from_savings_to_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertTrue(actual);
	}

	@Test
	public void transfer_from_cd_is_invalid() {
		bank.addAccount("12345678", 3.0, 1500);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void transfer_to_cd_is_invalid() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, 1500);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void transferring_more_than_1000_from_savings_is_invalid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 1100"));
		assertFalse(actual);
	}

	@Test
	public void transferring_more_than_2500_to_savings_is_invalid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 2500"));
		assertFalse(actual);
	}

	@Test
	public void transferring_more_than_400_from_checking_is_invalid() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 500"));
		assertFalse(actual);
	}

	@Test
	public void transferring_more_than_1000_to_checking_is_invalid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 1100"));
		assertFalse(actual);
	}

	@Test
	public void transferring_to_yourself_is_invalid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 12345678 300"));
		assertFalse(actual);
	}

	@Test
	public void transferring_huge_amount_is_invalid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 2147483647"));
		assertFalse(actual);
	}

	@Test
	public void transferring_zero_is_valid() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 0"));
		assertTrue(actual);
	}

	@Test
	public void transferring_from_savings_boundary_condition() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 1000"));
		assertTrue(actual);
	}

	@Test
	public void max_transfer_to_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 1000"));
		assertTrue(actual);
	}

	@Test
	public void max_transfer_to_checking() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 1000"));
		assertTrue(actual);
	}

	@Test
	public void transferring_from_checking_boundary_condition() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 400"));
		assertTrue(actual);
	}

	@Test
	public void transferring_negative_value_is_invalid() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 -400"));
		assertFalse(actual);
	}

	@Test
	public void transfer_amount_is_not_a_number() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 abc"));
		assertFalse(actual);
	}

	@Test
	public void from_id_is_not_a_number() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer abc 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void to_id_is_not_a_number() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 abc 300"));
		assertFalse(actual);
	}

	@Test
	public void missing_amount() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321"));
		assertFalse(actual);
	}

	@Test
	public void missing_amount_and_toID() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678"));
		assertFalse(actual);
	}

	@Test
	public void missing_amount_fromID_and_toID() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer"));
		assertFalse(actual);
	}

	@Test
	public void missing_transfer() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("12345678 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_from_ID() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 123456789 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void too_many_digits_in_to_ID() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 987654321 300"));
		assertFalse(actual);
	}

	@Test
	public void too_many_arguments() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300 1500"));
		assertFalse(actual);
	}

	@Test
	public void multiple_transfer_commands() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"))
				&& transferValidator.validateTransfer(splitCommand("transfer 87654321 12345678 400"))
				&& transferValidator.validateTransfer(splitCommand("transfer 87654321 12345678 100"));
		assertTrue(actual);
	}

	@Test
	public void transfer_from_nonexistant_account_is_invalid() {
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertFalse(actual);
	}

	@Test
	public void transfer_to_nonexistant_account_is_invalid() {
		bank.addAccount("87654321", 3.0, false);
		boolean actual = transferValidator.validateTransfer(splitCommand("transfer 12345678 87654321 300"));
		assertFalse(actual);
	}

}
