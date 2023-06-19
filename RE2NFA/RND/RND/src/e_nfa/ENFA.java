package e_nfa;

import java.util.*;

public class ENFA {

    private Set<String> stateSet; // 상태 집합을 저장하는 변수
    public Set<Character> terminalSet; // 터미널 집합을 저장하는 변수
    private Map<String, Map<Character, Set<String>>> deltaFunctions; // 델타 함수를 저장하는 변수
    private String startState; // 시작 상태를 저장하는 변수
    private Set<String> finalStateSet; // 최종 상태 집합을 저장하는 변수
    public static final char EPSILON = 'ε'; // ε 심볼을 나타내는 상수

    public ENFA() {
        stateSet = new HashSet<>();
        terminalSet = new HashSet<>();
        deltaFunctions = new HashMap<>();
        finalStateSet = new HashSet<>();
    }

    // ENFA 속성들의 Setter와 Getter 메서드

    // 상태를 추가합니다.
    public void createState(String state) {
        stateSet.add(state);
    }

    // 터미널을 추가합니다.
    public void createTerminal(char terminal) {
        terminalSet.add(terminal);
    }

    // 터미널 집합을 설정합니다.
    public void setTerminalSet(Set terminal) {
        terminalSet.addAll(terminal);
    }

    // 델타 함수를 추가합니다.
    public void addDeltaFunction(String state, char terminal, Set<String> outputStates) {
        deltaFunctions.putIfAbsent(state, new HashMap<>());
        deltaFunctions.get(state).put(terminal, outputStates);
    }

    // 시작 상태를 설정합니다.
    public void setStartState(String startState) {
        this.startState = startState;
    }

    // 최종 상태를 추가합니다.
    public void addFinalState(String finalState) {
        finalStateSet.add(finalState);
    }

    // 상태 집합을 반환합니다.
    public Set<String> getStateSet() {
        return stateSet;
    }

    // 터미널 집합을 반환합니다.
    public Set<Character> getTerminalSet() {
        return terminalSet;
    }

    // 델타 함수를 반환합니다.
    public Map<String, Map<Character, Set<String>>> getDeltaFunctions() {
        return deltaFunctions;
    }

    // 시작 상태를 반환합니다.
    public String getStartState() {
        return startState;
    }

    // 최종 상태 집합을 반환합니다.
    public Set<String> getFinalStateSet() {
        return finalStateSet;
    }
}
