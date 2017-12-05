package Priusbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class priusbot {
	public static String prefix = null;
	private static final Logger logger = LoggerFactory.getLogger("Priusbot");
	// Insert Token Here
	// Prefix used for all commands, enter it in here.
	private static IDiscordClient client;
	
	public static void createFile() {
		Properties prop = new Properties();
		OutputStream output = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			output = new FileOutputStream("bot.properties");
			// Get Variables and assign them
			System.out.println("Insert token"); // Token
			String token = br.readLine();
			logger.debug("Input:" + token);
			prop.setProperty("Token", token);
			// Prefix
			System.out.println("Input prefix");
			String prefix = br.readLine();
			prop.setProperty("Prefix", prefix);
			logger.debug("Input:" + prefix);
			// Save Properties
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		}

	}
	public static void main(String[] args) throws DiscordException, RateLimitException {
		System.out.println("PriusBot v. 0.0.4 \"Inspiring\"");
		System.out.println("Copyright (c) 2017 SpacePrius Released under MIT license.");
		System.out.println("Starting up...");
		System.out.println("Loading bot.properties...");
		Properties prop = new Properties();
		InputStream input = null;
		String token = null;
		
		try {
			input = new FileInputStream("bot.properties");
			prop.load(input);
			if (prop.isEmpty()) {
				System.out.println("bot.properties not found, creating...");
				logger.debug("Launching File Wizard...");
				createFile(); 
			} 
				// load token
			prop.load(input);
				token = prop.getProperty("Token");
				prefix = prop.getProperty("Prefix");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		client = new ClientBuilder().withToken(token).build(); // Creating a client.
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
		if (split.length >= 1 && split[0].startsWith(prefix)) {
			String command = split[0].replaceFirst(prefix, "");
			String[] args = split.length >= 2 ? Arrays.copyOfRange(split, 1, split.length) : new String[0];
			// Test Command
			if (command.equalsIgnoreCase("test")) {
				test(channel);
				logger.debug("Message test command called by " + user.getName() + "in " + channel.getName());
			}
			if (command.equalsIgnoreCase("embedtest")) {
				embed(user, profilePicture, channel);
				logger.debug("Embed test command called by " + user.getName() + "in " + channel.getName());
			}
			if (command.equalsIgnoreCase("inspireme")) {
				inspire(channel);
				logger.debug("inspireme called by " + user.getName() + "in " + channel.getName());
			}

		}
	}

	private void test(IChannel channel) {
		channel.sendMessage("test!");
		logger.debug("Message sent");
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
		Document doc = Jsoup.connect("http://inspirobot.me/api?generate=true").get(); // Retrieves the api page
		String body = doc.select("body").text(); // Takes api output, selects the body, and turns that into text
		logger.debug("inspirobot api output:" + body);
		URL connection = new URL(body); //New URL from API
		URLConnection connectionOpen = connection.openConnection(); //Turns into Opens connection
		connectionOpen.addRequestProperty("User-Agent", "Mozilla/4.0");
		InputStream openConnection = connectionOpen.getInputStream();
		MessageBuilder message = new MessageBuilder(client);
		message.withFile(openConnection, "inspire.png"); //Message with this file
		message.withChannel(channel); //Message in channel
		message.build(); // Sends embed
		logger.debug("Message sent in channel: " + channel.getName());
	}
}
