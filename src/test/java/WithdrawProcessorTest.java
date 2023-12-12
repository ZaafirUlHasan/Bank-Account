import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WithdrawProcessorTest {
	Bank bank;
	WithdrawProcessor withdrawProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		withdrawProcessor = new WithdrawProcessor(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void withdraw_from_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 750"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1250, actualBalance);
	}

	@Test
	public void withdraw_from_checking() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 250"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1750, actualBalance);
	}

	@Test
	public void withdraw_from_cd() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 2000"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}

	@Test
	public void multiple_accounts_withdrawn_from() {
		bank.addAccount("12345678", 0.6, true);
		bank.addAccount("01234567", 0.6, false);

		bank.deposit("12345678", 1520);
		bank.deposit("01234567", 60.3);

		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 250"));
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 01234567 15"));

		Account account1 = bank.getAccountByID("12345678");
		Account account2 = bank.getAccountByID("01234567");

		double actualBalance1 = account1.getBalance();
		double actualBalance2 = account2.getBalance();

		assertEquals(1270, actualBalance1);
		assertEquals(45.3, actualBalance2);
	}

	@Test
	public void zero_withdrawn_from_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 0"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(2000, actualBalance);
	}

	@Test
	public void zero_withdrawn_from_checking() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 0"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(2000, actualBalance);
	}

	@Test
	public void more_than_balance_withdrawn_from_cd() {
		bank.addAccount("12345678", 3.0, 1500);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 50000"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}

	@Test
	public void more_than_balance_withdrawn_from_checking() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 50000"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1600, actualBalance);
	}

	@Test
	public void more_than_balance_withdrawn_from_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 50000"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1000, actualBalance);
	}

	@Test
	public void decimal_amount_withdrawn_from_savings() {
		bank.addAccount("12345678", 3.0, false);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 663.4"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1336.6, actualBalance);
	}

	@Test
	public void decimal_amount_withdrawn_from_checking() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 45.7"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1954.3, actualBalance);
	}

	@Test
	public void decimal_amount_withdrawn_from_cd() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 2000.8);
		withdrawProcessor.processWithdrawal(splitCommand("withdraw 12345678 2000.8"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}
}