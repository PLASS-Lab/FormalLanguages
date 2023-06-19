package e_nfa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ENFACreator {
    private static int stateCount = 0;

    public static ENFA convertToENFA(String regex) {
        Stack<ENFA> enfaStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();
        Set<Character> terminalSet = new HashSet<>();

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i);

            if (isOperand(c)) {
                ENFA enfa = new ENFA();

                String startState = "q" + String.format("%03d", stateCount++);;
                String finalState = "q" + String.format("%03d", stateCount++);;

                enfa.createState(startState);
                enfa.createState(finalState);
                enfa.setStartState(startState);
                enfa.addFinalState(finalState);

                enfa.createTerminal(c);
                enfa.addDeltaFunction(startState, c, new HashSet<>(Collections.singletonList(finalState)));

                enfaStack.push(enfa);
                enfa.createTerminal(c);
                terminalSet.add(c);

            } else if (isOperator(c)) {
                while (!operatorStack.empty() && getPrecedence(c) <= getPrecedence(operatorStack.peek())) {
                    processOperator(enfaStack, operatorStack);
                }
                operatorStack.push(c);

            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.empty() && operatorStack.peek() != '(') {
                    processOperator(enfaStack, operatorStack);
                }
                operatorStack.pop();
            }
        }

        while (!operatorStack.empty()) {
            processOperator(enfaStack, operatorStack);
        }

        enfaStack.peek().setTerminalSet(terminalSet);

        return enfaStack.pop();
    }





    private static void processOperator(Stack<ENFA> enfaStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();

        if (operator == '*') {
            ENFA enfa = enfaStack.pop();
            ENFA closureENFA = applyClosure(enfa);
            enfaStack.push(closureENFA);
        } else {
            ENFA enfa2 = enfaStack.pop();
            ENFA enfa1 = enfaStack.pop();

            ENFA concatenatedENFA = applyConcatenation(enfa1, enfa2);
            enfaStack.push(concatenatedENFA);

            if (operator == '+') {
                ENFA unionENFA = applyUnion(enfa1, enfa2);
                enfaStack.push(unionENFA);
            }
        }
    }


    private static ENFA applyClosure(ENFA enfa) {
        String startState =  "q" + String.format("%03d", stateCount++);
        String finalState =  "q" + String.format("%03d", stateCount++);

        enfa.createState(startState);
        enfa.createState(finalState);

        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));
        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa.getStartState())));

        for (String finalState1 : enfa.getFinalStateSet()) {
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa.getStartState())));
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));
        }

        enfa.setStartState(startState);
        enfa.addFinalState(finalState);

        return enfa;
    }

    private static ENFA applyConcatenation(ENFA enfa1, ENFA enfa2) {
        ENFA enfa = new ENFA();

        // Create a new start state and final state for the concatenated ENFA
        String startState =  "q" + String.format("%03d", stateCount++);
        String finalState =  "q" + String.format("%03d", stateCount++);

        // Add the new states to the state set of the concatenated ENFA
        enfa.createState(startState);
        enfa.createState(finalState);

        // Add all states, delta functions, and final states from enfa1 to the concatenated ENFA
        enfa.getStateSet().addAll(enfa1.getStateSet());
        enfa.getDeltaFunctions().putAll(enfa1.getDeltaFunctions());
        enfa.getFinalStateSet().addAll(enfa1.getFinalStateSet());
        // Copy the TerminalSet from enfa1 to the concatenated ENFA
        enfa.getTerminalSet().addAll(enfa1.getTerminalSet());

        // Update the delta function for each final state in enfa1 to transition to the start state of enfa2 with an epsilon transition
        for (String finalState1 : enfa1.getFinalStateSet()) {
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa2.getStartState())));
        }

        // Add all states, delta functions, and final states from enfa2 to the concatenated ENFA,
        // while also renaming the states to avoid conflicts with enfa1
        for (String state : enfa2.getStateSet()) {
            String newState =  "q" + String.format("%03d", stateCount++);
            enfa.createState(newState);
            enfa.getStateSet().add(newState);

            // Rename the delta function keys
            if (enfa2.getDeltaFunctions().containsKey(state)) {
                Map<Character, Set<String>> delta = enfa2.getDeltaFunctions().remove(state);
                enfa.getDeltaFunctions().put(newState, delta);
                enfa.getTerminalSet().addAll(enfa2.getTerminalSet());

            }

            // Rename the final states
            if (enfa2.getFinalStateSet().contains(state)) {
                enfa.getFinalStateSet().add(newState);
                enfa.getFinalStateSet().remove(state);
            }
        }

        // Update the delta functions of enfa2 to reflect the renamed states in the concatenated ENFA
        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa2.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();
            Map<Character, Set<String>> delta = entry.getValue();

            Map<Character, Set<String>> updatedDelta = new HashMap<>();
            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();
                Set<String> outputStates = deltaEntry.getValue();

                Set<String> updatedStates = new HashSet<>();
                for (String outputState : outputStates) {
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState : "q" + String.format("%03d", stateCount++);;
                    updatedStates.add(updatedState);
                }

                updatedDelta.put(terminal, updatedStates);
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);
        }

        // Add epsilon transition from the final state of enfa1 to the start state of enfa2
        enfa.addDeltaFunction(enfa1.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa2.getStartState())));

        // Set the start state and final state of the concatenated ENFA
        enfa.setStartState(enfa1.getStartState());
        enfa.getFinalStateSet().clear();
        enfa.getFinalStateSet().add(finalState);

        return enfa;
    }





    private static ENFA applyUnion(ENFA enfa1, ENFA enfa2) {
        ENFA enfa = new ENFA();

        // Create a new start state and final state for the unioned ENFA
        String startState =  "q" + String.format("%03d", stateCount++);
        String finalState =  "q" + String.format("%03d", stateCount++);

        // Add the new states to the state set of the unioned ENFA
        enfa.createState(startState);
        enfa.createState(finalState);
        // Copy the TerminalSet from enfa1 to the unioned ENFA
        enfa.getTerminalSet().addAll(enfa1.getTerminalSet());
        // Add terminals from enfa2 to the TerminalSet of the unioned ENFA
        enfa.getTerminalSet().addAll(enfa2.getTerminalSet());

        // Add all states, delta functions, and final states from enfa1 to the unioned ENFA,
        // while also renaming the states to avoid conflicts with enfa2
        for (String state : enfa1.getStateSet()) {
            String newState =  "q" + String.format("%03d", stateCount++);
            enfa.createState(newState);
            enfa.getStateSet().add(newState);

            // Rename the delta function keys
            if (enfa1.getDeltaFunctions().containsKey(state)) {
                Map<Character, Set<String>> delta = enfa1.getDeltaFunctions().remove(state);
                enfa.getDeltaFunctions().put(newState, delta);
            }

            // Rename the final states
            if (enfa1.getFinalStateSet().contains(state)) {
                enfa.getFinalStateSet().add(newState);
                enfa.getFinalStateSet().remove(state);
            }
        }

        // Update the delta functions of enfa1 to reflect the renamed states in the unioned ENFA
        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa1.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();
            Map<Character, Set<String>> delta = entry.getValue();

            Map<Character, Set<String>> updatedDelta = new HashMap<>();
            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();
                Set<String> outputStates = deltaEntry.getValue();

                Set<String> updatedStates = new HashSet<>();
                for (String outputState : outputStates) {
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState :  "q" + String.format("%03d", stateCount++);;
                    updatedStates.add(updatedState);
                }

                updatedDelta.put(terminal, updatedStates);
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);
        }

        // Add all states, delta functions, and final states from enfa2 to the unioned ENFA
        enfa.getStateSet().addAll(enfa2.getStateSet());
        enfa.getDeltaFunctions().putAll(enfa2.getDeltaFunctions());
        enfa.getFinalStateSet().addAll(enfa2.getFinalStateSet());

        // Create an epsilon transition from the new start state to the start states of enfa1 and enfa2
        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Arrays.asList(enfa1.getStartState(), enfa2.getStartState())));

        // Update the delta functions of enfa2 to reflect the renamed states in the unioned ENFA
        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa2.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();
            Map<Character, Set<String>> delta = entry.getValue();

            Map<Character, Set<String>> updatedDelta = new HashMap<>();
            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();
                Set<String> outputStates = deltaEntry.getValue();

                Set<String> updatedStates = new HashSet<>();
                for (String outputState : outputStates) {
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState : "q" + String.format("%03d", stateCount++);;
                    updatedStates.add(updatedState);
                }

                updatedDelta.put(terminal, updatedStates);
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);
        }

        // Add epsilon transitions from the final states of enfa1 and enfa2 to the new final state
        enfa.addDeltaFunction(enfa1.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));
        enfa.addDeltaFunction(enfa2.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));

        // Set the start state and final state of the unioned ENFA
        enfa.setStartState(startState);
        enfa.addFinalState(finalState);

        enfa.getTerminalSet().addAll(enfa2.getTerminalSet());
        return enfa;
    }

    public static void saveENFAToFile(ENFA enfa) {
        try {
            FileWriter writer = new FileWriter("Epsilon_nfa_output2.txt");

            writer.write("StateSet = { " + String.join(", ", enfa.getStateSet()) + " }\n");
            writer.write("TerminalSet = { " + enfa.getTerminalSet().toString().replaceAll("[\\[\\]]", "") + " }\n");
            writer.write("DeltaFunctions = {\n");
            for (String state : enfa.getStateSet()) {
                Map<Character, Set<String>> transitions = enfa.getDeltaFunctions().getOrDefault(state, new HashMap<>());
                for (Map.Entry<Character, Set<String>> entry : transitions.entrySet()) {
                    char terminal = entry.getKey();
                    Set<String> outputStates = entry.getValue();
                    writer.write("(" + state + ", " + terminal + ") = { " + String.join(", ", outputStates) + " }\n");
                }
            }
            writer.write("}\n");
            writer.write("StartState = " + enfa.getStartState() + "\n");
            writer.write("FinalStateSet = { " + String.join(", ", enfa.getFinalStateSet()) + " }\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '*' || c == '.';
    }

    private static int getPrecedence(char c) {
        switch (c) {
            case '*':
                return 3;
            case '.':
                return 2;
            case '+':
                return 1;
            default:
                return 0;
        }
    }
}
