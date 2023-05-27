import java.io.*;

public class Scanner { // Scanner 클래스

    // 토큰 종류
    private boolean isEof = false;              // 파일의 끝인지 체크
    private char ch = ' ';                      // 현재 문자
    private BufferedReader input;               // 입력 스트림
    private String line = "";                   // 입력에서 읽은 한 줄
    private int lineno = 0;                     // 현재 행 번호
    private int col = 1;                        // 현재 열 번호
    private final String letters = "abcdefghijklmnopqrstuvwxyz" // 문자
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String digits = "0123456789"; // 숫자
    private final char eolnCh = '\n'; // 개행 문자
    private final char eofCh = '\004'; // 파일의 끝을 나타내는 문자


    // 스캐너 생성자
    public Scanner(String fileName) { // 소스 파일 이름

        // 파일 이름 출력
        System.out.println("Begin scanning... programs/" + fileName + "\n");

        // 파일 열기
        try {
            input = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            // 파일이 없을 경우 오류 출력
            System.out.println("File not found: " + fileName);
            // 프로그램 종료
            System.exit(1);
        }
    }


    private char nextChar() { // 다음 문자 반환
        // 파일의 끝이면
        if (ch == eofCh)
            // 오류 출력
            error("Attempt to read past end of file");
        // 열 번호 증가
        col++;
        // 열 번호가 줄의 길이보다 크면
        if (col >= line.length()) {
            try {
                // 다음 줄 읽기
                line = input.readLine();
            } catch (IOException e) {
                // 오류 출력
                System.err.println(e);
                System.exit(1);
            }
            // 다음 줄이 없으면
            if (line == null) // 파일의 끝
                line = "" + eofCh;
            else {
                // System.out.println(lineno + ":\t" + line);
                // 줄 번호 증가
                lineno++;
                line += eolnCh;
            } // if line
            // 열 번호 초기화
            col = 0;
        } // if col
        // 다음 문자 반환
        return line.charAt(col);
    }


    // next를 통해서 다음 토큰을 읽어온다.
    public Token next() {
        do {
            // 공백 문자 건너뛰기
            if (isLetter(ch) || ch == '_') { // 식별자 또는 키워드
                // 식별자 또는 키워드 읽기
                String spelling = concat(letters + digits + '_');
                // 키워드인지 검사
                return Token.keyword(spelling);
            } else if (isDigit(ch)) { // 정수 리터럴
                // 정수 리터럴 읽기
                String number = concat(digits);
                // 정수 리터럴 반환
                return Token.mkIntLiteral(number);
            } else
                // switch 문을 사용하여 토큰을 구분
                switch (ch) {
                // ' ', \t 또는 \r 문자를 건너뛰기
                    case ' ':
                    case '\t':
                    case '\r':
                        // 다음 문자 읽기
                    case eolnCh:
                        ch = nextChar();
                        break;

                    case '/': // 나누기, 할당 또는 주석
                        ch = nextChar();
                        if (ch == '=') { // divAssign
                            ch = nextChar();
                            return Token.divAssignTok;
                        }

                        // 주석이 아니면 나누기
                        if (ch != '*' && ch != '/')
                            return Token.divideTok;

                        // "/* */"블록 주석 읽기
                        if (ch == '*') {
                            do {
                                // ch가 * 일때까지 읽기
                                while (ch != '*')
                                    ch = nextChar();
                                ch = nextChar();
                            } while (ch != '/');

                            ch = nextChar();
                        }

                        // "//" 한 줄 주석
                        else if (ch == '/') {
                            do {
                                ch = nextChar();
                            } while (ch != eolnCh);
                            ch = nextChar();
                        }
                        break;
                    /*
                    case '\'':  // 문자 리터럴
                        char ch1 = nextChar();
                        nextChar(); // '
                        ch = nextChar();
                        return Token.mkCharLiteral("" + ch1);
                    */
                    // eofCh 문자를 만나면(End Of File)
                    case eofCh:
                        // 파일의 끝
                        return Token.eofTok;
                    case '+': // 더하기, 증가 또는 할당
                        ch = nextChar();
                        // +=
                        if (ch == '=') { // +=
                            ch = nextChar();
                            return Token.addAssignTok;
                        } else if (ch == '+') { // ++
                            ch = nextChar();
                            return Token.incrementTok;
                        }
                        // 그냥 "+"
                        return Token.plusTok;

                    case '-':
                        ch = nextChar();
                        if (ch == '=') { // -=
                            ch = nextChar();
                            return Token.subAssignTok;
                        } else if (ch == '-') { // --
                            ch = nextChar();
                            return Token.decrementTok;
                        }
                        return Token.minusTok;

                    case '*':
                        ch = nextChar();
                        if (ch == '=') { // multAssign
                            ch = nextChar();
                            return Token.multAssignTok;
                        }
                        return Token.multiplyTok;

                    case '%':
                        ch = nextChar();
                        if (ch == '=') { // remAssign
                            ch = nextChar();
                            return Token.remAssignTok;
                        }
                        return Token.reminderTok;

                    case '(':
                        ch = nextChar();
                        return Token.leftParenTok;

                    case ')':
                        ch = nextChar();
                        return Token.rightParenTok;

                    case '{':
                        ch = nextChar();
                        return Token.leftBraceTok;

                    case '}':
                        ch = nextChar();
                        return Token.rightBraceTok;

                    case ';':
                        ch = nextChar();
                        return Token.semicolonTok;

                    case ',':
                        ch = nextChar();
                        return Token.commaTok;

                    case '&':
                        check('&');
                        return Token.andTok;
                    case '|':
                        check('|');
                        return Token.orTok;

                    case '=':
                        return chkOpt('=', Token.assignTok, Token.eqeqTok);

                    case '<':
                        return chkOpt('=', Token.ltTok, Token.lteqTok);
                    case '>':
                        return chkOpt('=', Token.gtTok, Token.gteqTok);
                    case '!':
                        return chkOpt('=', Token.notTok, Token.noteqTok);

                    default:
                        error("Illegal character " + ch);
                } // switch
        } while (true);
    } // next


    private boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return (c >= '0' && c <= '9');
    }

    private void check(char c) {
        ch = nextChar();
        if (ch != c)
            error("Illegal character, expecting " + c);
        ch = nextChar();
    }

    private Token chkOpt(char c, Token one, Token two) {
        ch = nextChar();
        if (ch != c)
            return one;
        ch = nextChar();
        return two;
    }

    private String concat(String set) {
        String r = "";
        do {
            r += ch;
            ch = nextChar();
        } while (set.indexOf(ch) >= 0);
        return r;
    }

    public void error(String msg) {
        System.err.print(line);
        System.err.println("Error: column " + col + " " + msg);
        System.exit(1);
    }

    static public void main(String[] argv) {
        Scanner lexer = new Scanner(argv[0]);
        Token tok = lexer.next();
        while (tok != Token.eofTok) {
            System.out.println(tok.toString());
            tok = lexer.next();
        }
    } // main
}
