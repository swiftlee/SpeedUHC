package org.royalmc.Framework.Achievements;

import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

/**
 * Created by Max604 on 22/05/2016.
 */
public class SwimmerAchievement extends Achievement {

    public SwimmerAchievement(UHCMain plugin) {
        this.name = DataColumn.SWIMMER_ACHIEVEMENT.toString();
        this.description = "Be in the the water for a total of 3 minutes";
        this.coins = plugin.getConfig().getInt("achievements.rewards.swimmer");
    }
}

