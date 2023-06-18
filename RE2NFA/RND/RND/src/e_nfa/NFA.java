package e_nfa;
import java.util.*;


public class NFA {
    public static final char EPSILON = 'ε';

    private int initialState;
    private Set<Integer> finalStates;
    private Map<Character, Set<Integer>> delta;

    public NFA(int initialState, int finalState, Map<Character, Set<Integer>> delta) {
        this.initialState = initialState;
        this.finalStates = new HashSet<>(Collections.singletonList(finalState));
        this.delta = delta;
    }

    public NFA(int initialState, Set<Integer> finalStates, Map<Character, Set<Integer>> delta) {
        this.initialState = initialState;
        this.finalStates = finalStates;
        this.delta = delta;
    }

    public int getInitialState() {
        return initialState;
    }

    public Set<Integer> getFinalStates() {
        return finalStates;
    }

    public Map<Character, Set<Integer>> getDelta() {
        return delta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StateSet = { ");
        for (int state : getAllStates()) {
            sb.append("q").append(state).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }\n");

        sb.append("TerminalSet = { ");
        for (char terminal : delta.keySet()) {
            if (terminal != EPSILON) {
                sb.append(terminal).append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }\n");

        sb.append("Delta Functions = {\n");
        for (int state : getAllStates()) {
            for (char terminal : delta.keySet()) {
                Set<Integer> outputStates = delta.get(terminal);
                if (outputStates.contains(state)) {
                    sb.append("\t(q").append(state).append(", ").append(terminal).append(") = { ");
                    for (int outputState : outputStates) {
                        sb.append("q").append(outputState).append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    sb.append(" }\n");
                }
            }
        }
        sb.append("}\n");

        sb.append("StartState = q").append(initialState).append("\n");

        sb.append("FinalStateSet = { ");
        for (int state : finalStates) {
            sb.append("q").append(state).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }");

        return sb.toString();
    }
    public String toFormattedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("StateSet = { ");
        for (int state : getAllStates()) {
            sb.append("q").append(String.format("%03d", state)).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }\n");

        sb.append("TerminalSet = { ");
        for (char terminal : delta.keySet()) {
            if (terminal != EPSILON) {
                sb.append(terminal).append(", ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }\n");

        sb.append("Delta Functions = {\n");
        for (int state : getAllStates()) {
            for (char terminal : delta.keySet()) {
                Set<Integer> outputStates = delta.get(terminal);
                if (outputStates.contains(state)) {
                    sb.append("\t(q").append(String.format("%03d", state)).append(", ").append(terminal).append(") = { ");
                    for (int outputState : outputStates) {
                        sb.append("q").append(String.format("%03d", outputState)).append(", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    sb.append(" }\n");
                }
            }
        }
        sb.append("}\n");

        sb.append("StartState = q").append(String.format("%03d", initialState)).append("\n");

        sb.append("FinalStateSet = { ");
        for (int state : finalStates) {
            sb.append("q").append(String.format("%03d", state)).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(" }");

        return sb.toString();
    }
    private Set<Integer> getAllStates() {
        Set<Integer> allStates = new HashSet<>();
        allStates.add(initialState);
        allStates.addAll(finalStates);
        for (Set<Integer> outputStates : delta.values()) {
            allStates.addAll(outputStates);
        }
        return allStates;
    }
}
