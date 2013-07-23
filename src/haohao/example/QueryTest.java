package haohao.example;

import java.io.*;

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
			RepositoryException, MalformedQueryException, IOException {
		repo = new RepoUtil();
		
		/*****************************************某个用户某段时间内的微博数————用到count，ANDargs，RANarg， ran1，ran2，ORDERBY**********************************/
		String[] prefix = {"property:<http://weibo.com/property/>", "uid:<http://weibo.com/userID/>", "cid:<http://weibo.com/commentID/>", "wid:<http://weibo.com/weiboID/>"};
		String[] Args = {"(count(?y) as ?c)"};
		String[] ANDargs = {"uid:1979814003 property:createWeibo ?y", "?y property:weiboDate ?date"};
		String RANarg = "?date";
		String ran1 = "2013/07/01 15:15:56";
		String ran2 = "2013/07/22 12:53:24";
		String ORDERBY = "?date";
		SparqlUtil query = new SparqlUtil(repo);
		TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null, RANarg, ran1, ran2, null, null, ORDERBY, null);
/*****************************************某个用户某段时间内的微博数————用到count，ANDargs，RANarg， ran1，ran2，ORDERBY**********************************/

		
//		ObjectOutputStream file = new ;
//		File file = new File("QueryResult.txt");
//		file.createNewFile();
//		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		while (itr.hasNext()) {
			BindingSet bf = itr.next();
//			bw.write(bf.getValue("y"));
			System.out.println(bf.getValue("c"));
//			System.out.println(bf.getValue("y"));
		}
//		bw.close();
		itr.close();
		
		System.out.println("Program exit.");
		
	}
}
