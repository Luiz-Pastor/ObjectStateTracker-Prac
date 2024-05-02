package Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents a registration for an event or activity. This class extends the
 * Observable class to support the Observer pattern.
 */
public class Registration extends Observable {

    private String name, affiliation;
    private RegistrationKind kind;
    private int amountPayed;
    private boolean validated;

    private static List<Observer> observers = new ArrayList<>();

    /**
     * Constructs a new Registration object with the given name and kind.
     *
     * @param name The name of the registrant.
     * @param kind The kind of registration.
     */
    public Registration(String name, RegistrationKind kind) {
        this.kind = kind;
        this.name = name;
    }

    /**
     * Adds the specified amount to the amountPayed field and updates the
     * observers.
     *
     * @param amount The amount to be paid.
     */
    public void pay(double amount) {
        this.amountPayed += amount;
        updateObserver(this);
    }

    /**
     * Returns the amount that has been paid for this registration.
     *
     * @return The amount paid.
     */
    public double getAmountPayed() {
        return this.amountPayed;
    }

    /**
     * Returns the total amount that needs to be paid for this registration.
     *
     * @return The total amount.
     */
    public double getTotalAmount() {
        return this.kind.getPrice();
    }

    /**
     * Returns the affiliation of the registrant.
     *
     * @return The affiliation.
     */
    public String getAffiliation() {
        return this.affiliation;
    }

    /**
     * Sets the affiliation of the registrant and updates the observers.
     *
     * @param aff The affiliation to be set.
     */
    public void setAffiliation(String aff) {
        this.affiliation = aff;
        updateObserver(this);
    }

    /**
     * Returns whether the registration has been validated or not.
     *
     * @return True if the registration is validated, false otherwise.
     */
    public boolean getValidated() {
        return this.validated;
    }

    /**
     * Returns a string representation of the registration.
     *
     * @return A string representation of the registration.
     */
    public String toString() {
        return "Reg. of: " + this.name;
    }

    /**
     * Sets the validation status of the registration and updates the observers.
     *
     * @param b The validation status to be set.
     */
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

    /**
     * Adds the specified observer to the list of observers.
     *
     * @param observer The observer to be added.
     */
    public static void withTracker(Observer observer) {
        if (observers.contains(observer) == false) {
            observers.add(observer);
        }
    }

    /*________________________________________________________________*/
    /**
     * Checks if this registration is equal to the specified object. Two
     * registrations are considered equal if their names are equal.
     *
     * @param o The object to compare with.
     * @return True if the registrations are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Registration == false) {
            return false;
        }
        Registration compared = (Registration) o;
        return this.name.equals(compared.name);
    }

    /**
     * Returns the hash code value for this registration. The hash code is based
     * on the name of the registration.
     *
     * @return The hash code value for this registration.
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }
}
