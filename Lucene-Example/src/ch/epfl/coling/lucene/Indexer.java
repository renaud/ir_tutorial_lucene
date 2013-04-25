package ch.epfl.coling.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello Lucene: demonstrates indexing text
 * 
 * @author renaud.richardet@fhnw.ch
 */
public class Indexer {

	public static final String CONTENT_FIELD = "text";
	public static final String INDEX_PATH = "index";

	public static void main(String[] args) throws Exception {

		// create writer
		Directory dir = FSDirectory.open(new File(INDEX_PATH));
		Analyzer analyzer = getAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_40,
		        analyzer);
		iwc.setOpenMode(OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, iwc);

		// index
		for (String text : getText()) {
			System.out.println("indexing:: " + text);
			Document doc = new Document();
			doc.add(new TextField(CONTENT_FIELD, text, Store.YES));
			indexWriter.addDocument(doc);
		}

		// don't forget to close / optimize
		indexWriter.close();
	}

	/** Stopwords, TODO */
	static String[] stopwords = { "the", "a" };

	/** Analyzer(s) */
	static Analyzer getAnalyzer() {
		return new StandardAnalyzer(Version.LUCENE_40);
		// Stopwords, TODO
		// return new StandardAnalyzer(Version.LUCENE_40, new CharArraySet(
		// Version.LUCENE_40, Arrays.asList(stopwords), true));
	}

	/** Sample content for indexing */
	private static String[] getText() {
		return new String[] { //
		"The quick brown fox    jumps over the lazy dog",//
		        "The slow  red   fox    jumps over the red dog",//
		        "The quick brown rabbit jumps over the lazy dog" };
	}

	/** More sample content for indexing */
	private static String[] getText2() throws IOException {
		return readFile("data/pmc_sample.txt");
	}

	private static String[] readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		List<String> sb = new ArrayList<String>();
		try {
			String line = br.readLine();

			while (line != null) {
				sb.add(line);
				line = br.readLine();
			}
		} finally {
			br.close();
		}
		return sb.toArray(new String[sb.size()]);
	}
}