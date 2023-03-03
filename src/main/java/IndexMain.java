import Util.TimeChecker;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;

public class IndexMain {
    public static final String INDEX_DIR = "."+File.separator+"data_small";

    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();

        File indexDirectory = new File(IndexMain.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        System.out.println("indexDirectory : "+indexDirectory.getAbsolutePath());
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                return null;
            }
        }));
        long count = 0L;

        timeChecker.setStartTime(System.currentTimeMillis());

        do{
            int randomInt = (int)(Math.random()*100);
            int randomInt2 = (int)(Math.random()*100);
            Term term = new Term("ID", "index_"+count);
            Document document = new Document();
            document.add(new StringField("id", "ID_"+randomInt, Field.Store.YES));
            document.add(new StringField("name1", "Name1_"+randomInt2, Field.Store.YES));
            document.add(new StringField("name2", "Name2_"+randomInt, Field.Store.YES));
            document.add(new StringField("name3", "Name3_"+randomInt, Field.Store.YES));
            document.add(new StringField("name4", "Name4_"+randomInt, Field.Store.YES));
            document.add(new StringField("name5", "Name5_"+randomInt, Field.Store.YES));
            document.add(new StringField("name6", "Name6_"+randomInt, Field.Store.YES));
            document.add(new StringField("name7", "Name7_"+randomInt, Field.Store.YES));
            document.add(new StringField("name8", "Name8_"+randomInt, Field.Store.YES));
            document.add(new StringField("name9", "Name9_"+randomInt, Field.Store.YES));
            document.add(new StringField("name10", "Name10_"+randomInt, Field.Store.YES));
            document.add(new LongPoint("age1", randomInt));
            document.add(new LongPoint("age2", randomInt));

            indexWriter.updateDocument(term, document);
            count++;
            if(count%1000000L==0){
                indexWriter.commit();
                System.out.println("insert : "+count);
            }
        }while (count<1000L);

        indexWriter.commit();
        indexWriter.close();

        timeChecker.setEndTime(System.currentTimeMillis());

        System.out.printf("Execution time in seconds: %.2f sec" , (timeChecker.getCheckTimeDot()));

//
//
//        IndexSearcher indexSearcher = new IndexSearcher(DirectoryReader.open(directory));
//
//        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
//        //booleanQueryBuilder.add(ageQuery, BooleanClause.Occur.MUST);
//        ScoreDoc lastPageDoc = null;
//        long docCount = 0;
//        BooleanQuery query = booleanQueryBuilder.build();
//        timeChecker.setStartTime();
//        TotalHitCountCollector totalHitCollector = new TotalHitCountCollector();
//        indexSearcher.search(query, totalHitCollector);
//        System.out.println();
//        System.out.println("getTotalHits : "+totalHitCollector.getTotalHits());
    }
}

final class CustomStringField extends Field {
    public static final FieldType CUSTOM_TYPE_NOT_STORED = new FieldType();
    public static final FieldType CUSTOM_TYPE_STORED = new FieldType();

    public CustomStringField(String name, String value, Store stored) {
        super(name, value, stored == Store.YES ? CUSTOM_TYPE_STORED : CUSTOM_TYPE_NOT_STORED);
    }

    public CustomStringField(String name, BytesRef value, Store stored) {
        super(name, value, stored == Store.YES ? CUSTOM_TYPE_STORED : CUSTOM_TYPE_NOT_STORED);
    }

    static {
        CUSTOM_TYPE_NOT_STORED.setOmitNorms(false);
        CUSTOM_TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
        CUSTOM_TYPE_NOT_STORED.setTokenized(false);
        CUSTOM_TYPE_NOT_STORED.freeze();
        CUSTOM_TYPE_STORED.setOmitNorms(false);
        CUSTOM_TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
        CUSTOM_TYPE_STORED.setStored(false);
        CUSTOM_TYPE_STORED.setTokenized(false);
        CUSTOM_TYPE_STORED.freeze();

//        CUSTOM_TYPE_NOT_STORED.setOmitNorms(true);
//        CUSTOM_TYPE_NOT_STORED.setIndexOptions(IndexOptions.DOCS);
//        CUSTOM_TYPE_NOT_STORED.setTokenized(false);
//        CUSTOM_TYPE_NOT_STORED.freeze();
//        CUSTOM_TYPE_STORED.setOmitNorms(true);
//        CUSTOM_TYPE_STORED.setIndexOptions(IndexOptions.DOCS);
//        CUSTOM_TYPE_STORED.setStored(true);
//        CUSTOM_TYPE_STORED.setTokenized(false);
//        CUSTOM_TYPE_STORED.freeze();
    }
}