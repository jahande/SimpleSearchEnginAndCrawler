package main;

import java.io.File;
import java.security.acl.LastOwnerException;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SimpleSearcher {
	static SimpleSearcher simpleSearcher = null;

	public static SimpleSearcher getSimpleSearcher() {
		if (null == SimpleSearcher.simpleSearcher) {
			SimpleSearcher.simpleSearcher = new SimpleSearcher();
		}
		return simpleSearcher;
	}

	public static String SearchHelper(String query, int hits) throws Exception {
		File indexDir = new File(SimpleFileIndexer.getIndexDirAddr());

		// number of results

		return SimpleSearcher.getSimpleSearcher().searchIndex(indexDir, query, hits);

	}

	public static void main(String[] args) throws Exception {

		File indexDir = new File(
				"G:\\arsh\\projects\\mkyoung_mirtest\\indexes\\");

		// number of results
		String query = "index";
		int hits = 3;

		SimpleSearcher searcher = new SimpleSearcher();
		searcher.searchIndex(indexDir, query, hits);

	}

	private String searchIndex(File indexDir, String queryStr, int maxHits)
			throws Exception {

		Directory directory = FSDirectory.open(indexDir);

		IndexSearcher searcher = new IndexSearcher(directory);
		QueryParser parser = new QueryParser(Version.LUCENE_30, "contents",
				new SimpleAnalyzer());
		Query query = parser.parse(queryStr);

		TopDocs topDocs = searcher.search(query, maxHits);
		String result = "";
		ScoreDoc[] hits = topDocs.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			// System.out.println(d.get("filename"));
			String longFileName = d.get("filename");
			int lastBSlash = longFileName.lastIndexOf("\\");

			String shortFileName = longFileName.substring(lastBSlash + 1);

			System.out.println(PageRankerHelper.getWebPageFromFileName(
					shortFileName).getURL());
					//+ "   BOOST= " + d.getBoost());
			result += PageRankerHelper.getWebPageFromFileName(shortFileName)
					.getURL()+"\n";
		}

		System.out.println("Found " + hits.length);
		return result;

	}
}
