package e_nfa;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ENFACreator {
    private static int stateCount = 0;

    public static ENFA convertToENFA(String regex) {
        Stack<ENFA> enfaStack = new Stack<>(); // ENFA 스택 생성
        Stack<Character> operatorStack = new Stack<>(); // 연산자 스택 생성
        Set<Character> terminalSet = new HashSet<>(); // 터미널(알파벳 및 숫자) 집합 생성

        for (int i = 0; i < regex.length(); i++) {
            char c = regex.charAt(i); // 정규식 문자열에서 한 글자씩 읽어옴

            if (isOperand(c)) { // 터미널인 경우
                ENFA enfa = new ENFA(); // 새로운 ENFA 객체 생성

                String startState = "q" + String.format("%03d", stateCount++); // 시작 상태 생성
                String finalState = "q" + String.format("%03d", stateCount++); // 최종 상태 생성

                enfa.createState(startState); // 시작 상태 생성
                enfa.createState(finalState); // 최종 상태 생성
                enfa.setStartState(startState); // 시작 상태 설정
                enfa.addFinalState(finalState); // 최종 상태 추가

                enfa.createTerminal(c); // 터미널 추가
                enfa.addDeltaFunction(startState, c, new HashSet<>(Collections.singletonList(finalState))); // 시작 상태에서 최종 상태로의 터미널 전이 설정

                enfaStack.push(enfa); // ENFA 스택에 추가
                enfa.createTerminal(c); // 터미널 추가
                terminalSet.add(c); // 터미널 집합에 추가

            } else if (isOperator(c)) { // 연산자인 경우
                while (!operatorStack.empty() && getPrecedence(c) <= getPrecedence(operatorStack.peek())) {
                    processOperator(enfaStack, operatorStack); // 연산자 우선순위 처리
                }
                operatorStack.push(c); // 연산자 스택에 추가

            } else if (c == '(') { // 여는 괄호인 경우
                operatorStack.push(c); // 연산자 스택에 추가
            } else if (c == ')') { // 닫는 괄호인 경우
                while (!operatorStack.empty() && operatorStack.peek() != '(') {
                    processOperator(enfaStack, operatorStack); // 여는 괄호를 만날 때까지 연산자 우선순위 처리
                }
                operatorStack.pop(); // 여는 괄호 제거
            }
        }

        while (!operatorStack.empty()) {
            processOperator(enfaStack, operatorStack); // 남은 연산자 처리
        }

        enfaStack.peek().setTerminalSet(terminalSet); // 최종적인 ENFA의 터미널 집합 설정

        return enfaStack.pop(); // 변환된 ENFA 반환
    }






    private static void processOperator(Stack<ENFA> enfaStack, Stack<Character> operatorStack) {
        char operator = operatorStack.pop();

        if (operator == '*') { // 별표(*) 연산자인 경우
            ENFA enfa = enfaStack.pop();
            ENFA closureENFA = applyClosure(enfa); // 클로저 연산 적용
            enfaStack.push(closureENFA); // 결과를 ENFA 스택에 추가
        } else {
            ENFA enfa2 = enfaStack.pop();
            ENFA enfa1 = enfaStack.pop();

            if(operator == '•'){
                ENFA concatenatedENFA = applyConcatenation(enfa1, enfa2); // 연결 연산 적용
                enfaStack.push(concatenatedENFA); // 결과를 ENFA 스택에 추가
            }


            if (operator == '+') { // 덧셈(+) 연산자인 경우
                ENFA unionENFA = applyUnion(enfa1, enfa2); // 합집합 연산 적용
                enfaStack.push(unionENFA); // 결과를 ENFA 스택에 추가
            }
        }
    }

    private static ENFA applyClosure(ENFA enfa) {
        String startState = "q" + String.format("%03d", stateCount++); // 시작 상태 생성
        String finalState = "q" + String.format("%03d", stateCount++); // 종료 상태 생성

        enfa.createState(startState); // ENFA에 시작 상태 추가
        enfa.createState(finalState); // ENFA에 종료 상태 추가

        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState))); // 시작 상태에서 종료 상태로 이동하는 빈 문자열(EPSILON) 전이 추가
        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa.getStartState()))); // 시작 상태에서 ENFA의 시작 상태로 이동하는 빈 문자열(EPSILON) 전이 추가

        for (String finalState1 : enfa.getFinalStateSet()) { // ENFA의 모든 종료 상태에 대해 반복
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa.getStartState()))); // 종료 상태에서 ENFA의 시작 상태로 이동하는 빈 문자열(EPSILON) 전이 추가
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState))); // 종료 상태에서 자기 자신인 종료 상태로 이동하는 빈 문자열(EPSILON) 전이 추가
        }

        enfa.setStartState(startState); // ENFA의 시작 상태 설정
        enfa.addFinalState(finalState); // ENFA의 종료 상태 추가

        return enfa; // 변환된 ENFA 반환
    }

    private static ENFA applyConcatenation(ENFA enfa1, ENFA enfa2) {
        ENFA enfa = new ENFA();

        // 연결된 ENFA에 대한 새로운 시작 상태와 종료 상태 생성
        String startState = "q" + String.format("%03d", stateCount++);  // 새로운 시작 상태 이름 생성
        String finalState = "q" + String.format("%03d", stateCount++);  // 새로운 종료 상태 이름 생성

        // 새로운 상태를 연결된 ENFA의 상태 집합에 추가
        enfa.createState(startState);  // 새로운 시작 상태 추가
        enfa.createState(finalState);  // 새로운 종료 상태 추가

        // enfa1에서 상태 집합, 델타 함수, 종료 상태를 연결된 ENFA에 추가
        enfa.getStateSet().addAll(enfa1.getStateSet());  // enfa1의 상태 집합 추가
        enfa.getDeltaFunctions().putAll(enfa1.getDeltaFunctions());  // enfa1의 델타 함수 추가
        enfa.getFinalStateSet().addAll(enfa1.getFinalStateSet());  // enfa1의 종료 상태 추가
        enfa.getTerminalSet().addAll(enfa1.getTerminalSet());  // enfa1의 터미널 집합 복사

        // enfa1의 각 종료 상태에서 enfa2의 시작 상태로 이동하는 빈 문자열(EPSILON) 전이를 추가
        for (String finalState1 : enfa1.getFinalStateSet()) {
            enfa.addDeltaFunction(finalState1, ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa2.getStartState())));
        }

        // enfa2의 상태 집합, 델타 함수, 종료 상태를 연결된 ENFA에 추가하면서 상태 이름을 충돌을 피하기 위해 변경
        for (String state : enfa2.getStateSet()) {
            String newState = "q" + String.format("%03d", stateCount++);  // 새로운 상태 이름 생성
            enfa.createState(newState);  // 새로운 상태 추가
            enfa.getStateSet().add(newState);  // 상태 집합에 추가

            // 델타 함수 키 이름 변경
            if (enfa2.getDeltaFunctions().containsKey(state)) {
                Map<Character, Set<String>> delta = enfa2.getDeltaFunctions().remove(state);
                enfa.getDeltaFunctions().put(newState, delta);  // 상태 이름 변경 후 델타 함수 추가
                enfa.getTerminalSet().addAll(enfa2.getTerminalSet());  // 터미널 집합 추가
            }

            // 종료 상태 이름 변경
            if (enfa2.getFinalStateSet().contains(state)) {
                enfa.getFinalStateSet().add(newState);  // 상태 이름 변경 후 종료 상태 추가
                enfa.getFinalStateSet().remove(state);  // 기존 종료 상태 삭제
            }
        }

        // enfa2의 델타 함수를 연결된 ENFA에서 상태 이름 변경을 반영하여 업데이트
        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa2.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();
            Map<Character, Set<String>> delta = entry.getValue();

            Map<Character, Set<String>> updatedDelta = new HashMap<>();
            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();
                Set<String> outputStates = deltaEntry.getValue();

                Set<String> updatedStates = new HashSet<>();
                for (String outputState : outputStates) {
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState : "q" + String.format("%03d", stateCount++);
                    updatedStates.add(updatedState);
                }

                updatedDelta.put(terminal, updatedStates);
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);  // 델타 함수 업데이트
        }

        // enfa1의 종료 상태에서 enfa2의 시작 상태로 빈 문자열(EPSILON) 전이 추가
        enfa.addDeltaFunction(enfa1.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(enfa2.getStartState())));

        // 연결된 ENFA의 시작 상태와 종료 상태 설정
        enfa.setStartState(enfa1.getStartState());  // enfa1의 시작 상태로 설정
        enfa.getFinalStateSet().clear();
        enfa.getFinalStateSet().add(finalState);  // 새로운 종료 상태로 설정

        return enfa;  // 변환된 ENFA 반환
    }





    private static ENFA applyUnion(ENFA enfa1, ENFA enfa2) {
        ENFA enfa = new ENFA();

        // 연결된 ENFA에 대한 새로운 시작 상태와 종료 상태 생성
        String startState = "q" + String.format("%03d", stateCount++);  // 새로운 시작 상태 이름 생성
        String finalState = "q" + String.format("%03d", stateCount++);  // 새로운 종료 상태 이름 생성

        // 새로운 상태를 연결된 ENFA의 상태 집합에 추가
        enfa.createState(startState);  // 새로운 시작 상태 추가
        enfa.createState(finalState);  // 새로운 종료 상태 추가

        // enfa1의 터미널 집합을 연결된 ENFA의 터미널 집합에 추가
        enfa.getTerminalSet().addAll(enfa1.getTerminalSet());  // enfa1의 터미널 집합 추가
        // enfa2의 터미널 집합을 연결된 ENFA의 터미널 집합에 추가
        enfa.getTerminalSet().addAll(enfa2.getTerminalSet());  // enfa2의 터미널 집합 추가

        // enfa1의 상태 집합, 델타 함수, 종료 상태를 연결된 ENFA에 추가하면서 상태 이름을 충돌을 피하기 위해 변경
        for (String state : enfa1.getStateSet()) {
            String newState = "q" + String.format("%03d", stateCount++);  // 새로운 상태 이름 생성
            enfa.createState(newState);  // 새로운 상태 추가
            enfa.getStateSet().add(newState);  // 상태 집합에 추가

            // 델타 함수 키 이름 변경
            if (enfa1.getDeltaFunctions().containsKey(state)) {
                Map<Character, Set<String>> delta = enfa1.getDeltaFunctions().remove(state);
                enfa.getDeltaFunctions().put(newState, delta);  // 상태 이름 변경 후 델타 함수 추가
            }

            // 종료 상태 이름 변경
            if (enfa1.getFinalStateSet().contains(state)) {
                enfa.getFinalStateSet().add(newState);  // 상태 이름 변경 후 종료 상태 추가
                enfa.getFinalStateSet().remove(state);  // 기존 종료 상태 삭제
            }
        }

        // enfa1의 델타 함수를 연결된 ENFA에서 상태 이름 변경을 반영하여 업데이트
        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa1.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();
            Map<Character, Set<String>> delta = entry.getValue();

            Map<Character, Set<String>> updatedDelta = new HashMap<>();
            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();
                Set<String> outputStates = deltaEntry.getValue();

                Set<String> updatedStates = new HashSet<>();
                for (String outputState : outputStates) {
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState : "q" + String.format("%03d", stateCount++);
                    updatedStates.add(updatedState);
                }

                updatedDelta.put(terminal, updatedStates);
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);  // 델타 함수 업데이트
        }

        // enfa2의 상태 집합, 델타 함수, 종료 상태를 연결된 ENFA에 추가
        enfa.getStateSet().addAll(enfa2.getStateSet());  // enfa2의 상태 집합 추가
        enfa.getDeltaFunctions().putAll(enfa2.getDeltaFunctions());  // enfa2의 델타 함수 추가
        enfa.getFinalStateSet().addAll(enfa2.getFinalStateSet());  // enfa2의 종료 상태 추가

        // 새로운 시작 상태에서 enfa1과 enfa2의 시작 상태로 빈 문자열(EPSILON) 전이 추가
        enfa.addDeltaFunction(startState, ENFA.EPSILON, new HashSet<>(Arrays.asList(enfa1.getStartState(), enfa2.getStartState())));

        // enfa2의 델타 함수를 연결된 ENFA에서 상태 이름 변경을 반영하여 업데이트

        for (Map.Entry<String, Map<Character, Set<String>>> entry : enfa2.getDeltaFunctions().entrySet()) {
            String state = entry.getKey();  // 현재 상태 이름
            Map<Character, Set<String>> delta = entry.getValue();  // 현재 상태의 델타 함수

            Map<Character, Set<String>> updatedDelta = new HashMap<>();  // 업데이트된 델타 함수를 저장할 맵 생성

            for (Map.Entry<Character, Set<String>> deltaEntry : delta.entrySet()) {
                Character terminal = deltaEntry.getKey();  // 현재 델타 함수의 터미널
                Set<String> outputStates = deltaEntry.getValue();  // 현재 델타 함수에서 터미널을 입력했을 때의 출력 상태 집합

                Set<String> updatedStates = new HashSet<>();  // 업데이트된 출력 상태 집합을 저장할 집합 생성

                for (String outputState : outputStates) {
                    // 출력 상태가 연결된 ENFA의 상태 집합에 이미 포함되어 있는 경우에는 상태 이름 변경 없이 그대로 사용
                    // 그렇지 않은 경우에는 새로운 상태 이름을 생성하여 사용
                    String updatedState = enfa.getStateSet().contains(outputState) ? outputState : "q" + String.format("%03d", stateCount++);
                    updatedStates.add(updatedState);  // 업데이트된 상태를 출력 상태 집합에 추가
                }

                updatedDelta.put(terminal, updatedStates);  // 업데이트된 델타 함수에 새로운 터미널과 출력 상태 집합 추가
            }

            enfa.getDeltaFunctions().put(state, updatedDelta);  // 상태에 업데이트된 델타 함수를 저장
        }

        // enfa1과 enfa2의 종료 상태에서 새로운 종료 상태로 빈 문자열(EPSILON) 전이 추가
        enfa.addDeltaFunction(enfa1.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));
        enfa.addDeltaFunction(enfa2.getFinalStateSet().iterator().next(), ENFA.EPSILON, new HashSet<>(Collections.singletonList(finalState)));

        // 연결된 ENFA의 시작 상태와 종료 상태 설정
        enfa.setStartState(startState);
        enfa.addFinalState(finalState);

        // enfa2의 터미널 집합을 연결된 ENFA의 터미널 집합에 추가
        enfa.getTerminalSet().addAll(enfa2.getTerminalSet());

        return enfa;  // 변환된 ENFA 반환
    }

    // ENFA 객체를 파일에 저장합니다.
    public static void saveENFAToFile(ENFA enfa) {
        try {
            FileWriter writer = new FileWriter("Epsilon_nfa_output.txt");

            // 상태 집합 출력
            writer.write("StateSet = { " + String.join(", ", enfa.getStateSet()) + " }\n");

            // 터미널 집합 출력
            writer.write("TerminalSet = { " + enfa.getTerminalSet().toString().replaceAll("[\\[\\]]", "") + " }\n");

            // 델타 함수 출력
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

            // 시작 상태 출력
            writer.write("StartState = " + enfa.getStartState() + "\n");

            // 최종 상태 집합 출력
            writer.write("FinalStateSet = { " + String.join(", ", enfa.getFinalStateSet()) + " }\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 주어진 문자가 피연산자인지 확인합니다.
    private static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    // 주어진 문자가 연산자인지 확인합니다.
    private static boolean isOperator(char c) {
        return c == '+' || c == '*' || c == '•';
    }

    // 주어진 연산자의 우선순위를 반환합니다.
    private static int getPrecedence(char c) {
        switch (c) {
            case '*':
                return 3;
            case '•':
                return 2;
            case '+':
                return 1;
            default:
                return 0;
        }
    }
}
