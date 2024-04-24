package ObjectStateTracker;

import java.util.*;
import java.time.*;

public class Trajectory<S> {
    private final Map<S, LocalDateTime> states;
    
    public Trajectory() {
        this.states = new LinkedHashMap<>();
    }
    
    public Trajectory(S firstState) {
        this.states = new LinkedHashMap<>();
    }
    
    /*____________________________________________________________________*/
    
    public Map<S, LocalDateTime> getStates() {
        return this.states;
    }
    
    /*____________________________________________________________________*/
        
    public void add(S state) {
        this.states.put(state, LocalDateTime.now());
    }
    
    public S last() {
        S lastState = null;
        
        for (S currentState : this.states.keySet())
            lastState = currentState;
        return lastState;
    }
    
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
            if (first == true)
                first = false;
            else
                buffer += ", ";
            
            /* Check the type of msg to print */
            if (beforeState == null)
                buffer += "(in: " + currentState +
                        " at: " + currentTime + ")";
            else
                buffer += "(from: " + beforeState + " to " + currentState +
                        " at " + currentTime + ")";
            
            /* Iterate the state */
            beforeState = currentState;
        }
        
        buffer += "]";
        return buffer;
    }
}
