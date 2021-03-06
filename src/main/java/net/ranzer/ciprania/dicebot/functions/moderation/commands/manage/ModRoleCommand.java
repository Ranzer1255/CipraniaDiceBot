package net.ranzer.ciprania.dicebot.functions.moderation.commands.manage;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.commands.Category;
import net.ranzer.ciprania.dicebot.commands.Describable;
import net.ranzer.ciprania.dicebot.commands.admin.HelpCommand;
import net.ranzer.ciprania.dicebot.data.GuildManager;
import net.ranzer.ciprania.dicebot.util.StringUtil;

import java.util.Arrays;
import java.util.List;

public class ModRoleCommand extends BotCommand implements Describable {
	@Override
	protected boolean isApplicableToPM() {
		return false;
	}

	@Override
	protected void process(String[] args, MessageReceivedEvent event) {

		if (args.length == 0) {
			List<Role> roles = GuildManager.getGuildData(event.getGuild()).getModRoles();
			StringBuilder sb = new StringBuilder();

			for (Role r :
					roles) {
				sb.append(r.getName()).append(" ,");
			}
			sb.delete(sb.length() - 1, sb.length());

			event.getChannel().sendMessage(String.format(
					"Allowed Roles: %s",
					sb.toString()
			)).queue();
		} else {
			switch (args[0]) {
				case "add":
					List<Role> roles = event.getJDA().getRolesByName(
						StringUtil.arrayToString(Arrays.copyOfRange(args, 1, args.length), " "),
						true);
					if (roles.isEmpty()) {
						roleNotFound(event.getChannel());
						return;
					}
					Role r = roles.get(0);

					if (GuildManager.getGuildData(event.getGuild()).addModRole(r)) {
						roleAdd(event.getChannel(), r, true);
					} else {
						roleAdd(event.getChannel(), r, false);
					}
					break;
				case "remove":
					roles = event.getJDA().getRolesByName(
							StringUtil.arrayToString(Arrays.copyOfRange(args, 1, args.length), " "),
							true);
					if (roles.isEmpty()) {
						roleNotFound(event.getChannel());
						return;
					}
					r = roles.get(0);

					if (GuildManager.getGuildData(event.getGuild()).removeModRole(r)) {
						roleRemove(event.getChannel(), r, true);
					} else {
						roleRemove(event.getChannel(), r, false);
					}
					break;
				default:
					event.getChannel().sendMessage(HelpCommand.getDescription(this/*, event.getGuild()*/)).queue();
					break;
			}


		}
	}

	private void roleRemove(MessageChannel channel, Role r, boolean b) {
		channel.sendMessage(String.format(
				"The role %s was %s",
				r.getName(),
				b?"removed":"not in the list"
		)).queue();
	}

	private void roleAdd(MessageChannel channel, Role r, boolean b) {
		channel.sendMessage(String.format(
				"The role %s was %s",
				r.getName(),
				b?"added":"already in the list"
		)).queue();
	}

	private void roleNotFound(MessageChannel channel) {
		channel.sendMessage(
				"I'm sorry but i cannot find that role"
		).queue();
	}


	@Override
	public Category getCategory() {
		return Category.ADMIN;
	}

	@Override
	public List<String> getAlias() {
		return Arrays.asList("modrole");
	}

	@Override
	public String getShortDescription() {
		return "adds and removes roles that are allowed to moderate this server";
	}

	@Override
	public String getLongDescription() {
		return getShortDescription()+"\n\n" +
				"add: add the specified role to the list\n" +
				"remove: remove the specified role from the list";
	}

	@Override
	public String getUsage(Guild g) {
		return String.format(
				"`%s%s [{add | remove} <role>]",
				getPrefix(g),
				getName()
		);
	}
}
