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
    public Process(S ...values) {
        /* Add the states to the set */
        this.states.addAll(Arrays.asList(values));
        
        /* Init all the states value */
        for (S currentState : values) {
            this.statesBegin.put(currentState, 0);
            this.statesFinal.put(currentState, 0);
        }
        
        for (S stateToSave : values) {
            Map<S, Integer> connections = new HashMap<>();
            
            for (S stateToAdd : values) {
                if (stateToSave != stateToAdd)
                    connections.put(stateToAdd, 0);
            }
            
            this.statesConnection.put(stateToSave, connections);
        }
    }
    
    
    public void add(Trajectory<S> trayectory) {
        
    }
    
    @Override
    public String toString() {
        String buffer = "";
        
        for (S printedState : this.states) {
            buffer += printedState + "():\n";
            
            
            for (Map.Entry<S, Integer> connections : this.statesConnection.get(printedState).entrySet()) {
                buffer += "\t# " + connections + " #\n";
            }
        }
        return buffer;
    }
}
