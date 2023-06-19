package main;

import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import e_nfa.ENFA;
import e_nfa.ENFACreator;
import re.RE;
import dfa.DFA;
import mfa.MFA;
import nfa.NFA;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String re = "";

        String expandedRegex = "";

        boolean isValid = false;

        /*--------------------re 체크--------------------*/
        while (!isValid) {
            System.out.println("정규 표현식을 입력하세요");
            re = scanner.nextLine();  // 사용자로부터 정규 표현식을 입력받습니다.
            isValid = RE.validateRegex(re); // isValid Check from ValidDateRegex

            // 정규 표현식에 맞지 않을경우 다시 입력받음, 이를 위한 안내문 출력
            if (!isValid) {
                System.out.println("입력한 정규 표현식이 조건에 맞지 않습니다. 다시 입력해주세요.");
            }
        }
        /*--------------------Exapnd Regex --------------------*/
        expandedRegex = RE.getExpandedRegex(re);

        System.out.println("Re:" + re);
        System.out.println("Expand Re"+expandedRegex);

        /*--------------------Convert RE to ε-NFA --------------------*/
        // 정규 표현식을 ε-NFA로 변환합니다.
        ENFA enfa = ENFACreator.convertToENFA(expandedRegex);

        // 변환된 ε-NFA를 파일로 저장합니다.
        ENFACreator.saveENFAToFile(enfa);


        // re to nfa
        NFA nfa = new NFA(re);  // 입력받은 정규 표현식으로 NFA 객체를 생성합니다.
        nfa.add_join_symbol();  // 조인 기호를 추가합니다
        // .
        nfa.postfix();  // 후위 표기법으로 변환합니다.
        nfa.re2nfa();  // 정규 표현식을 NFA로 변환합니다.
        nfa.print();  // 변환된 NFA를 출력합니다.


        /*--------------------Convert ε-NFA to DFA --------------------*/
        DFA dfa = new DFA(nfa.getPair(),nfa.getLetter());  // NFA로부터 DFA 객체를 생성합니다.
        dfa.createDFA();  // DFA를 생성합니다.
        dfa.printDFA();  // 생성된 DFA를 출력합니다.


        /*--------------------Convert DFA To MFA --------------------*/
        MFA mfa = new MFA(dfa.getDFA(),dfa.getEndState(),dfa.getLetter());  // DFA로부터 MFA 객체를 생성합니다.
        mfa.minimize();  // MFA를 최소화합니다.
        mfa.merge();  // 상태들을 병합합니다.
        mfa.printMFA();  // 최소화된 MFA를 출력합니다.



        scanner.close();
    }

}
