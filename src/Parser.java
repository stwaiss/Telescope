import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;


public class Parser extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static pathPanel pp;
	public static statsPanel statPanel;
	public static graphPanel graphPanel;
	public static choicePanel choicePanel;
	public static screenCapPanel screenCapPanel;
	public static parserMenuBar menuBar;
	private static List<Review> reviews = new ArrayList<Review>();
	private static List<keywordSet> masterKeywordSet = new ArrayList<keywordSet>();
	
	//**********************************************************************
	
	public Parser() {
		//Boilerplate JFrame methods
		setTitle("Review Camp Parser");
		setSize(1000,750);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Add menu bar to the top of the frame
		menuBar = new parserMenuBar();
		setJMenuBar(menuBar);
		
		//Add PathPanel at top of JFrame
		pp = new pathPanel();
		add(pp, BorderLayout.NORTH);
	
		//Add new JPanel to encapsulate everything not the path panel
		JPanel lowerWrapper = new JPanel();
		lowerWrapper.setLayout(new BorderLayout());
		add(lowerWrapper, BorderLayout.CENTER);
		
		//Add new JPanel to encapsulate console and choices and add to lower wrapper
		JPanel consoleAndChoiceWrapper = new JPanel();
		consoleAndChoiceWrapper.setLayout(new GridLayout(2,1,5,5));
		consoleAndChoiceWrapper.setSize(300,600);
		lowerWrapper.add(consoleAndChoiceWrapper, BorderLayout.WEST);
		
		//Set default console output as JTextArea
		//Create console
		JTextArea taConsole = new JTextArea(15,30);
		taConsole.setWrapStyleWord(true);
		taConsole.setLineWrap(true);
		taConsole.setEditable(false);
		taConsole.setFocusable(false);
		
		//Force console to auto-scroll to bottom of window
		DefaultCaret caret = (DefaultCaret)taConsole.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//Create a scroll-able wrapper
		JScrollPane scroll = new JScrollPane(taConsole,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		PrintStream out = new PrintStream (new TextAreaOutputStream (taConsole));
		System.setOut(out);
		
		//this has to be down here to force the output to display in the gui
		readInKeywordSets();
		
		System.out.println("Delete all header data, and save your excel file."
				+ "\nEnter the file path into the box above! \n");
		
		//Add a new JPanel for the console and add to JFrame
		JPanel consolePanel = new JPanel();
		consolePanel.setBorder(new EmptyBorder((new Insets(20,20,20,20))));
		add(consolePanel,BorderLayout.WEST);
		consolePanel.add(scroll);
		consoleAndChoiceWrapper.add(consolePanel);
		
		//Add choice Panel to console and choice panel
		choicePanel = new choicePanel();
		consoleAndChoiceWrapper.add(choicePanel);
		
		
		//Create new wrapper to hold stats and graphs
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2,1,5,5));
		
		//add lower components to frame
		add(consoleAndChoiceWrapper, BorderLayout.WEST);
		add(infoPanel, BorderLayout.CENTER);
		//Add a new JPanel for numerical results
		statPanel = new statsPanel();
		statPanel.setLayout(new GridLayout(7,6,5,5));
		statPanel.setBorder(new EmptyBorder((new Insets(20,20,20,20))));
		infoPanel.add(statPanel);
		
		//Add graphPanel to the infoPanel Wrapper
		graphPanel = new graphPanel();
		graphPanel.setLayout(new BorderLayout());
		graphPanel.setBorder(new EmptyBorder((new Insets(20,20,20,20))));
		infoPanel.add(graphPanel);
				
		screenCapPanel = new screenCapPanel();
		add(screenCapPanel, BorderLayout.SOUTH);
		
		
		repaint();
		validate();
		 
	}
	
	//********************************************************************************
	
	public static List<Review> getReviews() {
		return reviews;
	}
	
	public static void setReviews(List<Review> r){
		reviews = r;
	}
	
	public static List<keywordSet> getKeywordSet(){
		return masterKeywordSet;
	}
	
	//This method pulls all the saved keywords from the txt file and makes objects out of them
	public void readInKeywordSets() {
		
		try {
			//Open the .txt file
			File keywordSetsFile = new File("keywordSets.txt");
			Scanner scanner = new Scanner(keywordSetsFile);
			scanner.useDelimiter(";");
			
			//while there are more lines delimited with a ;
			while(scanner.hasNext()) {
				
				//Save the data
				String line = scanner.nextLine();
				
				//Split the line by the hyphen, trim the whitespace, and save the product type
				String productType = line.split("-")[0].trim();
				
				//Use everything else to save into a string array, split by commas
				String keywordDataArray[] = line.split("-")[1].split(",");
				
				//Create a List to finish the keywordSet object
				List<String> keywordDataList = new ArrayList<String>();
				
				//iterate over the array and save the individual keywords into the list
				for(String s : keywordDataArray) {
					keywordDataList.add(s.trim());
				}
				
				//Create a new keywordSet and add to master;
				keywordSet k = new keywordSet(productType, keywordDataList);
				masterKeywordSet.add(k);	
			}
			
			
			System.out.println(masterKeywordSet.size() + " keyword sets were added\n");
		
			scanner.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("KeywordSets file failed to load");
			e.printStackTrace();
		}
	}
}	