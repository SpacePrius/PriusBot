package Priusbot;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class priusbot {

	private static final String TOKEN = "12345"; // Insert Token Here
	private static final String PREFIX = "p!"; // Prefix used for all commands, enter it in here.
	private static IDiscordClient client;

	public static void main(String[] args) throws DiscordException, RateLimitException {
		System.out.println("PriusBot v. 0.0.3 \"Inspired\"");
		System.out.println("Copyright (c) 2017 SpacePrius Released under MIT license.");
		System.out.println("Starting");

		client = new ClientBuilder().withToken(TOKEN).build(); // Creating a client.
		client.getDispatcher().registerListener(new priusbot());
		client.login();
	}

	@EventSubscriber
	public void onReadyEvent(ReadyEvent event) {
		System.out.println("Connection Successful!");
	}

	@EventSubscriber
	public void onMessage(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		IMessage message = event.getMessage(); // Gets messages
		IUser user = message.getAuthor(); // Gets the author
		if (user.isBot())
			return; // If the user is a bot ignore it

		IChannel channel = message.getChannel(); // Gets the channel
		IGuild guild = message.getGuild(); // Gets the guild
		String profilePicture = user.getAvatarURL();
		String[] split = message.getContent().split(" "); // Splits message by space
		// Test to see if it even works
		if (split.length >= 1 && split[0].startsWith(PREFIX)) {
			String command = split[0].replaceFirst(PREFIX, "");
			String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
			// Test Command
			if (command.equalsIgnoreCase("test")) {
				test(channel);
				System.out.println(user + "Called test");
			}
			if (command.equalsIgnoreCase("embedtest")) {
				embed(user, profilePicture, channel);
			}
			if (command.equalsIgnoreCase("inspireme")) {
				inspire(channel);
			}
		}
	}

	private void test(IChannel channel) {
		channel.sendMessage("test!");
	}

	/*
	 * This creates an embed in order to test how embeds work
	 */
	private void embed(IUser user, String avatar, IChannel channel)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		EmbedBuilder embed = new EmbedBuilder(); // New Embed
		embed.withTitle("testing"); // Embed Title
		embed.withAuthorName(user.getName()); // Person who sent it is the author
		embed.withAuthorIcon(avatar); // Users avatar
		embed.appendField("test", "doubletest", false); // Appends test field
		EmbedObject finalembed = embed.build(); // Builds Embed
		channel.sendMessage(finalembed); // Sends message
	}

	/*
	 * This creates an inspirobot image and embeds it.
	 */
	private void inspire(IChannel channel) throws IOException {
		Document doc = Jsoup.connect("http://inspirobot.me/api?generate=true").get();
		String body = doc.select("body").text();
		String image = body;
		EmbedBuilder inspiration = new EmbedBuilder();
		inspiration.withImage(image);
		EmbedObject finalimage = inspiration.build();
		channel.sendMessage(finalimage);
	}
}
