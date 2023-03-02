import Util.TimeChecker;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class DistinctQuerySample {
    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();
        TimeChecker timeChecker2 = new TimeChecker();
        File indexDirectory = new File(IndexMain.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        int splitPage = 10000;
        //IndexReader indexReader = new IndexReader(directory);

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        TermQuery nameQuery = new TermQuery(new Term("name1", "Name1_22"));
        //TermQuery nameQuery = new TermQuery(new Term("name1", "Name1_22"));
        //Query ageQuery = LongPoint.newSetQuery("age", 31);

        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        booleanQueryBuilder.add(nameQuery, BooleanClause.Occur.MUST);
        //booleanQueryBuilder.add(ageQuery, BooleanClause.Occur.MUST);
        ScoreDoc lastPageDoc = null;
        long docCount = 0;
        BooleanQuery query = booleanQueryBuilder.build();
        timeChecker.setStartTime();

        timeChecker2.setStartTime();
        TotalHitCountCollector totalHitCollector = new TotalHitCountCollector();
        indexSearcher.search(query, totalHitCollector);
        timeChecker2.setEndTime();
        System.out.println("getTotalHits : "+totalHitCollector.getTotalHits());
        timeChecker2.printCheckTime();

        do{
            TopDocs topDocs = indexSearcher.searchAfter(lastPageDoc, query, splitPage);
            //indexSearcher.search
            //System.out.println("count : " + topDocs.totalHits.value);

            //검색어 결과가 없을때
            if(topDocs.totalHits.value==0)
                break;

            int docLength = topDocs.scoreDocs.length;
            for (int index = 0; index < docLength; index++) {
                Document document = indexSearcher.doc(topDocs.scoreDocs[index].doc);
                lastPageDoc = topDocs.scoreDocs[index];
                docCount++;
                //System.out.println(docCount+" - id : " + document.get("id")+", "+document.get("name1"));
            }
            if(docLength != splitPage)
                break;
        }while (true);

        timeChecker.setEndTime();
        timeChecker.printCheckTime();

        System.out.println("docCount : "+docCount);
    }
}