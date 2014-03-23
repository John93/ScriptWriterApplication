package BarbarianOutpost;

import java.awt.Graphics;
import org.powerbot.script.lang.Filter;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;
import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class ClimbWall extends Task {

	private AgilityScript mainScript;
	private GameObject wall;
	private final int[] wallBounds = {168, 208, -300, -80, -156, 180};
	public ClimbWall(MethodContext c, AgilityScript as) {
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

				public boolean accept(GameObject g) {
					if (g.getLocation().getY() == 3553
							&& g.getLocation().getX() == 2542) {
						return true;
					} else {
						return false;
					}
				}
			}).first().poll();
			wall.setBounds(wallBounds);
			ctx.camera.turnTo(wall);
			if (wall.isInViewport()) {
				wall.interact("Climb-over");
			} else if (!wall.isInViewport()) {
				ctx.camera.turnTo(wall);
				ctx.movement.stepTowards(wall);
			}
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (wall != null) {
			wall.draw(g);
		}
	}
}