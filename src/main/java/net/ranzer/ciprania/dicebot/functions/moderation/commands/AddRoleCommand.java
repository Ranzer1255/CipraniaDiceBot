package net.ranzer.ciprania.dicebot.functions.moderation.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.commands.Category;
import net.ranzer.ciprania.dicebot.commands.Describable;
import net.ranzer.ciprania.dicebot.data.GuildManager;
import net.ranzer.ciprania.dicebot.functions.moderation.RoleManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddRoleCommand extends BotCommand implements Describable {
	private RoleManager rm;
	public AddRoleCommand(){
		rm = RoleManager.getInstance();
	}

	@Override
	protected boolean isApplicableToPM() {
		return false;
	}

	@Override
	protected void process(String[] args, MessageReceivedEvent event) {

		System.out.println(Arrays.toString(args));

		if (args.length<3){
			return;
		}

		//parse role
		List<Role> list = event.getJDA().getRolesByName(args[0],true);
		if(list.size()==0){
			event.getChannel().sendMessage(String.format("i'm sorry i can't find role `%s`", args[0])).queue();
			return;
		}
		Role role = list.get(0);

		//parse members to add role to
		List<Member> users = event.getMessage().getMentionedMembers();
		if(users.size()!=1){
			event.getChannel().sendMessage("i'm sorry but you must mention the user/s to whom you would like to add this role").queue();
			return;
		}

		//parse number of days
		int days;
		try {
			days = Integer.parseInt(args[args.length-1]);
		} catch (NumberFormatException e) {
			event.getChannel().sendMessage(String.format("i'm sorry but i didn't understand `%s`",args[args.length-1])).queue();
			return;
		}

		//apply roles
		for (Member m :	users) {
			try {
				rm.addRole(role,m,days);
			} catch (InsufficientPermissionException pe) {
				event.getChannel().sendMessage(
						String.format("i'm sorry but i lack the `%s` permission in the server settings to do this",
						pe.getPermission().getName())).queue();
			}
		}
	}

	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public List<String> getAlias() {
		return Collections.singletonList("addrole");
	}

	@Override
	public String getShortDescription() {
		return "adds a role to a user for a number of days";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription();
	}

	@Override
	public Permission getPermissionRequirements() {
		return Permission.ADMINISTRATOR;
	}

	@Override
	protected List<Role> getRoleRequirements(Guild guild) {
		return GuildManager.getGuildData(guild).getModRoles();
	}

	@Override
	public String getUsage(Guild g) {
		return String.format("`%s%s <role> <mentioned user/s> <num of days>`",getPrefix(g),getName());
	}
}
