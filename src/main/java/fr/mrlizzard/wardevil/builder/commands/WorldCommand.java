package fr.mrlizzard.wardevil.builder.commands;

import fr.mrlizzard.wardevil.builder.WardBuilder;
import fr.mrlizzard.wardevil.builder.managers.WorldManager;
import fr.mrlizzard.wardevil.builder.objects.World;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldCommand extends ACommand {

    private WorldManager                    manager;

    public WorldCommand(WardBuilder instance, String subCommand) {
        super(instance, subCommand, "Gestion des mondes");

        this.manager = instance.getWorldManager();
    }

    @Override
    public void loadSubCommands() {
        subCommands.put("list", () -> listWorlds());
    }

    @Override
    public void displayHelp() {
        sender.sendMessage("§c--[ §6WardBuilder | Worlds Help §c]--");
        sender.sendMessage("§e /build worlds help §f- §6Afficher la page d'aide");
        sender.sendMessage("§e /build worlds list §f- §6Afficher la liste");
        sender.sendMessage("§e /build worlds add <world> §f- §6Ajouter");
        sender.sendMessage("§e /build worlds del <world> §f- §6Supprimer");
        sender.sendMessage("§e /build worlds disable <world> §f- §6Désactiver");
        sender.sendMessage("§e /build worlds enable <world> §f- §6Activer");
        sender.sendMessage("§e /build worlds players list <world> §f- §6Liste des joueurs");
        sender.sendMessage("§e /build worlds players add <world> <name> §f- §6Ajouter joueur");
        sender.sendMessage("§e /build worlds players del <world> <name> §f- §6Supprimer joueur");
    }

    private void listWorlds() {
        Integer page = 1;
        Integer maxPages = 5;
        Integer key;
        List<World> worlds;

        if (args.length > 3) {
            sender.sendMessage("§cUsage: /build worlds list <page>");
            return;
        } else if (args.length == 3) {
            try {
                page = Integer.parseInt(args[2]);
            } catch (Exception err) {
                sender.sendMessage("§cLe numéro de page doit être un nombre.");
                return;
            }
        }

        key = ((page == 1) ? 0 : (maxPages * page - 1));
        worlds = new ArrayList<>(manager.getWorlds().values());

        if (worlds.size() == 0) {
            sender.sendMessage("§cAucun monde n'a été créé. Tapez /build worlds pour voir l'aide.");
            return;
        }

        sender.sendMessage("§eListe des mondes (1/" + maxPages + "):");
        for (int loop = 0; loop < maxPages; loop++) {
            World world = worlds.get(key);
            ChatColor color;

            if (world == null)
                break;

            color = ((world.isDisabled()) ? ChatColor.RED : ChatColor.GREEN);
            sender.sendMessage("  §b- " + color + world.getName());
        }
    }

    @Override
    public boolean executeCommand() {
        for (Map.Entry<String, Runnable> entry : subCommands.entrySet()) {
            if (args[1].equalsIgnoreCase(entry.getKey())) {
                entry.getValue().run();
                return true;
            }
        }

        sender.sendMessage("§cCommande inconnue. Taper /build worlds pour voir l'aide.");
        return true;
    }
}
