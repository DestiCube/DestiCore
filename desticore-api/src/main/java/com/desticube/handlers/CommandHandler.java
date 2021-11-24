package com.desticube.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.desticube.API;
import com.desticube.annotations.Command;
import com.desticube.objects.AbstractCommand;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class CommandHandler {
//    HashMap<Command, String> cmds = API.a().server().getCommands();
//	HashMap<String, ICommand> aliases;
	
	public CommandHandler(Plugin plug, JavaPlugin main, String pluginname, String pkg) {
//		aliases = new HashMap<String, ICommand>();
		main.getLogger().info("Loading commands..");
		try (ScanResult scanResult =
		        new ClassGraph()
//	            	.verbose()               // Log to stderr
		            .enableAnnotationInfo()  // Scan classes, methods, fields, annotations
		            .acceptPackages(pkg)     // Scan com.xyz and subpackages (omit to scan all packages)
		            .scan()) {               // Start the scan
    		main.getLogger().info("Starting command");
		    for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation("com.desticube.annotations.Command")) {
	    		main.getLogger().info("Scanning classes with annotations");
		    	Class<?> clazz = routeClassInfo.loadClass();
		    	if (AbstractCommand.class.isAssignableFrom(clazz)) {
		    		main.getLogger().info("Cmd is AbstractCommand");
		    		Annotation[] annons = clazz.getAnnotations();
		    		for (Annotation annon : annons) {
			    		main.getLogger().info("Checking annon for cmd..");
		    			if (annon instanceof Command) {
				    		main.getLogger().info("Annon is Command");
		    				Command myannon = (Command) annon;
				    		try {
				    			//String command, String usage, String description, String permissionMessage, List<String> aliases
				    			AbstractCommand cmdclass = (AbstractCommand) clazz.getConstructor().newInstance();
					    		main.getLogger().info("Cast cmd to abstract");
								cmdclass.register(myannon.command(), "", myannon.description(), "", Arrays.asList(myannon.aliases()), pluginname);
					    		main.getLogger().info("register command");
					    		API.a().server().getCommands().put(myannon, pluginname);
					    		main.getLogger().info("put in list");
								
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException e) {
								e.printStackTrace();
							}
		    			}
		    		}
		    	}
		    }
		}
		main.getLogger().info("Finished loading commands");
		return;
	}
	
	
	public HashMap<Command, String> cmds()	{return API.a().server().getCommands();}

}