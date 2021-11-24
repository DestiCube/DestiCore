package com.desticube.configs;

public class msg {
	private msg() { }
    static msg instance = new msg();
    public static msg a() {return instance;}
	public static String ECO_CURRENCY_PLURAL, ECO_CURRENCY_SINGULAR, ECO_CURRENCY_FORMAT, ECO_POSITIVE, ECO_NOT_ENOUGH, ECO_SENT_TO, ECO_RECIEVED_FROM,
		   ECO_MONEY_TOGGLE_ON, ECO_MONEY_TOGGLE_OFF, ECO_MONEY_DENIED, ECO_MONEY_YOURSELF, ECO_MONEY, ECO_MONEY_PLAYER, ECO_PAY_ACCOUNT,
		   ECO_PAY_NOT_NUMBER, ECO_TOP, ECO_TOP_RANK, ECO_TOP_NOT_FOUND, ECO_TOP_ALL_MONEY;
	public static String GENERAL_NO_PERMISSIONS, GENERAL_PLAYER_NOT_ONLINE, GENERAL_CHANGE_GAMEMODE, GENERAL_SEEN_HOVER_FORMAT, GENERAL_SEEN_FORMAT_OFFLINE, 
				  GENERAL_SEEN_FORMAT_ONLINE, GENERAL_CHANGE_GAMEMODE_OTHER, GENERAL_SIGN_EDIT_NOT_SIGN, GENERAL_REPAIR_ITEM, GENERAL_REPAIR_ALL_ITEMS,
				  GENERAL_TPALL, GENERAL_TPOFFLINE, GENERAL_PLAYER_NEVER_JOINED, GENERAL_PLAYER_ONLY_COMMAND, GENERAL_CLEAR_INVENTORY, GENERAL_CLEAR_INVENTORY_OTHER,
				  GENERAL_NO_PERMISSIONS_OTHER, GENERAL_FEED, GENERAL_FEED_OTHER, GENERAL_HEAL, GENERAL_HEAL_OTHER, GENERAL_TELEPORTING, GENERAL_TELEPORTING_COUNT_DOWN,
				  GENERAL_TELEPORT_CANCELLED, GENERAL_SUICIDE, GENERAL_MORE, GENERAL_MORE_AIR, GENERAL_FLY_ON, GENERAL_FLY_OFF, GENERAL_SPAWN, GENERAL_SPAWN_SET,
				  GENERAL_HOME_SET_DEFAULT, GENERAL_HOME_SET_NAMED, GENERAL_HOME_LIST, GENERAL_HOME_REMOVE, GENERAL_HOME_MAXED, GENERAL_HOME_NOT_SET, 
				  GENERAL_HOME_ONLY_ONE, GENERAL_HOME_NONE, GENERAL_WARP_SET, GENERAL_WARP_NOT_SET, GENERAL_WARP_LIST, GENERAL_WARP_REMOVED, GENERAL_PWEATHER_NOT_OPTION,
				  GENERAL_PWEATHER_SET, GENERAL_PWEATHER_RESET, GENERAL_PTIME_SET, GENERAL_PTIME_RESET, GENERAL_STONE_CUTTER_OPEN, GENERAL_SMITHING_TABLE_OPEN,
				  GENERAL_WORK_BENCH_OPEN, GENERAL_GRIND_STONE_OPEN, GENERAL_LOOM_OPEN, GENERAL_ANVIL_OPEN, GENERAL_CART_TABLE_OPEN, GENERAL_ENDER_CHEST_OPEN,
				  GENERAL_NOT_NUMBER, GENERAL_FLY_SPEED_SET, GENERAL_WALK_SPEED_SET, GENERAL_SPEED_HAS_TO_BE_BETWEEN, GENERAL_NEAR_FORMAT, GENERAL_NEAR_DISTANCE_FORMAT,
				  GENERAL_PING_FORMAT_OTHER, GENERAL_PING_FORMAT, GENERAL_BACK_DEATH_MESSAGE, GENERAL_BACK_NO_LOCATION, GENERAL_SKULL_GIVEN, GENERAL_GOD_ENABLED,
				  GENERAL_GOD_DISABLED, GENERAL_GOD_ENABELD_OTHER, GENERAL_GOD_DISABLED_OTHER, GENERAL_REST, GENERAL_REST_OTHER, GENERAL_HAT, GENERAL_SOCIALSPY_ENABLED,
				  GENERAL_SOCIALSPY_DISABLED, GENERAL_NICK, GENERAL_NICK_REMOVED, GENERAL_REALNAME, GENERAL_IGNORE, GENERAL_UNIGNORE, GENERAL_TPTOGGLE_ENABLED,
				  GENERAL_TPTOGGLE_DISABLE, GENERAL_TPACANCEL, GENERAL_TPADENIED, GENERAL_IGNORE_ALREADY, GENERAL_IGNORE_NEVER, GENERAL_NICK_NO_COLOR, GENERAL_TPAACCEPT;


