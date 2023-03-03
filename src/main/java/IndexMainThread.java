import Util.TimeChecker;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class IndexMainThread {
    public static final String INDEX_DIR = "."+File.separator+"data";

    public static void main(String[] args) throws Exception {
        TimeChecker timeChecker = new TimeChecker();

        File indexDirectory = new File(IndexMainThread.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        System.out.println("indexDirectory : "+indexDirectory.getAbsolutePath());
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        long count = 0L;

        timeChecker.setStartTime(System.currentTimeMillis());
        ExecutorService service = Executors.newFixedThreadPool(2);

        do{
            int randomInt = (int)(Math.random()*100);
            int randomInt2 = (int)(Math.random()*100);
            Runnable runnable = new InsertThread(indexWriter, count, randomInt, randomInt2);
            service.submit(runnable);
            count++;
            if(count%1000000L==0){
                System.out.println("insert : "+count);
            }
            if(count>8000000 && count%100L==0){
                System.out.println(count);
            }
        }while (count<10000000L);

        System.out.println("ThreadPool Shutdown waiting...");
        service.shutdown();
        System.out.println("ThreadPool Shutdown");

        indexWriter.commit();
        indexWriter.close();

        timeChecker.setEndTime(System.currentTimeMillis());

        System.out.printf("Execution time in seconds: %.2f sec" , (timeChecker.getCheckTimeDot()));
    }
}
class InsertThread implements Callable<Boolean>,  Runnable{
    private IndexWriter indexWriter;
    private int randomInt;
    private int randomInt2;
    private long count;
    public InsertThread(IndexWriter indexWriter, long count, int randomInt, int randomInt2){
        this.indexWriter = indexWriter;
        this.randomInt = randomInt;
        this.randomInt2 = randomInt2;
        this.count = count;
    }
    //Runnable
    @Override
    public void run() {
        try {
            this.addDocument();
            //indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Callable<Boolean>
    @Override
    public Boolean call() throws Exception {
        boolean success = true;
        try {
            this.addDocument();
            //indexWriter.commit();
        } catch (IOException e) {
            success = false;
        }
        return success;
    }

    private void addDocument() throws IOException {
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
    }
}

