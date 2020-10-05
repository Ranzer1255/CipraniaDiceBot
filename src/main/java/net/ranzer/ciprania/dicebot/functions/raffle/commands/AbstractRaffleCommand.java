package net.ranzer.ciprania.dicebot.functions.raffle.commands;

import net.ranzer.ciprania.dicebot.commands.BotCommand;
import net.ranzer.ciprania.dicebot.commands.Category;
import net.ranzer.ciprania.dicebot.commands.Describable;
import net.ranzer.ciprania.dicebot.data.GuildManager;
import net.ranzer.ciprania.dicebot.data.IRaffleData;
import net.ranzer.ciprania.dicebot.functions.raffle.Raffle;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRaffleCommand extends BotCommand implements Describable {

    protected static final Map<TextChannel, Raffle> raffles = new HashMap<>();

    @Override
    public boolean isApplicableToPM() {
        return false;
    }

    @Override
    public Category getCategory() {
        return Category.RAFFLE;
    }

    /**
     * gets the Roles that can manage Raffles in the supplied Guild
     * @param guild guild for which these roles apply
     * @return a List<Role> of all the Roles allowed to manage raffles
     */
    protected static List<Role> getAllowedManagementRoles(Guild guild) {
       return GuildManager.getGuildData(guild).getRaffleData().allowedRaffleRoles();
    }

    protected IRaffleData getRaffleData(Guild g){
        return GuildManager.getGuildData(g).getRaffleData();
    }
}
