package net.ranzer.ciprania.dicebot.functions.dice.expressions;

import net.ranzer.ciprania.dicebot.functions.dice.Lexer;

import java.util.ArrayList;


public class Atomic extends Expression {

	public Atomic(ArrayList<Lexer.Token> tokens) {
		super(tokens);
		for (Lexer.Token token : tokens) {
			if (token.type == Lexer.TokenType.ATOMIC) {
				value = Integer.parseInt(token.data);
			}
			description.append(token.data);
		}
	}

}
