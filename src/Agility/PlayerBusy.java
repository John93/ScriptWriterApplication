package Agility;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;

public class PlayerBusy extends Task {

	public PlayerBusy(MethodContext c, AgilityScript as) {
		super(c);
	}

	@Override
	public boolean activate() {
		if (!ctx.players.local().isIdle() || ctx.players.local().isInCombat()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void execute() {

	}

	@Override
	public void repaint(Graphics g) {

	}
}