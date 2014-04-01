package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.Filter;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class ClimbWall extends Task {

	private AgilityScript mainScript;
	private GameObject wall;
	private final int[] wallBounds = { 168, 208, -300, -80, -156, 180 };

	public ClimbWall(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if (mainScript.currentTask.get() == 5) {

			mainScript.updateStatus("Climbing wall 2");
			return true;

		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		if (!ctx.objects.select().id(Constants.crumblingWallID).isEmpty()) {

			wall = ctx.objects.select(new Filter<GameObject>() {

				@Override
				public boolean accept(GameObject g) {
					if (g.tile().y() == 3553 && g.tile().x() == 2542) {
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
			} else if (!wall.inViewport()) {
				ctx.camera.turnTo(wall);
				ctx.movement.step(wall);
			}
		} else {
			mainScript.log.info("Wall not found");
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (wall != null) {
			wall.draw(g);
		}
	}
}