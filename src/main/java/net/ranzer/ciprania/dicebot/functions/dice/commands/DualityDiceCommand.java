package net.ranzer.ciprania.dicebot.functions.dice.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.commands.Category;
import net.ranzer.ciprania.dicebot.commands.Describable;
import net.ranzer.ciprania.dicebot.functions.dice.DiceRoll;
import net.ranzer.ciprania.dicebot.functions.dice.DiceRollBuilder;

import java.util.Arrays;
import java.util.List;

public class DualityDiceCommand extends BotCommand implements Describable {
	@Override
	protected boolean isApplicableToPM() {
		return true;
	}

	@Override
	protected void process(String[] args, MessageReceivedEvent event) {


		StringBuilder expression = new StringBuilder();

		for (String arg : args) {
			expression.append(" ").append(arg);
		}

		DiceRoll roll;
		if(args.length==0) roll = DiceRollBuilder.newDualityRoll();
		else roll = DiceRollBuilder.newDualityRoll(expression.substring(1));

		event.getChannel().sendMessage(
				String.format("%s: %s",
		            event.getAuthor().getAsMention(),
		            (roll.getLongReadout())))
				.queue();

	}

	@Override
	public Category getCategory() {
		return Category.MISC;
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("duality","dd", "dh","rdd","hope");
	}

	@Override
	public String getShortDescription() {
		return "Rolls the Duality dice of the Daggerheart system";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription() + "\n\n" +
		       "Shows 2 different D12s one Labeled Hope: 'H' and one Labled Fear: 'F' these are summed with your modifier, " +
		       "either positive or negative to give you your total With Either Hope, or Fear, Which ever is the higher of the 2 Dice. " +
		       "Doubles of *any* kind are treated as a Critical Success.\n\n" +
			   "Include advantage or disadvantage die by adding it as a part of your modifier in standard RPG dice notation, \n\n" +
		       "Examples:\n" +
		       "Command: "+getName()+" 5\n"+
		       "Returns: 2d12 (__**Hope**: 5__, Fear: 4) + 5 = 14 With Hope!\n" +
			   "Advantage: "+getName()+" 1d6 + 5\n" +
			   "Returns: 2d12 (Hope: 3, __**Fear**: 11__) + 1d6 (6) + 1 = 21 With Fear...\n" +
			   "Disadvantage: "+getName()+" -1d6 + 5\n" +
			   "Returns: 2d12 (Hope: 3, __**Fear**: 11__) + -1d6 (6) + 1 = 9 With Fear...";
	}

	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <+|-><modifier>'";
	}
}
