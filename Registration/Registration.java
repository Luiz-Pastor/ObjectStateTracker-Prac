package Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Registration extends Observable{
	private String name, affiliation;
	private RegistrationKind kind;
	private int amountPayed;
	private boolean validated;
        
        private static List<Observer> observers = new ArrayList<>();
	
	public Registration(String name, RegistrationKind kind) {
		this.kind = kind;
		this.name = name;
	}
	
	public void pay (double amount) {		
		this.amountPayed+=amount;
                updateObserver(this);
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
                updateObserver(this);
	}

	public boolean getValidated() {
		return this.validated;
	}
	
	public String toString() {
		return "Reg. of: "+this.name;
	}

	public void setValidated(boolean b) {
		this.validated = b;
                updateObserver(this);
	}
        
        /*________________________________________________________________*/
        
        private static void updateObserver(Observable o) {
            for (Observer current : observers) {
                current.update(o, null);
            }
        }
        
        public static void withTracker(Observer observer) {
            if (observers.contains(observer) == false)
                observers.add(observer);
        }
        
        /*________________________________________________________________*/
        
        @Override
        public boolean equals(Object o) {
            if (o instanceof Registration == false) return false;
            Registration compared = (Registration)o;
            return this.name.equals(compared.name);
        }
        
        @Override
        public int hashCode() {
            return this.name.hashCode();
        }
}
