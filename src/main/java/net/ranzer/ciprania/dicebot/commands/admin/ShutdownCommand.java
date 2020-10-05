package net.ranzer.ciprania.dicebot.commands.admin;

import java.util.Collections;
import java.util.List;

import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.config.BotConfiguration;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ShutdownCommand extends BotCommand {
	
	@Override
	public void process(String[] args,  MessageReceivedEvent event) {
		if (!event.getAuthor().getId().equals(BotConfiguration.getInstance().getOwner())){
			noPermission(event);
			return;
		}
		event.getChannel().sendMessage("if you insist boss.... *blerg*").complete();
		try {Thread.sleep(1000L);} catch (InterruptedException ignored) {}
		event.getJDA().shutdown();
		System.exit(0);
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("vdri");//this is "sleep" in draconic
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}
}