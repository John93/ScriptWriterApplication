package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class Ladder extends Task {

	private AgilityScript mainScript;
	private GameObject ladder;
	private GameObject wall;
	private final int[] ladderBounds = {-108, 80, -352, -64, -12, 28};
	private final int[] wallBounds = {168, 208, -300, -80, -156, 128};
	public Ladder(MethodContext c, AgilityScript as) {
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
		if (ctx.players.local().getLocation().getPlane() == 1) {
			mainScript.updateStatus("Climbing down ladder");
			if (!ctx.objects.select().id(Constants.ladderID).isEmpty()) {
				ladder = ctx.objects.nearest().poll();
				ladder.setBounds(ladderBounds);
				ctx.camera.turnTo(ladder);
				if (ladder.isInViewport()) {
					ladder.interact("Climb-down");
				} else if (!ladder.isInViewport()) {
					ctx.camera.turnTo(ladder);
					ctx.movement.stepTowards(ladder);
				}
			} else {
				System.out.println("ladder not found");
			}
		} else if (ctx.players.local().getLocation().getPlane() == 0) {
			mainScript.updateStatus("Moving to wall");

			if (!ctx.objects.select().id(Constants.crumblingWallID).isEmpty()) {
				wall = ctx.objects.select(new Filter<GameObject>() {

					public boolean accept(GameObject g) {
						if (g.getLocation().getY() == 3553
								&& g.getLocation().getX() == 2537) {
							return true;
						} else {
							return false;
						}
					}
				}).first().poll();
				wall.setBounds(wallBounds);
				if (wall.isInViewport()) {
					wall.interact("Climb-over");
					//ctx.camera.setAngle(Random.nextInt(260, 280));
					//ctx.camera.setPitch(Random.nextInt(45, 60));
				} else if (!wall.isInViewport()) {
					ctx.camera.turnTo(wall);
					ctx.movement.stepTowards(wall);
				}
			}
		}
	}

	@Override
	public void repaint(Graphics g) {
		if (ladder != null) {
			ladder.draw(g);
		}
		if(wall != null){
			wall.draw(g);
		}
	}
}