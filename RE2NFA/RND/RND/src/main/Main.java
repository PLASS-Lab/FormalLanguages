package main;

import java.util.Scanner;

import re.RE;
import dfa.DFA;
import mfa.MFA;
import nfa.NFA;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String re = "";

        boolean isValid = false;
        while (!isValid) {
            System.out.println("정규 표현식을 입력하세요");
            re = scanner.nextLine();  // 사용자로부터 정규 표현식을 입력받습니다.



            isValid = RE.validateRegex(re); // isValid Check from ValidDateRegex

            // 정규 표현식에 맞지 않을경우 다시 입력받음, 이를 위한 안내문 출력
            if (!isValid) {
                System.out.println("입력한 정규 표현식이 조건에 맞지 않습니다. 다시 입력해주세요.");
            }
        }

        System.out.println("re:" + re);



        NFA nfa = new NFA(re);  // 입력받은 정규 표현식으로 NFA 객체를 생성합니다.
        nfa.add_join_symbol();  // 조인 기호를 추가합니다.
        nfa.postfix();  // 후위 표기법으로 변환합니다.
        nfa.re2nfa();  // 정규 표현식을 NFA로 변환합니다.
        nfa.print();  // 변환된 NFA를 출력합니다.



        DFA dfa = new DFA(nfa.getPair(),nfa.getLetter());  // NFA로부터 DFA 객체를 생성합니다.
        dfa.createDFA();  // DFA를 생성합니다.
        dfa.printDFA();  // 생성된 DFA를 출력합니다.

        MFA mfa = new MFA(dfa.getDFA(),dfa.getEndState(),dfa.getLetter());  // DFA로부터 MFA 객체를 생성합니다.
        mfa.minimize();  // MFA를 최소화합니다.
        mfa.merge();  // 상태들을 병합합니다.
        mfa.printMFA();  // 최소화된 MFA를 출력합니다.

        System.out.println();
        System.out.println("re:" + re);
        System.out.println("테스트 문자열을 입력하세요. 종료하려면 Q를 입력하세요.");
        while(scanner.hasNextLine()) {
            String string = scanner.nextLine();  // 사용자로부터 테스트 문자열을 입력받습니다.
            if(string.equals("Q"))
                break;
            else
                mfa.valid(string);  // 입력된 문자열의 유효성을 검사합니다.
            System.out.println();
            System.out.println("테스트 문자열을 입력하세요. 종료하려면 Q를 입력하세요.");
        }
        scanner.close();
    }

}
