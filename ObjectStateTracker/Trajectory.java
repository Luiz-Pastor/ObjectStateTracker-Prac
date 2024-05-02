package ObjectStateTracker;

import java.util.*;
import java.time.*;

/**
 * Represents a trajectory of states.
 *
 * @param <S> the type of the states in the trajectory
 */
public class Trajectory<S> implements Iterable<S> {

    private final Map<S, LocalDateTime> states;

    /**
     * Constructs an empty trajectory.
     */
    public Trajectory() {
        this.states = new LinkedHashMap<>();
    }

    /**
     * Constructs a trajectory with the specified initial state.
     *
     * @param firstState the initial state to add to the trajectory
     */
    public Trajectory(S firstState) {
        this.states = new LinkedHashMap<>();
        this.states.put(firstState, LocalDateTime.now());
    }

    /**
     * Returns the map of states and their corresponding timestamps in the
     * trajectory.
     *
     * @return the map of states and timestamps
     */
    public Map<S, LocalDateTime> getStates() {
        return this.states;
    }

    /**
     * Returns an iterator over the elements in this trajectory.
     *
     * @return an iterator over the elements in this trajectory
     */
    @Override
    public Iterator<S> iterator() {
        return this.states.keySet().iterator();
    }

    /**
     * Adds a state to the trajectory with the current timestamp.
     *
     * @param state the state to add
     */
    public void add(S state) {
        this.states.put(state, LocalDateTime.now());
    }

    /**
     * Returns the first state in the trajectory.
     *
     * @return the first state
     */
    public S first() {
        return new ArrayList<>(this.states.keySet()).get(0);
    }

    /**
     * Returns the last state in the trajectory.
     *
     * @return the last state
     */
    public S last() {
        S lastState = null;

        for (S currentState : this.states.keySet()) {
            lastState = currentState;
        }
        return lastState;
    }

    /**
     * Returns a string representation of the trajectory. The string
     * representation consists of a list of states and their corresponding
     * timestamps. If there are multiple states, it shows the transition from
     * the previous state to the current state. If there is only one state, it
     * shows the state and its timestamp.
     *
     * @return a string representation of the trajectory
     */
    @Override
    public String toString() {
        String buffer = "[";
        S beforeState = null;
        boolean first = true;

        /* Iteration of each state saved */
        for (Map.Entry<S, LocalDateTime> currentEntry : this.states.entrySet()) {
            S currentState = currentEntry.getKey();
            LocalDateTime currentTime = currentEntry.getValue();

            /* Put a comma if it isn't the first element */
            if (first == true) {
                first = false;
            } else {
                buffer += ", ";
            }

            /* Check the type of msg to print */
            if (beforeState == null) {
                buffer += "(in: " + currentState
                        + " at: " + currentTime + ")";
            } else {
                buffer += "(from: " + beforeState + " to " + currentState
                        + " at " + currentTime + ")";
            }

            /* Iterate the state */
            beforeState = currentState;
        }

        buffer += "]";
        return buffer;
    }
}
