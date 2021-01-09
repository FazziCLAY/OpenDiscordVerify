package ru.fazziclay.projects.discordverify;

import org.bukkit.configuration.file.FileConfiguration;


public class Config {
    Discord discord;
    CodeGeneration code_generation;
    Messages messages;

    public Config(FileConfiguration config) {
        discord = new Discord(config);
        code_generation = new CodeGeneration(config);
        messages = new Messages(config);
    }
}




class Discord {
    String bot_token;
    String channel;
    String mode;
    String role;
    boolean change_nick_in_minecraft;
    int message_delete_delay;

    public Discord(FileConfiguration config) {
        bot_token = config.getString("discord.bot-token");
        channel = config.getString("discord.channel");
        mode = config.getString("discord.mode");
        role = config.getString("discord.role");
        change_nick_in_minecraft = config.getBoolean("discord.change-nick-in-minecraft");
        message_delete_delay = config.getInt("discord.message-delete-delay");
    }
}


class Messages {
    String code_command;
    String code_expired;
    String code_you_already;
    String discord_code_not_found;
    String account_linked;


    public Messages(FileConfiguration config) {
        code_command = config.getString("messages.code-command");
        code_expired = config.getString("messages.code-expired");
        code_you_already = config.getString("messages.code-you-already");
        discord_code_not_found = config.getString("messages.discord-code-not-found");
        account_linked = config.getString("messages.account-linked");
    }
}


class CodeGeneration {
    int maximum;
    int minimum;
    int delay;

    public CodeGeneration(FileConfiguration config) {
        maximum = config.getInt("code-generation.maximum");
        minimum = config.getInt("code-generation.minimum");
        delay = config.getInt("code-generation.delay");
    }
}