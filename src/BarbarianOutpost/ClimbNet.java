package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Tile;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class ClimbNet extends Task {

	private AgilityScript mainScript;
	private GameObject net;
	private Tile getInPositionTile;
	private final int[] netBounds = {-8, 24, -736, -448, -124, 60};
	public ClimbNet(MethodContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
		getInPositionTile = new Tile(Random.nextInt(2539, 2541),
				Random.nextInt(3547, 3549));
		
	}

	@Override
	public boolean activate() {
		if (mainScript.currentTask.get() == 2) {
			if (!ctx.objects.select().id(Constants.obstacleIDBARB).isEmpty()) {
				mainScript.updateStatus("Climb net");
				return true;
			} else {
				System.out.println("net not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {
		if (ctx.players.local().getLocation().getX() <= 2538) {
			if (!getInPositionTile.getMatrix(ctx).isInViewport()) {
				ctx.camera.turnTo(getInPositionTile);
			}
			ctx.movement.stepTowards(getInPositionTile);

		}
		net = ctx.objects.nearest().poll();
		net.setBounds(netBounds);
		ctx.camera.turnTo(net);
		if (net.isInViewport()) {
			net.interact("Climb-over");
		} else if (!net.isInViewport()) {
			ctx.camera.turnTo(net);
			ctx.movement.stepTowards(net);
		}

	}

	@Override
	public void repaint(Graphics g) {
		if (net != null) {
			net.draw(g);
		}
	}
}