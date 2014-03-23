package Agility;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.Hud.Window;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.Item;

import Agility.AgilityScript;
import Agility.Task;

public class Eat extends Task {

	private AgilityScript mainScript;
	private int eatAtPercent = 50;
	public Eat(MethodContext c, AgilityScript as) {
		super(c);
		this.mainScript = as;
	}

	@Override
	public boolean activate() {
		if ((100 * ctx.combatBar.getHealth() / ctx.combatBar.getMaximumHealth()) < eatAtPercent) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void execute() {
		if (!ctx.backpack.select().id(Constants.foodIDs).isEmpty()) {
			if (ctx.hud.open(Window.BACKPACK) && ctx.hud.view(Window.BACKPACK)) {
				Item food = ctx.backpack.shuffle().first().poll();
				food.interact("Eat");
				eatAtPercent = Random.nextInt(35, 70);
			}else{
				System.out.println("Opening backpack");
			}
		} else {
			mainScript.getController().stop();
			System.out.println("Out of food, stopping script!");
		}

	}

	@Override
	public void repaint(Graphics g) {

	}
}