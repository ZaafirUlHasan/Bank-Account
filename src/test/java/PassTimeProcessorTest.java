import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassTimeProcessorTest {
	Bank bank;
	PassTimeProcessor passTimeProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		passTimeProcessor = new PassTimeProcessor(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void pass_time_deletes_zero_balance_accounts() {
		bank.addAccount("12345678", 3.0, true);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		boolean actual = bank.accountExistsById("12345678");

		assertFalse(actual);
	}

	@Test
	public void pass_time_deducts_minimum_balance_fee() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 75);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		actual = Double.parseDouble(String.format("%.2f", actual));
		assertEquals(50.13, actual);
	}

	@Test
	public void checking_correct_apr_calculation_after_pass_time() {
		bank.addAccount("12345678", 3.0, true);
		bank.deposit("12345678", 1000);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		assertEquals(1002.5, actual);
	}

	@Test
	public void savings_correct_apr_calculation_after_pass_time() {
		bank.addAccount("12345678", 4.0, false);
		bank.deposit("12345678", 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		actual = Double.parseDouble(String.format("%.2f", actual));
		assertEquals(2006.67, actual);
	}

	@Test
	public void cd_correct_apr_calculation_after_pass_time() {
		bank.addAccount("12345678", 2.1, 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		actual = Double.parseDouble(String.format("%.2f", actual));
		assertEquals(2014.04, actual);
	}

	@Test
	public void multiple_apr_calculations() {
		bank.addAccount("12345678", 1.1, true);
		bank.deposit("12345678", 2000);

		bank.addAccount("87654321", 3.1, 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 1"));

		Account account1 = bank.getAccountByID("12345678");
		Account account2 = bank.getAccountByID("87654321");

		double actual1 = account1.getBalance();
		double actual2 = account2.getBalance();

		actual1 = Double.parseDouble(String.format("%.2f", actual1));
		actual2 = Double.parseDouble(String.format("%.2f", actual2));
		assertEquals(2001.83, actual1);
		assertEquals(2020.75, actual2);
	}

	@Test
	public void multiple_months_passed() {
		bank.addAccount("12345678", 4.0, false);
		bank.deposit("12345678", 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 3"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		actual = Double.parseDouble(String.format("%.2f", actual));
		assertEquals(2020.07, actual);
	}

	@Test
	public void max_months_passed() {
		bank.addAccount("12345678", 4.0, true);
		bank.deposit("12345678", 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 60"));

		Account account = bank.getAccountByID("12345678");

		double actual = account.getBalance();
		actual = Double.parseDouble(String.format("%.2f", actual));
		assertEquals(2441.99, actual);
	}

	@Test
	public void multiple_months_passed_on_multiple_accounts() {
		bank.addAccount("12345678", 1.1, true);
		bank.deposit("12345678", 2000);

		bank.addAccount("87654321", 3.1, 2000);
		passTimeProcessor.processPassTime(splitCommand("pass 24"));

		Account account1 = bank.getAccountByID("12345678");
		Account account2 = bank.getAccountByID("87654321");

		double actual1 = account1.getBalance();
		double actual2 = account2.getBalance();

		actual1 = Double.parseDouble(String.format("%.2f", actual1));
		actual2 = Double.parseDouble(String.format("%.2f", actual2));
		assertEquals(2044.47, actual1);
		assertEquals(2562.1, actual2);
	}

}