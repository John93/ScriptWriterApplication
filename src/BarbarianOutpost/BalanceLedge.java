package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class BalanceLedge extends Task {

	private AgilityScript mainScript;
	private GameObject ledge;
	private final int[] ledgeBounds = { -96, 132, 0, 0, 124, 196 };

	public BalanceLedge(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if (mainScript.currentTask.get() == 3) {
			if (!ctx.objects.select().id(Constants.LedgeIDBARB).isEmpty()) {
				mainScript.updateStatus("Balance ledge");
				return true;
			} else {
				mainScript.log.info("Ledge not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {

		ledge = ctx.objects.nearest().poll();
		ledge.bounds(ledgeBounds);
		ctx.camera.turnTo(ledge);
		if (ledge.inViewport()) {
			ledge.interact("Walk-across");
		} else if (!ledge.inViewport()) {
			ctx.camera.turnTo(ledge);
			ctx.movement.step(ledge);
		}
	}

	@Override
	public void repaint(Graphics g) {
		if (ledge != null) {
			ledge.draw(g);
		}
	}
}