import Util.TimeChecker;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class OrderByMain {
    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();
        TimeChecker timeChecker2 = new TimeChecker();
        File indexDirectory = new File(IndexMain.INDEX_BIG_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        int splitPage = 10000;

        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
        Query query  = new MatchAllDocsQuery();

        ScoreDoc lastPageDoc = null;
        long docCount = 0;
        timeChecker.setStartTime();

        timeChecker2.setStartTime();
        TotalHitCountCollector totalHitCollector = new TotalHitCountCollector();
        indexSearcher.search(query, totalHitCollector);
        timeChecker2.setEndTime();
        System.out.println("getTotalHits : "+totalHitCollector.getTotalHits());
        timeChecker2.printCheckTime();

        do{
            TopDocs topDocs = indexSearcher.searchAfter(lastPageDoc, query, splitPage);

            //검색어 결과가 없을때
            if(topDocs.totalHits.value==0)
                break;

            int docLength = topDocs.scoreDocs.length;
            for (int index = 0; index < docLength; index++) {
                int docId = topDocs.scoreDocs[index].doc;
                Document document = indexSearcher.doc(docId);
                lastPageDoc = topDocs.scoreDocs[index];
                docCount++;
                if(docCount%100000==0)
                    System.out.println(docCount+" - id["+docId+"] : " + document.get("id")+", "+document.get("name1"));
            }
            if(docLength != splitPage)
                break;
        }while (true);

        timeChecker.setEndTime();
        timeChecker.printCheckTime();

        System.out.println("docCount : "+docCount);
    }
}