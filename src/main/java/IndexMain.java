import Util.TimeChecker;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;

public class IndexMain {
    public static final String INDEX_DIR = "."+File.separator+"data";
    public static final String INDEX_BIG_DIR = "."+File.separator+"data_big_Stored";
    public static final String INDEX_SMALL_DIR = "."+File.separator+"data_small_Stored";

    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();
        TimeChecker finalTimeChecker = new TimeChecker();

        File indexDirectory = new File(IndexMain.INDEX_BIG_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        System.out.println("indexDirectory : "+indexDirectory.getAbsolutePath());
        Directory directory = FSDirectory.open(indexDirectory.toPath());
//        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new Analyzer() {
//            @Override
//            protected TokenStreamComponents createComponents(String s) {
//                return null;
//            }
//        }));

        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));

        long count = 0L;

        timeChecker.setStartTime(System.currentTimeMillis());

        Runtime rt = Runtime.getRuntime();
        rt.addShutdownHook(
                new Thread() {
                    public void run() {
                        System.out.println("\nShutdown Main...");
                        finalTimeChecker.setEndTime();
                        finalTimeChecker.printCheckTime();
                    }
                } );

        do{
            int randomInt = (int)(Math.random()*100);
            int randomInt2 = (int)(Math.random()*100);
            Term term = new Term("ID", "index_"+count);
            Document document = new Document();

            //order by 정렬용 필드
            document.add(new SortedDocValuesField("name1_sort", new BytesRef("Name1_"+randomInt2)));
            document.add(new SortedDocValuesField("name2_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name3_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name4_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name5_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name6_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name7_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name8_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name9_sort", new BytesRef("Name1_"+randomInt)));
            document.add(new SortedDocValuesField("name10_sort", new BytesRef("Name1_"+randomInt)));

            //값필드
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
                System.out.println("insert : "+count);
            }
        }while (count<100000000L);

        indexWriter.commit();
        indexWriter.close();

        timeChecker.setEndTime(System.currentTimeMillis());
        timeChecker.printCheckTime();
    }
}
