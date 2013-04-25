package ch.epfl.coling.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**
 * Hello Lucene: demonstate index search. try it with:<br/>
 * <li>jumps</li> <li>jum, then jum*</li> <li>jumsp then jumsp~</li> <li>fox
 * then fox NOT red</li>
 * 
 * @author renaud.richardet@fhnw.ch
 */
public class Searcher {

	public static void main(String[] args) throws Exception {

		Directory dir = FSDirectory.open(new File(Indexer.INDEX_PATH));
		IndexReader indexReader = DirectoryReader.open(dir);
		IndexSearcher searcher = new IndexSearcher(indexReader);

		Analyzer analyzer = Indexer.getAnalyzer();
		
		QueryParser parser = new QueryParser(Version.LUCENE_40,
		        Indexer.CONTENT_FIELD, analyzer);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in,
		        "UTF-8"));
		while (true) {

			// prompt the user
			System.out.println("\nEnter query: ");
			String queryString = in.readLine();
			if (queryString == null || queryString.length() == -1)
				break;
			queryString = queryString.trim();
			if (queryString.length() == 0)
				break;

			Query query = parser.parse(queryString);
			TopDocs results = searcher.search(query, 10);

			System.out.println(results.totalHits + " hits for '" + queryString
			        + "'");
			ScoreDoc[] hits = results.scoreDocs;
			for (ScoreDoc hit : hits) {
				Document doc = searcher.doc(hit.doc);
				System.out.printf("%5.3f %s\n", hit.score, doc.get(Indexer.CONTENT_FIELD));
			}
		}

		indexReader.close();
	}
}