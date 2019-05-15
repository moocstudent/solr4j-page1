package how2java;
import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.beans.DocumentObjectBinder;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.util.NamedList;


public class SolrUtil {
    public static SolrClient client;
    private static String url;
    private static String pre_keyWord = "name:";
    static {
        url = "http://localhost:8983/solr/ukyo";
        client = new HttpSolrClient.Builder(url).build();
    }


    public static <T> boolean batchSaveOrUpdate(List<T> entities) throws SolrServerException, IOException {

        DocumentObjectBinder binder = new DocumentObjectBinder();
		int total = entities.size();
		int count=0;
        for (T t : entities) {
            SolrInputDocument doc = binder.toSolrInputDocument(t);
            client.add(doc);
            System.out.printf("添加数据到索引中，总共要添加 %d 条记录，当前添加第%d条 %n",total,++count);
		}
        client.commit();
        return true;
    }


    public static QueryResponse query(String keywords,int startOfPage, int numberOfPage) throws SolrServerException, IOException {

        SolrQuery query = new SolrQuery();
        query.setStart(startOfPage);
        query.setRows(numberOfPage);
        
        query.setQuery(pre_keyWord+keywords);
        QueryResponse rsp = client.query(query);
        return rsp;
    }

}