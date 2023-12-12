public class Checking extends Account {

	public Checking(String id, double apr) {
		super(id, apr, "checking");
	}

	public boolean validDeposit(double depositAmount) {
		return depositAmount <= 1000 && depositAmount >= 0;

	}

}
