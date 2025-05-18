package fr.maxlego08.essentials.commands.commands.teleport;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.commands.CommandResultType;
import fr.maxlego08.essentials.api.commands.Permission;
import fr.maxlego08.essentials.api.messages.Message;
import fr.maxlego08.essentials.api.user.TeleportRequest;
import fr.maxlego08.essentials.module.modules.TeleportationModule;
import fr.maxlego08.essentials.zutils.utils.commands.VCommand;
import org.bukkit.entity.Player;

public class CommandTeleportAccept extends VCommand {

    public CommandTeleportAccept(EssentialsPlugin plugin) {
        super(plugin);
        this.setModule(TeleportationModule.class);
        this.setPermission(Permission.ESSENTIALS_TPA_ACCEPT);
        this.setDescription(Message.DESCRIPTION_TPA_ACCEPT);
        this.onlyPlayers();
        this.addOptionalPlayerNameArg(); // Allow /tpaccept [player]
    }

    @Override
    protected CommandResultType perform(EssentialsPlugin plugin) {
        Player fromPlayer = null;
        TeleportRequest teleportRequest = null;
        if (args.length > 0) {
            fromPlayer = this.argAsPlayer(0);
            if (fromPlayer == null) {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }
            teleportRequest = this.user.getTeleportRequestFrom(fromPlayer.getUniqueId());
            if (teleportRequest == null) {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }
        } else {
            teleportRequest = this.user.getTeleportRequest();
            if (teleportRequest == null) {
                message(sender, Message.COMMAND_TPA_ERROR_TO_LATE);
                return CommandResultType.DEFAULT;
            }
        }

        if (!teleportRequest.isValid()) {
            this.user.removeTeleportRequest(teleportRequest.getFromUser());
            message(sender, Message.COMMAND_TPA_ERROR_TO_LATE_2);
            return CommandResultType.DEFAULT;
        }

        teleportRequest.accept();
        this.user.removeTeleportRequest(teleportRequest.getFromUser());

        return CommandResultType.SUCCESS;
    }
}
