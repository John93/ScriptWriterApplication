package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class BalanceLog extends Task {

	private AgilityScript mainScript;
	private GameObject log;
	private final int[] logBounds = {-4, 76, 140, 192, -76, -4};


	public BalanceLog(MethodContext c, AgilityScript as) {
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
				System.out.println("log not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {
		log = ctx.objects.nearest().poll();
		log.setBounds(logBounds);
		ctx.camera.turnTo(log);
		if (log.isInViewport()) {
			log.interact("Walk-across");
		} else if (!log.isInViewport()) {
			ctx.camera.turnTo(log);
			ctx.movement.stepTowards(log);
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (log != null) {
			log.draw(g);
		}
	}
}