package com.desticube.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.API;
import com.desticube.annotations.Command;
import com.desticube.objects.AbstractCommand;

public class CommandHandler {
	JavaPlugin main;
	String pluginname;
	Set<Class<?>> classes;
	public CommandHandler(JavaPlugin main, String pluginname, Set<Class<?>> classes) {
		this.main = main;
		this.pluginname = pluginname;
		this.classes = classes;
		return;
	}
	
	public void startup() {
		main.getLogger().info("Loading commands..");
		for (Class<?> clazz : classes) {
	    	if (AbstractCommand.class.isAssignableFrom(clazz)) {
	    		Annotation[] annons = clazz.getAnnotations();
	    		for (Annotation annon : annons) {
	    			if (annon instanceof Command) {
	    				Command myannon = (Command) annon;
			    		try {
			    			AbstractCommand cmdclass = (AbstractCommand) clazz.getConstructor().newInstance();
							cmdclass.register(myannon.command(), "", myannon.description(), "", Arrays.asList(myannon.aliases()), pluginname);
				    		API.a().server().getCommands().put(myannon, pluginname);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e) {
							e.printStackTrace();
						}
	    			}
	    		}
	    	}
		}
		main.getLogger().info("Finished loading commands");
	}
	
	
	
	public HashMap<Command, String> cmds()	{return API.a().server().getCommands();}

}
