public class CD extends Account {
	public CD(String id, double apr, double balance) {
		super(id, apr, "cd");
		deposit(balance);
	}

}
