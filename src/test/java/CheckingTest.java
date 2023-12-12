import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CheckingTest {

	Checking checking;

	@BeforeEach
	public void setUp() {
		checking = new Checking("00000001", 1.2);
	}

	@Test
	public void checking_account_is_created_with_correct_apr() {
		double actualApr = checking.getApr();

		assertEquals(1.2, actualApr);
	}

	@Test
	public void checking_account_works_as_expected() {
		checking.deposit(500);
		checking.withdraw(250);

		assertEquals(250, checking.getBalance());
	}

}
