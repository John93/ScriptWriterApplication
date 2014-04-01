package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class SwingRope extends Task {

	private AgilityScript mainScript;
	private Tile startTile;
	private GameObject rope;
	private final int[] ropeBounds = { -20, 40, -1680, -212, -44, 12 };
	private final int[] ladderBounds = { -32, -8, -824, -12, -156, 144 };

	public SwingRope(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
		startTile = new Tile(2552, 3554, 0);
	}

	@Override
	public boolean activate() {

		if (mainScript.currentTask.get() == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {

		if (!ctx.objects.select().id(Constants.ropeSwingID).isEmpty()) {
			mainScript.updateStatus("Swinging rope");
			rope = ctx.objects.shuffle().first().poll();
			rope.bounds(ropeBounds);
			ctx.camera.turnTo(rope);
			if (rope.inViewport()) {
				rope.interact("Swing-on");
			} else if (!rope.inViewport()) {
				ctx.camera.turnTo(rope);
				ctx.movement.step(startTile);
			}
		} else if (!ctx.objects.select().id(Constants.ropeLadderID).isEmpty()) {
			mainScript.updateStatus("Climbing up ladder");
			rope = ctx.objects.nearest().poll();
			rope.bounds(ladderBounds);
			ctx.camera.turnTo(rope);
			if (rope.inViewport()) {
				rope.interact("Climb-up", "Ladder");
			} else if (!rope.inViewport()) {
				ctx.camera.turnTo(rope);
				ctx.movement.step(rope);
			}
		} else {
			mainScript.log.info("Object not found");
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (startTile != null && rope != null) {
			startTile.matrix(ctx).draw(g);
			rope.draw(g);
		}
	}
}