import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SavingsTest {

	Savings savings;

	@BeforeEach
	public void setUp() {
		savings = new Savings("00000001", 6.5);
	}

	@Test
	public void savings_account_is_created_with_correct_apr() {
		double actualApr = savings.getApr();

		assertEquals(6.5, actualApr);
	}

	@Test
	public void savings_account_works_as_expected() {
		savings.deposit(500);
		savings.withdraw(250);

		assertEquals(250, savings.getBalance());
	}

}
