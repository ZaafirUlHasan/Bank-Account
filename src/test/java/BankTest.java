import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankTest {
	Bank bank;

	@BeforeEach
	public void setUp() {
		bank = new Bank();

	}

	@Test
	public void bank_has_no_accounts_upon_creation() {
		Bank bank = new Bank();

		assertTrue(bank.getAccounts().isEmpty());
	}

	@Test
	public void when_account_is_added_to_bank_it_has_1_account() {
		bank.addAccount("00000001", 2.5, true);

		assertEquals(1, bank.getAccounts().size());

	}

	@Test
	public void when_two_accounts_are_added_to_bank_it_has_2_accounts() {
		bank.addAccount("00000001", 2.5, true);
		bank.addAccount("00000002", 1.5, 55.6);

		assertEquals(2, bank.getAccounts().size());
	}

	@Test
	public void correct_account_is_retrieved() {
		bank.addAccount("00000001", 2.5, true);
		bank.addAccount("00000002", 3.6, 544050.5);
		Account retrievedAccount = bank.getAccountByID("00000002");
		String actualId = retrievedAccount.getId();

		assertEquals("00000002", actualId);
	}

	@Test
	public void depositing_sends_money_to_correct_account() {
		bank.addAccount("00000001", 2.5, true);
		bank.addAccount("00000002", 2.5, 55.6);
		bank.deposit("00000002", 505);

		assertEquals(560.6, bank.getAccountByID("00000002").getBalance());
	}

	@Test
	public void withdrawing_subtracts_money_from_correct_account() {
		bank.addAccount("00000001", 2.5, true);
		bank.addAccount("00000002", 2.5, false);
		bank.deposit("00000002", 55.6);
		bank.withdraw("00000002", 10.6);

		assertEquals(45, bank.getAccountByID("00000002").getBalance());
	}

	@Test
	public void depositing_twice_works_as_expected() {
		bank.addAccount("00000001", 2.5, false);
		bank.deposit("00000001", 505.3);
		bank.deposit("00000001", 494.7);

		assertEquals(1000, bank.getAccountByID("00000001").getBalance());

	}

	@Test
	public void withdrawing_twice_works_as_expected() {
		bank.addAccount("00000001", 2.5, false);
		bank.deposit("00000001", 2000);
		bank.withdraw("00000001", 494.7);
		bank.withdraw("00000001", 505.3);

		assertEquals(1000, bank.getAccountByID("00000001").getBalance());
	}

}
