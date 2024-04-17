package Tests;

import Registration.*;
import static Registration.RegistrationState.*;
import static Registration.RegistrationKind.*;
import ObjectStateTracker.*;

public class TesterStateChanges {

    protected ObjectStateTracker<Registration, RegistrationState> regState;
    protected Registration annSmith, johnDoe, lisaMartin;

    public static void main(String[] args) {
        TesterStateChanges tsc = new TesterStateChanges();
        tsc.createRegistrations();
        System.out.println(tsc.regState);
        tsc.changeRegistrations();
        System.out.println(tsc.regState);
    }

    protected void changeRegistrations() {
        /*this.annSmith.setAffiliation("University of Miskatonic"); // now it is filled
        this.johnDoe.pay(STUDENT.getPrice()); // becomes payed
        this.regState.updateStates();*/
    }

    protected void createRegistrations() {
        this.regState = new ObjectStateTracker<>(RegistrationState.values());        
        regState.withState(PAYED, r -> r.getAmountPayed() == r.getTotalAmount() && !r.getValidated())
                .withState(STARTED, r -> r.getAffiliation() == null && !r.getValidated())
                .withState(FILLED, r -> r.getAffiliation() != null && !r.getValidated())
                .withState(VALIDATED, r -> r.getAmountPayed() == 0 && r.getValidated())
                .withState(FINISHED, r -> r.getAmountPayed() == r.getTotalAmount() && r.getValidated())
                .elseState(REJECTED);
        this.annSmith = new Registration("Ann Smith", FULL);
        this.johnDoe = new Registration("John Doe", STUDENT);
        this.lisaMartin = new Registration("Lisa Martin", MEMBER);
        
        System.out.println(this.regState);
        System.exit(1);
        //this.regState.addObjects(annSmith, johnDoe, lisaMartin);
    }
}
