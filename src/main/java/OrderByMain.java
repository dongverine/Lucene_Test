import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
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
        System.out.println("start");
        SortField sf = new SortField("name1", SortField.Type.STRING,true);
        System.out.println("sort start");
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        //booleanQueryBuilder.add(nameQuery, BooleanClause.Occur.MUST);
        //booleanQueryBuilder.add(ageQuery, BooleanClause.Occur.MUST);
        TopDocs topDocs = indexSearcher.search(booleanQueryBuilder.build(), Integer.MAX_VALUE, new Sort(sf));
        //indexSearcher.search(booleanQueryBuilder.build(), TopScoreDocCollector)
        System.out.println("sort end");
        System.out.println("count : " + topDocs.totalHits.value);
        long searchCount = topDocs.totalHits.value;
        for (int index = 0; index < searchCount; index++) {
            int docId = topDocs.scoreDocs[index].doc;
            Document document = indexSearcher.doc(docId);
            System.out.println(" - id["+docId+"] : " + document.get("id"));
        }
    }
}
/*
class CustomCollector extends TopDocsCollector<ScoreDocWithTime> {

    ScoreDocWithTime pqTop;

    // prevents instantiation
    public CustomCollector(int numHits) {
        super(new HitQueueWithTime(numHits, true));
        // HitQueue implements getSentinelObject to return a ScoreDoc, so we know
        // that at this point top() is already initialized.
        pqTop = pq.top();
    }

    @Override
    public LeafCollector getLeafCollector(LeafReaderContext context)
            throws IOException {
        final int docBase = context.docBase;
        final NumericDocValues modifiedDate =
                DocValues.getNumeric(context.reader(), "modifiedDate");

        return new LeafCollector() {
            Scorable scorer;


//            @Override
//            public void setScorer(Scorer scorer) throws IOException {
//                this.scorer = scorer;
//            }

            @Override
            public void setScorer(Scorable scorable) throws IOException {
                this.scorer = scorer;
            }

            @Override
            public void collect(int doc) throws IOException {
                float score = scorer.score();

                // This collector cannot handle these scores:
                assert score != Float.NEGATIVE_INFINITY;
                assert !Float.isNaN(score);

                totalHits++;
                if (score <= pqTop.score) {
                    // Since docs are returned in-order (i.e., increasing doc Id), a document
                    // with equal score to pqTop.score cannot compete since HitQueue favors
                    // documents with lower doc Ids. Therefore reject those docs too.
                    return;
                }
                pqTop.doc = doc + docBase;
                pqTop.score = score;
                pqTop.timestamp = modifiedDate.longValue();//modifiedDate.get(doc);
                pqTop = pq.updateTop();
            }

            @Override
            public DocIdSetIterator competitiveIterator() throws IOException {
                return null;
            }

        };
    }

}

class ScoreDocWithTime extends ScoreDoc {
    public long timestamp;

    public ScoreDocWithTime(long timestamp, int doc, float score) {
        super(doc, score);
        this.timestamp = timestamp;
    }

    public ScoreDocWithTime(long timestamp, int doc, float score, int shardIndex) {
        super(doc, score, shardIndex);
        this.timestamp = timestamp;
    }
}

class HitQueueWithTime extends PriorityQueue<ScoreDocWithTime> {

    public HitQueueWithTime(int numHits, boolean b) {
        super(numHits, b);
    }

    @Override
    protected ScoreDocWithTime getSentinelObject() {
        return new ScoreDocWithTime(0, Integer.MAX_VALUE, Float.NEGATIVE_INFINITY);
    }

    @Override
    protected boolean lessThan(ScoreDocWithTime hitA, ScoreDocWithTime hitB) {
        if (hitA.score == hitB.score)
            return (hitA.timestamp == hitB.timestamp) ?
                    hitA.doc > hitB.doc :
                    hitA.timestamp < hitB.timestamp;
        else
            return hitA.score < hitB.score;

    }
}
*/