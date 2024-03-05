package me.emmy.hub.utils.assemble.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class AssembleBoardCreateEvent extends Event implements Cancellable {

	@Getter
	public static HandlerList handlerList = new HandlerList();

	private Player player;
	private boolean cancelled = false;

	public AssembleBoardCreateEvent(Player player) {
		this.player = player;
	}

	@Override
	public HandlerList getHandlers() {
		return handlerList;
	}
}