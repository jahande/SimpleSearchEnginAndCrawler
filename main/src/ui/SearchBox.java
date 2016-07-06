package ui;

import javax.swing.*;

import main.Crawler;
import main.SimpleFileIndexer;
import main.SimpleSearcher;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchBox extends JFrame {
	public class ButtonActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			button_actionPerformed(e);

		}

	}

	protected void button_actionPerformed(ActionEvent e) {
		System.out.println("sasas");
		try {
				String s = SimpleSearcher.SearchHelper(this.txtf.getText(),
						new Integer(this.hint.getText()));
				this.textArea.setText(s);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public class BuildIndexActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			buildIndex_actionPerformed(e);

		}

	}

	protected void buildIndex_actionPerformed(ActionEvent e) {
		System.out.println("sasas");
		try {
			SimpleFileIndexer.IndexHelper();
			System.out.println("Indexing successfuly complete");
			JOptionPane.showMessageDialog(this, "Indexing successfuly complete");
			// TODO Auto-generated catch block

		} catch (Exception e1) {
			System.out.println("Indexing failed");
			JOptionPane.showMessageDialog(this, "Indexing failed", "Indexing failed", JOptionPane.ERROR_MESSAGE);
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	
	
	
	
	
	public class CrawlActionListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			crawl_actionPerformed(e);

		}

	}

	protected void crawl_actionPerformed(ActionEvent e) {
		System.out.println("sasas");
		try {
			if(false){
				Crawler crawler = new Crawler();
				// crawler.resetAll();
				// crawler.saveCorpusToFile("1", "assasa");
				crawler.crawl();
			}
			JOptionPane.showMessageDialog(this, "crawling successfuly complete");
			// TODO Auto-generated catch block

		} catch (Exception e1) {
			System.out.println("Indexing failed");
			JOptionPane.showMessageDialog(this, "crawling failed", "Indexing failed", JOptionPane.ERROR_MESSAGE);
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	

	private TextField txtf = new TextField();
	private TextField hint = new TextField();
	private JPanel jp = new JPanel();
	private TextArea textArea = new TextArea();
	private final JButton button = new JButton();
	private final JButton buildIndex = new JButton();
	private final JButton crawl = new JButton("recrawl");

	
	private JLabel query = new JLabel("query:");
	private JLabel result = new JLabel("result:");
	private JLabel resultnumber = new JLabel("number of results:");

	public static void main(String[] args) {
		SearchBox s = new SearchBox();
		s.setVisible(true);

	}

	public SearchBox() {
		this.getContentPane().setLayout(null);
		this.setSize(800, 600);
		this.txtf.setBounds(20, 30, 500, 20);
		this.query.setBounds(20, 10, 50, 20);

		this.hint.setBounds(540, 30, 20, 20);
		this.hint.setText("20");
		this.resultnumber.setBounds(540, 10, 120, 20);

		this.result.setBounds(20, 65, 50, 20);
		this.textArea.setBounds(20, 90, 540, 200);
		this.textArea.setEditable(false);

		this.button.setBounds(20, 300, 100, 20);
		this.button.setText("search");
		this.button.addActionListener(new ButtonActionListener());
		
		this.buildIndex.addActionListener(new BuildIndexActionListener());
		this.buildIndex.setText("build index");
		this.buildIndex.setBounds(140, 300, 100, 20);
		
		this.crawl.addActionListener(new CrawlActionListener());
		this.crawl.setBounds(260, 300, 100, 20);
		

		
		
		this.jp.setBounds(10, 10, 700, 500);
		this.jp.setBackground(Color.WHITE);
		this.jp.setLayout(null);

		this.jp.add(this.txtf);
		this.jp.add(this.hint);
		this.jp.add(this.textArea);
		this.jp.add(this.button);
		this.jp.add(this.query);
		this.jp.add(this.result);
		this.jp.add(this.resultnumber);
		this.jp.add(this.buildIndex);
		this.jp.add(this.crawl);
		

		this.getContentPane().add(this.jp);
		// this.getContentPane().add(this.txtf);
		// TODO Auto-generated constructor stub
	}

}
