package Tests;

import Registration.*;
import static Registration.RegistrationKind.*;

public class TesterObserver extends TesterStateChanges {

    public static void main(String[] args) {
        TesterObserver tsc = new TesterObserver();
        tsc.createRegistrations();
        Registration.withTracker(tsc.regState);
        System.out.println(tsc.regState);
        tsc.changeRegistrations();
        System.out.println(tsc.regState);
    }

    @Override
    protected void changeRegistrations() {
        this.annSmith.setAffiliation("University of Miskatonic"); // now it is filled
        this.johnDoe.pay(STUDENT.getPrice());
        // becomes payed
        // regState.updateStates(); // not needed anymore
    }
}
