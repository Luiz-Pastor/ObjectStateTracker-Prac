public enum RegistrationKind {
	FULL (1100), MEMBER (900), STUDENT (450);

	private int price;

	RegistrationKind(int price) {
		this.price = price;
	}

	public double getPrice() {
		return this.price;
	}	
}
