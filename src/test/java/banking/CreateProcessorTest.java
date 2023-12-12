package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CreateProcessorTest {
	Bank bank;
	CreateProcessor createProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		createProcessor = new CreateProcessor(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void account_created_with_0_apr() {
		createProcessor.processCreate(splitCommand("create checking 12345678 0"));
		Account account = bank.getAccountByID("12345678");
		double actualApr = account.getApr();

		assertEquals(0.0, actualApr);
	}

	@Test
	public void account_created_with_all_0_id() {
		createProcessor.processCreate(splitCommand("create checking 00000000 0.6"));
		Account account = bank.getAccountByID("00000000");
		String actualId = account.getId();

		assertEquals("00000000", actualId);
	}

	@Test
	public void account_created_with_max_apr() {
		createProcessor.processCreate(splitCommand("create savings 12345678 10"));
		Account account = bank.getAccountByID("12345678");
		double actualApr = account.getApr();

		assertEquals(10, actualApr);
	}

	@Test
	public void account_created_with_all_9_id() {
		createProcessor.processCreate(splitCommand("create checking 99999999 0.6"));
		Account account = bank.getAccountByID("99999999");
		String actualId = account.getId();

		assertEquals("99999999", actualId);
	}

	@Test
	public void cd_created_with_minimum_initial_deposit() {
		createProcessor.processCreate(splitCommand("create cd 99999999 0.6 1000"));
		Account account = bank.getAccountByID("99999999");
		Double actualBalance = account.getBalance();

		assertEquals(1000, actualBalance);
	}

	@Test
	public void cd_created_with_maximum_initial_deposit() {
		createProcessor.processCreate(splitCommand("create cd 99999999 0.6 10000"));
		Account account = bank.getAccountByID("99999999");
		Double actualBalance = account.getBalance();

		assertEquals(10000, actualBalance);
	}

	@Test
	public void cd_created_initial_deposit_is_a_decimal() {
		createProcessor.processCreate(splitCommand("create cd 99999999 0.6 1550.05"));
		Account account = bank.getAccountByID("99999999");
		Double actualBalance = account.getBalance();

		assertEquals(1550.05, actualBalance);
	}

}
