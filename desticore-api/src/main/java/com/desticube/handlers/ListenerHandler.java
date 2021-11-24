package com.desticube.handlers;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.plugin.Plugin;

import com.desticube.interfaces.IListener;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

public class ListenerHandler {
	private ListenerHandler() { }
    static ListenerHandler instance = new ListenerHandler();
    public static ListenerHandler a() {return instance;}
	
	Plugin plug;	
	
	public boolean setup(Plugin plug, String pkg) {
		this.plug = plug;
		
		String routeAnnotation = "com.desticube.annotations.Listener";
		try (ScanResult scanResult =
		        new ClassGraph()
//	            	.verbose()               // Log to stderr
		            .enableAllInfo()         // Scan classes, methods, fields, annotations
		            .acceptPackages(pkg)     // Scan com.xyz and subpackages (omit to scan all packages)
		            .scan()) {               // Start the scan
		    for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(routeAnnotation)) {
		    	Class<?> clazz = routeClassInfo.loadClass();
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
		}
		return true;
	}
}
