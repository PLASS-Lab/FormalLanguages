package re;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RE {
    public static boolean validateRegex(String regex) {
        // 정규표현식 확장부터 진행
        String expandedRegex = expandRegex(regex);

        // 알파벳 조건 확인: 알파벳은 a~z, A~Z, 0~9로만 이루어져야 합니다.
        if (!expandedRegex.matches("[a-zA-Z0-9()+•*]*")) {
            System.out.println("Step 1.");
            System.out.println("알파벳은 a~z, A~Z, 0~9로만 이루어져야 합니다.");
            return false;
        }

        // ()를 사용하는지 확인
        if (expandedRegex.contains("(") || expandedRegex.contains(")")) {
            int openCount = expandedRegex.length() - expandedRegex.replace("(", "").length();
            int closeCount = expandedRegex.length() - expandedRegex.replace(")", "").length();
            if (openCount != closeCount) {
                return false;
            }
        }
        return true;
    }
    public static String getExpandedRegex(String regex) {
        return expandRegex(regex);
    }

    private static String expandRegex(String regex) {
        // 공백 제거
        regex = regex.replaceAll("\\s+", "");


        // 정규식 패턴을 컴파일, [a-zA-Z0-9]+는 알파벳 대소문자와 숫자가 한 번 이상 연속해서 나타나는 패턴을 의미
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");

        // 입력된 정규식과 패턴을 비교하여 매칭되는 부분을 찾는 Matcher 객체를 생성
        Matcher matcher = pattern.matcher(regex);

        // 매칭된 결과를 저장할 StringBuffer 객체를 생성합니다.
        StringBuffer sb = new StringBuffer();

        // 정규식 매칭 결과를 반복적으로 찾아내기 위해 while 문을 사용
        while (matcher.find()) {
            // •로 축약된 부분을 다시 확장하여 expandedGroup 변수에 저장
            // "(.)(?=.)"는 한 글자를 선택하고 그 다음에 글자가 있을 경우에만 매칭하는 정규식
            // "$1•"은 선택된 글자 뒤에 •를 추가하는 것을 의미
            String expandedGroup = matcher.group().replaceAll("(.)(?=.)", "$1•");

            // expandedGroup을 찾은 매칭 결과에 대체하여 StringBuffer인 sb에 추가
            matcher.appendReplacement(sb, expandedGroup);
        }

        // while 루프가 끝나고 나머지 부분을 sb에 붙여줌
        matcher.appendTail(sb);

        // 확장된 정규표현 반환
        return sb.toString();
    }

}