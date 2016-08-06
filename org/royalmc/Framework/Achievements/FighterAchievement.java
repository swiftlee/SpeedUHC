package org.royalmc.Framework.Achievements;

import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

/**
 * Created by Max604 on 22/05/2016.
 */
public class FighterAchievement extends Achievement {

    public FighterAchievement(UHCMain plugin) {
        this.name = DataColumn.FIGHTER_ACHIEVEMENT.toString();
        this.description = "Kill in total 50 people in game!";
        this.coins = plugin.getConfig().getInt("achievements.rewards.fighter");
    }
}
