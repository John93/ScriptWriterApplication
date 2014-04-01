package BarbarianOutpost;

import java.awt.Graphics;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import Agility.AgilityScript;
import Agility.Constants;
import Agility.Task;

public class ClimbNet extends Task {

	private AgilityScript mainScript;
	private GameObject net;
	private Tile getInPositionTile;
	private final int[] netBounds = { -8, 24, -736, -448, -124, 60 };

	public ClimbNet(ClientContext c, AgilityScript as) {
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
				mainScript.log.info("Net not found");
			}
		}
		return false;
	}

	@Override
	public void execute() {
		if (ctx.players.local().tile().x() <= 2538) {
			if (!getInPositionTile.matrix(ctx).inViewport()) {
				ctx.camera.turnTo(getInPositionTile);
			}
			ctx.movement.step(getInPositionTile);

		} else {
			net = ctx.objects.nearest().poll();
			net.bounds(netBounds);
			ctx.camera.turnTo(net);
			if (net.inViewport()) {
				net.interact("Climb-over");
			} else if (!net.inViewport()) {
				ctx.camera.turnTo(net);
				ctx.movement.step(net);
			}
		}
	}

	@Override
	public void repaint(Graphics g) {
		if (net != null) {
			net.draw(g);
		}
	}
}