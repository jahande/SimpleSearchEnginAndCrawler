package main;

import java.io.IOException;
import java.util.ArrayList;

import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mkyong.common.Stock;
import com.mkyong.persistence.HibernateUtil;

public class Test {
	
	
	
	public static void main(String[] args) {
		//testhibernate();
		testgoog();
	}
	
	public static void testhibernate() {
		//System.out.println("Hibernate one to one (Annotation)");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
 
		session.beginTransaction();
 
		Stock stock = new Stock();
 
		stock.setStockCode("7052");
		stock.setStockName("PADINI");
 
	
	
		session.save(stock);
		session.getTransaction().commit();
 
		System.out.println("Done");
	}
	
	
	public static void regexTest(){
		String s = "/wiki/.*";
		System.out.println("++++adaasdsd 43ddsf trfssd.jjn y ,. sdf.......".matches("\\w.*"));
	}
	public static void test1() {
		Document doc= null;
			try {
				doc = Jsoup.connect("http://en.wikipedia.org/wiki/Information_retrieval").get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(doc.text());
			Elements newsHeadlines = doc.select("#href");
			System.out.println(newsHeadlines.size());
			for (int i = 0; i < newsHeadlines.size(); i++) {
				System.out.println(newsHeadlines.get(i));
			}
			
	}
	
	 public static void testgoog() {
		 ArrayList<String> hrefs = new ArrayList<String>();
			Document doc;
			try {
		 
				// need http protocol
				doc = Jsoup.connect("http://en.wikipedia.org/wiki/Information_retrieval").get();
		 
				// get page title
				String title = doc.title();
				System.out.println("title : " + title);
		 
				// get all links
				Elements links = doc.select("a[href]");
				
					for (int i = 0; i < links.size(); i++) {
						
					Element link = links.get(i);
		 
					// get the value from href attribute
					//System.out.println("\nlink : " + link.attr("href"));
					//System.out.println("text : " + link.text());
					
					String hrefAttr = link.attr("href");
					if(hrefAttr.matches("/wiki/.*") && !hrefs.contains(hrefAttr)){
						hrefs.add(hrefAttr);
					}
		 
				}
		 
			} catch (IOException e) {
				e.printStackTrace();
			}
		 
			for (String  str : hrefs) {
				System.out.println(str);
			}
		  }

}
