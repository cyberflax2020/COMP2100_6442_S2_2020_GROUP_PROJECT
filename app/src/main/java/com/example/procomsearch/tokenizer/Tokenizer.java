package com.example.procomsearch.tokenizer;
/**
 * Author:Yuliang Ma, Yuchen Wang
 * Reference: Lab materials
 */


public class Tokenizer {

    private String _buffer;        //save text
    private Token currentToken;    //save token extracted from next()
    private static int bracketMatchNum;


    public Tokenizer(String text) {
        // Initialization of bracketMarchNum, it's used to find whether the number of left
        // brackets is matched with right brackets.
        this.bracketMatchNum = 0;
        _buffer = text.toUpperCase();
        next();
    }


    public Boolean checkBracketMatching() {
        /**
         * Author:Yuliang Ma
         */
        // bracketMatchNum == 1 means that one right bracket is missing, so return false.
        if (this.bracketMatchNum == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public boolean hasNext() {
        return currentToken != null;
    }

    public Token current() {
        return currentToken;
    }

    public void next() {
        /**
         * Author:Yuliang Ma
         */
        _buffer = _buffer.trim();

        if (_buffer.isEmpty()) {
            this.bracketMatchNum++;
            currentToken = null;
            return;
        }

        char firstChar = _buffer.charAt(0);
        // Justify the type of the token
        if (firstChar == '=')
            currentToken = new Token("=", Token.Type.EQUAL);
        else if (firstChar == '>')
            currentToken = new Token(">", Token.Type.BIGGER);
        else if (firstChar == '<')
            currentToken = new Token("<", Token.Type.SMALLER);
        else if (firstChar == '&')
            currentToken = new Token("&", Token.Type.AND);
        else if ((firstChar == '|') || (firstChar == ';'))
            currentToken = new Token("|", Token.Type.OR);
        else if (firstChar == '!')
            currentToken = new Token("!", Token.Type.NOT);
        else if (firstChar == '(')
            currentToken = new Token("(", Token.Type.LBRA);
        else if (firstChar == ')')
            currentToken = new Token(")", Token.Type.RBRA);
        else if (Character.isLetter(firstChar)) {
            int start = 0;
            String word = "";
            while ((Character.isLetter(firstChar)) && start < _buffer.length()) {
                word = word + String.valueOf(_buffer.charAt(start));
                start++;

                if (start < _buffer.length()) {
                    firstChar = _buffer.charAt(start);
                }
            }
            // word means attributes of one company, for example "CODE" is unique ID
            // of one company.
            switch (word) {
                case "CODE":
                    currentToken = new Token("CODE", Token.Type.ATTRIBUTE);
                    break;

                case "NPA":
                    currentToken = new Token("NPA", Token.Type.ATTRIBUTE);
                    break;

                case "NPG":
                    currentToken = new Token("NPG", Token.Type.ATTRIBUTE);
                    break;
                case "TOIA":
                    currentToken = new Token("TOIA", Token.Type.ATTRIBUTE);
                    break;
                case "TOIG":
                    currentToken = new Token("TOIG", Token.Type.ATTRIBUTE);
                    break;
                case "OE":
                    currentToken = new Token("OE", Token.Type.ATTRIBUTE);
                    break;
                case "SE":
                    currentToken = new Token("SE", Token.Type.ATTRIBUTE);
                    break;
                case "ME":
                    currentToken = new Token("ME", Token.Type.ATTRIBUTE);
                    break;
                case "FE":
                    currentToken = new Token("FE", Token.Type.ATTRIBUTE);
                    break;
                case "TOE":
                    currentToken = new Token("TOE", Token.Type.ATTRIBUTE);
                    break;
                case "OP":
                    currentToken = new Token("OP", Token.Type.ATTRIBUTE);
                    break;
                case "TP":
                    currentToken = new Token("TP", Token.Type.ATTRIBUTE);
                    break;
                default:
                    currentToken = new Token(word, Token.Type.UNKNOWN);
            }
        } else if ((Character.isDigit(firstChar)) || (firstChar == '-')) {
            int start = 0;
            String Dou = "";

            // If firstChar == '-' and the latter char is not digit, consider it as Unknown Type.
            if ((firstChar == '-') && (!Character.isDigit(_buffer.charAt(1)))) {
                currentToken = new Token("-", Token.Type.UNKNOWN);
            } else {
                // Use length of the buffer as condition to get the int type number, double type number,
                // and negative number(with a "-").
                while (((Character.isDigit(firstChar)) && start < _buffer.length()) || ((firstChar == '.') && start < _buffer.length()) || ((firstChar == '-') && start < _buffer.length())) {
                    // Use while loop to get each digit, start is the position of the digit in the buffer.
                    Dou = Dou + String.valueOf(_buffer.charAt(start));
                    start++;
                    if (start < _buffer.length()) {
                        firstChar = _buffer.charAt(start);
                    }
                }

                currentToken = new Token(Dou, Token.Type.DOU);
            }
        } else {
            currentToken = new Token(Character.toString(_buffer.charAt(0)), Token.Type.UNKNOWN);

        }

        int tokenlen = currentToken.token().length();
        _buffer = _buffer.substring(tokenlen);
    }
}