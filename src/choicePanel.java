import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;


public class choicePanel extends JPanel{
	
	private static JRadioButton[] options = new JRadioButton[4];
	ButtonGroup group = new ButtonGroup();
	public choicePanel() {
		
		//set border and layout type
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(new EmptyBorder((new Insets(20,20,20,20))));
		
		//define options array to include 4 JRadioButtons
		options[0] = new JRadioButton("By Star Rating");
		options[0].addActionListener(new totalStarRatingListener());
		
		options[1] = new JRadioButton("By Star Rating vs Year");
		options[1].addActionListener(new starRatingVsYearListener());
		
		options[2] = new JRadioButton("By Star Rating vs Seller");
		options[2].addActionListener(new starRatingVsSellerListener());
		
		options[3] = new JRadioButton("Keyword Pareto");
		//options[3].addActionListener(new graphPanel.keywordParetoListener());
		
		
		//add the radio buttons to the group and to the pane, and also add separators for space
		for (JRadioButton b: options) {
			group.add(b);
			add(b);
			this.add(Box.createRigidArea(new Dimension(0,25)));
		}	
	}
	
	//*******************************************************************************************************
	public static class totalStarRatingListener  implements ActionListener {		
		public void actionPerformed(ActionEvent e) {

			//Only do something if there are reviews in the system
			if(Parser.getReviews().size() != 0) {
				//System.out.println("Doing something");
				JFreeChart barChart = ChartFactory.createBarChart(
						"Total Star Ratings",
						"Star Rating",
						"Count",
						createDataset(),
						PlotOrientation.VERTICAL,
						true,
						true,
						false);
				
				ChartPanel myChart = new ChartPanel(barChart);
				Parser.graphPanel.add(myChart, BorderLayout.CENTER);
				Parser.graphPanel.validate();
				
			}
			else {
				return;
			}


		}
		
		private CategoryDataset createDataset() {
			//create labels for each bar in the bar graph
			final String[] starLabels = {"1 Star", "2 Star","3 Star", "4 Star", "5 Star"};
			final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			dataset.addValue(Integer.parseInt(statsPanel.starStatTextFields[6].getText()), "Series 1", starLabels[0]);
			dataset.addValue(Integer.parseInt(statsPanel.starStatTextFields[5].getText()), "Series 1", starLabels[1]);
			dataset.addValue(Integer.parseInt(statsPanel.starStatTextFields[4].getText()), "Series 1", starLabels[2]);
			dataset.addValue(Integer.parseInt(statsPanel.starStatTextFields[3].getText()), "Series 1", starLabels[3]);
			dataset.addValue(Integer.parseInt(statsPanel.starStatTextFields[2].getText()), "Series 1", starLabels[4]);
			
			return dataset;
		}
	}
	
	//*******************************************************************************************************
	
