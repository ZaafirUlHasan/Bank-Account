package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountTest {

	Account checkingAccount;

	@BeforeEach
	public void setUp() {
		checkingAccount = new Checking("00000001", 2.5);
	}

	@Test
	public void account_created_with_0_starting_balance() {
		double actualBalance = checkingAccount.getBalance();
		assertEquals(0, actualBalance);
	}

	@Test
	public void savings_account_created_with_0_starting_balance() {
		Account savingsAccount;
		savingsAccount = new Savings("00000001", 2.5);
		double actualBalance = savingsAccount.getBalance();

		assertEquals(0, actualBalance);
	}

	@Test
	public void account_created_with_specified_apr() {
		double actualApr = checkingAccount.getApr();
		assertEquals(2.5, actualApr);
	}

	@Test
	public void depositing_increases_balance() {
		checkingAccount.deposit(1003);
		double actualBalance = checkingAccount.getBalance();
		assertEquals(1003, actualBalance);
	}

	@Test
	public void withdrawing_decreases_balance() {
		checkingAccount.deposit(1003);
		checkingAccount.withdraw(503);
		double actualBalance = checkingAccount.getBalance();
		assertEquals(500, actualBalance);
	}

	@Test
	public void withdrawing_does_not_decrease_balance_below_0() {
		checkingAccount.deposit(350);
		checkingAccount.withdraw(2003);
		double actualBalance = checkingAccount.getBalance();
		assertEquals(0, actualBalance);
	}

	@Test
	public void depositing_twice_works_as_expected() {
		checkingAccount.deposit(1003.54);
		checkingAccount.deposit(997.46);
		double actualBalance = checkingAccount.getBalance();
		assertEquals(2001, actualBalance);
	}

	@Test
	public void withdrawing_twice_works_as_expected() {
		checkingAccount.deposit(3001);
		checkingAccount.withdraw(1003.54);
		checkingAccount.withdraw(997.46);
		double actualBalance = checkingAccount.getBalance();
		assertEquals(1000, actualBalance);
	}

}
