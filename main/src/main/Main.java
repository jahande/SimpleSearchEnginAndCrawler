package main;

import com.mkyong.persistence.HibernateUtil;

public class Main {

	public static void main(String[] args) {
		try {
			boolean crawlPhase = false;
			if (crawlPhase) {
				Crawler crawler = new Crawler();
				// crawler.resetAll();
				// crawler.saveCorpusToFile("1", "assasa");
				crawler.crawl();
			}

			boolean indexPhase = true;
			if (indexPhase) {
				try {
					SimpleFileIndexer.IndexHelper();
					System.out.println("Indexing successfuly complete");
				} catch (Exception e) {
					System.out.println("Indexing failed");

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			
			SimpleSearcher.SearchHelper("information retrival", 20);
			// new PageRankerHelper().getDirectedGraph();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HibernateUtil.shutdown();
		}

	}
}
