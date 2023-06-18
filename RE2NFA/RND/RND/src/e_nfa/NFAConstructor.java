package e_nfa;

import java.util.*;

public class NFAConstructor {
    private int stateCount;

    public NFA constructEpsilonNFA(String regex) {
        stateCount = 0;
        Stack<NFA> stack = new Stack<>();
        Stack<Character> opStack = new Stack<>();

        for (int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);
            if (isOperand(ch)) {
                NFA nfa = createBasicNFA(ch);
                stack.push(nfa);
            } else if (ch == '(') {
                opStack.push(ch);
            } else if (ch == ')') {
                while (!opStack.isEmpty() && opStack.peek() != '(') {
                    applyOperator(stack, opStack);
                }
                opStack.pop();  // Pop '('
            } else if (isOperator(ch)) {
                while (!opStack.isEmpty() && precedence(ch) <= precedence(opStack.peek())) {
                    applyOperator(stack, opStack);
                }
                opStack.push(ch);
            }
        }

        while (!opStack.isEmpty()) {
            applyOperator(stack, opStack);
        }

        if (stack.isEmpty()) {
            return null;
        } else {
            return stack.pop();
        }
    }

    private NFA createBasicNFA(char symbol) {
        int initialState = stateCount++;
        int finalState = stateCount++;

        Map<Character, Set<Integer>> delta = new HashMap<>();
        delta.put(symbol, new HashSet<>(Collections.singletonList(finalState)));

        return new NFA(initialState, finalState, delta);
    }

    private void applyOperator(Stack<NFA> stack, Stack<Character> opStack) {
        char operator = opStack.pop();
        if (operator == '*') {
            NFA nfa = stack.pop();
            NFA newNFA = createClosureNFA(nfa);
            stack.push(newNFA);
        } else if (operator == '|') {
            NFA nfa2 = stack.pop();
            NFA nfa1 = stack.pop();
            NFA newNFA = createUnionNFA(nfa1, nfa2);
            stack.push(newNFA);
        } else if (operator == '.') {
            NFA nfa2 = stack.pop();
            NFA nfa1 = stack.pop();
            NFA newNFA = createConcatenationNFA(nfa1, nfa2);
            stack.push(newNFA);
        }
    }

    private NFA createClosureNFA(NFA nfa) {
        int initialState = stateCount++;
        int finalState = stateCount++;

        // Add ε-transitions from initial state to final state and vice versa
        Map<Character, Set<Integer>> delta = new HashMap<>();
        delta.put(NFA.EPSILON, new HashSet<>(Arrays.asList(initialState, finalState)));
        delta.putAll(nfa.getDelta());

        // Add ε-transitions from final state of NFA to initial state of NFA
        for (int state : nfa.getFinalStates()) {
            delta.computeIfAbsent(NFA.EPSILON, k -> new HashSet<>()).add(initialState);
            delta.computeIfAbsent(NFA.EPSILON, k -> new HashSet<>()).add(state);
        }

        return new NFA(initialState, finalState, delta);
    }

    private NFA createUnionNFA(NFA nfa1, NFA nfa2) {
        int initialState = stateCount++;
        int finalState = stateCount++;

        // Add ε-transitions from initial state to initial states of nfa1 and nfa2
        Map<Character, Set<Integer>> delta = new HashMap<>();
        delta.put(NFA.EPSILON, new HashSet<>(Arrays.asList(nfa1.getInitialState(), nfa2.getInitialState())));
        delta.putAll(nfa1.getDelta());
        delta.putAll(nfa2.getDelta());

        // Add ε-transitions from final states of nfa1 and nfa2 to final state
        for (int state : nfa1.getFinalStates()) {
            delta.computeIfAbsent(NFA.EPSILON, k -> new HashSet<>()).add(finalState);
        }
        for (int state : nfa2.getFinalStates()) {
            delta.computeIfAbsent(NFA.EPSILON, k -> new HashSet<>()).add(finalState);
        }

        return new NFA(initialState, finalState, delta);
    }

    private NFA createConcatenationNFA(NFA nfa1, NFA nfa2) {
        // Modify delta functions of nfa1 to merge final states with initial state of nfa2
        Map<Character, Set<Integer>> delta = new HashMap<>(nfa1.getDelta());
        for (int state : nfa1.getFinalStates()) {
            delta.computeIfAbsent(NFA.EPSILON, k -> new HashSet<>()).add(nfa2.getInitialState());
        }
        delta.putAll(nfa2.getDelta());

        return new NFA(nfa1.getInitialState(), nfa2.getFinalStates(), delta);
    }

    private boolean isOperand(char ch) {
        return Character.isLetterOrDigit(ch);
    }

    private boolean isOperator(char ch) {
        return ch == '*' || ch == '|' || ch == '.';
    }

    private int precedence(char ch) {
        if (ch == '*') {
            return 3;
        } else if (ch == '.') {
            return 2;
        } else if (ch == '|') {
            return 1;
        } else {
            return 0;
        }
    }
}
