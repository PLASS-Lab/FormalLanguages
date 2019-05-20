D      [0-9]
L      [a-zA-Z_]
H      [a-fA-F0-9]
E      [Ee][+-]?{D}+
FS      (f|F|l|L)
IS      (u|U|l|L)*

%{
#include <stdio.h>
/* #include "c_tab.h" */

enum token_number { TAUTO,TBREAK,TCASE,TCHAR,TCONST,TCONTINUE,
		    TDEFAULT,TDO,TDOUBLE,TELSE,TENUM,TEXTERN,
		    TFLOAT,TFOR,TGOTO,TIF,TINT,TLONG,TREGISTER,
		    TRETURN,TSHORT,TSINGLE,TSIZEOF,TSTATIC,
		    TSTRUCT,TSWITCH,TUNION,TUNSIGNED,TVOID,
		    TVOLATILE,TWHILE,TCONSTANT,TSTRING_LITERAL,
		    TELLIPSIS,TRIGHT_ASSIGN,TLEFT_ASSIGN,
		    TADD_ASSIGN,TSUB_ASSIGN,TMUL_ASSIGN,
		    TDIV_ASSIGN,TMOD_ASSIGN,TAND_ASSIGN,
		    TXOR_ASSIGN,TOR_ASSIGN,TRIGHT_OP,TLEFT_OP
		    TINC_OP,TDEC_OP,TPTR_OP,TAND_OP,TOR_OP,
		    TLE_OP,TGE_OP,TEQ_OP,TNE_OP,TIDENTIFIER,
		    TASSIGN_OP,TNOT_OP,TCOMPLEMENT_OP };

void count();
%}

%%
"/*"        { comment(); }

"auto"      { count(); return(TAUTO); }
"break"     { count(); return(TBREAK); }
"case"      { count(); return(TCASE); }
"char"      { count(); return(TCHAR); }
"const"     { count(); return(TCONST); }
"continue"  { count(); return(TCONTINUE); }
"default"   { count(); return(TDEFAULT); }
"do"        { count(); return(TDO); }
"double"    { count(); return(TDOUBLE); }
"else"      { count(); return(TELSE); }
"enum"      { count(); return(TENUM); }
"extern"    { count(); return(TEXTERN); }
"float"     { count(); return(TFLOAT); }
"for"       { count(); return(TFOR); }
"goto"      { count(); return(TGOTO); }
"if"        { count(); return(TIF); }
"int"       { count(); return(TINT); }
"long"      { count(); return(TLONG); }
"register"  { count(); return(TREGISTER); }
"return"    { count(); return(TRETURN); }
"short"     { count(); return(TSHORT); }
"signed"    { count(); return(TSIGNED); }
"sizeof"    { count(); return(TSIZEOF); }
"static"    { count(); return(TSTATIC); }
"struct"    { count(); return(TSTRUCT); }
"switch"    { count(); return(TSWITCH); }
"union"     { count(); return(TUNION); }
"unsigned"  { count(); return(TUNSIGNED); }
"void"      { count(); return(TVOID); }
"volatile"  { count(); return(TVOLATILE); }
"while"     { count(); return(TWHILE); }

{L}({L}|{D})*     { count(); return(TIDENTIFIER); }

0[xX]{H}+{IS}?    { count(); return(TCONSTANT); }
0{D}+{IS}?        { count(); return(TCONSTANT); }
{D}+{IS}?         { count(); return(TCONSTANT); }
L?'(\\.|[^\\'])+' { count(); return(TCONSTANT); }

{D}+{E}{FS}?      { count(); return(TCONSTANT); }
{D}*"."{D}+({E})?{FS}?  { count(); return(TCONSTANT); }
{D}+"."{D}*({E})?{FS}?  { count(); return(TCONSTANT); }

L?\"(\\.|[^\\"])*\" { count(); return(TSTRING_LITERAL); }

"..."       { count(); return(TELLIPSIS); }
">>="       { count(); return(TRIGHT_ASSIGN); }
"<<="       { count(); return(TLEFT_ASSIGN); }
"+="        { count(); return(TADD_ASSIGN); }
"-="        { count(); return(TSUB_ASSIGN); }
"*="        { count(); return(TMUL_ASSIGN); }
"/="        { count(); return(TDIV_ASSIGN); }
"%="        { count(); return(TMOD_ASSIGN); }
"&="        { count(); return(TAND_ASSIGN); }
"^="        { count(); return(TXOR_ASSIGN); }
"|="        { count(); return(TOR_ASSIGN); }
">>"        { count(); return(TRIGHT_OP); }
"<<"        { count(); return(TLEFT_OP); }
"++"        { count(); return(TINC_OP); }
"--"        { count(); return(TDEC_OP); }
"->"        { count(); return(TPTR_OP); }
"&&"        { count(); return(TAND_OP); }
"||"        { count(); return(TOR_OP); }
"<="        { count(); return(TLE_OP); }
">="        { count(); return(TGE_OP); }
"=="        { count(); return(TEQ_OP); }
"!="        { count(); return(TNE_OP); }
";"         { count(); return(';'); }
("{"|"<%")  { count(); return('{'); }
("}"|"%>")  { count(); return('}'); }
","         { count(); return(','); }
":"         { count(); return(':'); }
"="         { count(); return(TASSIGN_OP); }
"("         { count(); return('('); }
")"         { count(); return(')'); }
("["|"<:")  { count(); return('['); }
("]"|":>")  { count(); return(']'); }
"."         { count(); return('.'); }
"&"         { count(); return('&'); }
"!"         { count(); return(TNOT_OP); }
"~"         { count(); return(TCOMPLEMENT_OP); }
"-"         { count(); return('-'); }
"+"         { count(); return('+'); }
"*"         { count(); return('*'); }
"/"         { count(); return('/'); }
"%"         { count(); return('%'); }
"<"         { count(); return('<'); }
">"         { count(); return('>'); }
"^"         { count(); return('^'); }
"|"         { count(); return('|'); }
"?"         { count(); return('?'); }

[ \t\v\n\f] { count(); }
.           { return(TERROR); }   /* bad characters */

%%

int yywrap()
{
  printf( "." );
  return(1);
}

void comment()    /* processing comment */
{
  char c, c1;

loop:
  while( ( c = input() ) != '*' && c != 0 );
//    putchar( c );

  if( ( c1 = input() ) != '/' && c != 0 ) {
    unput( c1 );
    goto loop;
  }

  if( c != 0 );
//    putchar( c1 );
}

int column = 0;

void count()
{
  int i;

  for( i = 0; yytext[i] != '\0'; i++ )
    if (yytext[i] == '\n') 
    {
      column = 0;
      printf( "." );
      ECHO;
    }
    else if( yytext[i] == '\t' )
      column += 4 - ( column % 4 );     /* assuming tab size == 4 */
    else
      column++;

//  ECHO;
}







	    			
			     			
			     				  	
	
	
	



	      
	     
	     
	   
	       
		