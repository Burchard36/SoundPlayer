package com.burchard36.plugin;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static com.burchard36.plugin.SoundPlayerPlugin.INCREMENT_VALUE_KEY;

public class SoundListener {

    protected final Player player;
    protected final PersistentDataContainer dataContainer;

    public SoundListener(final Player player) {
        this.player = player;
        this.dataContainer = this.player.getPersistentDataContainer();
    }

    public final float getIncrementAmount() {
        if (!this.dataContainer.has(INCREMENT_VALUE_KEY, PersistentDataType.FLOAT)) {
            this.dataContainer.set(INCREMENT_VALUE_KEY, PersistentDataType.FLOAT, 0.05f);
        }

        Float theValue = dataContainer.get(INCREMENT_VALUE_KEY, PersistentDataType.FLOAT);
        assert theValue != null;
        return theValue;
    }

}
