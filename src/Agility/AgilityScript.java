package Agility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JDialog;

import org.powerbot.script.PollingScript;
import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.methods.Skills;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.Widget;

import Burthrope.ClimbOver;
import Burthrope.ClimbWall;
import Burthrope.JumpDown;
import Burthrope.SwingMonkeyBars;
import Burthrope.SwingRope;
import Burthrope.WalkAcross;
import Burthrope.WalkLogBeam;
import GnomeVillage.BalanceLog;
import GnomeVillage.BalanceRope;
import GnomeVillage.ClimbDownTree;
import GnomeVillage.ClimbNet;
import GnomeVillage.ClimbObstacleNet;
import GnomeVillage.Pipe;
import GnomeVillage.TreeBranch;

@Manifest(name = "Agility", description = "Supports Burthrope, GnomeVillage and Barbarian Outpost")
public class AgilityScript extends PollingScript implements MessageListener,
		PaintListener {
	private List<Task> taskList = new ArrayList<Task>();
	private String status = "started";
	private String yourlocation = "N/A";
	private long seconds;
	private long minutes;
	private long hours;
	private long time;
	private Tile finishTile;
	private int lastExp;
	private int expGained = 0;
	private int xpHour = 0;
	private int xpToLevel = 0;
	private ConfigDialog dialog;
	private Task tempTask;
	public AtomicInteger currentTask;
	public String location;

	@Override
	public void start() {
		yourlocation = "Location: Not set";
		currentTask = new AtomicInteger(0);
		try {

			dialog = new ConfigDialog(this);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
			// new StartPrompt().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startScript(String s) {
		dialog.dispose();
		finishTile = new Tile(2916, 3552, 0);
		Constants.startingAgilityLevel = ctx.skills.getLevel(Skills.AGILITY);
		Constants.startingAgilityExp = ctx.skills.getExperience(Skills.AGILITY);
		lastExp = ctx.skills.getExperience(Skills.AGILITY);
		// taskList.add(new CloseWidgets(ctx, this));
		taskList.add(new PlayerBusy(ctx, this));
		taskList.add(new Eat(ctx, this));
		if (s.equals("Burthrope")) {
			location = s;
			taskList.add(new WalkLogBeam(ctx, this));
			taskList.add(new ClimbWall(ctx, this));
			taskList.add(new WalkAcross(ctx, this));
			taskList.add(new ClimbOver(ctx, this));
			taskList.add(new SwingRope(ctx, this));
			taskList.add(new SwingMonkeyBars(ctx, this));
			taskList.add(new JumpDown(ctx, this));
			yourlocation = "Location: Burthrope";
		} else if (s.equals("GnomeVillage")) {
			yourlocation = "Location: GnomeVillage";
			location = s;
			taskList.add(new BalanceLog(ctx, this));
			taskList.add(new ClimbNet(ctx, this));
			taskList.add(new TreeBranch(ctx, this));
			taskList.add(new BalanceRope(ctx, this));
			taskList.add(new ClimbDownTree(ctx, this));
			taskList.add(new ClimbObstacleNet(ctx, this));
			taskList.add(new Pipe(ctx, this));
		} else if (s.equals("BarbOutpost")) {
			yourlocation = "Location: Barb Outpost";
			location = s;
			taskList.add(new BarbarianOutpost.SwingRope(ctx, this));
			taskList.add(new BarbarianOutpost.BalanceLog(ctx, this));
			taskList.add(new BarbarianOutpost.ClimbNet(ctx, this));
			taskList.add(new BarbarianOutpost.BalanceLedge(ctx, this));
			taskList.add(new BarbarianOutpost.Ladder(ctx, this));
			taskList.add(new BarbarianOutpost.ClimbWall(ctx, this));
			ctx.camera.setAngle(Random.nextInt(150, 200));
			ctx.camera.setPitch(Random.nextInt(50, 65));
		}

	}

	@Override
	public void resume() {

	}

	@Override
	public void suspend() {
	}

	@Override
	public int poll() {

		closeWidgets();

		// Burthrope code
		if (ctx.players.local().getLocation().equals(finishTile)) {
			if (ctx.skills.getExperience(Skills.AGILITY) > lastExp) {
				lastExp = ctx.skills.getExperience(Skills.AGILITY);
				Constants.laps++;

			}
		}// Burthrope code end

		for (int i = 0; i < taskList.size(); i++) {
			tempTask = taskList.get(i);
			if (tempTask.activate()) {
				tempTask.execute();
				return Random.nextInt(500, 1200);
			}
		}
		updateStatus("No task available");
		System.out.println("No task found");
		System.out.println("Data tasknumber: " + currentTask.get()
				+ "Number of tasks: " + taskList.size());
		System.out.println("Player X: "
				+ ctx.players.local().getLocation().getX() + "Player Y: "
				+ ctx.players.local().getLocation().getY());

		return 500;
	}

	public void closeWidgets() {

		// Close worldmap
		Widget w = ctx.widgets.get(1422);
		if (w.isValid()) {
			if (w.getComponent(96).isVisible()) {
				if (w.getComponent(96).isInViewport()) {
					w.getComponent(96).click();
				}
			}
		}

		// Close HERO GUIDE
		w = ctx.widgets.get(1477);
		if (w.isValid()) {
			if (w.getComponent(55).isVisible()) {
				// System.out.println("Widget is open");
				if (w.getComponent(55).getChild(2).isInViewport()) {
					w.getComponent(55).getChild(2).click();
				}
			} else {
				// System.out.println("Widget is not open");
			}
		}
	}

	@Override
	public void messaged(MessageEvent e) {
		if (location.equals("GnomeVillage")) {
			if (e.getMessage().startsWith(
					"... and make it safely to the other side")) {
				System.out.println("TASK DONE: WALKING LOG");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("You climb the netting...")) {
				System.out.println("TASK DONE: CLIMB NET");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("... to the platform above")) {
				System.out.println("TASK DONE: CLIMB TREE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"You carefully cross the tightrope")) {
				System.out.println("TASK DONE: CROSS THE ROPE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("You land on the ground")) {
				System.out.println("TASK DONE: CLIMB DOWN");
				currentTask.incrementAndGet();
			} else if (e.getMessage()
					.startsWith("You squeeze into the pipe...")) {
				System.out.println("TASK DONE: CROSS PIPE");
				Constants.laps++;
				currentTask.set(0);
			}
		} else if (location.equals("BarbOutpost")) {
			if (e.getMessage().startsWith("You skilfully swing across.")) {
				System.out.println("TASK DONE: SWINGING ROPE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"... and make it safely to the other side.")) {
				System.out.println("TASK DONE: WALK LOG");

				currentTask.incrementAndGet();

			} else if (e.getMessage().startsWith("You climb the netting...")) {
				System.out.println("TASK DONE: CLIMB NET");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"You skilfully edge across the gap.")) {
				System.out.println("TASK DONE: WALK ACROSS");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("Talent scout: Hey")) {
				System.out.println("TALENT SCOUT");
			} else if (e.getMessage().startsWith("You climb the low wall...")) {
				System.out.println("TASK DONE: CLIMB WALL");
				currentTask.incrementAndGet();
				if (currentTask.get() == 6) {
					currentTask.set(0);
					Constants.laps++;
				}
			} else if (e.getMessage().startsWith(
					"You slip and fall onto the spikes below.")) {
				System.out.println("TASK FAILED: TASK SET TO CLIMB NET");
				currentTask.set(2);
			}
		}
	}

	@Override
	public void repaint(Graphics g) {

		if (!taskList.isEmpty()) {
			g.setColor(Color.ORANGE);
			g.setFont(new Font("Tahoma", Font.BOLD, 15));
			g.drawString("Agilityscript by Johnmad", 10, 30);
			g.setColor(Color.GREEN);
			g.drawString("Task: " + status, 10, 70);
			g.drawString(yourlocation, 10, 50);
			// g.setColor(Color.RED);
			time = this.getTotalRuntime();
			seconds = (time / 1000) % 60;
			minutes = (time / (1000 * 60)) % 60;
			hours = (time / (1000 * 60 * 60)) % 24;

			g.drawString(
					"Time: "
							+ String.format("%d:%02d:%02d", hours, minutes,
									seconds), 10, 90);
			g.drawString(
					"Levels gained: "
							+ Integer.toString(ctx.skills
									.getLevel(Skills.AGILITY)
									- Constants.startingAgilityLevel), 10, 110);
			g.drawString("Total laps: " + Constants.laps, 10, 130);
			expGained = ctx.skills.getExperience(Skills.AGILITY)
					- Constants.startingAgilityExp;

			g.drawString("Xp gained: " + String.valueOf(expGained), 10, 150);
			xpHour = (int) (((float) expGained / (float) (this
					.getTotalRuntime() / 1000)) * 3600);
			xpToLevel = (ctx.skills.getExperienceAt(ctx.skills
					.getLevel(Skills.AGILITY) + 1) - ctx.skills
					.getExperience(Skills.AGILITY));
			g.drawString(xpHour + " xp/h", 10, 170);
			g.drawString("Xp to " + (ctx.skills.getLevel(Skills.AGILITY) + 1)
					+ ": " + xpToLevel, 10, 190);
			if (xpHour != 0) {
				// System.out.println(((float)xpToLevel / (float)xpHour) *
				// 3600000f);
				time = (long) (((float) xpToLevel / (float) xpHour) * 3600000f);
				seconds = (time / 1000) % 60;
				minutes = (time / (1000 * 60)) % 60;
				hours = (time / (1000 * 60 * 60)) % 24;
				g.drawString(
						"Time to "
								+ (ctx.skills.getLevel(Skills.AGILITY) + 1)
								+ ": "
								+ String.format("%d:%02d:%02d", hours, minutes,
										seconds), 10, 210);
			}
			if (Constants.debugMode) {
				for (Task t : taskList) {
					t.repaint(g);
				}
			}
		}
	}

	public void updateStatus(String status) {
		this.status = status;
	}

}