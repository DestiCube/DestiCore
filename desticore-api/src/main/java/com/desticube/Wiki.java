package com.desticube;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import com.desticube.annotations.Command;
import com.desticube.handlers.CommandHandler;

public class Wiki {
	private Wiki() { }
    static Wiki instance = new Wiki();
    public static Wiki b() {return instance;}
    boolean even;
	public void create(Plugin pl) throws IOException, URISyntaxException {
		pl.saveResource("wiki/wiki.html", true);
//		pl.saveResource("wiki/bootstrap.min.css", true);
		File file = new File(pl.getDataFolder() + "/wiki/wiki.html");
		String htmlString = FileUtils.readFileToString(file);
		StringBuilder body = new StringBuilder();
		ArrayList<Command> commands = new ArrayList<Command>();
		API.a().server().getCommands().keySet().forEach(cmd -> {
			if (!commands.contains(cmd)) commands.add(cmd);
		});
		even = false;
		commands.forEach(cmd -> {
			Command annon = cmd;
			if (annon instanceof Command) {
				List<String> permlist = new ArrayList<String>(Arrays.asList(annon.permissions().clone()));
				StringBuilder permstring = new StringBuilder();
				permlist.forEach(s -> {
					if (permlist.get(permlist.size() - 1).equalsIgnoreCase(s)) { permstring.append(s); return; }
					permstring.append(s + ", ");
				});
				List<String> aliaslist = new ArrayList<String>(Arrays.asList(annon.aliases().clone()));
				StringBuilder aliastring = new StringBuilder();
				aliaslist.forEach(s -> {
					if (aliaslist.get(aliaslist.size() - 1).equalsIgnoreCase(s)) { aliastring.append(s); return; }
					aliastring.append(s + ", ");
				});
				if (even == false) {
					body.append("<tr role=\"row\" class=\"even\">" + 
							"	<td class=\"sorting_1\">" + API.a().server().getCommands().get(cmd) + "</td>" + 
							"	<td>" + annon.command() + "</td>" + 
							"	<td>" + aliastring + "</td>" + 
							"	<td>" + annon.description() + "</td><td>\r\n" + 
							"	<code>" + permstring.toString() + "</code></td></tr>");
					even = true;
				} else {
					body.append("<tr role=\"row\" class=\"odd\">" + 
							"	<td class=\"sorting_1\">" + API.a().server().getCommands().get(cmd) + "</td>" + 
							"	<td>" + annon.command() + "</td>" + 
							"	<td>" + aliastring + "</td>" + 
							"	<td>" + annon.description() + "</td><td>\r\n" + 
							"	<code>" + permstring.toString() + "</code></td></tr>");
					even = false;
				}
			}
		});
		htmlString = htmlString.replace("$body", body.toString());
		File newHtmlFile = new File(pl.getDataFolder() + "/wiki/wiki.html");
		FileUtils.writeStringToFile(newHtmlFile, htmlString);
	}
	
	public void delete(Plugin pl) {
		File wiki = new File(pl.getDataFolder() + File.separator + "wiki/wiki.html");
		if (wiki.exists()) wiki.delete();
	}
	
}
