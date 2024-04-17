package ObjectStateTracker;

import java.util.*;
import java.util.function.Predicate;

/*                       <Object , State> */
public class ObjectStateTracker<O, S> {
    
    private Map<O, S> objects = new LinkedHashMap<>();
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

    private S getCurrentState(O object) {
        for (S state : this.states.keySet()) {
            if (this.states.get(state).test(object) == true)
                return state;
        }
        return this.defaultState;
    }
    
    public void addObjects(O ...objects) {
        for (O currentObject : objects) {
            this.objects.put(currentObject, this.getCurrentState(currentObject));
        }
    }
    
    public void updateStates() {
        for (O currentObject : this.objects.keySet()) {
            this.objects.put(currentObject, this.getCurrentState(currentObject));
        }
    }
    
    @Override
    public String toString() {
        int stateCount = this.states.size();
        String buffer;
        boolean flag;
        
        buffer = "{";       
        /* Print the info of each state */
        for (S currentState : this.states.keySet()) {
            flag = false;
            buffer += currentState + "=[";
            
            for (O currentObject : this.objects.keySet()) {
                if (this.objects.get(currentObject) == currentState) {
                    /* If there has been an element before, a comma is put */
                    if (flag == false)
                        flag = true;
                    else
                        buffer += ", ";
                    
                    /* The object is added */
                    buffer += currentObject;
                }
            }
            buffer += "]";
            
            /* If there is another element after, a comma is added */
            stateCount--;
            if (stateCount > 0)
                buffer += ", ";
        }
        buffer += "}";
        return buffer;
    }
    
}
