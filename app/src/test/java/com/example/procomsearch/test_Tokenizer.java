package com.example.procomsearch;


import com.example.procomsearch.tokenizer.Token;
import com.example.procomsearch.tokenizer.Tokenizer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class test_Tokenizer {


    private static Tokenizer tokenizer;
    private static final String testCase = "(NPA>50.23)&(OE=28.3)|(NPG<100.1)";
    private static final String passCase = "NPA>50.23";
    private static final String passCase1 = "&|!<=";

    @Test
    public void testToken() {
        tokenizer = new Tokenizer(passCase);
        assertEquals("wrong token type", Token.Type.ATTRIBUTE, tokenizer.current().type());
        assertEquals("wrong token value", "NPA", tokenizer.current().token());
    }

    @Test
    public void testNextToken() {
        tokenizer = new Tokenizer(passCase);
        tokenizer.next();
        assertEquals("wrong token type", Token.Type.BIGGER, tokenizer.current().type());
        assertEquals("wrong token value", ">", tokenizer.current().token());
    }

    @Test
    public void testDoubleToken() {
        tokenizer = new Tokenizer(passCase);
        tokenizer.next();
        tokenizer.next();
        assertEquals("wrong token type", Token.Type.DOU, tokenizer.current().type());
        assertEquals("wrong token value", "50.23", tokenizer.current().token());
    }

    @Test
    public void showall() {
        tokenizer = new Tokenizer(testCase);
        String rst = "";
        while (tokenizer.hasNext()) {
            rst = rst + tokenizer.current().token();
            System.out.println(tokenizer.current().type().toString());
            tokenizer.next();
        }
        System.out.println(rst);
        assertEquals(rst, testCase);
    }

    @Test
    public void testAnd_Exp() {
        tokenizer = new Tokenizer(testCase);
        String rst = "";
        while (tokenizer.hasNext()) {
            rst = rst + tokenizer.current().token();
            System.out.println(tokenizer.current().type().toString());
            tokenizer.next();
        }
        System.out.println(rst);
        assertEquals(rst, testCase);
    }
    // passCase1 = "&|!<="
    @Test
    public void testOr_Exp() {
        tokenizer = new Tokenizer(passCase1);
        tokenizer.next();
        //check the type of the token
        assertEquals("wrong token type", Token.Type.OR, tokenizer.current().type());

        //check the actual token value
        assertEquals("wrong token value", "|", tokenizer.current().token());
    }
    @Test
    public void testNot_Exp() {
        tokenizer = new Tokenizer(passCase1);
        tokenizer.next();
        tokenizer.next();
        //check the type of the token
        assertEquals("wrong token type", Token.Type.NOT, tokenizer.current().type());

        //check the actual token value
        assertEquals("wrong token value", "!", tokenizer.current().token());
    }
    @Test
    public void testSmaller_Exp() {
        tokenizer = new Tokenizer(passCase1);
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        //check the type of the token
        assertEquals("wrong token type", Token.Type.SMALLER, tokenizer.current().type());

        //check the actual token value
        assertEquals("wrong token value", "<", tokenizer.current().token());
    }
    @Test
    public void testEqual_Exp() {
        tokenizer = new Tokenizer(passCase1);
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        //check the type of the token
        assertEquals("wrong token type", Token.Type.EQUAL, tokenizer.current().type());

        //check the actual token value.
        assertEquals("wrong token value", "=", tokenizer.current().token());
    }
    @Test
    public void testTokenResult() {
        tokenizer = new Tokenizer(testCase);
        tokenizer.next();
        // testCase = "(NPA>50.23)&(OE=28.3)|(NPG<100.1)"

        // check the token "NPA"
        assertEquals("wrong token type", Token.Type.ATTRIBUTE, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "NPA", tokenizer.current().token());

        tokenizer.next();
        // check the token ">"
        assertEquals("wrong token type", Token.Type.BIGGER, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", ">", tokenizer.current().token());

        tokenizer.next();
        // check the first token NPA
        assertEquals("wrong token type", Token.Type.DOU, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "50.23", tokenizer.current().token());

        tokenizer.next();
        tokenizer.next();
        // check the token "&"
        assertEquals("wrong token type", Token.Type.AND, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "&", tokenizer.current().token());

        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        // check the token "="
        assertEquals("wrong token type", Token.Type.EQUAL, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "=", tokenizer.current().token());

        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        // check the token "|"
        assertEquals("wrong token type", Token.Type.OR, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "|", tokenizer.current().token());

        tokenizer.next();
        tokenizer.next();
        tokenizer.next();
        // check the token "<"
        assertEquals("wrong token type", Token.Type.SMALLER, tokenizer.current().type());
        //check the actual token value
        assertEquals("wrong token value", "<", tokenizer.current().token());

    }

}
