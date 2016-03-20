package de.avalon.bossbar;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract interface BossBar {
	public abstract Collection<? extends Player> getPlayers();

	public abstract void addPlayer(Player paramPlayer);

	public abstract void removePlayer(Player paramPlayer);

	public abstract BossBarAPI.Color getColor();

	public abstract void setColor(BossBarAPI.Color paramColor);

	public abstract BossBarAPI.Style getStyle();

	public abstract void setStyle(BossBarAPI.Style paramStyle);

	public abstract void setProperty(BossBarAPI.Property paramProperty, boolean paramBoolean);

	public abstract String getMessage();

	public abstract void setVisible(boolean paramBoolean);

	public abstract boolean isVisible();

	public abstract float getProgress();

	public abstract void setProgress(float paramFloat);

	public abstract float getMaxHealth();

	public abstract void setHealth(float paramFloat);

	public abstract float getHealth();

	public abstract void setMessage(String paramString);

	public abstract Player getReceiver();

	public abstract Location getLocation();

	public abstract void updateMovement();
}
