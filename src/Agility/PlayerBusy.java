package Agility;

import java.awt.Graphics;

import org.powerbot.script.rt6.ClientContext;

public class PlayerBusy extends Task {
	private AgilityScript mainScript;

	public PlayerBusy(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if (!ctx.players.local().idle() || ctx.players.local().inCombat()) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void execute() {
		mainScript.updateStatus("Player busy");
		mainScript.log.info("Player busy");
	}

	@Override
	public void repaint(Graphics g) {

	}
}