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

public class IndexMainGroup {
    public static final String INDEX_BIG_DIR = "."+File.separator+"data_big";
    public static final String INDEX_BIG_DIR_GROUP = "."+File.separator+"data_big_Stored";

    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();

        File indexDirectory = new File(IndexMainGroup.INDEX_BIG_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        File indexGroupDirectory = new File(IndexMainGroup.INDEX_BIG_DIR_GROUP); // 인덱싱 파일이 저장될 디렉토리 경로
        System.out.println("indexDirectory : "+indexDirectory.getAbsolutePath());
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        Directory groupDirectory = FSDirectory.open(indexGroupDirectory.toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                return null;
            }
        }));

        long count = 0L;

        IndexWriter indexGroupWriter = new IndexWriter(groupDirectory, new IndexWriterConfig(new Analyzer() {
            @Override
            protected TokenStreamComponents createComponents(String s) {
                return null;
            }
        }));

        timeChecker.setStartTime(System.currentTimeMillis());

        do{
            int randomInt = (int)(Math.random()*100);
            int randomInt2 = (int)(Math.random()*100);
            Term term = new Term("ID", "index_"+count);
            Document document = new Document();
            document.add(new StringField("ID", "index_"+count, Field.Store.YES));
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
        }while (count<10000000L);

        directory.close();

        indexWriter.commit();
        indexWriter.close();

        timeChecker.setEndTime(System.currentTimeMillis());

        System.out.printf("Execution time in seconds: %.2f sec" , (timeChecker.getCheckTimeDot()));
    }
}
