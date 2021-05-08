package com.example.procomsearch.tokenizer;
/**
 * Author:Yuliang Ma
 * Reference: Lab materials
 */

public class Token {
    private String _token = "";

    private Type _type = Type.UNKNOWN;
    public Token(String token, Type type) {
        _token = token;
        _type = type;
    }

    public String token() {
        return _token;
    }

    public Type type() {
        return _type;
    }

    public enum Type {UNKNOWN, ATTRIBUTE, BIGGER, SMALLER, EQUAL, DOU, AND, OR, NOT, LBRA, RBRA}
}
