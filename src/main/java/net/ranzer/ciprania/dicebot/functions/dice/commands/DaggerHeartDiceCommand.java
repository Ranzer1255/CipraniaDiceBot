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

public class DaggerHeartDiceCommand extends BotCommand implements Describable {
	@Override
	protected boolean isApplicableToPM() {
		return true;
	}

	@Override
	protected void process(String[] args, MessageReceivedEvent event) {

		if(args.length>1){
			if(event.isFromGuild()){
				invalidUsage(event.getGuild());
			} else {
				invalidUsage(null);
			}
			return;
		}
		DiceRoll roll;
		if(args.length==0) roll = DiceRollBuilder.newDaggerRoll("0");
		else roll = DiceRollBuilder.newDaggerRoll(args[0]);

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
		return Arrays.asList("dh", "dagger","hope");
	}

	@Override
	public String getShortDescription() {
		return "Rolls both Hope and Fear dice of the Dagger Heart system";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription() + "\n\n" +
		       "Shows 2 different D12s one Labeled Hope: 'H' and one Labled Fear: 'F' these are summed with your modifier, " +
		       "either positive or negative to give you your total With Either Hope, or Fear, Which ever is the higher of the 2 Dice. " +
		       "Doubles of *any* kind are treated as a Critical Success.\n\n" +
		       "Examples:\n" +
		       "Command: "+getName()+" 5\n"+
		       "Returns: 2d12+5: (Hope: 5, Fear: 4) + 5 = 14 With Hope!";
	}

	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" <+|-><modifier>'";
	}
}
