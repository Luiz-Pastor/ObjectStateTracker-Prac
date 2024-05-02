package ObjectStateTracker;

import java.util.*;
import java.util.function.Predicate;

/**
 * A class that tracks the state of objects and their trajectories.
 *
 * @param <O> the type of the observable objects being tracked
 * @param <S> the type of the states associated with the objects
 *
 * @author Gonzalo Jiménez and Luis Pastor
 */
public class ObjectStateTracker<O extends Observable, S> implements Iterable<O>, Observer {

    /* Object list, with the object and the list of states with his time */
    private final Map<O, Trajectory<S>> objects = new LinkedHashMap<>();

    /* State set, state-lambda map and default state*/
    private final Set<S> states = new TreeSet<>();
    private final Map<S, Predicate<O>> asignedStates = new LinkedHashMap<>();
    private S defaultState;

    /**
     * Constructs an ObjectStateTracker with the specified states.
     *
     * @param states the states to be tracked
     */
    public ObjectStateTracker(S... states) {
        this.states.addAll(Arrays.asList(states));
        this.defaultState = null;
    }

    /*____________________________________________________________________*/
    /**
     * Adds a state and its associated function to the ObjectStateTracker.
     *
     * @param state the state to be added
     * @param function the function that determines if an object is in the state
     * @return the ObjectStateTracker instance
     * @throws IllegalStateException if the state does not exist in the initial
     * list of states
     */
    public ObjectStateTracker<O, S> withState(S state, Predicate<O> function) throws IllegalStateException {
        /* If the state isn't exist on the started list, throw an error */
        if (this.states.contains(state) == false) {
            throw new IllegalStateException();
        }

        /* Add the state and his action */
        this.asignedStates.put(state, function);
        return this;
    }

    /**
     * Sets the default state for objects that do not match any other state.
     *
     * @param state the default state to be set
     * @return the ObjectStateTracker instance
     * @throws IllegalStateException if the state does not exist in the initial
     * list of states
     */
    public ObjectStateTracker<O, S> elseState(S state) throws IllegalStateException {
        /* If the state isn't exist on the started list, throw an error */
        if (this.states.contains(state) == false) {
            throw new IllegalStateException();
        }

        /* Set the state as default */
        this.defaultState = state;
        return this;
    }

    /**
     * Gets the current state of the specified object.
     *
     * @param object the object to get the state for
     * @return the current state of the object
     */
    private S getCurrentState(O object) {
        for (S state : this.asignedStates.keySet()) {
            if (this.asignedStates.get(state).test(object) == true) {
                return state;
            }
        }
        return this.defaultState;
    }

    /**
     * Adds objects to be tracked by the ObjectStateTracker.
     *
     * @param objects the objects to be added
     */
    public void addObjects(O... objects) {
        /* Adding an entry on the list for each new object, setting an initial state */
        for (O currentObject : objects) {
            this.objects.put(currentObject, new Trajectory<>(this.getCurrentState(currentObject)));
        }
    }

    /**
     * Updates the states of the tracked objects.
     */
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

            /* If it is a new state, add the state to the object trajectory, adding it to the reference getted */
            objectTrajectory.add(newState);
            //this.objects.put(currentObject, objectTrajectory);
        }
    }

    /**
     * This method is called when an observed object notifies a change. It
     * updates the states of the object.
     *
     * @param object the observed object that triggered the update
     * @param arg an argument passed by the observed object (optional)
     */
    @Override
    public void update(Observable object, Object arg) {
        this.updateStates();
    }

    /*____________________________________________________________________*/
    /**
     * Returns an iterator over the objects in this ObjectStateTracker.
     *
     * @return an iterator over the objects in this ObjectStateTracker
     */
    @Override
    public Iterator<O> iterator() {
        return this.objects.keySet().iterator();
    }

    /**
     * Returns a string representation of the ObjectStateTracker. The string
     * representation contains the information of each state and the objects
     * associated with each state.
     *
     * @return a string representation of the ObjectStateTracker
     */
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

    /**
     * Gets the trajectory of the specified object.
     *
     * @param o the object to get the trajectory for
     * @return the trajectory of the object, or null if it is not saved
     */
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
