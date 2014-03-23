package Agility;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JList;
import java.awt.Color;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.AbstractListModel;
import javax.swing.UIManager;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import java.awt.GridLayout;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;

public class ConfigDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JCheckBox DebugModeCheckbox;
	private JList locationList;
	private AgilityScript script;
	

	public ConfigDialog(AgilityScript as) {
		this.script = as;
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
				locationList = new JList();
				panel.add(locationList);
				locationList.setBorder(UIManager.getBorder("List.focusCellHighlightBorder"));
				locationList.setModel(new AbstractListModel() {
					String[] values = new String[] {"Burthrope", "GnomeVillage", "BarbOutpost"};
					public int getSize() {
						return values.length;
					}
					public Object getElementAt(int index) {
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
				FlowLayout flowLayout = (FlowLayout) panel_1.getLayout();
				panel.add(panel_1, BorderLayout.EAST);
				{
					DebugModeCheckbox = new JCheckBox("Debug mode");
					panel_1.add(DebugModeCheckbox);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, BorderLayout.CENTER);
				panel_1.setLayout(new GridLayout(0, 2, 0, 0));
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.BOTTOM, null, null));
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
				JLabel lblScriptCreatedBy = new JLabel("Script created by johnmad @ powerbot.com");
				lblScriptCreatedBy.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblScriptCreatedBy.setHorizontalAlignment(SwingConstants.CENTER);
				buttonPane.add(lblScriptCreatedBy);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Constants.debugMode = DebugModeCheckbox.isSelected();
					
						
						script.startScript((String)locationList.getSelectedValue());
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						script.getController().stop();
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
}
