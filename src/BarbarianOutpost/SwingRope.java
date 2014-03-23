package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Tile;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class SwingRope extends Task {

	private AgilityScript mainScript;
	private Tile startTile;
	private GameObject rope;
	private final int[] ropeBounds = {-20, 40, -1680, -212, -44, 12};
	private final int[] ladderBounds = {-28, 12, -852, -180, -92, 68};
	public SwingRope(MethodContext c, AgilityScript as) {
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

		if (!ctx.movement.isRunning() && ctx.movement.getEnergyLevel() > 15) {
			ctx.movement.setRunning(true);
		}

		if (!ctx.objects.select().id(Constants.ropeSwingID).isEmpty()) {
			mainScript.updateStatus("Swinging rope");
			rope = ctx.objects.shuffle().first().poll();
			rope.setBounds(ropeBounds);
			ctx.camera.turnTo(rope);
			if (rope.isInViewport()) {
				rope.interact("Swing-on");
			} else if (!rope.isInViewport()) {
				ctx.camera.turnTo(rope);
				ctx.movement.stepTowards(startTile);
			}
		} else if (!ctx.objects.select().id(Constants.ropeLadderID).isEmpty()) {
			mainScript.updateStatus("Climbing up ladder");
			rope = ctx.objects.nearest().poll();
			rope.setBounds(ladderBounds);
			ctx.camera.turnTo(rope);
			if (rope.isInViewport()) {
				rope.interact("Climb-up");
			} else if (!rope.isInViewport()) {
				ctx.camera.turnTo(rope);
				ctx.movement.stepTowards(rope);
			}
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (startTile != null && rope != null) {
			startTile.getMatrix(ctx).draw(g);
			rope.draw(g);
		}
	}
}