package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeSet;

import javax.persistence.Query;

import models.WebPage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private String startURL = "http://en.wikipedia.org/wiki/Information_retrieval";
	private String baseURLAddress = "http://en.wikipedia.org";
	private String prefixRegex = "/wiki/.*";
	private LinkedList<String> uRLsToCrawl = new LinkedList<String>();
	private ArrayList<String> crawledURLs = new ArrayList<String>();
	private String corpusDirectory = SimpleFileIndexer.getDataDir();
	private String corpusExtension = SimpleFileIndexer.getSuffix();
	private int maxCrawlNumber = 200;

	private ArrayList<String>[] pageLinks = new ArrayList[300];
	// private WebPage[] webPages = new WebPage[300];

	private TreeSet<WebPage> storedWebpages = new TreeSet<WebPage>();

	public Document getJSOUPDocument(String absoluteURL) throws IOException {
		Document doc;
		doc = Jsoup.connect(absoluteURL).get();
		return doc;
	}

	public void saveCorpusToFile(String fileName, String text) {
		try {

			// String ab = this.corpusDirectory + fileName + "."
			// + this.corpusExtension;
			// System.out.println(ab);
			File file = new File(this.corpusDirectory + fileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();

			// System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public WebPage storeWebPageInDB(String absoluteURL) {
		WebPage page = new WebPage(absoluteURL);
		page.save();
		this.storedWebpages.add(page);
		return page;
	}

	public WebPage isStored(String url) {
		WebPage wp = new WebPage(url);
		if (!this.storedWebpages.contains(wp)) {
			return null;
		} else {
			return this.storedWebpages.ceiling(wp);
		}
	}

	public void crawl() {
		int i = 0;
		this.uRLsToCrawl.addLast(this.startURL);
		while (this.crawledURLs.size() < this.maxCrawlNumber) {
			String url = this.uRLsToCrawl.getFirst();
			this.uRLsToCrawl.removeFirst();
			this.crawledURLs.add(url);
			i++;
			try {
				this.pageResolver(url);
				System.out.println("page " + i + " successful: " + url);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("page " + i + " failed: " + url);

			}
		}
	}

	public void pageResolver(String absoluteURL) {
		try {
			// line below moved to upper level
			// this.crawledURLs.add(absoluteURL);
			Document doc = this.getJSOUPDocument(absoluteURL);

			ArrayList<String> fetchedURLs = this.getURLsFromPageText(doc,
					this.baseURLAddress);
			for (String string : fetchedURLs) {
				if (!this.crawledURLs.contains(string)
						&& !this.uRLsToCrawl.contains(string)
						&& ((this.uRLsToCrawl.size() + this.crawledURLs.size()) < (this.maxCrawlNumber ))
						&& this.crawledURLs.size() < this.maxCrawlNumber
						&& this.uRLsToCrawl.size() < this.maxCrawlNumber) {
					this.uRLsToCrawl.addLast(string);
				}
			}
			String text = this.getPageText(doc);
			WebPage mainWebPage = WebPage.getByURL(absoluteURL);
			int webPageId = 0;
			if (mainWebPage == null) {
				mainWebPage = this.storeWebPageInDB(absoluteURL);
				webPageId = mainWebPage.getWebPageId();
			} else {
				webPageId = mainWebPage.getWebPageId();
			}
			int i = 0;
			for (String string : fetchedURLs) {
				if (i <= this.maxCrawlNumber
						&& (this.crawledURLs.contains(string) || this.uRLsToCrawl
								.contains(string))) {
					WebPage temp = WebPage.getByURL(string);
					if (temp == null) {
						temp = this.storeWebPageInDB(string);
					}
					mainWebPage.getLinkedWebPages().add(temp);
					i++;
				}
			}
			mainWebPage.save();
			this.saveCorpusToFile(Integer.toString(webPageId) + "."
					+ this.corpusExtension, text);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void fastPageResolver(String absoluteURL) {
		try {
			// line below moved to upper level
			// this.crawledURLs.add(absoluteURL);
			Document doc = this.getJSOUPDocument(absoluteURL);

			ArrayList<String> fetchedURLs = this.getURLsFromPageText(doc,
					this.baseURLAddress);
			for (String string : fetchedURLs) {
				if (!this.crawledURLs.contains(string)
						&& (this.uRLsToCrawl.size() + this.crawledURLs.size()) < this.maxCrawlNumber ) {
					this.uRLsToCrawl.addLast(string);
				}
			}
			String text = this.getPageText(doc);
			WebPage mainWebPage = this.isStored(absoluteURL);
			int webPageId = 0;
			if (mainWebPage == null) {
				mainWebPage = this.storeWebPageInDB(absoluteURL);
				webPageId = mainWebPage.getWebPageId();
			} else {
				webPageId = mainWebPage.getWebPageId();
			}
			int i = 0;
			for (String string : fetchedURLs) {
				if (i <= this.maxCrawlNumber
						&& (this.crawledURLs.contains(string) || this.uRLsToCrawl
								.contains(string))) {
					WebPage temp = this.isStored(string);
					if (temp == null) {
						temp = this.storeWebPageInDB(string);
					}
					mainWebPage.getLinkedWebPages().add(temp);
					i++;
				} else {
					break;
				}
			}
			mainWebPage.save();
			this.saveCorpusToFile(Integer.toString(webPageId) + "."
					+ this.corpusExtension, text);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getPageText(Document doc) {
		return doc.text();
	}

	public String getPageText1(String absoluteURL) {
		ArrayList<String> hrefs = new ArrayList<String>();
		Document doc;
		try {

			// need http protocol
			// doc =
			// Jsoup.connect("http://en.wikipedia.org/wiki/Information_retrieval").get();
			doc = Jsoup.connect(absoluteURL).get();
			doc.text();
			// get page title
			String title = doc.title();
			System.out.println("title : " + title);

			// get all links
			Elements links = doc.select("a[href]");

			for (int i = 0; i < links.size(); i++) {

				Element link = links.get(i);

				// get the value from href attribute
				// System.out.println("\nlink : " + link.attr("href"));
				// System.out.println("text : " + link.text());

				String hrefAttr = link.attr("href");
				if (hrefAttr.matches(prefixRegex) && !hrefs.contains(hrefAttr)) {
					hrefs.add(hrefAttr);
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String str : hrefs) {
			System.out.println(str);
		}
		return null;
	}

	public ArrayList<String> getURLsFromPageText(Document doc, String baseUrl) {
		Elements links = doc.select("a[href]");
		ArrayList<String> results = new ArrayList<String>();
		for (int i = 0; i < links.size(); i++) {

			Element link = links.get(i);

			// get the value from href attribute
			// System.out.println("\nlink : " + link.attr("href"));
			// System.out.println("text : " + link.text());

			String hrefAttr = link.attr("href");
			if (hrefAttr.matches("/wiki/.*")
					&& !Utils.lastsWith(hrefAttr, ".png")
					&& !Utils.lastsWith(hrefAttr, ".jpg")
					&& !Utils.lastsWith(hrefAttr, ".jpeg")
					&& !Utils.lastsWith(hrefAttr, ".gif")
					&& !results.contains(baseUrl + hrefAttr)) {
				results.add(baseUrl + hrefAttr);
			}

		}
		return results;
	}

	public void buildDirectedGraph() {

	}

	public void resetAll() {
		File dir = new File(this.corpusDirectory);
		for (File file : dir.listFiles()) {
			file.delete();
		}

		WebPage.trunc();
	}
}
