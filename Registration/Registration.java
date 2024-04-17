public class Registration {
	private String name, affiliation;
	private RegistrationKind kind;
	private int amountPayed;
	private boolean validated;
	
	
	public Registration(String name, RegistrationKind kind) {
		this.kind = kind;
		this.name = name;
	}
	
	public void pay (double amount) {		
		this.amountPayed+=amount;
	}

	public double getAmountPayed() {
		return this.amountPayed;
	}

	public double getTotalAmount() {
		return this.kind.getPrice();
	}

	public String getAffiliation() {
		return this.affiliation;
	}
	
	public void setAffiliation(String aff) {
		this.affiliation = aff;
	}

	public boolean getValidated() {
		return this.validated;
	}
	
	public String toString() {
		return "Reg. of: "+this.name;
	}

	public void setValidated(boolean b) {
		this.validated = b;
	}
}
