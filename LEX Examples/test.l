%{
#include <stdio.h>
%}
L       [a-zA-Z]
D       [0-9]
%%
{L}({L}|{D})*                   printf("     id : %s\n", yytext);
[a-zA-Z_][a-zA-Z0-9_]*          printf("   C id : %s\n", yytext);
[+-]?[0-9]+                     printf("integer : %s\n", yytext);
[0-9]+"."[0-9]+(e[+-]?[0-9]+)?  printf("   real : %s\n", yytext);
\'([^']|"''")*\'                printf(" string : %s\n", yytext);
"(*"([^*]|"*"+[^*)])*"*"+")"    printf(" Comment form : (* ... *)\n");
"//".*                          printf(" Comment form : // ... \n");

"\n" |
.    ;
%%
int yywrap()
{
  return 1;
}

void main()
{
  yylex();
}

