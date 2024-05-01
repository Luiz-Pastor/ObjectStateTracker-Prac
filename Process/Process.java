package Process;

import java.util.*;
import ObjectStateTracker.*;

public class Process<S> {

    private final Set<S> states = new TreeSet<>();
    private final Map<S, Integer> statesBegin = new HashMap<>();
    private final Map<S, Integer> statesFinal = new HashMap<>();
    private final Map<S, Map<S, Integer>> statesConnection = new HashMap<>();

    /*
     * -> Save states. For each states
     *      · Save the times that it is the initial state
     *      · Save the times that it is the final state
     *      · Which state is going to go, and how many times
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
