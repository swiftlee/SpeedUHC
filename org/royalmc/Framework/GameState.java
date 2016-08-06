package org.royalmc.Framework;

public enum GameState 
{
	
	IN_LOBBY, GRACE_PERIOD, PVP_PERIOD, DEATHMATCH, GAME_OVER;
	
	private static GameState state = IN_LOBBY;
	
	public static void setState(GameState state){
		GameState.state = state;
	}
	
	public static GameState getState(){
		return state;
	}
}
