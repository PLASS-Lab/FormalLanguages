
public class Token {


    // 아래는 토큰의 종류를 나타내는 상수들
    private static final int KEYWORDS = TokenType.Eof.ordinal(); // Keywords는 TokenType의 Eof의 ordinal값을 가진다.

    private static final String[] reserved = new String[KEYWORDS]; // reserved는 String[keywords]의 크기를 가진다.
    private static Token[] token = new Token[KEYWORDS]; // token은 Token[keywords]의 크기를 가진다.

    public static final Token eofTok = new Token(TokenType.Eof, "<<EOF>>"); // eofTok는 TokenType의 Eof와 "<<EOF>>"를 가진다.
    public static final Token constTok = new Token(TokenType.Const, "const"); // constTok는 TokenType의 Const와 "const"를 가진다.
    public static final Token returnTok = new Token(TokenType.Return, "return"); // returnTok는 TokenType의 Return과 "return"을 가진다.
    public static final Token voidTok = new Token(TokenType.Void, "void"); // voidTok는 TokenType의 Void와 "void"를 가진다.
    public static final Token elseTok = new Token(TokenType.Else, "else"); // elseTok는 TokenType의 Else와 "else"를 가진다.
    public static final Token ifTok = new Token(TokenType.If, "if"); // ifTok는 TokenType의 If와 "if"를 가진다.
    public static final Token intTok = new Token(TokenType.Int, "int"); // intTok는 TokenType의 Int와 "int"를 가진다.
    public static final Token whileTok = new Token(TokenType.While, "while"); // whileTok는 TokenType의 While와 "while"를 가진다.
    public static final Token leftBraceTok = new Token(TokenType.LeftBrace, "{"); // leftBraceTok는 TokenType의 LeftBrace와 "{"를 가진다.
    public static final Token rightBraceTok = new Token(TokenType.RightBrace, "}"); // rightBraceTok는 TokenType의 RightBrace와 "}"를 가진다.
    public static final Token leftBracketTok = new Token(TokenType.LeftBracket, "["); // leftBracketTok는 TokenType의 LeftBracket와 "["를 가진다.
    public static final Token rightBracketTok = new Token(TokenType.RightBracket, "]"); // rightBracketTok는 TokenType의 RightBracket와 "]"를 가진다.
    public static final Token leftParenTok = new Token(TokenType.LeftParen, "("); // leftParenTok는 TokenType의 LeftParen와 "("를 가진다.
    public static final Token rightParenTok = new Token(TokenType.RightParen, ")"); // rightParenTok는 TokenType의 RightParen와 ")"를 가진다.
    public static final Token semicolonTok = new Token(TokenType.Semicolon, ";"); // semicolonTok는 TokenType의 Semicolon와 ";"를 가진다.
    public static final Token commaTok = new Token(TokenType.Comma, ","); // commaTok는 TokenType의 Comma와 ","를 가진다.
    public static final Token assignTok = new Token(TokenType.Assign, "="); // assignTok는 TokenType의 Assign와 "="를 가진다.
    public static final Token eqeqTok = new Token(TokenType.Equals, "=="); // eqeqTok는 TokenType의 Equals와 "=="를 가진다.
    public static final Token ltTok = new Token(TokenType.Less, "<"); // ltTok는 TokenType의 Less와 "<"를 가진다.
    public static final Token lteqTok = new Token(TokenType.LessEqual, "<="); // lteqTok는 TokenType의 LessEqual와 "<="를 가진다.
    public static final Token gtTok = new Token(TokenType.Greater, ">"); // gtTok는 TokenType의 Greater와 ">"를 가진다.
    public static final Token gteqTok = new Token(TokenType.GreaterEqual, ">="); // gteqTok는 TokenType의 GreaterEqual와 ">="를 가진다.
    public static final Token notTok = new Token(TokenType.Not, "!"); // notTok는 TokenType의 Not와 "!"를 가진다.
    public static final Token noteqTok = new Token(TokenType.NotEqual, "!="); // noteqTok는 TokenType의 NotEqual와 "!="를 가진다.
    public static final Token plusTok = new Token(TokenType.Plus, "+"); // plusTok는 TokenType의 Plus와 "+"를 가진다.
    public static final Token minusTok = new Token(TokenType.Minus, "-"); // minusTok는 TokenType의 Minus와 "-"를 가진다.
    public static final Token multiplyTok = new Token(TokenType.Multiply, "*"); // multiplyTok는 TokenType의 Multiply와 "*"를 가진다.
    public static final Token divideTok = new Token(TokenType.Divide, "/"); // divideTok는 TokenType의 Divide와 "/"를 가진다.
    public static final Token reminderTok = new Token(TokenType.Reminder, "%"); // reminderTok는 TokenType의 Reminder와 "%"를 가진다.
    public static final Token addAssignTok = new Token(TokenType.AddAssign, "+="); // addAssignTok는 TokenType의 AddAssign와 "+="를 가진다.
    public static final Token subAssignTok = new Token(TokenType.SubAssign, "-="); // subAssignTok는 TokenType의 SubAssign와 "-="를 가진다.
    public static final Token multAssignTok = new Token(TokenType.MultAssign, "*="); // multAssignTok는 TokenType의 MultAssign와 "*="를 가진다.
    public static final Token divAssignTok = new Token(TokenType.DivAssign, "/="); // divAssignTok는 TokenType의 DivAssign와 "/="를 가진다.
    public static final Token remAssignTok = new Token(TokenType.RemAssign, "%="); // remAssignTok는 TokenType의 RemAssign와 "%="를 가진다.
    public static final Token incrementTok = new Token(TokenType.Increment, "++"); // incrementTok는 TokenType의 Increment와 "++"를 가진다.
    public static final Token decrementTok = new Token(TokenType.Decrement, "--"); // decrementTok는 TokenType의 Decrement와 "--"를 가진다.
    public static final Token andTok = new Token(TokenType.And, "&&"); // andTok는 TokenType의 And와 "&&"를 가진다.
    public static final Token orTok = new Token(TokenType.Or, "||"); // orTok는 TokenType의 Or와 "||"를 가진다.


