import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import java.io.File;

public class OrderByMain {
    public static void main(String[] args) throws Exception {
        File indexDirectory = new File(IndexMain.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        Directory directory = FSDirectory.open(indexDirectory.toPath());

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        //TermQuery nameQuery = new TermQuery(new Term("name1", "Name1_22"));
        //Query ageQuery = LongPoint.newSetQuery("age", 31);

        SortField sf = new SortField("name1", SortField.Type.STRING_VAL,true);

        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        //booleanQueryBuilder.add(nameQuery, BooleanClause.Occur.MUST);
        //booleanQueryBuilder.add(ageQuery, BooleanClause.Occur.MUST);
        TopDocs topDocs = indexSearcher.search(booleanQueryBuilder.build(), Integer.MAX_VALUE, new Sort(sf));

        System.out.println("count : " + topDocs.totalHits.value);
        long searchCount = topDocs.totalHits.value;
        for (int index = 0; index < searchCount; index++) {
            Document document = indexSearcher.doc(topDocs.scoreDocs[index].doc);
            System.out.println(" - id : " + document.get("id"));
        }
    }
}