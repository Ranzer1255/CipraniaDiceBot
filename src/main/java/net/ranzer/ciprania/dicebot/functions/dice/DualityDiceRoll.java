package net.ranzer.ciprania.dicebot.functions.dice;

import java.util.ArrayList;

public class DualityDiceRoll extends DiceRoll{

	private int hopeDie;
	private int fearDie;

	private final boolean crit;

	public DualityDiceRoll(String input, ArrayList<Lexer.Token> tokens) {
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

		String diceBreakout;
		if(crit){
			diceBreakout = "(Hope: **%d**, Fear: **%d**)";
		} else if (hopeDie>fearDie){ //with Hope
			diceBreakout = "(__**Hope**: %d__, Fear: %d)";
		} else { //with Fear
			diceBreakout = "(Hope: %d, __**Fear**: %d__)";
		}

		rtn.append(String.format(diceBreakout,hopeDie,fearDie));

		rtn.append(breakdown,breakdown.indexOf(')')+1,breakdown.length()).append(" = ").append(total).append(" ");

		if(crit){
			rtn.append("**Critical!!**");
		} else if (hopeDie>fearDie) {
			rtn.append("With Hope!");
		} else {
			rtn.append("With Fear...");
		}

		return rtn.toString();
	}

	private void parseDice(String readout){
		String[] numbers = readout.substring(readout.indexOf('(')+1,readout.indexOf(')')).split(", ");
		hopeDie = Integer.parseInt(numbers[0].replace("*",""));
		fearDie = Integer.parseInt(numbers[1].replace("*",""));
	}
}
