package ObjectStateTracker;

import java.util.*;
import java.util.function.Predicate;

/*                       <Object , State> */
public class ObjectStateTracker<O, S> {

    /* Object list, with the object and the list of states with his time */
    private final Map<O, Trajectory<S>> objects = new LinkedHashMap<>();

    /* State set, state-lambda map and default state*/
    private final Set<S> states = new TreeSet<>();
    private final Map<S, Predicate<O>> asignedStates = new LinkedHashMap<>();
    private S defaultState;

    public ObjectStateTracker(S... states) {
        this.states.addAll(Arrays.asList(states));
        this.defaultState = null;
    }

    /*____________________________________________________________________*/
    public ObjectStateTracker<O, S> withState(S state, Predicate<O> function) throws IllegalStateException {
        if (this.states.contains(state) == false) {
            throw new IllegalStateException();
        }
        this.asignedStates.put(state, function);
        return this;
    }

    public ObjectStateTracker<O, S> elseState(S state) throws IllegalStateException {
        if (this.states.contains(state) == false) {
            throw new IllegalStateException();
        }

        this.defaultState = state;
        return this;
    }

    private S getCurrentState(O object) {
        for (S state : this.asignedStates.keySet()) {
            if (this.asignedStates.get(state).test(object) == true) {
                return state;
            }
        }
        return this.defaultState;
    }

    public void addObjects(O... objects) {
        
        /* Adding an entry on the list for each new object, setting an initial state */
        for (O currentObject : objects) {
            this.objects.put(currentObject, new Trajectory<>(this.getCurrentState(currentObject)));
        }
    }

    /*____________________________________________________________________*/
    public void updateStates() {
        for (O currentObject : this.objects.keySet()) {
            /* Get the new state */
            S newState = this.getCurrentState(currentObject);
            
            /* Get the previous list of states */
            Trajectory<S> objectTrajectory = this.objects.get(currentObject);
            
            /* Check the last state, if it is the same than the new state, continue with the next element */
            S last = objectTrajectory.last();
            if (last == null || last == newState) {
                continue;
            }

            /* If it is a new state, add the state to the object trajectory */
            objectTrajectory.add(newState);
            this.objects.put(currentObject, objectTrajectory); /* TODO: se puede quitar? modificando la referencia en si */
        }
    }

    @Override
    public String toString() {
        int stateCount = this.states.size();
        String buffer;
        boolean flag;

        buffer = "{";
        /* Print the info of each state */
        for (S savingState : this.states) {
            flag = false;
            buffer += savingState + "=[";

            /* Iterate the users list */
            for (Map.Entry<O, Trajectory<S>> currentEntry : this.objects.entrySet()) {
                O currentObject = currentEntry.getKey();
                Trajectory<S> currentTrajectory = currentEntry.getValue();

                /* If the last state it is the same that the one that is being printing, it is saved */
                S lastState = currentTrajectory.last();
                if (lastState == savingState) {
                    /* If there has been an element before, a comma is put */
                    if (flag == false) {
                        flag = true;
                    } else {
                        buffer += ", ";
                    }

                    /* The object is added to the string */
                    buffer += currentObject;
                }
            }
            buffer += "]";

            /* If there is another element after, a comma is added */
            stateCount--;
            if (stateCount > 0) {
                buffer += ", ";
            }
        }
        buffer += "}";
        return buffer;
    }

    public Trajectory<S> trajectory(O o) {
        Map.Entry<O, Trajectory<S>> searchEntry = null;
        
        /* Search the entry of the specified object */
        for (Map.Entry<O, Trajectory<S>> currentEntry : this.objects.entrySet()) {
            if (currentEntry.getKey().equals(o)) {
                searchEntry = currentEntry;
                break;
            }
        }

        /* Returning the trajectory of the object, or null if it is not saved */
        return (searchEntry == null) ? null : searchEntry.getValue();
    }
}
