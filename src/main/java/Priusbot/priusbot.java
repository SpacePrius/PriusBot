package Priusbot;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.audio.AudioPlayer;
import sx.blah.discord.util.audio.events.TrackFinishEvent;
import sx.blah.discord.util.audio.events.TrackQueueEvent;
import sx.blah.discord.util.audio.events.TrackStartEvent;
import sx.blah.discord.*;


import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
	public void onMessage(MessageRecievedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException {
		IMessage message = event.getMessage(); //Gets messages
		IUser user = message.getAuthor(); //Gets the author
		if (user.isBot()) return; //If the user is a bot ignore it
		
		IChannel channel = message.getChannel(); //Gets the channel
		IGuild guild = message.getGuild(); //Gets the guild
		String[] split = message.getContent().split(" "); //Splits message by space
		//Test to see if it even works
		if (split.length >= 1 && split[0].startsWith(PREFIX)) {
			String commmand = split[0].replaceFirst(PREFIX, "");
			String[] args = split.length >= 2 ? Arrays.copyOfRange(split,  1,  split.length) : new String[0];
			//Test Command
			if (command.equalsIgnoreCase("test")) {
				guild = event.getPlayer().getGuild();
				channel.get(guild).sendMessage("Test"); //Sends Test
			}
		}
	}
}

