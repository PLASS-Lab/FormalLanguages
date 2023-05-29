//
//  Scanner.hpp
//  MiniCScanner
//
//  Created by 서희찬 on 2023/05/29.
//

#ifndef Scanner_hpp
#define Scanner_hpp

/***************************************************************
*      scanner routine for Mini C language                    *
*                                   2003. 3. 10               *
***************************************************************/

#pragma once


#define NO_KEYWORD 7
#define ID_LENGTH 12

struct tokenType {
    int number;
    union {
        char id[ID_LENGTH];
        int num;
    } value;
};


enum tsymbol {
    tnull = -1,
    tnot, tnotequ, tremainder, tremAssign, tident, tnumber,
    /* 0          1            2         3            4          5     */
    tand, tlparen, trparen, tmul, tmulAssign, tplus,
    /* 6          7            8         9           10         11     */
    tinc, taddAssign, tcomma, tminus, tdec, tsubAssign,
    /* 12         13          14        15           16         17     */
    tdiv, tdivAssign, tsemicolon, tless, tlesse, tassign,
    /* 18         19          20        21           22         23     */
    tequal, tgreat, tgreate, tlbracket, trbracket, teof,
    /* 24         25          26        27           28         29     */
    //   ...........    word symbols ................................. //
    /* 30         31          32        33           34         35     */
    tconst, telse, tif, tint, treturn, tvoid,
    /* 36         37          38        39                             */
    twhile, tlbrace, tor, trbrace
};


struct tokenType scanner();
void printToken(struct tokenType token);


#endif /* Scanner_hpp */
