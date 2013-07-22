package haohao.example;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import Utils.RepoUtil;
import Utils.SparqlUtil;

public class QueryTest {
	static RepoUtil repo;
	static weibo wb;

	public static void main(String[] args) throws QueryEvaluationException,
			RepositoryException, MalformedQueryException {
		repo = new RepoUtil();
		
/*****************************************所有人的微博id和昵称，并以昵称排序————用到ANDargs，ORDRBY**********************************/	
		String[] prefix = {"property:<http://weibo.com/property/>", "uid:<http://weibo.com/userID/>", "cid:<http://weibo.com/commentID/>", "wid:<http://weibo.com/weiboID/>"};
		String[] Args = {"?x", "?y"};
		String[] ANDargs = {"?x rdfs:label ?y"};
		String ORDERBY = "?y";
		SparqlUtil query = new SparqlUtil(repo);
		TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null, null, null, null, null, null, ORDERBY, null);
/*****************************************所有人的微博id和昵称，并以昵称排序————用到ANDargs，ORDRBY**********************************/	

		while (itr.hasNext()) {
			BindingSet bf = itr.next();
			System.out.println(bf.getValue("y"));
//			System.out.println(bf.getValue("date"));
		}
		itr.close();
		
		System.out.println("Program exit.");
		
	}
}
