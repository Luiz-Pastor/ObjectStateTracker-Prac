package ObjectStateTracker;

import java.util.*;
import java.util.function.Predicate;

/*                       <Object , State> */
public class ObjectStateTracker<O, S> {
    
    //List<S> states = new ArrayList<>();
    private Map<S, Predicate<O>> states = new TreeMap<>();
    private S defaultState;
    
    public ObjectStateTracker(S ...states) {
        for (S current : states)
            this.states.put(current, null);
        this.defaultState = null;
    }
    
    /*____________________________________________________________________*/
    
    public Map<S, Predicate<O>> getStates() {
        return this.states;
    }
    
    public S getDefaultState() {
        return this.defaultState;
    }
    
    /*____________________________________________________________________*/
    
    public ObjectStateTracker<O,S> withState(S state, Predicate<O> function) {
        this.states.put(state, function);
        return this;
    }
    
    public ObjectStateTracker<O, S> elseState(S state) {
        this.defaultState = state;
        return this;
    }
    
    @Override
    public String toString() {
        int index;
        String buffer;
        
        buffer = "{ ";
        index = 0;
        for (S current : this.states.keySet()) {
            buffer += current;
            index++;
            
            if (index != this.states.size())
                buffer += ", ";
        }
        buffer += " }";
        return buffer;
    }
    
}