	public void setup() {
		GENERAL_NO_PERMISSIONS = getMessage("NoPermissions");
		GENERAL_NO_PERMISSIONS_OTHER = getMessage("NoPermissionsOther");
		GENERAL_NOT_NUMBER = getMessage("NotNumber");
		GENERAL_PLAYER_NOT_ONLINE = getMessage("PlayerNotOnline");
		GENERAL_CHANGE_GAMEMODE = getMessage("GamemodeChange");
		GENERAL_CHANGE_GAMEMODE_OTHER = getMessage("GamemodeChangeOther");
		GENERAL_SEEN_HOVER_FORMAT = getMessage("SeenHoverFormat");
		GENERAL_SEEN_FORMAT_OFFLINE = getMessage("SeenFormatOffline");
		GENERAL_SEEN_FORMAT_ONLINE = getMessage("SeenFormatOnline");
		GENERAL_SIGN_EDIT_NOT_SIGN = getMessage("SignEditNotSign");
		GENERAL_REPAIR_ITEM = getMessage("RepairItem");
		GENERAL_REPAIR_ALL_ITEMS = getMessage("RepairAllItems");
		GENERAL_TPALL = getMessage("TpAll");
		GENERAL_TPOFFLINE = getMessage("TpOffline");
		GENERAL_PLAYER_NEVER_JOINED = getMessage("PlayerNeverJoined");
		GENERAL_PLAYER_ONLY_COMMAND = getMessage("PlayerOnlyCommand");
		GENERAL_CLEAR_INVENTORY= getMessage("ClearInventory");
		GENERAL_CLEAR_INVENTORY_OTHER = getMessage("ClearInventoryOther");
		GENERAL_FEED = getMessage("Feed");
		GENERAL_FEED_OTHER = getMessage("FeedOther");
		GENERAL_HEAL = getMessage("Heal");
		GENERAL_HEAL_OTHER = getMessage("HealOther");
		GENERAL_TELEPORTING = getMessage("Teleporting");
		GENERAL_TELEPORTING_COUNT_DOWN = getMessage("TeleportingCountDown");
		GENERAL_TELEPORT_CANCELLED = getMessage("TeleportCanceled");
		GENERAL_SUICIDE = getMessage("Suicide");
		GENERAL_MORE = getMessage("More");
		GENERAL_MORE_AIR = getMessage("MoreAir");
		GENERAL_FLY_ON = getMessage("FlyOn");
		GENERAL_FLY_OFF = getMessage("FlyOff");
		GENERAL_SPAWN = getMessage("Spawn");
		GENERAL_SPAWN_SET = getMessage("SpawnSet");
		GENERAL_HOME_LIST = getMessage("HomeList");
		GENERAL_HOME_SET_DEFAULT = getMessage("HomeSetDefault");
		GENERAL_HOME_SET_NAMED = getMessage("HomeSetNamed");
		GENERAL_HOME_REMOVE = getMessage("HomeRemove");
		GENERAL_HOME_MAXED = getMessage("HomesMaxed");
		GENERAL_HOME_NOT_SET = getMessage("HomeNotSet");
		GENERAL_HOME_ONLY_ONE = getMessage("HomeOnlyOne");
		GENERAL_HOME_NONE = getMessage("HomeNone");
		GENERAL_WARP_SET = getMessage("WarpSet");
		GENERAL_WARP_NOT_SET = getMessage("WarpNotSet");
		GENERAL_WARP_LIST = getMessage("WarpList");
		GENERAL_WARP_REMOVED = getMessage("WarpRemoved");
		GENERAL_PWEATHER_NOT_OPTION = getMessage("PWeatherNotOption");
		GENERAL_PWEATHER_SET = getMessage("PWeatherSet");
		GENERAL_PWEATHER_RESET = getMessage("PWeatherReset");
		GENERAL_PTIME_SET = getMessage("PTimeSet");
		GENERAL_PTIME_RESET = getMessage("PTimeReset");
		GENERAL_STONE_CUTTER_OPEN = getMessage("StoneCutterOpen");
		GENERAL_SMITHING_TABLE_OPEN = getMessage("SmithingTableOpen");
		GENERAL_WORK_BENCH_OPEN = getMessage("WorkBenchOpen");
		GENERAL_GRIND_STONE_OPEN = getMessage("GrindStoneOpen"); 
		GENERAL_LOOM_OPEN = getMessage("LoomOpen");
		GENERAL_ANVIL_OPEN = getMessage("AnvilOpen");
		GENERAL_CART_TABLE_OPEN = getMessage("CartTableOpen");
		GENERAL_ENDER_CHEST_OPEN = getMessage("EnderChestOpen");
		GENERAL_FLY_SPEED_SET = getMessage("FlySpeedSet");
		GENERAL_WALK_SPEED_SET = getMessage("WalkSpeedSet");
		GENERAL_SPEED_HAS_TO_BE_BETWEEN = getMessage("SpeedHasToBeBetween");
		GENERAL_NEAR_FORMAT = getMessage("NearFormat");
		GENERAL_NEAR_DISTANCE_FORMAT = getMessage("NearWithDistanceFormat");
		GENERAL_PING_FORMAT = getMessage("PingFormat");
		GENERAL_PING_FORMAT_OTHER = getMessage("PingOtherFormat");
		GENERAL_BACK_DEATH_MESSAGE = getMessage("BackDeathMessage");
		GENERAL_BACK_NO_LOCATION = getMessage("BackNoLocation");
		GENERAL_SKULL_GIVEN = getMessage("SkullGiven");
		GENERAL_GOD_ENABLED = getMessage("GodEnabled");
		GENERAL_GOD_DISABLED = getMessage("GodDisabled");
		GENERAL_GOD_ENABELD_OTHER = getMessage("GodEnabledOther");
		GENERAL_GOD_DISABLED_OTHER = getMessage("GodDisabledOther");
		GENERAL_REST = getMessage("Rest");
		GENERAL_REST_OTHER = getMessage("RestOther");
		GENERAL_HAT = getMessage("Hat");
		GENERAL_SOCIALSPY_ENABLED = getMessage("SocialspyEnabled");
		GENERAL_SOCIALSPY_DISABLED = getMessage("SocialspyDisabled");
		GENERAL_NICK = getMessage("Nick");
		GENERAL_NICK_NO_COLOR = getMessage("NickNoColor");
		GENERAL_NICK_REMOVED = getMessage("NickRemoved");
		GENERAL_REALNAME = getMessage("Realname");
		GENERAL_IGNORE = getMessage("Ignore");
		GENERAL_IGNORE_ALREADY = getMessage("IgnoreAlready");
		GENERAL_UNIGNORE = getMessage("UnIgnore");
		GENERAL_IGNORE_NEVER = getMessage("IgnoreNever");
		GENERAL_TPTOGGLE_ENABLED = getMessage("TpToggleEnabled");
		GENERAL_TPTOGGLE_DISABLE = getMessage("TpToggleDisable");
		GENERAL_TPACANCEL = getMessage("TpaCancel");
		GENERAL_TPADENIED = getMessage("TpaDenied");
		GENERAL_TPAACCEPT = getMessage("TpaAccepted");
		
		
		ECO_CURRENCY_PLURAL = getMessage("CurrencyPlural");
		ECO_CURRENCY_SINGULAR = getMessage("CurrencySingular");
		ECO_CURRENCY_FORMAT = getMessage("CurrencyFormat");
		ECO_POSITIVE = getMessage("Positive");
		ECO_NOT_ENOUGH = getMessage("NotEnough");
		ECO_SENT_TO = getMessage("SentTo");
		ECO_RECIEVED_FROM = getMessage("RecievedFrom");
		ECO_MONEY_TOGGLE_ON = getMessage("MoneyToggleOn");
		ECO_MONEY_TOGGLE_OFF = getMessage("MoneyToggleOff");
		ECO_MONEY_DENIED = getMessage("MoneyDenied");
		ECO_MONEY_YOURSELF = getMessage("MoneyYourself");
		ECO_MONEY = getMessage("Money");
		ECO_MONEY_PLAYER = getMessage("MoneyPlayer");
		ECO_PAY_ACCOUNT = getMessage("PayAccount");
		ECO_PAY_NOT_NUMBER = getMessage("PayNotNumber");
		ECO_TOP = getMessage("Top");
		ECO_TOP_RANK = getMessage("TopRank");
		ECO_TOP_NOT_FOUND = getMessage("TopNotFound");
		ECO_TOP_ALL_MONEY = getMessage("TopAllMoney");
	}

	
	public String getMessage(String path) {return ConfigYAML.a().getMessages().getString(path);}
}
