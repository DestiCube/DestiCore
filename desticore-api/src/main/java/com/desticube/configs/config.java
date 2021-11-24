package com.desticube.configs;

import java.util.HashMap;
import java.util.List;

public class config {
		static config instance = new config();
	    public static config a() {return instance;}
	    
		public static Integer GENERAL_TELEPORT_WARMUP, GENERAL_JUMP_DISTANCE;
		public static String GENERAL_SPAWN_WORLD, GENERAL_BROADCAST_FORMAT, GENERAL_MESSAGE_SENT_FORMAT, GENERAL_MESSAGE_RECIEVED_FORMAT, GENERAL_SOCIAL_SPY_FORMAT;
		public static Double GENERAL_NEAR_RADIUS;
		public static HashMap<String, List<String>> LIST_PLACEHOLDERS;
		public static List<String> LIST_FORMAT, ITEM_DB_FORMAT, ITEM_DB_FORMAT_FOR_TOOL, TPA_SENT_FORMAT, TPA_ACCEPTED_FORMAT, TPA_RECIEVED_FORMAT, 
									TPA_HERE_RECIEVED_FORMAT;
		public void setup() {
			GENERAL_TELEPORT_WARMUP = getInt("TeleportWarmup");
			GENERAL_BROADCAST_FORMAT = getString("BroadcastFormat");
			GENERAL_NEAR_RADIUS = getDouble("NearRadius");
			GENERAL_JUMP_DISTANCE = getInt("JumpDistance");
			LIST_PLACEHOLDERS = new HashMap<String, List<String>>();
			for (String s : ConfigYAML.a().getConfig().getConfigurationSection("ListRankPlaceHolders").getKeys(false)) {
				List<String> ranks = getStringList("ListRankPlaceHolders." + s);
				LIST_PLACEHOLDERS.put("%" + s + "%", ranks);
			}
			LIST_FORMAT = getStringList("ListFormat");
			ITEM_DB_FORMAT = getStringList("ItemDBFormat");
			ITEM_DB_FORMAT_FOR_TOOL = getStringList("ItemDBFormatForTool");
			GENERAL_MESSAGE_SENT_FORMAT = getString("MessageSentFormat");
			GENERAL_MESSAGE_RECIEVED_FORMAT = getString("MessageRecievedFormat");
			TPA_SENT_FORMAT = getStringList("TPASentFormat");
			TPA_ACCEPTED_FORMAT = getStringList("TPAAcceptedFormat");
			TPA_RECIEVED_FORMAT = getStringList("TPARecievedFormat");
			TPA_HERE_RECIEVED_FORMAT = getStringList("TPAHereRecievedFormat");
			GENERAL_SOCIAL_SPY_FORMAT = getString("SocialSpyFormat");
		}
		public String getString(String path) {return ConfigYAML.a().getConfig().getString(path);}
		public List<String> getStringList(String path) {return ConfigYAML.a().getConfig().getStringList(path);}
		public Boolean getBoolean(String path) {return ConfigYAML.a().getConfig().getBoolean(path);}
		public Integer getInt(String path) {return ConfigYAML.a().getConfig().getInt(path);}
		public Double getDouble(String path) {return ConfigYAML.a().getConfig().getDouble(path);}
		public Long getLong(String path) {return ConfigYAML.a().getConfig().getLong(path);}
}
