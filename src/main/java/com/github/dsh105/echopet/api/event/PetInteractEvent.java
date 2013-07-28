package com.github.dsh105.echopet.api.event;

import com.github.dsh105.echopet.entity.pet.Pet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PetInteractEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;

	private Pet pet;
	private Player player;
	private Action action;

	public PetInteractEvent(Pet pet, Player player, Action action, boolean cancelledByDefault) {
		this.pet = pet;
		this.action = action;
		this.cancelled = cancelledByDefault;
	}

	/**
	 * Gets the {@link Pet} involved in this event
	 *
	 * @return the {@link Pet} involved
	 */
	public Pet getPet() {
		return this.pet;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Action getAction() {
		return this.action;
	}

	/**
	 * Gets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @return true if this event is cancelled
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	public boolean isPlayerOwner() {
		return this.player == this.pet.getOwner();
	}

	/**
	 * Sets the cancellation state of this event. A cancelled event will not
	 * be executed in the server, but will still pass to other plugins
	 *
	 * @param cancel true if you wish to cancel this event
	 */
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public enum Action {
		LEFT_CLICK,
		RIGHT_CLICK;
	}
}