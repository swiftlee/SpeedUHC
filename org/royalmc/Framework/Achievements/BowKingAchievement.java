package org.royalmc.Framework.Achievements;

import org.royalmc.Framework.SQLStorage.DataColumn;
import org.royalmc.uhc.UHCMain;

/**
 * Created by Max604 on 22/05/2016.
 */
public class BowKingAchievement extends Achievement {

    public BowKingAchievement(UHCMain plugin) {
        this.name = DataColumn.BOWKING_ACHIEVEMENT.toString();
        this.description = "Shoot a total of 100 arrows at people!";
        this.coins = plugin.getConfig().getInt("achievements.rewards.bowking");
    }
}
