%{
   /* lex source for MiniPascal */
%}
%%
"array"         return(TARRAY);
"begin"         return(TBEGIN);
"const"         return(TCONST);
"div"           return(TDIV);
"do"            return(TDO);
"end"           return(TEND);
"if"            return(TIF);
"integer"       return(TINTEGER);
"mod"           return(TMOD);
"of"            return(TOF);
"procedure"     return(TPROCEDURE);
"program"       return(TPROGRAM);
"then"          return(TTHEN);
"var"           return(TVAR);
"while"         return(TWHILE);
"<>"            return(TNEQ);
"<="            return(TLESSE);
">="            return(TGREATE);
":="            return(TASSIGN);
".."            return(TDDOT);
[A-Za-z][A-Za-z0-9]*     return(TIDENT);
[0-9]+          return(TNUM);
"(*"([^*]|"*"+[^*)])*"*"+")"   ;
[ \t\n]         ;
.               return(yytext[0]);
%%
int yywrap()
{
   return 1;
}