    public static final Token charTok = new Token(TokenType.Char, "char"); // charTok는 TokenType의 Char와 "char"를 가진다.
    public static final Token doubleTok = new Token(TokenType.Double, "double"); // doubleTok는 TokenType의 Double와 "double"를 가진다.
    public static final Token forTok = new Token(TokenType.For, "for"); // forTok는 TokenType의 For와 "for"를 가진다.
    public static final Token doTok = new Token(TokenType.Do, "do"); // doTok는 TokenType의 Do와 "do"를 가진다.
    public static final Token gotoTok = new Token(TokenType.Goto, "goto"); // gotoTok는 TokenType의 Goto와 "goto"를 가진다.
    public static final Token switchTok = new Token(TokenType.Switch, "switch"); // switchTok는 TokenType의 Switch와 "switch"을 가진다.
    public static final Token caseTok = new Token(TokenType.Case, "case"); // caseTok는 TokenType의 Case와 "case"을 가진다.
    public static final Token breakTok = new Token(TokenType.Break, "break"); // breakTok는 TokenType의 Break와 "break"을 가진다.
    public static final Token DefaultTok = new Token(TokenType.Default, "default"); // DefaultTok는 TokenType의 Default와 "default"을 가진다.
    public static final Token colonTok = new Token(TokenType.Colon, "colon"); // colonTok는 TokenType의 Colon와 "colon"을 가진다.

    private TokenType type; // 토큰의 타입을 저장하는 변수
    private String value = ""; // 토큰의 값을 저장하는 변수



