package edu.asu.conceptpower.servlet.lucene.impl;

import java.sql.Timestamp;

import javax.annotation.PostConstruct;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.TSerializable;

import edu.asu.conceptpower.servlet.core.LuceneBean;
import edu.asu.conceptpower.servlet.lucene.ILuceneDAO;

public class LuceneDAO implements ILuceneDAO {

    ObjectContainer luceneClient;

    private String dbPath;

    @PostConstruct
    public void init() {

        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().objectClass(Timestamp.class).translate(new TSerializable());
        luceneClient = Db4oEmbedded.openFile(config, getDbPath());
    }

    public void storeValues(long numberOfIndexedWords) {

        long now = System.currentTimeMillis();
        LuceneBean bean = new LuceneBean(new Timestamp(now), numberOfIndexedWords);
        luceneClient.store(bean);
        luceneClient.commit();
    }

    public LuceneBean getTotalNumberOfWordsIndexed() {
        LuceneBean bean = new LuceneBean(null, 0);
        ObjectSet result = luceneClient.queryByExample(bean);
        return getTotalNumberFromResult(result);

    }

    private LuceneBean getTotalNumberFromResult(ObjectSet result) {
        int totalIndex = 0;
        Timestamp latestTimeStamp = null;
        for (Object item : result) {
            LuceneBean bean = (LuceneBean) item;
            totalIndex += bean.getIndexedWordsCount();

            Timestamp t = bean.getLastRunTimeStamp();

            if (latestTimeStamp == null) {
                latestTimeStamp = t;
            }

            else if (t.after(latestTimeStamp)) {
                latestTimeStamp = t;
            }
        }
        return new LuceneBean(latestTimeStamp, totalIndex);
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }

}