	public static class starRatingVsYearListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			//Only do something if there are reviews in the system
			if(Parser.getReviews().size() != 0) {
				//System.out.println("Doing something");
				JFreeChart barChart = ChartFactory.createBarChart(
						"Star Ratings Per Year",
						"Star Rating",
						"Count",
						createDataset(),
						PlotOrientation.VERTICAL,
						true,
						true,
						false);
				
				ChartPanel myChart = new ChartPanel(barChart);
				Parser.graphPanel.add(myChart, BorderLayout.CENTER);
				Parser.graphPanel.validate();
				
			}
			else {
				return;
			}
		}

		private CategoryDataset createDataset() {	

			final String[] starLabels = {"1 Star", "2 Star","3 Star", "4 Star", "5 Star"}; 
			final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			// pull the years from year distribution 
			String[][] yearDistribution = statsPanel.yearDistribution();

			// create a year x 6 array to hold star ratings per year 
			int[][] reviewsPerYear = new int[5][6];

			//iterate through array and assign the years into the reviewsPerYear, year column 
			for(int i = 0; i < 5; i++) { 
				if(yearDistribution[i][0] != null) {
					reviewsPerYear[i][0] = Integer.valueOf(yearDistribution[i][0]); 
				} 
			}

			//Save the master list of parsed reviews locally 
			List<Review> allReviewsList = Parser.getReviews();

			//Iterate over all the years 
			for(int i = 0; i < 5; i++) { 
				//Iterate over all the reviews 
				for(Review r : allReviewsList) {

					//Pull the year out from the date of the current review 
					String thisReviewsYear = r.getDate().split("-")[0];
	
					//check to see if the year from this current review is the same as the year under analysis 
					if(Integer.valueOf(thisReviewsYear) == reviewsPerYear[i][0]) {
	
						//switch case depending on value, and then add to that count 
						double thisStarDouble = Double.valueOf(r.getStar());
						int thisStarInt = (int) thisStarDouble; 
						
						switch(thisStarInt) { 
							case 1: 
								reviewsPerYear[i][1]++; 
								break;
			
							case 2: 
								reviewsPerYear[i][2]++;
								break; 
							
							case 3: 
								reviewsPerYear[i][3]++; 
								break;
							
							case 4: 
								reviewsPerYear[i][4]++; 
								break; 
								
							case 5: 
								reviewsPerYear[i][5]++; 
								break;
							default: 
								break;
						}//end switch case 
					}//end if 
				}//end inner loop

				dataset.addValue(reviewsPerYear[i][1], String.valueOf(reviewsPerYear[i][0]), starLabels[0]);
				dataset.addValue(reviewsPerYear[i][2], String.valueOf(reviewsPerYear[i][0]), starLabels[1]);
				dataset.addValue(reviewsPerYear[i][3], String.valueOf(reviewsPerYear[i][0]), starLabels[2]);
				dataset.addValue(reviewsPerYear[i][4], String.valueOf(reviewsPerYear[i][0]), starLabels[3]);
				dataset.addValue(reviewsPerYear[i][5], String.valueOf(reviewsPerYear[i][0]), starLabels[4]);
					
			}//end outer loop			
			
			return dataset;
			
		}
	}

	//*******************************************************************************************************
	
	public static class starRatingVsSellerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {

			//Only do something if there are reviews in the system
			if(Parser.getReviews().size() != 0) {
				//System.out.println("Doing something");
				JFreeChart barChart = ChartFactory.createBarChart(
						"Star Ratings vs Seller",
						"Star Rating",
						"Count",
						createDataset(),
						PlotOrientation.VERTICAL,
						true,
						true,
						false);
				
				ChartPanel myChart = new ChartPanel(barChart);
				Parser.graphPanel.add(myChart, BorderLayout.CENTER);
				Parser.graphPanel.validate();
				
			}
			else {
				return;
			}


		}
		
		private CategoryDataset createDataset() {
			//create labels for each bar in the bar graph
			final String[] starLabels = {"1 Star", "2 Star","3 Star", "4 Star", "5 Star"};
			final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
						  
			  
			// pull the years from year distribution 
			String[][] sellerDistribution = statsPanel.sellerDistribution();
			  
			// create a Seller x 6 array to hold star ratings per Seller 
			String[][] reviewsPerSeller = { 
					{null,"0","0","0","0","0"}, 
					{null,"0","0","0","0","0"},
					{null,"0","0","0","0","0"}, 
					{null,"0","0","0","0","0"},
					{null,"0","0","0","0","0"}
				};
			  
			//iterate through array and assign the Sellers into the reviewsPerSeller, Seller column
			for(int i = 0; i < 5; i++) { 
				if(sellerDistribution[i][0] != null) { 
					reviewsPerSeller[i][0] = sellerDistribution[i][0];
				}
			}
			  
			//Save the master list of parsed reviews locally 
			List<Review> allReviewsList = Parser.getReviews();
			  
			//Iterate over all the Sellers 
			for(int i = 0; i < 5; i++) {
				if(reviewsPerSeller[i][0] != null) { //Iterate over all the reviews
					for(Review r : allReviewsList) {
			  
						//Pull the Seller out from the date of the current review 
						String thisReviewsSeller = r.getSeller();
						  
						//check to see if the year from this current review is the same as the year under analysis
						if(thisReviewsSeller == reviewsPerSeller[i][0]) {
						  
							//switch case depending on value, and then add to that count 
							double thisStarDouble = Double.valueOf(r.getStar()); 
							int thisStarInt = (int) thisStarDouble;
							  
							int oldValue = 0; int newValue = 0;
							 
							//Since this is a string array, pull the old string out, convert to int
							//increment the old value and save, convert new value to string and save new
							//value into array
							  
							  
							switch(thisStarInt) { 
								case 1: 
									oldValue = Integer.valueOf(reviewsPerSeller[i][1]); 
									newValue = oldValue + 1;
									reviewsPerSeller[i][1] = String.valueOf(newValue); 
									break; 
								case 2: 
									oldValue = Integer.valueOf(reviewsPerSeller[i][2]); 
									newValue = oldValue + 1;
									reviewsPerSeller[i][2] = String.valueOf(newValue); 
									break; 
								case 3: 
									oldValue = Integer.valueOf(reviewsPerSeller[i][3]); 
									newValue = oldValue + 1;
									reviewsPerSeller[i][3] = String.valueOf(newValue); 
									break; 
								case 4: 
									oldValue = Integer.valueOf(reviewsPerSeller[i][4]); 
									newValue = oldValue + 1;
									reviewsPerSeller[i][4] = String.valueOf(newValue); 
									break; 
								case 5: 
									oldValue = Integer.valueOf(reviewsPerSeller[i][5]); 
									newValue = oldValue + 1;
									reviewsPerSeller[i][5] = String.valueOf(newValue); 
									break; 
								default: 
									break;
							}//end switch case 
						}//end if 
					}//end inner loop
				
				dataset.addValue(Integer.parseInt(reviewsPerSeller[i][1]), String.valueOf(reviewsPerSeller[i][0]), starLabels[0]);
				dataset.addValue(Integer.parseInt(reviewsPerSeller[i][2]), String.valueOf(reviewsPerSeller[i][0]), starLabels[1]);
				dataset.addValue(Integer.parseInt(reviewsPerSeller[i][3]), String.valueOf(reviewsPerSeller[i][0]), starLabels[2]);
				dataset.addValue(Integer.parseInt(reviewsPerSeller[i][4]), String.valueOf(reviewsPerSeller[i][0]), starLabels[3]);
				dataset.addValue(Integer.parseInt(reviewsPerSeller[i][5]), String.valueOf(reviewsPerSeller[i][0]), starLabels[4]);		
				
				}//end outer if 
			}//end outer loop

			return dataset;
		}
	}

}


