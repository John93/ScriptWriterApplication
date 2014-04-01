package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class BalanceLog extends Task {

	private AgilityScript mainScript;
	private GameObject log;
	private final int[] logBounds = { -4, 76, 140, 192, -76, -4 };

	public BalanceLog(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if (mainScript.currentTask.get() == 1) {
			if (!ctx.objects.select().id(Constants.logBalanceID).isEmpty()) {
				mainScript.updateStatus("Balancing log");
				return true;
			} else {
				mainScript.log.info("Log not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {
		log = ctx.objects.nearest().poll();
		log.bounds(logBounds);
		ctx.camera.turnTo(log);
		if (log.inViewport()) {
			log.interact("Walk-across");
		} else if (!log.inViewport()) {
			ctx.camera.turnTo(log);
			ctx.movement.step(log);
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (log != null) {
			log.draw(g);
		}
	}
}