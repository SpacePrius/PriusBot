package Priusbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.*;
import java.util.Arrays;

public class priusbot {

	private static final String TOKEN = "Mjg3MzM1MTc1NDUxNDQzMjAx.DQHiwA.MiSVNybhCS5oBZ1XOdCy1p0UfJM";
	private static final String PREFIX = "p!";
	private static IDiscordClient client;
	
	public static void main(String[] args) throws DiscordException, RateLimitException {
		System.out.println("Copyright (c) 2017 SpacePrius Released under MIT license.");
		System.out.println("Connecting...");
		client = new ClientBuilder().withToken(TOKEN).build(); //Creating a client.
		client.getDispatcher().registerListener(new priusbot());
		client.login();
	}
	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Connection Successful!");
	}
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		IMessage message = event.getMessage(); //Gets messages
		IUser user = message.getAuthor(); //Gets the author
		if (user.isBot()) return; //If the user is a bot ignore it
		
		IChannel channel = message.getChannel(); //Gets the channel
		IGuild guild = message.getGuild(); //Gets the guild
		String profilePicture = user.getAvatarURL();
		String[] split = message.getContent().split(" "); //Splits message by space
		//Test to see if it even works
		if (split.length >= 1 && split[0].startsWith(PREFIX)) {
			String command = split[0].replaceFirst(PREFIX, "");
			String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
			//Test Command
			if (command.equalsIgnoreCase("test")) {
				test(channel);
				System.out.println(user + "Called test");
			}
			if (command.equalsIgnoreCase("embedtest")) {
				embed(user, profilePicture, channel);
			}
		}
	}
	private void test(IChannel channel) {
		channel.sendMessage("test!");
	}
	private void embed(IUser user, String avatar, IChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withTitle("testing");	
		embed.withAuthorName(user.getName());
		embed.withAuthorIcon(avatar);
		embed.appendField("test","doubletest",false);
		EmbedObject finalembed = embed.build();
		channel.sendMessage(finalembed);
	}

}

