package ObjectStateTracker;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

/*                       <Object , State> */
public class ObjectStateTracker<O, S> {

    /* Object list, with the object and the list of states with his time */
    private final Map<O, LinkedHashMap<S, LocalDateTime>> objects = new LinkedHashMap<>();

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
        for (O currentObject : objects) {
            /* Create a linked hash map, to store the init state and the time */
            LinkedHashMap<S, LocalDateTime> tempMap = new LinkedHashMap<>();
            tempMap.put(this.getCurrentState(currentObject), LocalDateTime.now());

            /* Set the value of the current object */
            this.objects.put(currentObject, tempMap);
        }
    }

    /*____________________________________________________________________*/
    public void updateStates() {
        for (O currentObject : this.objects.keySet()) {

            /* Get the new state */
            S newState = this.getCurrentState(currentObject);

            /* Get the previous list of states */
            LinkedHashMap<S, LocalDateTime> tempMap = this.objects.get(currentObject);

            /* Check the last state, if it is the same than the new state, continue with the next element */
            S last = null;
            for (Map.Entry<S, LocalDateTime> it : tempMap.entrySet()) {
                last = it.getKey();
            }
            if (last == null || last == newState) {
                continue;
            }

            /* If it is a new state, add the state to the object trajectory */
            tempMap.put(newState, LocalDateTime.now());
            this.objects.put(currentObject, tempMap);
        }
    }

    @Override
    public String toString() {
        int stateCount = this.states.size();
        String buffer;
        boolean flag;

        buffer = "{";
        /* Print the info of each state */
        for (S currentState : this.states) {
            flag = false;
            buffer += currentState + "=[";

            /* Iterate the users list */
            for (Map.Entry<O, LinkedHashMap<S, LocalDateTime>> currentEntry : this.objects.entrySet()) {
                O currentObject = currentEntry.getKey();
                LinkedHashMap<S, LocalDateTime> currentTrajectory = currentEntry.getValue();

                /* Get his last state */
                S lastObjectState = null;
                for (S objectState : currentTrajectory.keySet()) {
                    lastObjectState = objectState;
                }

                /* If it is the same state, it is saved */
                if (lastObjectState == currentState) {
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

    public String trajectory(O o) {
        Map.Entry<O, LinkedHashMap<S, LocalDateTime>> searchEntry = null;
        for (Map.Entry<O, LinkedHashMap<S, LocalDateTime>> currentEntry : this.objects.entrySet()) {
            if (currentEntry.getKey().equals(o)) {
                searchEntry = currentEntry;
                break;
            }
        }

        if (searchEntry == null) {
            return null;
        }

        String buffer = "[";
        S beforeState = null;
        int index = 0, max = searchEntry.getValue().entrySet().size();
        for (Map.Entry<S, LocalDateTime> currentData : searchEntry.getValue().entrySet()) {
            /* Get the entry data */
            S currentState = currentData.getKey();
            LocalDateTime time = currentData.getValue();

            /* Check what msg to print */
            if (beforeState == null) {
                buffer += "(in: " + currentState + " at: " + time + ")";
            } else {
                buffer += "(from: " + beforeState + " to " + currentState + " at: " + time + ")";
            }

            /* Check if there are going to be more msgs */
            index++;
            if (index < max) {
                buffer += ", ";
            }

            /* Update the last state */
            beforeState = currentState;
        }

        buffer += "]";
        return buffer;
    }
}
