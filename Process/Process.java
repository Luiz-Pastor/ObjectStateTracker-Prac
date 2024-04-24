package Process;

import java.util.*;

public class Process<S> {
    
    private Map<S, List<Integer>> states = new TreeMap<>();
    
    public Process(S ...values) {
        for (S currentState : values) {
            List<Integer> dataList = new ArrayList<>();
            dataList.add(0);
            dataList.add(0);
            this.states.put(currentState, dataList);
        }
    }
    
    
    public void add(String trayectory) {
        
    }
    
}
