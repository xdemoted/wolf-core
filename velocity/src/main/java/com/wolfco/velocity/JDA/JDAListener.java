package com.wolfco.velocity.JDA;

import java.awt.Color;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import javax.annotation.Nonnull;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.wolfco.velocity.wolfcore;
import com.wolfco.velocity.types.Code;
import dev.dejvokep.boostedyaml.YamlDocument;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class JDAListener extends ListenerAdapter implements EventListener {
    private static wolfcore plugin;
    private Guild guild;
    private TextChannel channel;
    private Webhook activeHook = null;
    static JDA jda;
    private ArrayList<Code> codes = new ArrayList<>();

    public Code createCode(String uuid) {
        Code Code = new Code();
        Code.UUID = uuid;
        Code.Code = generateCode();
        codes.add(Code);
        return Code;
    }

    public String generateCode() {
        ArrayList<String> activeCodes = new ArrayList<>();
        codes.forEach(code -> {
            activeCodes.add(code.Code);
        });
        String code = "";
        String[] codeMap = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z" };
        while (true) {
            for (int i = 0; i < 6; i++) {
                int numCode = (int) Math.floor(Math.random() * 36);
                if (numCode > 9) {
                    code = code + codeMap[numCode - 10];
                } else {
                    code = code + String.valueOf(numCode);
                }
            }
            if (!activeCodes.contains(code)) {
                break;
            }
        }
        return code;
    }

    public JDAListener(wolfcore plugn) throws InterruptedException {
        plugin = plugn;
        YamlDocument config = plugin.config;
        jda = JDABuilder.createDefault(config.getString("token"))
                .addEventListeners(this)
                .build();
        jda.updateCommands().addCommands(
                Commands.slash("online", "Show all online players and their location"),
                Commands.slash("link", "Used for the purpose of linking")
                        .addOption(OptionType.STRING, "code", "This is the code sent by the minecraft server", true),
                Commands.slash("ping", "pong!")).queue();
        jda.awaitReady();
        String gui = config.getString("guild");
        String chan = config.getString("channel");
        if (jda != null && chan != null && gui != null) {
            guild = jda.getGuildById(gui);
            channel = guild.getTextChannelById(chan);
        } else {
            plugin.logger.severe("Null values in config");
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ping":
                event.reply("Pong!").queue();
                break;
            case "online":
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle("Online Players")
                        .setDescription("This lists all players currently online.")
                        .setColor(Color.GREEN);
                plugin.server.getAllPlayers().forEach(player -> {
                    embed.addField(player.getUsername(), player.getCurrentServer().get().getServerInfo().getName(), false);
                });
                event.reply("")
                        .addEmbeds(embed.build())
                        .queue();
                break;
            case "link":
            default:
                break;
        }
    }

    public synchronized boolean sendMessage(String Name, String Message, String AvatarURL) throws IOException {
        if (Name == null || channel == null) {
            return false;
        }
        if (activeHook == null) {
            List<Webhook> hooks = channel.retrieveWebhooks().complete();
            if (hooks.size() >= 1) {
                activeHook = hooks.get(0);
            } else {
                channel.createWebhook("jdahook")
                        .queue((webhook) -> {
                            activeHook = webhook;
                        });
            }
            if (activeHook == null) {
                plugin.logger.warning("Could not create webhook");
                return false;
            }
        }
        WebhookClient hookClient = WebhookClient.withUrl(activeHook.getUrl());
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(Name);
        builder.setContent(Message);
        builder.setAvatarUrl(AvatarURL);
        hookClient.send(builder.build());
        return true;
    }

    public synchronized boolean sendMessage(String Name, String Message) throws MalformedURLException, IOException {
        return sendMessage(Name, Message,
                "https://cdn.discordapp.com/attachments/758884272572071944/1124798370809118740/wolf_wback.png");
    }
}