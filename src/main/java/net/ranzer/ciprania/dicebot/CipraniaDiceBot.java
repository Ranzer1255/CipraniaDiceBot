package net.ranzer.ciprania.dicebot;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.ranzer.ciprania.dicebot.config.BotConfiguration;
import net.ranzer.ciprania.dicebot.data.GuildManager;
import net.ranzer.ciprania.dicebot.functions.listeners.CommandListener;
import net.ranzer.ciprania.dicebot.util.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 *
 * Raffle bot Built for The League of Ordinary Gamers
 *
 * @author Ranzer
 *
 */
public class CipraniaDiceBot {
	
	private static JDA JDA;
	public final static LocalDateTime START_TIME = LocalDateTime.now();
	private static final BotConfiguration config = BotConfiguration.getInstance();

	public static void main (String[] args){
		Logging.info("Huu... Wha... who... Oh, I guess it's time to [start up]");

		JDABuilder build;

		//setup intients
		Collection<GatewayIntent> intents = Arrays.asList(
			GatewayIntent.GUILD_MEMBERS, //privileged
			GatewayIntent.DIRECT_MESSAGES,
			GatewayIntent.GUILD_MESSAGES
		);
		
		//set token
		if (config.isDebug()) {
			build = JDABuilder.create(config.getTestToken(),intents);
		} else {			
			build = JDABuilder.create(config.getToken(),intents);
		}

		build.enableIntents(GatewayIntent.GUILD_MEMBERS);
		build.setMemberCachePolicy(MemberCachePolicy.ALL);
		//add Listeners
		//TODO setup each module as its own command listner
		build.addEventListeners(/*new LevelUpdater(),*/
							   new StartUpListener());
		build.setActivity(Activity.watching("Waking up, please wait"));
		build.setStatus(OnlineStatus.DO_NOT_DISTURB);
		
		//build
		try {
			JDA = build.build();
		} catch (LoginException | IllegalArgumentException e) {
			Logging.error(e.getMessage());
			Logging.log(e);
		}
	}	

	public static JDA getJDA(){
		return JDA;
	}
	
	private static class StartUpListener extends ListenerAdapter{
		
		
		
		@Override
		public void onReady(ReadyEvent event) {
			super.onReady(event);
			JDA=event.getJDA();
			JDA.addEventListener(CommandListener.getInstance(),
								 new GuildManager());
			JDA.getPresence().setActivity(Activity.playing(config.getStatus()));
			
			Logging.info("Done Loading and ready to go!");
			JDA.getPresence().setStatus(OnlineStatus.ONLINE);
		}
		
		@Override
		public void onShutdown(ShutdownEvent event) {
		
			Logging.info("Shutting down....");
			JDA.getPresence().setStatus(OnlineStatus.IDLE);
			JDA.getPresence().setActivity(Activity.playing("shutting down...."));
		}
		
	}
}
