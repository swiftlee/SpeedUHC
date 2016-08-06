package org.royalmc.Framework;

import static org.royalmc.util.TextUtils.formatText;

public enum GameType 
{
	DIAMONDLESS("Diamondless"),
	GOLDLESS("Goldless"),
	HORSELESS("Horseless"),
	RODLESS("Rodless"),
	DOUBLEORES("Double Ores"),
	NONE("None");
	
	private final String valuePrefix;
	
	private static GameType type = NONE;
	
	public static void setType(GameType type)
	{
		GameType.type = type;
	}
	
	public static GameType getType()
	{
		return type;
	}
	
	GameType(String valuePrefix)
	{
		this.valuePrefix = formatText(valuePrefix);
	}
	
	@Override
	public String toString()
	{
		return valuePrefix;
	}
	
}
