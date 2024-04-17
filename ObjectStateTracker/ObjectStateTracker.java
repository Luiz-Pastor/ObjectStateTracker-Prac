package ObjectStateTracker;

import java.util.*;
import java.util.function.Predicate;

/*                       <Object , State> */
public class ObjectStateTracker<O, S> {
    
    //List<S> states = new ArrayList<>();
    private Map<Predicate<O>, S> states = new TreeMap<>();
    private S defaultState;
    
    public ObjectStateTracker(S ...states) {
        for (S current : states)
            this.states.put(null, current);
        this.defaultState = null;
    }
    
    /*____________________________________________________________________*/
    
    public Map<Predicate<O>, S> getStates() {
        return this.states;
    }
    
    public S getDefaultState() {
        return this.defaultState;
    }
    
    /*____________________________________________________________________*/
    
    public ObjectStateTracker withState(S state, Predicate<O> function) {
        this.states.put(function, state);
        return this;
    }
    
    public ObjectStateTracker elseState(S state) {
        this.defaultState = state;
        return this;
    }
    
    @Override
    public String toString() {
        int index;
        String buffer;
        
        buffer = "{ ";
        index = 0;
        for (S current : this.states.values()) {
            buffer += current;
            index++;
            
            if (index != this.states.size())
                buffer += ", ";
        }
        buffer += " }";
        return buffer;
    }
    
}
