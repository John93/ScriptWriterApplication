package Agility;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ConfigDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JCheckBox DebugModeCheckbox;
	private JList<String> locationList;
	private AgilityScript script;
	private JTextField foodInput;

	public ConfigDialog(AgilityScript as) {
		this.script = as;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.WEST);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JLabel Location = new JLabel("Locations");
				Location.setFont(new Font("Tahoma", Font.PLAIN, 15));
				panel.add(Location, BorderLayout.NORTH);
			}
			{
				locationList = new JList<String>();
				locationList.setToolTipText("Select agility course");
				locationList
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				panel.add(locationList);
				locationList.setBorder(UIManager
						.getBorder("List.focusCellHighlightBorder"));
				locationList.setModel(new AbstractListModel<String>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;
					String[] values = new String[] { "Burthrope",
							"GnomeVillage", "BarbOutpost", "Wilderness" };

					@Override
					public int getSize() {
						return values.length;
					}

					@Override
					public String getElementAt(int index) {
						return values[index];
					}
				});
				locationList.setSelectedIndex(0);
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.EAST);
				panel_1.setLayout(new GridLayout(8, 1, 0, 0));
				{
					JPanel panel_2 = new JPanel();
					panel_2.setAlignmentY(Component.TOP_ALIGNMENT);
					panel_2.setAlignmentX(Component.LEFT_ALIGNMENT);
					panel_1.add(panel_2);
					panel_2.setLayout(new BorderLayout(0, 0));
					{
						DebugModeCheckbox = new JCheckBox("Debug mode");
						DebugModeCheckbox
								.setAlignmentY(Component.BOTTOM_ALIGNMENT);
						DebugModeCheckbox
								.setVerticalAlignment(SwingConstants.TOP);
						panel_2.add(DebugModeCheckbox, BorderLayout.NORTH);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_2.setSize(new Dimension(0, 50));
					panel_2.setPreferredSize(new Dimension(10, 6));
					panel_1.add(panel_2);
					panel_2.setLayout(new GridLayout(1, 2, 0, 0));
					{
						JLabel lblNewLabel_1 = new JLabel("Food ID");
						lblNewLabel_1.setPreferredSize(new Dimension(38, 7));
						panel_2.add(lblNewLabel_1);
					}
					{
						foodInput = new JTextField();
						foodInput.setText("7946");
						foodInput.setToolTipText("Input food ID");
						panel_2.add(foodInput);
						foodInput.setColumns(1);
					}
				}
				{
					JPanel panel_2 = new JPanel();
					panel_1.add(panel_2);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new GridLayout(10, 1, 0, 0));
				{
					JLabel lblNewLabel_2 = new JLabel("Instructions");
					lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 15));
					panel_1.add(lblNewLabel_2);
				}
				{
					JLabel lblNewLabel = new JLabel(
							"1. Stand infront of the first obstacle.");
					lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 11));
					lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
					panel_1.add(lblNewLabel);
				}
				{
					JLabel lblNewLabel_3 = new JLabel(
							"2. Zoom out as much as possible");
					panel_1.add(lblNewLabel_3);
				}
				{
					JLabel lblNewLabel_4 = new JLabel("3. Start script.");
					panel_1.add(lblNewLabel_4);
				}
				{
					JLabel lblIfYouHave = new JLabel(
							"If you have to restart script");
					lblIfYouHave.setFont(new Font("Tahoma", Font.BOLD, 15));
					panel_1.add(lblIfYouHave);
				}
				{
					JLabel lblNewLabel_5 = new JLabel(
							"Some locations require that you go back to the first");
					panel_1.add(lblNewLabel_5);
				}
				{
					JLabel lblNewLabel_6 = new JLabel(
							"obstacle or they won't work.");
					panel_1.add(lblNewLabel_6);
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.BOTTOM, null, null));
			getContentPane().add(panel, BorderLayout.NORTH);
			{
				JLabel lblConfig = new JLabel("Config");
				lblConfig.setFont(new Font("Tahoma", Font.PLAIN, 16));
				panel.add(lblConfig);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JLabel lblScriptCreatedBy = new JLabel(
						"Script created by johnmad @ powerbot.com");
				lblScriptCreatedBy.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblScriptCreatedBy
						.setHorizontalAlignment(SwingConstants.CENTER);
				buttonPane.add(lblScriptCreatedBy);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						try {
							Constants.foodID = Integer.valueOf(foodInput
									.getText());

							Constants.debugMode = DebugModeCheckbox
									.isSelected();

							script.startScript(locationList.getSelectedValue());
						} catch (NumberFormatException nfe) {
							JOptionPane.showMessageDialog(null,
									"FOOD ID HAS TO BE AN INT! YOU ENTERED: "
											+ foodInput.getText(),
									"INPUT ERROR", JOptionPane.ERROR_MESSAGE);
							script.ctx().controller.stop();
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						script.ctx().controller.stop();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	}
}
