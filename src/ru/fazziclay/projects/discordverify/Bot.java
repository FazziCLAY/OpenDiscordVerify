package ru.fazziclay.projects.discordverify;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Timer;
import java.util.TimerTask;

public class Bot extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Variables
        Message msg = event.getMessage();
        MessageChannel channel = event.getChannel();

        if (msg.getAuthor().isBot()) {
            return;
        }


        if (Main.config.discord.mode.equalsIgnoreCase("default")) { // Если включен стандартный режим default а не api
            if (channel.getId().equals(Main.config.discord.channel)) { // Если канал сообщения == каналу в plugin.yml -> discord.channel
                if (Main.index_codes.contains(msg.getContentRaw())) { // Если текущий код есть в index_codes
                    // Премененные
                    String nick = (String) Main.data.get(msg.getContentRaw()).get("nick");
                    CommandSender sender = (CommandSender) Main.data.get(msg.getContentRaw()).get("sender_obj");

                    try {
                        // Выдача роли
                        Role role = event.getGuild().getRoleById(Main.config.discord.role);
                        if (role != null) {
                            event.getGuild().addRoleToMember(msg.getMember(), role).queue();
                        }

                    } catch (Exception e) {
                        Bukkit.getLogger().info("§c############################");
                        Bukkit.getLogger().info("§c##");
                        Bukkit.getLogger().info("§c## JavaError: " + e.toString());
                        Bukkit.getLogger().info("§c##");
                        Bukkit.getLogger().info("§c############################");
                    }


                    try {
                        // Смена ника если в plugin.yml так сказано
                        if (Main.config.discord.change_nick_in_minecraft) {
                            msg.getMember().modifyNickname(nick);
                        }

                    } catch (Exception e) {
                        Bukkit.getLogger().info("§c############################");
                        Bukkit.getLogger().info("§c##");
                        Bukkit.getLogger().info("§c## JavaError: " + e.toString());
                        Bukkit.getLogger().info("§c##");
                        Bukkit.getLogger().info("§c############################");
                    }

                    // Оповещение о привязанном аккаунте в майнкрафте
                    sender.sendMessage(Main.config.messages.account_linked.replace("&", "§").replace("%USER_NAME%", msg.getAuthor().getName()).replace("%USER_DISCRIMINATOR%", msg.getAuthor().getDiscriminator()));

                } else { // Если текущего кода нету в index_codes
                    // Оповещение в майнкрафте
                    channel.sendMessage(Main.config.messages.discord_code_not_found.replace("&", "§")).queue(response -> {
                        // Таймер для удаления сообщение через 7 секунд.
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                response.delete().queue();
                            }
                        }, Main.config.discord.message_delete_delay * 1000L);
                    });
                }
                msg.delete().queue();

            }
        }
    }
}
