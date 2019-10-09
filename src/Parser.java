import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
	public static ResultsPanel rp;
	private static List<Review> reviews = new ArrayList<Review>();
				
	public Parser() {
		//Boilerplate JFrame methods
		setTitle("Review Camp Parser");
		setSize(1000,1000);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Add PathPanel at top of JFrame
		pp = new pathPanel();
		add(pp, BorderLayout.NORTH);
		
		//Set default console output as JTextArea
		//Create console
		JTextArea taConsole = new JTextArea(10,30);
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
		
		System.out.println("Save your excel spreadsheet as a tab-delimited text file, and enter the file path into the box above! \n");
		
		//Add a new JPanel for the console and add to JFrame
		JPanel consolePanel = new JPanel();
		add(consolePanel,BorderLayout.WEST);
		consolePanel.add(scroll);
		
		//Add a new JPanel for numerical results
		rp = new ResultsPanel();
		rp.setSize(300, 300);
		rp.setLayout(new GridLayout(7,4,5,5));
		rp.setBorder(new EmptyBorder((new Insets(0,50,0,50))));
		add(rp);
		repaint();
		
		
		validate();
		 
	}
	
	public static List<Review> getReviews() {
		return reviews;
	}
	
	public static void setReviews(List<Review> r){
		reviews = r;
	}
}	