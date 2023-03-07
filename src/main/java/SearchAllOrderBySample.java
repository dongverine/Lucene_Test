import Util.TimeChecker;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

public class SearchAllOrderBySample {
    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();
        TimeChecker timeChecker2 = new TimeChecker();
        TimeChecker timeCheckerOrderBy = new TimeChecker();
        File indexDirectory = new File(IndexMain.INDEX_BIG_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        int splitPage = 1000;

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

        SortField sf1 = new SortField("name1_sort", SortField.Type.STRING_VAL,false);
        SortField sf2 = new SortField("name2_sort", SortField.Type.STRING_VAL,false);
        Sort sort = new Sort(sf1, sf2);

        do{
            timeCheckerOrderBy.setStartTime();
            TopDocs topDocs = indexSearcher.searchAfter(lastPageDoc, query, splitPage, sort);
            //indexSearcher.search
            //System.out.println("count : " + topDocs.totalHits.value);

            //검색어 결과가 없을때
            if(topDocs.totalHits.value==0)
                break;

            int docLength = topDocs.scoreDocs.length;
            for (int index = 0; index < docLength; index++) {
                int docId = topDocs.scoreDocs[index].doc;
                Document document = indexSearcher.doc(docId);
                lastPageDoc = topDocs.scoreDocs[index];
                docCount++;
                System.out.println(docCount+" - id["+docId+"] : " + document.get("name1")+" ,"+ document.get("name2"));
            }
            timeCheckerOrderBy.setEndTime();
            timeCheckerOrderBy.printCheckTime();
            if(docLength != splitPage)
                break;
        }while (true);

        timeChecker.setEndTime();
        timeChecker.printCheckTime();

        System.out.println("docCount : "+docCount);
    }
}