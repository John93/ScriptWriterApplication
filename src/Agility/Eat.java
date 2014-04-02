package Agility;

import java.awt.Graphics;

import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud.Window;
import org.powerbot.script.rt6.Item;

public class Eat extends Task {

	private AgilityScript mainScript;
	private int eatAtPercent = 50;

	public Eat(ClientContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if ((100 * ctx.combatBar.health() / ctx.combatBar.maximumHealth()) < eatAtPercent) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		if (!ctx.backpack.select().id(Constants.foodID).isEmpty()) {
			
			if (ctx.hud.open(Window.BACKPACK)) {

				mainScript.updateStatus("Eating");
				Item food = ctx.backpack.shuffle().first().poll();

				food.interact("Eat");

				eatAtPercent = Random.nextInt(30, 60);
			} else {
				mainScript.log.info("Opening backpack");
				mainScript.updateStatus("Opening backpack");
			}
		} else {
			ctx.controller.stop();
			mainScript.updateStatus("Out of food, stopping script!");
			mainScript.log.info("Out of food, stopping script!");
		}

	}

	@Override
	public void repaint(Graphics g) {

	}
}