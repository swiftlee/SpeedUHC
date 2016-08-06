package org.royalmc.Framework.Achievements;

import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

/**
 * Created by Max604 on 22/05/2016.
 */
public class WalkerAchievement extends Achievement {

    public WalkerAchievement(UHCMain plugin) {
        this.name = DataColumn.WALKER_ACHIEVEMENT.toString();
        this.description = "Walk 100 blocks in a UHC";
        this.coins = plugin.getConfig().getInt("achievements.rewards.walker");
    }
}
