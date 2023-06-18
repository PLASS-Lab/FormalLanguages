package re;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RE {
    public static boolean validateRegex(String regex) {
        // 정규표현식 확장부터 진행
        String expandedRegex = expandRegex(regex);

        if (!expandedRegex.matches("[a-zA-Z0-9()+•*]*")) {
            System.out.println("Ste1.");
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

    private static String expandRegex(String regex) {
        regex = regex.replaceAll("\\s+", ""); // 공백 제거
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(regex);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String expandedGroup = matcher.group().replaceAll("(.)(?=.)", "$1•");
            matcher.appendReplacement(sb, expandedGroup);
        }

        matcher.appendTail(sb);
        System.out.println(sb);
        return sb.toString();
    }

}
