package Priusbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import java.util.Arrays;

public class priusbot {

	private static final String TOKEN = "Mjg3MzM1MTc1NDUxNDQzMjAx.DQHiwA.MiSVNybhCS5oBZ1XOdCy1p0UfJM";
	private static final String PREFIX = "p!";
	private static IDiscordClient client;
	
	public static void main(String[] args) throws DiscordException, RateLimitException {
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
		String[] split = message.getContent().split(" "); //Splits message by space
		//Test to see if it even works
		if (split.length >= 1 && split[0].startsWith(PREFIX)) {
			String command = split[0].replaceFirst(PREFIX, "");
			String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
			//Test Command
			if (command.equalsIgnoreCase("test")) {
				test(channel);
			}
		}
	}
	private void test(IChannel channel) {
		channel.sendMessage("test!");
	}

}