    // 생성자 - 토큰의 타입과 값을 저장
    private Token(TokenType t, String v) {
        type = t;
        value = v;
        if (t.compareTo(TokenType.Eof) < 0) {
            int ti = t.ordinal();
            reserved[ti] = t.getTokenName();
            token[ti] = this;
        }
    }

    private int lineNum = 0;
    private int col = 0;

    // 토큰의 타입, 값, 행 번호, 열 번호를 저장하는 생성자
    private Token(TokenType t, String v, int lineNum, int col) {
        type = t;
        value = v;
        this.lineNum = lineNum;
        this.col = col;
    }



    // type을 반환하는 메소드
    public TokenType type( ) {
        return type;
    }

    // value를 반환하는 메소드
    public String value( ) {
        return value;
    }


    // 토큰의 타입을 저장하는 enum
    public static Token keyword  ( String name, int lineNum, int col ) {
        char ch = name.charAt(0);
        if (ch >= 'A' && ch <= 'Z')
            return mkIdentTok(name,lineNum,col);
        for (int i = 0; i < KEYWORDS; i++)
           if (name.equals(reserved[i]))
               return mkDefaultToken(token[i],lineNum,col);
        return mkIdentTok(name,lineNum,col);
    } // keyword

    // mkIdentTok 메소드 : 토큰의 타입이 Identifier인 토큰을 생성하는 메소드
    public static Token mkIdentTok (String name, int lineNum, int col) {
        return new Token(TokenType.Identifier, name,lineNum,col);
    }

    // mkIntLiteral 메소드 : 토큰의 타입이 IntLiteral인 토큰을 생성하는 메소드
    public static Token mkIntLiteral (String name, int lineNum, int col) {
        return new Token(TokenType.IntLiteral, name,lineNum,col);
    }
    // mkDoubleLiteral 메소드 : 토큰의 타입이 DoubleLiteral인 토큰을 생성하는 메소드
    public static Token mkCharLiteral(String name, int lineNum, int col) {
        return new Token(TokenType.CharLiteral, name, lineNum, col);
    }

    // mkDoubleLiteral 메소드 : 토큰의 타입이 DoubleLiteral인 토큰을 생성하는 메소드
    public static Token mkStringLiteral(String name, int lineNum, int col) {
        return new Token(TokenType.StringLiteral, name, lineNum, col);
    }

    // mkDoubleLiteral 메소드 : 토큰의 타입이 DoubleLiteral인 토큰을 생성하는 메소드
    public static Token mkDoubleLiteral(String name, int lineNum, int col) {
        return new Token(TokenType.DoubleLiteral, name, lineNum, col);
    }

    // mkDocumentToken 메소드 : 토큰의 타입이 Document인 토큰을 생성하는 메소드
    public static Token mkDocumentToken(String name, int lineNum, int col) {
        return new Token(TokenType.Document, name, lineNum, col);
    }

    // mkDefaultToken 메소드 : 토큰의 타입이 TokenType에 정의된 타입이 아닌 토큰을 생성하는 메소드
    public static Token mkDefaultToken(Token token, int lineNum, int col) {
        return new Token(token.type, token.value, lineNum, col);
    }








    // toString 메소드 : 토큰의 타입, 값, 파일 이름, 행 번호, 열 번호를 출력하는 메소드
    public String toString (String fileName) {
        if(type.equals(TokenType.Document)){
            return ("Documented Comments ------> " + value);
        }else {
            return "Token ------> \t" + type.getTokenName()
                    + " ( " + type.getTokenNumber() + ", "
                    + value  + ", " + fileName + ", "
                    + lineNum + ", "
                    + (col - value.length() + 1)
                    + " )";
        }
    } // toString


    // main 메소드
    public static void main (String[] args) {

        System.out.println("eofTok 출력 결과 : "+eofTok); // eofTok를 출력한다.
        System.out.println("remAssignTok 출력 결과 : "+remAssignTok); // whileTok를 출력한다.
    }
} // Token
