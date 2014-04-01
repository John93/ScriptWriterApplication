package Agility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import javax.swing.WindowConstants;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Skills;
import org.powerbot.script.rt6.Widget;

@Manifest(name = "Agility", description = "Supports Burthrope, GnomeVillage and Barbarian Outpost")
public class AgilityScript extends PollingScript<ClientContext> implements
		MessageListener, PaintListener {
	private ArrayList<Task> taskList = new ArrayList<Task>();
	private String status = "started";
	private String yourlocation = "N/A";
	private Task tempTask;
	private Font font;
	private long seconds;
	private long minutes;
	private long hours;
	private long time;
	private int expGained = 0;
	private int xpHour = 0;
	private int xpToLevel = 0;
	private ConfigDialog dialog;
	// private Task tempTask;
	public AtomicInteger currentTask;
	private String location;
	private long cameraUpdate = 0;
	private boolean started = false;

	@Override
	public void start() {
		yourlocation = "Location: Not set";
		location = "N/A";
		currentTask = new AtomicInteger(0);
		font = new Font("Tahoma", Font.BOLD, 15);
		try {

			dialog = new ConfigDialog(this);
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void startScript(String s) {
		dialog.dispose();
		// ctx.properties.setProperty("foodID",
		// String.valueOf(Constants.foodID));
		Constants.startingAgilityLevel = ctx.skills.level(Skills.AGILITY);
		Constants.startingAgilityExp = ctx.skills.experience(Skills.AGILITY);

		if (Constants.debugMode) {
			log.setLevel(Level.FINEST);
		}
		taskList.add(new PlayerBusy(ctx, this));
		taskList.add(new Eat(ctx, this));
		if (s.equals("Burthrope")) {
			yourlocation = "Location: Burthrope";
			location = s;
			taskList.add(new Burthrope.WalkLogBeam(ctx, this));
			taskList.add(new Burthrope.ClimbWall(ctx, this));
			taskList.add(new Burthrope.WalkAcross(ctx, this));
			taskList.add(new Burthrope.ClimbOver(ctx, this));
			taskList.add(new Burthrope.SwingRope(ctx, this));
			taskList.add(new Burthrope.SwingMonkeyBars(ctx, this));
			taskList.add(new Burthrope.JumpDown(ctx, this));
			yourlocation = "Location: Burthrope";
		} else if (s.equals("GnomeVillage")) {
			yourlocation = "Location: GnomeVillage";
			location = s;
			taskList.add(new GnomeVillage.BalanceLog(ctx, this));
			taskList.add(new GnomeVillage.ClimbNet(ctx, this));
			taskList.add(new GnomeVillage.TreeBranch(ctx, this));
			taskList.add(new GnomeVillage.BalanceRope(ctx, this));
			taskList.add(new GnomeVillage.ClimbDownTree(ctx, this));
			taskList.add(new GnomeVillage.ClimbObstacleNet(ctx, this));
			taskList.add(new GnomeVillage.Pipe(ctx, this));
		} else if (s.equals("BarbOutpost")) {
			yourlocation = "Location: Barb Outpost";
			location = s;
			taskList.add(new BarbarianOutpost.SwingRope(ctx, this));
			taskList.add(new BarbarianOutpost.BalanceLog(ctx, this));
			taskList.add(new BarbarianOutpost.ClimbNet(ctx, this));
			taskList.add(new BarbarianOutpost.BalanceLedge(ctx, this));
			taskList.add(new BarbarianOutpost.Ladder(ctx, this));
			taskList.add(new BarbarianOutpost.ClimbWall(ctx, this));
			ctx.camera.angle(Random.nextInt(150, 200));
			ctx.camera.pitch(Random.nextInt(50, 65));
		} else if (s.equals("Wilderness")) {
			yourlocation = "Location: Wilderness";
			location = s;
			taskList.add(new Wilderness.PipeObstacle(ctx, this));
			// ctx.camera.setAngle(Random.nextInt(150, 200));
			// ctx.camera.setPitch(Random.nextInt(50, 65));
		}
		started = true;
	}

	@Override
	public void resume() {
		log.info("Script resumed.");
	}

	@Override
	public void suspend() {
		log.info("Script suspended.");
	}

	@Override
	public void poll() {
		if (started) {
			closeWidgets(); // Close annoying widgets
			cameraHelper(); // Set a good camera angle based on current task

			// Run if possible
			if (!ctx.movement.running() && ctx.movement.energyLevel() > 15) {
				ctx.movement.running(true);
			}

			// Loop through tasks
			for (int i = 0; i < taskList.size(); i++) {
				tempTask = taskList.get(i);
				if (tempTask.activate()) {
					tempTask.execute();
					Condition.sleep(Random.nextInt(500, 1000));
					return;
				}
			}
			// If no task found print debug info
			updateStatus("No task available");
			log.fine("No task found \n Data tasknumber: " + currentTask.get()
					+ "Number of tasks: " + taskList.size() + "Player X: "
					+ ctx.players.local().tile().x() + "Player Y: "
					+ ctx.players.local().tile().y());
		}
		Condition.sleep(500);
	}

	public void closeWidgets() {
		Widget w;

		// Close HERO GUIDE
		w = ctx.widgets.widget(1477);
		if (w.valid()) {
			if (w.component(55).visible()) {
				if (w.component(55).component(2).inViewport()) {
					w.component(55).component(2).click();
				}
			}
		}
	}

	public void cameraHelper() {
		if (System.currentTimeMillis() - cameraUpdate > 10000
				&& ctx.players.local().idle()) {
			// Barbarian outpost code
			if (location.equals("BarbOutpost")) {
				log.info("Camerahelper.run");
				if (currentTask.get() == 0) {
					ctx.camera.pitch(Random.nextInt(60, 80));
					ctx.camera.angle(Random.nextInt(160, 190));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 1) {
					ctx.camera.pitch(Random.nextInt(60, 80));
					ctx.camera.angle(Random.nextInt(160, 190));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 2) {
					ctx.camera.pitch(Random.nextInt(70, 80));
					ctx.camera.angle(Random.nextInt(70, 115));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 3) {
					ctx.camera.pitch(Random.nextInt(70, 80));
					ctx.camera.angle(Random.nextInt(70, 115));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 4) {
					if (ctx.players.local().tile().floor() == 1) {
						ctx.camera.pitch(Random.nextInt(50, 65));
						ctx.camera.angle(Random.nextInt(260, 300));
					} else {
						ctx.camera.pitch(Random.nextInt(70, 80));
						ctx.camera.angle(Random.nextInt(120, 150));
					}
					cameraUpdate = System.currentTimeMillis();

				} else if (currentTask.get() == 5) {
					ctx.camera.pitch(Random.nextInt(40, 60));
					ctx.camera.angle(Random.nextInt(250, 280));
					cameraUpdate = System.currentTimeMillis();

				}
				// GnomeVillage code
			} else if (location.equals("GnomeVillage")) {
				log.info("Camerahelper.run");
				if (currentTask.get() == 0) {
					ctx.camera.pitch(Random.nextInt(50, 85));
					ctx.camera.angle(Random.nextInt(160, 190));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 1) {
					ctx.camera.pitch(Random.nextInt(50, 85));
					ctx.camera.angle(Random.nextInt(160, 190));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 2) {
					ctx.camera.pitch(Random.nextInt(50, 85));
					ctx.camera.angle(Random.nextInt(160, 190));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 3) {
					ctx.camera.pitch(Random.nextInt(55, 70));
					ctx.camera.angle(Random.nextInt(240, 290));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 4) {
					ctx.camera.pitch(Random.nextInt(55, 70));
					ctx.camera.angle(Random.nextInt(240, 290));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 5) {
					ctx.camera.pitch(Random.nextInt(10, 30));
					ctx.camera.angle(Random.nextInt(330, 355));
					cameraUpdate = System.currentTimeMillis();
				} else if (currentTask.get() == 6) {
					ctx.camera.pitch(Random.nextInt(49, 84));
					ctx.camera.angle(Random.nextInt(0, 30));
					cameraUpdate = System.currentTimeMillis();
				}
			} else {
				log.info("No location specified for camerahelper");
				cameraUpdate = System.currentTimeMillis();
			}
		}
	}

	@Override
	public void messaged(MessageEvent e) {
		// Check if task was succesfull/failed

		// GnomeVillage code
		if (location.equals("GnomeVillage")) {
			if (e.getMessage().startsWith(
					"... and make it safely to the other side")) {
				log.info("TASK DONE: WALKING LOG");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("You climb the netting...")) {
				log.info("TASK DONE: CLIMB NET");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("... to the platform above")) {
				log.info("TASK DONE: CLIMB TREE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"You carefully cross the tightrope")) {
				log.info("TASK DONE: CROSS THE ROPE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith("You land on the ground")) {
				log.info("TASK DONE: CLIMB DOWN");
				currentTask.incrementAndGet();
			} else if (e.getMessage()
					.startsWith("You squeeze into the pipe...")) {
				log.info("TASK DONE: CROSS PIPE");
				Constants.totalLaps++;
				currentTask.set(0);
			}

		}
		// Barb outpost code
		else if (location.equals("BarbOutpost")) {
			if (e.getMessage().startsWith("You skilfully swing across.")) {
				log.info("TASK DONE: CROSS PIPE");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"... and make it safely to the other side.")) {
				log.info("TASK DONE: CROSS PIPE");

				currentTask.incrementAndGet();

			} else if (e.getMessage().startsWith("You climb the netting...")) {
				log.info("TASK DONE: CLIMB NET");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"You skilfully edge across the gap.")) {
				log.info("TASK DONE: WALK ACROSS");
				currentTask.incrementAndGet();
			} else if (e.getMessage().startsWith(
					"Looks like the nearby scout is trying to ")) {
				log.info("TALENT SCOUT");
			} else if (e.getMessage().startsWith("You climb the low wall...")) {
				log.info("TASK DONE: CLIMB WALL");
				currentTask.incrementAndGet();
				if (currentTask.get() == 6) {
					currentTask.set(0);
					Constants.totalLaps++;
				}
			} else if (e.getMessage().startsWith(
					"You slip and fall onto the spikes below.")) {
				log.info("TASK FAILED: TASK SET TO CLIMB NET");
				currentTask.set(2);
			}
		}
		// Wilderness code
		else if (location.equals("Wilderness")) {
			if (e.getMessage()
					.startsWith("You slip and fall to the pit below.")) {
				log.info("Task Failed");
			} else if (e.getMessage().startsWith("You skilfully swing across.")) {
				log.info("Task done");
			} else if (e.getMessage().startsWith(
					"... and reach the other side safely.")) {
				log.info("Task done");
			} else if (e.getMessage().startsWith(
					"You skilfully edge across the gap.")) {
				log.info("Task done");
			} else if (e.getMessage().startsWith(
					"... but you lose your footing and fall into the lava.")) {
				log.info("Task failed");
			}
		}
	}

	@Override
	public void repaint(Graphics g) {

		if (started) { // Becomes true after config dialog is complete
			g.setColor(Color.ORANGE);
			g.setFont(font);
			g.drawString("Agilityscript by Johnmad", 10, 30);
			g.setColor(Color.GREEN);
			g.drawString("Task: " + status, 10, 70);
			g.drawString(yourlocation, 10, 50);
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
							+ Integer.toString(ctx.skills.level(Skills.AGILITY)
									- Constants.startingAgilityLevel), 10, 110);
			g.drawString("Total laps: " + Constants.totalLaps, 10, 130);
			expGained = ctx.skills.experience(Skills.AGILITY)
					- Constants.startingAgilityExp;

			g.drawString("Xp gained: " + String.valueOf(expGained), 10, 150);
			xpHour = (int) (((float) expGained / (float) (this
					.getTotalRuntime() / 1000)) * 3600);
			xpToLevel = (ctx.skills.experienceAt(ctx.skills
					.level(Skills.AGILITY) + 1) - ctx.skills
					.experience(Skills.AGILITY));
			g.drawString(xpHour + " xp/h", 10, 170);
			g.drawString("Xp to " + (ctx.skills.level(Skills.AGILITY) + 1)
					+ ": " + xpToLevel, 10, 190);
			if (xpHour != 0) {

				time = (long) (((float) xpToLevel / (float) xpHour) * 3600000f);
				seconds = (time / 1000) % 60;
				minutes = (time / (1000 * 60)) % 60;
				hours = (time / (1000 * 60 * 60)) % 24;
				g.drawString(
						"Time to "
								+ (ctx.skills.level(Skills.AGILITY) + 1)
								+ ": "
								+ String.format("%d:%02d:%02d", hours, minutes,
										seconds), 10, 210);
			}

			// If debugmode call tasks repaint method
			if (Constants.debugMode) {
				g.setColor(Color.RED);
				for (Task t : taskList) {
					t.repaint(g);
				}
			}
		}
	}

	// Used by tasks to update script status
	public void updateStatus(String status) {
		this.status = status;
	}
	public ClientContext ctx(){
		return ctx;
	}
}