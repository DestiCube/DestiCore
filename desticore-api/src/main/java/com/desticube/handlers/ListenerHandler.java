package com.desticube.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import org.bukkit.plugin.Plugin;

import com.desticube.interfaces.IListener;

public class ListenerHandler {
	private ListenerHandler() { }
    static ListenerHandler instance = new ListenerHandler();
    public static ListenerHandler a() {return instance;}
	
	Plugin plug;	
	
	public boolean setup(Plugin plug, Set<Class<?>> classes) {
		this.plug = plug;
		for (Class<?> clazz : classes) {
			if (IListener.class.isAssignableFrom(clazz)) {
	    		IListener listen = null;
				try {
					listen = (IListener) clazz.getConstructor().newInstance();
		    		plug.getServer().getPluginManager().registerEvents(listen, plug);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
}
