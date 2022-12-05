package uos.ai.jam;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import uos.ai.jam.plan.PlanTable;

public class MoniterInterface extends JFrame{
	private WorldModelTable worldModel;
	private IntentionStructure intentionStructure;
	private JLabel worldModelLabel;
	private JLabel planLabel;
	private JLabel currentPlanLabel;
	private ViewModel viewModel;
	private JButton refreshButton;
	private JTable intentionTable;
	private JTable worldModelTable;
	private JScrollPane intentionPane;
	private JScrollPane worldModelPane;
	
	private final Object[] worldModelColumn;
	private final Object[] intentionStructureColumn;
	private int count = 0;
	
	
	public MoniterInterface(WorldModelTable _worldModel, IntentionStructure _intentionStructure){
		this.worldModel = _worldModel;
		this.intentionStructure = _intentionStructure;
		
		worldModelLabel = new JLabel();
		planLabel = new JLabel();
		currentPlanLabel = new JLabel();

		intentionTable = new JTable();
		worldModelTable = new JTable();
		
		intentionPane = new JScrollPane(intentionTable);
		worldModelPane = new JScrollPane(worldModelTable);
		
		worldModelColumn = new Object[1];
		worldModelColumn[0] = "WorldModel";
		intentionStructureColumn = new Object[2];
		intentionStructureColumn[0] = "name";
		intentionStructureColumn[1] = "status";
		viewModel = new ViewModel();
		
		init();
	}
	
	public void init(){
		this.setSize(1025, 560);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		this.add(worldModelPane);
		worldModelPane.setBounds(10, 10, 490, 500);
		this.add(intentionPane);
		intentionPane.setBounds(510, 10, 490, 500);
		
		
		Vector<String> column = new Vector<String>();
		column.addElement("WorldModel");
		DefaultTableModel model = new DefaultTableModel(column,0);
		update();
		
		
		
	}
	
	public void update(){
		
		count++;
		if(count == 1000000){
			viewModel.updateData(intentionStructure, worldModel);
			
			DefaultTableModel worldModel = new DefaultTableModel(viewModel.getWorldModelList(),worldModelColumn);
			DefaultTableModel intentionSturcture = new DefaultTableModel(viewModel.getIntentionList(),intentionStructureColumn);
			worldModelTable.setModel(worldModel);
			intentionTable.setModel(intentionSturcture);
			count = 0;
		}
		

		
	}
	
	
	
	
	
	
	
	
}
