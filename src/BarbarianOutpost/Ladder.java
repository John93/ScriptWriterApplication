package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.Filter;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class Ladder extends Task {

	private AgilityScript mainScript;
	private GameObject ladder;
	private GameObject wall;
	private final int[] ladderBounds = { -108, 80, -352, -64, -12, 28 };
	private final int[] wallBounds = { 168, 208, -300, -80, -156, 128 };

	public Ladder(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if (mainScript.currentTask.get() == 4) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		if (ctx.players.local().tile().floor() == 1) {
			mainScript.updateStatus("Climbing down ladder");
			if (!ctx.objects.select().id(Constants.ladderID).isEmpty()) {
				ladder = ctx.objects.nearest().poll();
				ladder.bounds(ladderBounds);
				ctx.camera.turnTo(ladder);
				if (ladder.inViewport()) {
					ladder.interact("Climb-down");
				} else if (!ladder.inViewport()) {
					ctx.camera.turnTo(ladder);
					ctx.movement.step(ladder);
				}
			} else {
				mainScript.log.info("Ladder not found");
			}
		} else if (ctx.players.local().tile().floor() == 0) {
			mainScript.updateStatus("Moving to wall");

			if (!ctx.objects.select().id(Constants.crumblingWallID).isEmpty()) {
				wall = ctx.objects.select(new Filter<GameObject>() {

					@Override
					public boolean accept(GameObject g) {
						if (g.tile().y() == 3553 && g.tile().x() == 2537) {
							return true;
						} else {
							return false;
						}
					}
				}).first().poll();
				wall.bounds(wallBounds);
				ctx.camera.turnTo(wall);
				if (wall.inViewport()) {

					wall.interact("Climb-over");
					// ctx.camera.setAngle(Random.nextInt(260, 280));
					// ctx.camera.setPitch(Random.nextInt(45, 60));
				} else if (!wall.inViewport()) {
					ctx.camera.turnTo(wall);
					ctx.movement.step(wall);
				}
			} else {
				mainScript.log.info("Wall not found");
			}
		}
	}

	@Override
	public void repaint(Graphics g) {
		if (ladder != null) {
			ladder.draw(g);
		}
		if (wall != null) {
			wall.draw(g);
		}
	}
}