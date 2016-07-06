package main;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import models.WebPage;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.FSDirectory;

import edu.uci.ics.jung.algorithms.scoring.PageRank;

public class SimpleFileIndexer {
	static String indexDirAddr = "G:\\arsh\\projects\\mkyoung_mirtest\\indexes\\";
	static String dataDir = "G:\\arsh\\projects\\mkyoung_mirtest\\saved_corpuses\\";
	public static String getDataDir() {
		return dataDir;
	}


	public static void setDataDir(String dataDir) {
		SimpleFileIndexer.dataDir = dataDir;
	}

	static String suffix = "txt";
	
	public static String getSuffix() {
		return suffix;
	}


	public static String getIndexDirAddr() {
		return indexDirAddr;
	}


	public static void setIndexDirAddr(String indexDirAddr) {
		SimpleFileIndexer.indexDirAddr = indexDirAddr;
	}


	public static void IndexHelper() throws Exception {
		
		File indexDir = new File(SimpleFileIndexer.indexDirAddr);
		File dataDir = new File(SimpleFileIndexer.dataDir);
		String suffix = SimpleFileIndexer.suffix;
		
		
		SimpleFileIndexer indexer = new SimpleFileIndexer();
		
		int numIndex = indexer.index(indexDir, dataDir, suffix);
		
		//System.out.println("Total files indexed " + numIndex);
		
	}
	
	
	private int index(File indexDir, File dataDir, String suffix) throws Exception {
		
		IndexWriter indexWriter = new IndexWriter(
				FSDirectory.open(indexDir), 
				new SimpleAnalyzer(),
				true,
				IndexWriter.MaxFieldLength.LIMITED);
		indexWriter.setUseCompoundFile(false);
		
		indexDirectory(indexWriter, dataDir, suffix);
		
		int numIndexed = indexWriter.maxDoc();
		indexWriter.optimize();
		indexWriter.close();
		
		return numIndexed;
		
	}
	
	private void indexDirectory(IndexWriter indexWriter, File dataDir, String suffix) throws IOException {
		//PageRankerHelper ph = PageRankerHelper.getInstance();
		
		File[] files = dataDir.listFiles();
		System.out.println("BOOOOOST CHECK");
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				indexDirectory(indexWriter, f, suffix);
			}
			else {
				System.out.println(PageRankerHelper.getWebPageFromFileName(f.getName()).getURL()+">>>>BOOST>>>"+(int)(PageRankerHelper.getScore(f.getName())*100000));
				indexFileWithIndexWriter(indexWriter, f, suffix,(float)PageRankerHelper.getScore(f.getName()));
			}
		}
		System.out.println("END BOOOOOST CHECK");
		
	}
	
	private void indexFileWithIndexWriter(IndexWriter indexWriter, File f, String suffix,float boost) throws IOException {
		if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
			return;
		}
		if (suffix!=null && !f.getName().endsWith(suffix)) {
			return;
		}
		System.out.println("Indexing file " + f.getName());
		
		Document doc = new Document();
		doc.add(new Field("contents", new FileReader(f)));		
		doc.add(new Field("filename", f.getCanonicalPath(), Field.Store.YES, Field.Index.ANALYZED));
		
		
		//set booooooooooooooooooooooooooooooooooooooost
		System.out.println("lastBoooooost>>"+boost);
		doc.setBoost(boost);
		
		
		indexWriter.addDocument(doc);
	}

}
