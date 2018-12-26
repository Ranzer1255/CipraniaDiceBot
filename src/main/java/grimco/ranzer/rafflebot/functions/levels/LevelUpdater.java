package grimco.ranzer.rafflebot.functions.levels;

import java.util.concurrent.ThreadLocalRandom;

import grimco.ranzer.rafflebot.data.GuildManager;
import grimco.ranzer.rafflebot.data.IGuildData;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

//TODO change the XP eanrings to be adjustable
//todo add xp decay
//todo change alert system to PM Member once they reach the xp threshold for Raffle participation

/*
idea, rebuild database to include a table of xp history

**xp history table**
guild_id, member_id, earned, timestamp


a member's xp will be the sum of all the member's rows of this table

delete old rows according to decay time
 */

public class LevelUpdater extends ListenerAdapter{


	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event){
		IGuildData gd = GuildManager.getGuildData(event.getGuild());

		if (isXPChannel(event, gd)) {
			if (isNotBot(event)) {
				if (isNotTimedOut(event, gd)) {
					System.out.println("adding xp");
					gd.getMemberData(event.getMember()).addXP(getRandomXP(gd));
				}
			} 
		}
		
	}
	
	private boolean isNotTimedOut(GuildMessageReceivedEvent event, IGuildData gd) {
		
		return gd.getMemberData(event.getMember())==null
				||(System.currentTimeMillis()- gd.getMemberData(event.getMember()).lastXP()) > gd.getXPTimeout();
	}
	
	private boolean isNotBot(GuildMessageReceivedEvent event) {
		return (event.getAuthor() != event.getJDA().getSelfUser()) && !event.getAuthor().isBot();
	}
	
	private boolean isXPChannel(GuildMessageReceivedEvent event, IGuildData gd) {
		return gd.getChannel(event.getChannel()).getXPPerm();
	}
	
	private int getRandomXP(IGuildData gd) {
		
		return ThreadLocalRandom.current().nextInt(gd.getXPLowBound(), gd.getXPHighBound()+1);
	}

}
