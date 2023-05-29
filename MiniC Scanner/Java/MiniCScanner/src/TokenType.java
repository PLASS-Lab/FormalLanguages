public enum TokenType {
    // 예약어 토큰
    Const("const", 30),    // const 키워드, 번호: 30
    Else("else", 31),     // else 키워드, 번호: 31
    If("if", 32),         // if 키워드, 번호: 32
    Int("int", 33),       // int 키워드, 번호: 33
    Return("return", 34), // return 키워드, 번호: 34
    Void("void", 35),     // void 키워드, 번호: 35
    While("while", 36),   // while 키워드, 번호: 36

    // 기타 토큰
    Char("char", 40),         // char 타입, 번호: 40
    Double("double", 41),     // double 타입, 번호: 41
    For("for", 43),           // for 루프, 번호: 43
    Do("do", 44),             // do-while 루프, 번호: 44
    Goto("goto", 45),         // goto 문, 번호: 45
    Switch("switch", 46),     // switch 문, 번호: 46
    Case("case", 47),         // case 문, 번호: 47
    Break("break", 48),       // break 문, 번호: 48
    Default("default", 49),   // default 문, 번호: 49
    Eof("<<EOF>>", 29),       // 파일의 끝(End-of-File) 토큰, 번호: 29

    // 연산자 및 기호 토큰
    Not("!", 0),              // 논리 부정 연산자, 번호: 0
    NotEqual("!=", 1),        // 논리적으로 같지 않음을 나타내는 연산자, 번호: 1
    Reminder("%", 2),         // 나머지 연산자, 번호: 2
    RemAssign("%=", 3),       // 나머지 연산 후 할당 연산자, 번호: 3
    Identifier("%ident", 4),  // 식별자 토큰, 번호: 4
    IntLiteral("%i_lit", 5),  // 정수 리터럴 토큰, 번호: 5
    And("%%", 6),             // 논리적 AND 연산자, 번호: 6
    LeftParen("(", 7),        // 왼쪽 괄호, 번호: 7
    RightParen(")", 8),       // 오른쪽 괄호, 번호: 8
    Multiply("*", 9),         // 곱셈 연산자, 번호: 9
    MultAssign("*=", 10),     // 곱셈 후 할당 연산자, 번호: 10
    Plus("+", 11),            // 덧셈 연산자, 번호: 11
    Increment("++", 12),      // 증가 연산자, 번호: 12
    AddAssign("+=", 13),      // 덧셈 후 할당 연산자, 번호: 13
    Comma(",", 14),           // 쉼표, 번호: 14
    Minus("-", 15),           // 뺄셈 연산자, 번호: 15
    Decrement("--", 16),      // 감소 연산자, 번호: 16
    SubAssign("-=", 17),      // 뺄셈 후 할당 연산자, 번호: 17
    Divide("/", 18),          // 나눗셈 연산자, 번호: 18
    DivAssign("/=", 19),      // 나눗셈 후 할당 연산자, 번호: 19
    Semicolon(";", 20),       // 세미콜론, 번호: 20
    Less("<", 21),            // 작음을 나타내는 연산자, 번호: 21
    LessEqual("<=", 22),      // 작거나 같음을 나타내는 연산자, 번호: 22
    Assign("=", 23),          // 할당 연산자, 번호: 23
    Equals("==", 24),         // 같음을 나타내는 연산자, 번호: 24
    Greater(">", 25),         // 큼을 나타내는 연산자, 번호: 25
    GreaterEqual(">=", 26),   // 크거나 같음을 나타내는 연산자, 번호: 26
    LeftBracket("[", 27),     // 왼쪽 대괄호, 번호: 27
    RightBracket("]", 28),    // 오른쪽 대괄호, 번호: 28
    LeftBrace("{", 37),       // 왼쪽 중괄호, 번호: 37
    Or("||", 38),             // 논리적 OR 연산자, 번호: 38
    RightBrace("}", 39),      // 오른쪽 중괄호, 번호: 39
    Colon(":", 50),           // 콜론, 번호: 50
    CharLiteral("%c_lit", 51),     // 문자 리터럴 토큰, 번호: 51
    StringLiteral("%s_lit", 52),   // 문자열 리터럴 토큰, 번호: 52
    DoubleLiteral("%d_lit", 53),   // 실수 리터럴 토큰, 번호: 53
    Document("document", 54);      // 문서 토큰, 번호: 54

    private final String name; // 토큰의 이름을 저장하는 변수
    private final int number; // 토큰의 번호를 저장하는 변수

    /**
     * TokenType 생성자
     *
     * @param name   토큰의 이름
     * @param number 토큰의 번호
     */
    private TokenType(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * 토큰의 이름을 반환하는 메서드
     *
     * @return 토큰의 이름
     */
    public String getTokenName() {
        return this.name;
    }

    /**
     * 토큰의 번호를 문자열 형태로 반환하는 메서드
     *
     * @return 토큰의 번호 (문자열 형태)
     */
    public String getTokenNumber() {
        return Integer.toString(this.number);
    }
}
