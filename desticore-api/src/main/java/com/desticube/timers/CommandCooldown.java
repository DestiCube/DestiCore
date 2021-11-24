package com.desticube.timers;

import java.util.HashMap;
import java.util.UUID;

public class CommandCooldown {
	HashMap<HashMap<UUID, Double>, HashMap<String, Double>> cooldowns = new HashMap<HashMap<UUID, Double>, HashMap<String, Double>>();
	HashMap<UUID, Double> startinfo = new HashMap<UUID, Double>();
	HashMap<String, Double> commandinfo = new HashMap<String, Double>();
	
	
	
	
}
