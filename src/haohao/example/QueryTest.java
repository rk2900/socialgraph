package haohao.example;

import java.io.*;

import org.json.JSONException;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import SparqlQuery.JQuery;
import Utils.RepoUtil;

public class QueryTest {
	static RepoUtil repo;
	static weibo wb;

	public static void main(String[] args) throws QueryEvaluationException,
			RepositoryException, MalformedQueryException, IOException, JSONException {
		repo = new RepoUtil();
		
///*****************************************所有人的微博id和昵称，并以昵称排序————用到ANDargs，ORDRBY**********************************/	
//		String[] prefix = {"property:<http://weibo.com/property/>", "uid:<http://weibo.com/userID/>", "cid:<http://weibo.com/commentID/>", "wid:<http://weibo.com/weiboID/>"};
//		String[] Args = {"?x", "?y"};
//		String[] ANDargs = {"?x rdfs:label ?y"};
//		String ORDERBY = "?y";
//		SparqlUtil query = new SparqlUtil(repo);
//		TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null, null, null, null, null, null, ORDERBY, null);
///*****************************************所有人的微博id和昵称，并以昵称排序————用到ANDargs，ORDRBY**********************************/	

		
		
//		/*****************************************某个用户某段时间内的微博数————用到count，ANDargs，RANarg， ran1，ran2，ORDERBY**********************************/
//		String[] prefix = {"property:<http://weibo.com/property/>", "uid:<http://weibo.com/userID/>", "cid:<http://weibo.com/commentID/>", "wid:<http://weibo.com/weiboID/>"};
//		String[] Args = {"(count(?y) as ?c)"};
//		String[] ANDargs = {"uid:1979814003 property:createWeibo ?y", "?y property:weiboDate ?date"};
//		String RANarg = "?date";
//		String ran1 = "2013/07/01 15:15:56";
//		String ran2 = "2013/07/22 12:53:24";
//		String ORDERBY = "?date";
//		SparqlUtil query = new SparqlUtil(repo);
//		TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null, RANarg, ran1, ran2, null, null, ORDERBY, null);
///*****************************************某个用户某段时间内的微博数————用到count，ANDargs，RANarg， ran1，ran2，ORDERBY**********************************/
//
//		
////		ObjectOutputStream file = new ;
////		File file = new File("QueryResult.txt");
////		file.createNewFile();
////		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
//		while (itr.hasNext()) {
//			BindingSet bf = itr.next();
////			bw.write(bf.getValue("y"));
//			System.out.println(bf.getValue("c"));
////			System.out.println(bf.getValue("y"));
//		}
////		bw.close();
//		itr.close();
		
//		query.QUERY(	"{PREFIX:[" +
//								"{\"prop\":\"<http://weibo.com/property/>\"}," +
//								"{\"uid\":\"<http://weibo.com/userID/>\"}," +
//								"{\"cid\":\"<http://weibo.com/commentID/>\"}," +
//								"{\"wid\":\"<http://weibo.com/weiboID/>\"}" +
//								"]," +
//						 "SELECT:[" +
//						 		"\"x\"," +
//						 		"\"y\"" +
//						 		"]," +
//						  "WHERE:[" +
//						 		"{\"x\",\"rdfs:label\",\"y\"}" +
//						 		"]," +
//					   "ORDER BY:[" +
//					   			"\"y\"" +
//					   			"]" +
//						"}");
//		query.QUERY({"PREFIX":[{"prop":"<http://weibo.com/property/>"},{"uid":"<http://weibo.com/userID/>"},{"cid":"<http://weibo.com/commentID/>"},{"wid":"<http://weibo.com/weiboID/>"}]});
		
		
//		SparqlUtil query = new SparqlUtil(repo);
//		query.QUERY("{NAME:[{STATE1:[]}, {STATE2:[]}]}");

		
		
		
		JQuery query = new JQuery(repo);
		query.SetPrefix("test:<http://test.com/>");
		query.SetPrefix("uid:<http://weibo.com/1231322243>");
		query.SetSelect("name");
		query.SetSelect("s");
		query.SetCount("name", "c");
		query.SetCount("age", "c2");
		query.SetAndCondition("?s", "test:some", "\"yuyi\"");
		query.SetAndCondition("?sub", "test:name", "\"killa\"");
		query.SetOrCondition("?ki", "uid:property", "?o");
		query.SetOrCondition("?jihu", "uid:nani", "?o");
		query.SetOrCondition("?jif", "uid:fdsf", "?uy");
		query.SetRangeMin("name", "20");
		query.SetRangeMin("s", "10");
		query.SetRangeMax("s", "100");
		query.SetRangeMax("sonother", "99");
		query.SetRegex("name", "kill", true);
		query.SetRegex("s", "regex", false);
		query.SetOrderByAsc("s");
		query.SetOrderByAsc("name");
		query.SetOrderByDsc("sub");
		query.SetOrderByDsc("dsc");
		query.SetLimit("500");
		
		TupleQueryResult itr = query.Query();
		
		while (itr.hasNext()) {
		BindingSet bf = itr.next();
//		System.out.println(bf.getValue("c"));
//		System.out.println(bf.getValue("name"));
	}
	itr.close();
	
		
		System.out.println("Program exit.");
		
	}
}
