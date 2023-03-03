import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.File;

public class OrderByMainOutOfMem {
    public static void main(String[] args) throws Exception {
        File indexDirectory = new File(IndexMain.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        Directory directory = FSDirectory.open(indexDirectory.toPath());

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        System.out.println("start");
        SortField sf = new SortField("name1", SortField.Type.STRING,true);
        System.out.println("sort start");
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        TermQuery nameQuery = new TermQuery(new Term("name1", "Name1_22"));
        booleanQueryBuilder.add(nameQuery, BooleanClause.Occur.MUST);

        TopDocs topDocs = indexSearcher.search(booleanQueryBuilder.build(), Integer.MAX_VALUE, new Sort(sf));
        System.out.println("sort end");
        System.out.println("count : " + topDocs.totalHits.value);
        long searchCount = topDocs.totalHits.value;
        for (int index = 0; index < searchCount; index++) {
            Document document = indexSearcher.doc(topDocs.scoreDocs[index].doc);
            System.out.println(" - id : " + document.get("id"));
        }
    }
}