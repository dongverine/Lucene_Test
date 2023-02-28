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

public class IndexMain {
    public static final String INDEX_DIR = "."+File.separator+"data";
    public static void main(String[] args) throws Exception {
        File indexDirectory = new File(IndexMain.INDEX_DIR); // 인덱싱 파일이 저장될 디렉토리 경로
        System.out.println("indexDirectory : "+indexDirectory.getAbsolutePath());
        Directory directory = FSDirectory.open(indexDirectory.toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()));
        long count = 0L;

        do{
            int randomInt = (int)(Math.random()*100);
            Term term = new Term("ID", "index_"+count);
            Document document = new Document();
            document.add(new StringField("id", "ID_"+randomInt, Field.Store.YES));
            document.add(new StringField("name1", "Name1_"+randomInt, Field.Store.YES));
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
            if(count%100==0){
                System.out.println("insert : "+count);
            }
        }while (count<10000000L);

        indexWriter.commit();
    }
}