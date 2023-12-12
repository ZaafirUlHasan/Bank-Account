package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CommandProcessorTest {

	Bank bank;
	CommandProcessor commandProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		commandProcessor = new CommandProcessor(bank);
	}

	@Test
	public void saving_created_normally() {
		commandProcessor.process("create savings 12345678 0.6");
		Account account = bank.getAccountByID("12345678");
		double actualApr = account.getApr();
		String actualId = account.getId();

		assertEquals(0.6, actualApr);
		assertEquals("12345678", actualId);

	}

	@Test
	public void checking_created_normally() {
		commandProcessor.process("create checking 12345678 0.6");
		Account account = bank.getAccountByID("12345678");
		double actualApr = account.getApr();
		String actualId = account.getId();

		assertEquals(0.6, actualApr);
		assertEquals("12345678", actualId);

	}

	@Test
	public void cd_created_normally() {
		commandProcessor.process("create cd 12345678 0.6 1500");
		Account account = bank.getAccountByID("12345678");
		double actualApr = account.getApr();
		String actualId = account.getId();

		assertEquals(0.6, actualApr);
		assertEquals("12345678", actualId);
	}

	@Test
	public void two_accounts_created_normally() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("create savings 01234567 1.4");

		assertEquals(2, bank.getAccounts().size());
	}

	@Test
	public void two_accounts_created_normally_have_correct_ids() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("create savings 01234567 1.4");

		assertEquals("12345678", bank.getAccounts().get("12345678").getId());
		assertEquals("01234567", bank.getAccounts().get("01234567").getId());
	}

	@Test
	public void two_accounts_created_normally_have_correct_apr() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("create savings 01234567 1.4");

		assertEquals(0.6, bank.getAccounts().get("12345678").getApr());
		assertEquals(1.4, bank.getAccounts().get("01234567").getApr());
	}

	@Test
	public void create_and_deposit_into_savings() {
		commandProcessor.process("create savings 12345678 0.6");
		commandProcessor.process("deposit 12345678 1500");
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1500, actualBalance);
	}

	@Test
	public void create_and_deposit_into_checking() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("deposit 12345678 750");
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(750, actualBalance);
	}

	@Test
	public void multiple_accounts_created_and_deposited_into() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("create savings 01234567 0.6");

		commandProcessor.process("deposit 12345678 1500");
		commandProcessor.process("deposit 01234567 750");

		Account account1 = bank.getAccountByID("12345678");
		Account account2 = bank.getAccountByID("01234567");

		double actualBalance1 = account1.getBalance();
		double actualBalance2 = account2.getBalance();

		assertEquals(1500, actualBalance1);
		assertEquals(750, actualBalance2);
	}

	@Test
	public void create_deposit_and_withdraw_from_savings() {
		commandProcessor.process("create savings 12345678 0.6");
		commandProcessor.process("deposit 12345678 1500");
		commandProcessor.process("withdraw 12345678 650");
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(850, actualBalance);
	}

	@Test
	public void create_deposit_and_withdraw_from_checking() {
		commandProcessor.process("create checking 12345678 0.6");
		commandProcessor.process("deposit 12345678 750");
		commandProcessor.process("withdraw 12345678 150");
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(600, actualBalance);
	}

	@Test
	public void create_and_withdraw_from_cd() {
		commandProcessor.process("create cd 12345678 0.6 1500");
		commandProcessor.process("withdraw 12345678 1500");
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}

}
