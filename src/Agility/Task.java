package Agility;

import java.awt.Graphics;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;

public abstract class Task extends MethodProvider {
	public Task(MethodContext ctx) {
		super(ctx);
	}

	public abstract boolean activate();

	public abstract void execute();

	public abstract void repaint(Graphics g);
}
