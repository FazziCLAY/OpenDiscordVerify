package ru.fazziclay.projects.discordverify;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main extends JavaPlugin {
    // Переменные
    private Exception error;
    public static Config config;
    public static JDA jda = null;
    public static ArrayList<String> index_players = new ArrayList<String>();
    public static ArrayList<String> index_codes = new ArrayList<String>();
    public static Map<String, Map> data = new HashMap<String, Map>();


    // Рыбки
    @Override
    public void onEnable() {
        getLogger().info("##########################");
        getLogger().info("## §aGithub: §bhttps://github.com/FazziClay/OpenDiscordVerify");
        getLogger().info("##");
        getLogger().info("## §aPlugin starting...");

        loadConfig();
        if (!loadBot()) {
            getLogger().info("§c############################");
            getLogger().info("§c##");
            getLogger().info("§c## §eERROR: Bot starting error. ");
            getLogger().info("§c##");
            getLogger().info("§c############################");
            error.printStackTrace();
        }

        getLogger().info("## §aPlugin started!");
        getLogger().info("##########################");
    }

    @Override
    public void onDisable() {
        getLogger().info("## §cPlugin stopped!");
        jda.shutdownNow();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,   Command command,   @NotNull String label,   String[] args) {
        if (command.getName().equalsIgnoreCase("code")) {

            // Смотреть нету ли игрока в index_players, а в index_players находяться ники игроков которые уже запрашивали код в течении config.code_generation.delay
            if (index_players.contains(sender.getName())) {
                sender.sendMessage(config.messages.code_you_already.replace("&", "§"));
                return true;
            }

            //
            int code = Utils.getRandom(config.code_generation.minimum, config.code_generation.maximum); // Генерация кода
            sender.sendMessage(config.messages.code_command.replace("&", "§").replace("%CODE%", String.valueOf(code))); // Отправка сообщения
            index_players.add(sender.getName()); // Добавление ника в переменную что бы смотреть не выдавался ли код ранее
            index_codes.add(String.valueOf(code));


            String uuid;
            if (!sender.getName().equalsIgnoreCase("CONSOLE")) {
                Player p = (Player) sender;
                uuid = p.getUniqueId().toString();
            } else {
                uuid = "null";
            }

            Map<String, Object> data_temp = new HashMap<String, Object>();
            data_temp.put("nick", sender.getName());
            data_temp.put("uuid", uuid);
            data_temp.put("sender_obj", sender);

            data.put(""+code, data_temp);


            // Запуск таймера для убирания ника из index_players
            BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(this, new Runnable() {
                @Override
                public void run() {
                    sender.sendMessage(config.messages.code_expired.replace("&", "§"));
                    index_players.remove(sender.getName());
                    index_codes.remove(String.valueOf(code));
                    data.remove(""+code);
                }
            }, config.code_generation.delay * 20L);


            //
            if (config.discord.mode.equalsIgnoreCase("api")) {
                TextChannel d = jda.getTextChannelById(config.discord.channel);
                d.sendMessage("{'code':'"+code+"', 'time':'"+System.currentTimeMillis()+"', 'sender':{'name':'"+sender.getName()+"', 'uuid':'"+uuid+"'}}").queue();

            }
        }
        return true;
    }


    private void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = new Config(getConfig());

        if (config.discord.mode.equalsIgnoreCase("default") || config.discord.mode.equalsIgnoreCase("api")) {} else {
            getLogger().info("§c############################");
            getLogger().info("§c##");
            getLogger().info("§c## §eERROR: config.yml -> discord.mode not 'api' else 'default'. ");
            getLogger().info("§c##");
            getLogger().info("§c############################");
        }
    }

    private boolean loadBot() {
        try {
            jda = JDABuilder.createDefault(config.discord.bot_token)
                    .addEventListeners(new Bot())
                    .build();

            jda.awaitReady();

            return true;

        } catch (LoginException | InterruptedException e) {
            error = e;
            return false;
        }
    }

}
