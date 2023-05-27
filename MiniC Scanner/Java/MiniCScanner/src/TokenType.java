public enum TokenType {
    Const, Void, Else, If, Int, While, Return, // 예약어 토큰
    Eof, // 파일의 끝을 나타내는 토큰

    // 구분 기호 토큰
    LeftBrace, RightBrace, LeftBracket, RightBracket,
    LeftParen, RightParen, Semicolon, Comma, Assign,
    AddAssign, SubAssign, MultAssign, DivAssign, RemAssign,

    // 비교 연산자 토큰
    Equals, Less, LessEqual, Greater, GreaterEqual,
    Not, NotEqual,

    // 산술 연산자 토큰
    Plus, Minus, Multiply, Reminder,

    // 증감 연산자 토큰
    Increment, Decrement,

    // 기타 연산자 토큰
    Divide, And, Or,

    // 식별자와 정수 리터럴 토큰
    Identifier, IntLiteral,
}
