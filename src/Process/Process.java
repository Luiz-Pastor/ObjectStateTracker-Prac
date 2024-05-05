package Process;

import java.util.*;
import ObjectStateTracker.*;

/**
 * Represents a process that tracks states and connections between them.
 *
 * @param <S> the type of states in the process
 *
 * @author Gonzalo Jiménez and Luis Pastor
 */
public class Process<S> {

    private final Set<S> states = new TreeSet<>();
    private final Map<S, Integer> statesBegin = new HashMap<>();
    private final Map<S, Integer> statesFinal = new HashMap<>();
    private final Map<S, Map<S, Integer>> statesConnection = new HashMap<>();

    /**
     * Constructs a new Process object with the given states.
     *
     * @param values the states to be added to the process
     */
    public Process(S... values) {
        /* Add the states to the set */
        this.states.addAll(Arrays.asList(values));

        /* Init all the states value */
        for (S currentState : values) {
            this.statesBegin.put(currentState, 0);
            this.statesFinal.put(currentState, 0);
        }

        for (S stateToSave : values) {
            Map<S, Integer> connections = new TreeMap<>();

            for (S stateToAdd : values) {
                if (stateToSave != stateToAdd) {
                    connections.put(stateToAdd, 0);
                }
            }

            this.statesConnection.put(stateToSave, connections);
        }
    }

    /**
     * Adds a trajectory to the process.
     *
     * @param trajectory the trajectory to be added
     */
    public void add(Trajectory<S> trajectory) {
        Integer count;

        /* Get the first element */
        S firstState = trajectory.first();
        count = this.statesBegin.get(firstState);
        this.statesBegin.put(firstState, count + 1);

        /* Get the the last element */
        S lastState = trajectory.last();
        count = this.statesFinal.get(lastState);
        this.statesFinal.put(lastState, count + 1);

        /* Check the road of the states */
        S beforeState = null;
        for (S currentState : trajectory) {
            if (beforeState != null) {
                Map<S, Integer> nextConnectionState = this.statesConnection.get(beforeState);
                Integer roadCount = nextConnectionState.get(currentState);
                nextConnectionState.put(currentState, roadCount + 1);
            }

            beforeState = currentState;
        }
    }

    /**
     * Returns a string representation of the process.
     *
     * @return a string representation of the process
     */
    @Override
    public String toString() {
        String buffer = "";

        for (S printedState : this.states) {
            buffer += printedState
                    + "(initial " + this.statesBegin.get(printedState)
                    + " times, final " + this.statesFinal.get(printedState) + " times):\n";

            for (Map.Entry<S, Integer> connections : this.statesConnection.get(printedState).entrySet()) {
                S roadState = connections.getKey();
                Integer roadsCount = connections.getValue();
                if (roadsCount != 0) {
                    buffer += "  to state " + roadState + ": " + roadsCount + " times\n";
                }
            }
        }
        return buffer;
    }
}
