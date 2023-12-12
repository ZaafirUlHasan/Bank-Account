import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DepositProcessorTest {
	Bank bank;
	DepositProcessor depositProcessor;

	@BeforeEach
	public void setUp() {
		bank = new Bank();
		depositProcessor = new DepositProcessor(bank);
	}

	private String[] splitCommand(String command) {
		command = command.toLowerCase();
		return command.split(" ");
	}

	@Test
	public void deposit_into_savings() {
		bank.addAccount("12345678", 3.0, false);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 1500"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1500, actualBalance);
	}

	@Test
	public void deposit_into_checking() {
		bank.addAccount("12345678", 3.0, true);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 750"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(750, actualBalance);
	}

	@Test
	public void multiple_accounts_created_and_deposited_into() {
		bank.addAccount("12345678", 0.6, true);
		bank.addAccount("01234567", 0.6, false);

		depositProcessor.processDeposit(splitCommand("deposit 12345678 1500"));
		depositProcessor.processDeposit(splitCommand("deposit 01234567 750"));

		Account account1 = bank.getAccountByID("12345678");
		Account account2 = bank.getAccountByID("01234567");

		double actualBalance1 = account1.getBalance();
		double actualBalance2 = account2.getBalance();

		assertEquals(1500, actualBalance1);
		assertEquals(750, actualBalance2);
	}

	@Test
	public void zero_deposited_into_savings() {
		bank.addAccount("12345678", 0.6, false);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 0"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}

	@Test
	public void zero_deposited_into_checking() {
		bank.addAccount("12345678", 0.6, true);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 0"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0, actualBalance);
	}

	@Test
	public void maximum_deposited_into_savings() {
		bank.addAccount("12345678", 0.6, false);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 2500"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(2500, actualBalance);
	}

	@Test
	public void maximum_deposited_into_checking() {
		bank.addAccount("12345678", 0.6, true);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 1000"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(1000, actualBalance);
	}

	@Test
	public void decimal_amount_deposited_into_savings() {
		bank.addAccount("12345678", 0.6, false);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 0.6"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0.6, actualBalance);
	}

	@Test
	public void decimal_amount_deposited_into_checking() {
		bank.addAccount("12345678", 0.6, true);
		depositProcessor.processDeposit(splitCommand("deposit 12345678 0.1"));
		Account account = bank.getAccountByID("12345678");
		double actualBalance = account.getBalance();

		assertEquals(0.1, actualBalance);
	}

}
