package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class BalanceLedge extends Task {

	private AgilityScript mainScript;
	private GameObject ledge;
	private final int[] ledgeBounds = {-96, 132, 0, 0, 124, 196};
	public BalanceLedge(MethodContext c, AgilityScript as) {
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
				System.out.println("ledge not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {

		ledge = ctx.objects.nearest().poll();
		ledge.setBounds(ledgeBounds);
		ctx.camera.turnTo(ledge);
		if (ledge.isInViewport()) {
			ledge.interact("Walk-across");
		} else if (!ledge.isInViewport()) {
			ctx.camera.turnTo(ledge);
			ctx.movement.stepTowards(ledge);
		}
	}

	@Override
	public void repaint(Graphics g) {
		if (ledge != null) {
			ledge.draw(g);
		}
	}
}