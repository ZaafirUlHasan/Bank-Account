package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransferProcessorTest {
	Bank bank;
	TransferProcessor transferProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		transferProcessor = new TransferProcessor(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	private double getAccountBalance(String id) {
		Account account = bank.getAccountByID(id);
		return account.getBalance();
	}

	@Test
	public void transfer_decreases_money_from_account() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 600);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 300"));

		double actual = getAccountBalance("12345678");

		assertEquals(300, actual);
	}

	@Test
	public void transfer_increases_money_in_account() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 600);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 300"));

		double actual = getAccountBalance("87654321");

		assertEquals(300, actual);
	}

	@Test
	public void only_actually_withdrawn_amount_is_deposited() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 200);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 300"));

		double actual = getAccountBalance("87654321");

		assertEquals(200, actual);
	}

	@Test
	public void balance_is_zero_after_transferring_more_than_balance() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 200);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 300"));

		double actual = getAccountBalance("12345678");

		assertEquals(0, actual);
	}

	@Test
	public void from_multiple_accounts_correct_account_deposited_into_and_withdrawn_from() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.addAccount("23456789", 3.0, true);
		bank.addAccount("98765432", 3.0, false);
		bank.addAccount("01234567", 3.0, true);
		bank.addAccount("76543210", 3.0, false);

		bank.deposit("12345678", 800);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 300"));

		double checkingActual = getAccountBalance("12345678");
		double savingsActual = getAccountBalance("87654321");

		assertEquals(500, checkingActual);
		assertEquals(300, savingsActual);
	}

	@Test
	public void zero_dollars_transferred_does_not_increase_balance() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 200);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 0"));

		double actual = getAccountBalance("87654321");

		assertEquals(0, actual);
	}

	@Test
	public void zero_dollars_transferred_does_not_decrease_balance() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 400);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 0"));

		double actual = getAccountBalance("12345678");

		assertEquals(400, actual);
	}

	@Test
	public void checking_max_transfer_from() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 900);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 400"));

		double actual = getAccountBalance("12345678");

		assertEquals(500, actual);
	}

	@Test
	public void checking_max_transfer_to() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		bank.deposit("12345678", 1500);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 1000"));

		double actual = getAccountBalance("87654321");

		assertEquals(1000, actual);
	}

	@Test
	public void savings_max_transfer_from() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, true);
		bank.deposit("12345678", 1500);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 1000"));

		double actual = getAccountBalance("12345678");

		assertEquals(500, actual);
	}

	@Test
	public void savings_max_transfer_to() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 1500);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 1000"));

		double actual = getAccountBalance("87654321");

		assertEquals(1000, actual);
	}


	@Test
	public void transfer_more_than_balance_moves_correct_amount() {
		bank.addAccount("12345678", 3.0, true);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 200);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 400"));

		double actual1 = getAccountBalance("12345678");
		double actual2 = getAccountBalance("87654321");

		assertEquals(0, actual1);
		assertEquals(200, actual2);
	}

	@Test
	public void transfer_amount_is_a_decimal_has_correct_deposit() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 1500);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 500.34"));

		double actual = getAccountBalance("87654321");

		assertEquals(500.34, actual);
	}

	@Test
	public void transfer_amount_is_a_decimal_has_correct_withdrawal() {
		bank.addAccount("12345678", 3.0, false);
		bank.addAccount("87654321", 3.0, false);
		bank.deposit("12345678", 1500.3);
		transferProcessor.processTransfer(splitCommand("transfer 12345678 87654321 504.4"));

		double actual = getAccountBalance("12345678");

		assertEquals(995.9, actual);
	}

}
