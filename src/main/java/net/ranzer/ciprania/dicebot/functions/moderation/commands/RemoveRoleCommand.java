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

import java.util.Collections;
import java.util.List;

public class RemoveRoleCommand extends BotCommand implements Describable {
	private RoleManager rm;

	public RemoveRoleCommand(){
		rm = RoleManager.getInstance();
	}

	@Override
	protected boolean isApplicableToPM() {
		return false;
	}

	@Override
	protected void process(String[] args, MessageReceivedEvent event) {

//		System.out.println(Arrays.toString(args));

		if (args.length<2){
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

		//apply roles
		for (Member m :	users) {
			try {
				rm.removeRole(role,m);
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
		return Collections.singletonList("removerole");
	}

	@Override
	public String getShortDescription() {
		return "removes a timed role from a user";
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
		return String.format("`%s%s <role> <mentioned user/s>`",getPrefix(g),getName());
	}
}
