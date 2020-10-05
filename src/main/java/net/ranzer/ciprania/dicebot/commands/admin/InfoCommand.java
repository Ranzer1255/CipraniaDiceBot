package net.ranzer.ciprania.dicebot.commands.admin;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import net.ranzer.ciprania.dicebot.CipraniaDiceBot;
import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.commands.Category;
import net.ranzer.ciprania.dicebot.commands.Describable;
import net.ranzer.ciprania.dicebot.config.BotConfiguration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand extends BotCommand implements Describable{

	private static final String REQUIRED_PERMISSIONS = "19456";

	@Override
	public void process(String[] args, MessageReceivedEvent event) {

		EmbedBuilder eb = new EmbedBuilder();
		MessageBuilder mb = new MessageBuilder();
		User bot = event.getJDA().getSelfUser();
		
		if (event.isFromGuild()) {
			eb.setColor(event.getGuild().getMember(bot).getColor());
		}
		
		if (args.length == 1&&args[0].equals("stats")) { // 1 argument !info stats
			eb = statusEmbed(event.getJDA().getSelfUser());
		} else { // !info
			eb = infoEmbed(bot);
		}

		event.getChannel().sendMessage(mb.setEmbed(eb.build()).build()).queue();

	}

	static private EmbedBuilder statusEmbed(User bot) {
		EmbedBuilder rtn = coreEmbed(bot);
		rtn.addField("Guilds", String.valueOf(bot.getJDA().getGuilds().size()), false)
		  .addField("Users", countNonBotUsers(bot.getJDA()), true)
		  .addField("Bots", countBotUsers(bot.getJDA()), true)
		  .addField("Up Time",getUpTime(), true)
		  .addField("Game", bot.getJDA().getPresence().getActivity().getName(), true);
		return rtn;
	}

	static private EmbedBuilder infoEmbed(User bot) {
		EmbedBuilder rtn = coreEmbed(bot);
		  rtn.addField("Version", BotConfiguration.getInstance().getVersion(), true)
		  .addField("Language", "Java", true)
		  .addField("Artwork", "needed", false)
//		  .addField("Invite me!", inviteLinkBuilder(bot), true)
		  .addField("GitHub Repo", "[GitHub](https://github.com/Ranzer1255/CipraniaDiceBot)\n[Bugs and Suggestions](https://github.com/Ranzer1255/CipraniaDiceBot/issues)", true)
		  .setFooter("Please report bugs or suggestions in the link above", null);
		return rtn;
	}

	static private EmbedBuilder coreEmbed(User bot) {
		EmbedBuilder rtn = new EmbedBuilder();
		rtn.setAuthor(bot.getName(), "https://github.com/Ranzer1255/CipraniaDiceBot", bot.getAvatarUrl())
		  .setTitle("Dice Bot for Ciprania",null)
		  .setDescription("Written by Ranzer")
		  .setThumbnail(bot.getAvatarUrl());
		return rtn;
	}

	@SuppressWarnings("StringConcatenationInsideStringBufferAppend")
	private static String getUpTime() {
		StringBuilder sb = new StringBuilder();
		LocalDateTime now = LocalDateTime.now();
		
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.YEARS)!=0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.YEARS)+" Yrs, ");
			now=now.minusYears(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.YEARS));
		}
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MONTHS)!= 0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MONTHS)+" Mths, ");
			now=now.minusMonths(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MONTHS));
		}
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.DAYS)!=0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.DAYS)+" Days, ");
			now=now.minusDays(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.DAYS));
		}
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.HOURS)!=0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.HOURS)+" Hrs, ");
			now=now.minusHours(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.HOURS));
		}
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MINUTES)!=0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MINUTES)+" Mins, ");
			now=now.minusMinutes(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.MINUTES));
		}
		if(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.SECONDS)!=0){
			sb.append(CipraniaDiceBot.START_TIME.until(now, ChronoUnit.SECONDS)+" Secs, ");
		}
		
		
		sb.delete(sb.length()-2, sb.length());
		sb.append(".");
		
		return sb.toString();
	}

	private static String countBotUsers(JDA api) {
		int count = 0;
		
		for(User u:api.getUsers()){
			if (u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	private static String countNonBotUsers(JDA api) {
		int count = 0;
		
		for(User u:api.getUsers()){
			if (!u.isBot()){
				count++;
			}
		}
		
		return String.valueOf(count);
	}

	@Override
	public String getUsage(Guild g) {
		return "`"+getPrefix(g)+getName()+" [stats]`";

	}

	@Override
	public List<String> getAlias() {

		return Arrays.asList("info", "i");
	}

	@Override
	public String getShortDescription() {

		return "Information about Caex and Author";
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public String getLongDescription() {
		return    "This command gives detailed information about the bot\n\n"
				+ "`stats`: displays misc. stats reported by JDA";
	}

	@Override
	public boolean isApplicableToPM() {
		return true;
	}

}
