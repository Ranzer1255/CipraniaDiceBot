package net.ranzer.ciprania.dicebot.functions.dice;

import java.util.ArrayList;

public class DiceRollBuilder {

	public static DiceRoll newDiceRoll(String input){
		ArrayList<Lexer.Token> tokens;

		tokens = Lexer.lex(input);

		for(Lexer.Token t:tokens){
			if(t.type== Lexer.TokenType.TARGETNUMBER){
				return new TargetDiceRoll(input, tokens);
			}
		}
		return new SumDiceRoll(input, tokens);
	}

	public static DiceRoll newDualityRoll(String modifier) {
		ArrayList<Lexer.Token> tokens;

		String input = "2d12+"+modifier;

		tokens = Lexer.lex(input);

		return new DualityDiceRoll(input,tokens);
	}

	public static DiceRoll newDualityRoll(){
		ArrayList<Lexer.Token> tokens;

		String input = "2d12";

		tokens = Lexer.lex(input);

		return new DualityDiceRoll(input,tokens);
	}
}
