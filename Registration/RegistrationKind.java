package Registration;

/**
 * The RegistrationKind enum represents the different types of registration for
 * an event. Each registration kind has a corresponding price.
 */
public enum RegistrationKind {
    FULL(1100), MEMBER(900), STUDENT(450);

    private int price;

    /**
     * Constructs a RegistrationKind with the specified price.
     *
     * @param price the price of the registration kind
     */
    RegistrationKind(int price) {
        this.price = price;
    }

    /**
     * Returns the price of the registration kind.
     *
     * @return the price of the registration kind
     */
    public double getPrice() {
        return this.price;
    }
}
