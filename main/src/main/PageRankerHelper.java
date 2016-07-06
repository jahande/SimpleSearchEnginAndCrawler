package main;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.hibernate.Hibernate;

import com.mkyong.persistence.HibernateUtil;

import models.WebPage;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class PageRankerHelper {
	public static void main(String[] args) {
		PageRankerHelper.getInstance().testgetDirectedGraph();
	}
	private static PageRankerHelper instance= null;
	
	public static PageRankerHelper getInstance() {
		if(null==instance){
			PageRankerHelper.instance = new PageRankerHelper();
		}
		return instance;
	}

	private PageRank<WebPage, MyEdge> pageRank = null;
	private List<WebPage> webPages = null;

	public List<WebPage> getWebPages() {
		if (this.webPages == null) {
			this.webPages = WebPage.getAll();
		}
		return webPages;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	private double alpha = 1;

	public DirectedGraph<WebPage, MyEdge> testgetDirectedGraph() {

		DirectedSparseGraph<WebPage, MyEdge> dSGraph = new DirectedSparseGraph<WebPage, MyEdge>();

		List<WebPage> pages = this.getWebPages();
		for (WebPage from : pages) {
			dSGraph.addVertex(from);

		}
		for (WebPage from : pages) {
			for (WebPage to : from.getLinkedWebPages()) {
				dSGraph.addEdge(new MyEdge(from, to), from, to);
			}
		}

		this.pageRank = new PageRank<WebPage, MyEdge>(dSGraph, 0.1);
		pageRank.evaluate();
		// pageRank.

		Collections.sort(pages, new Comparator<WebPage>() {
			public int compare(WebPage o1, WebPage o2) {
				double s1 = PageRankerHelper.this.pageRank.getVertexScore(o1);
				double s2 = PageRankerHelper.this.pageRank.getVertexScore(o2);

				// int s11 =
				// (int)(PageRankerHelper.this.pageRank.getVertexScore(o1)*10000);
				return (int) (Integer.MAX_VALUE * (s1 - s2));

			}
		});
		double min = pageRank.getVertexScore(pages.get(0));
		for (WebPage webPage : pages) {
			System.out.println("1- " + webPage.getURL() + "   >>"
					+ pageRank.getVertexScore(webPage) );
		}
		return dSGraph;
	}

	public DirectedGraph<WebPage, MyEdge> getDirectedGraph() {

		DirectedSparseGraph<WebPage, MyEdge> dSGraph = new DirectedSparseGraph<WebPage, MyEdge>();
		List<WebPage> pages = WebPage.getAll();
		for (WebPage from : pages) {
			dSGraph.addVertex(from);

		}
		for (WebPage from : pages) {
			for (WebPage to : from.getLinkedWebPages()) {
				dSGraph.addEdge(new MyEdge(from, to), from, to);
			}
		}
		return dSGraph;

	}

	public PageRank<WebPage, MyEdge> getPageRank() {
		if (this.pageRank == null) {
			this.pageRank = new PageRank<WebPage, MyEdge>(this
					.getDirectedGraph(), this.alpha);
			pageRank.evaluate();
		}
		// pageRank.

		return pageRank;

	}

	public static WebPage getWebPageFromFileName(String fileName) {
		int lastIndexOfDot = fileName.lastIndexOf('.');
		int webPageId = new Integer(fileName.substring(0, lastIndexOfDot)) - 1;
		return PageRankerHelper.getInstance().getWebPages().get(webPageId);
	}

	public static double getScore(String fileName) {
		try {
			// int lastIndexOfDot = fileName.lastIndexOf('.');
			// int webPageId = new Integer(fileName.substring(0,
			// lastIndexOfDot))-1;
			return PageRankerHelper.getInstance().getPageRank().getVertexScore(
					PageRankerHelper.getWebPageFromFileName(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in get score from file name of file: "
					+ fileName);
			return 1.0 / PageRankerHelper.getInstance().getWebPages().size();
		}
	}

}
