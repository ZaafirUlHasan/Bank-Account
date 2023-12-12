package banking;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CDTest {
	@Test
	public void cd_account_is_created_with_specified_balance() {
		Account cdAccount = new CD("00000001", 2.5, 3506);
		double actual = cdAccount.getBalance();
		assertEquals(3506, actual);

	}
}
