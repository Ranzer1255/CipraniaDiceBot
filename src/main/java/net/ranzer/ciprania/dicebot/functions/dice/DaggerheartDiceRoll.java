package net.ranzer.ciprania.dicebot.functions.dice;

import java.util.ArrayList;

public class DaggerheartDiceRoll extends DiceRoll{

	private int hopeDie;
	private int fearDie;

	private int modifier;
	private final boolean crit;

	public DaggerheartDiceRoll(String input, ArrayList<Lexer.Token> tokens) {
		super(input, tokens);
		roll();
		parseDice(breakdown);
		crit = hopeDie==fearDie;
	}

	@Override
	public String getTotal() {
		return String.valueOf(total);
	}

	@Override
	public String getLongReadout() {
		StringBuilder rtn = new StringBuilder();
		rtn.append(breakdown, 0, breakdown.indexOf('('));

		if(crit){
			rtn.append(String.format("(Hope: **%d**, Fear: **%d**)",hopeDie,fearDie));
		}else {
			rtn.append(String.format("(Hope: %d, Fear: %d)",hopeDie,fearDie));
		}

		rtn.append(" + ").append(modifier).append(" = ").append(total).append(" ");

		if(crit){
			rtn.append("**Critical!!**");
		} else if (hopeDie>fearDie) {
			rtn.append("With Hope!");
		} else {
			rtn.append("With Fear!");
		}

		return rtn.toString();
	}

	private void parseDice(String readout){

		String[] numbers = readout.substring(readout.indexOf('(')+1,readout.indexOf(')')).split(", ");
		System.out.println(breakdown);
		System.out.println(numbers.length);
		hopeDie = Integer.parseInt(numbers[0].replace("*",""));
		fearDie = Integer.parseInt(numbers[1].replace("*",""));
		modifier = Integer.parseInt(readout.substring(readout.indexOf('+')+1).replace(" ",""));

	}
}
