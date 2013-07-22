package haohao.example;

import haohao.FileOutputManager;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import Utils.RepoUtil;
import Utils.SparqlUtil;

public class QueryTest {
	static RepoUtil repo;
	static weibo wb;

	public static void main(String[] args) throws QueryEvaluationException,
			RepositoryException, MalformedQueryException {

		repo = new RepoUtil();
//		String queryTemp = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y ?date WHERE { uid:1979814003 prop:createWeibo ?y . ?y prop:weiboDate ?date } ORDER BY ?date";
//		String query1 = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y WHERE { uid:1979814003 prop:createWeibo ?y . } ";
//
//		SparqlUtil sparql = new SparqlUtil(repo);
//		String[] prefix = { "uid", "http://weibo.com/userID/", 
//							"prop", "http://weibo.com/property/" };
//		TupleQueryResult itr = sparql.Query(prefix, "?y", "uid:1979814003 prop:createWeibo ?y", null, null);
//		while (itr.hasNext()) {
//			BindingSet bf = itr.next();
//			System.out.println(bf.getValue("y"));
//			// System.out.println(bf.getValue("date"));
//		}
//		itr.close();
//		// }
//		System.out.println("Program exit.");

		/***************************************** 所有人的微博id和昵称 **********************************/
		// String[] prefix = {"property:<http://weibo.com/property/>"};
		// String[] Args = {"x", "y"};
		// String[] ANDargs = {"?x rdfs:label ?y"};
		// //String[] ORargs = null;
		// //String[] OPTIONAL = null;
		// //String RANarg = null;
		// //String ran1 = null;
		// //String ran2 = null;
		// //String EXISTarg = null;
		// //String EXIST = null;
		// String ORDERBY = "?y";
		// //String LIMIT = null;
		//
		// SparqlUtil query = new SparqlUtil(repo);
		// //TupleQueryResult itr = query.Query(prefix, Args, ANDargs, ORargs,
		// OPTIONAL, RANarg, ran1, ran2, EXISTarg, EXIST, ORDERBY, LIMIT);
		//
		// TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null,
		// null, null, null, null, null, ORDERBY, null);
		/***************************************** 所有人的微博id和昵称 **********************************/

		/***************************************** 某个用户所有的微博和创建的时间 **********************************/
		String[] prefix = { "property:<http://weibo.com/property/>",
				"uid:<http://weibo.com/userID/>" };
		String[] Args = { "date", "y" };
		String[] ANDargs = { "uid:1979814003 property:createWeibo ?y",
				"?y property:weiboDate ?date" };
		// String[] ORargs = null;
		// String[] OPTIONAL = null;
		// String RANarg = "?date";
		// String ran1 = "2013-7-1 15:15:56";
		// String ran2 = "2013-7-8 12:53:24";
		String EXISTarg = "?data";
		String EXIST = "2013-7-8";// 正则表达式
		String ORDERBY = "?date";
		// String LIMIT = "3";

		SparqlUtil query = new SparqlUtil(repo);
		// TupleQueryResult itr = query.Query(prefix, Args, ANDargs, ORargs,
		// OPTIONAL, RANarg, ran1, ran2, EXISTarg, EXIST, ORDERBY, LIMIT);

		TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null,
				null/* RANarg */, null/* ran1 */, null/* ran2 */, EXISTarg,
				EXIST, ORDERBY, null/* LIMIT */);
		/***************************************** 某个用户所有的微博和创建的时间 **********************************/

		/***************************************** 某人发出的所有评论和评论的时间 **********************************/
		// String[] prefix = {"property:<http://weibo.com/property/>",
		// "uid:<http://weibo.com/userID/>"};
		// String[] Args = {"date", "y"};
		// String[] ANDargs = {"uid:1979814003 property:createComment ?y",
		// "?y property:commentDate ?date"};
		// //String[] ORargs = null;
		// //String[] OPTIONAL = null;
		// //String RANarg = null;
		// //String ran1 = null;
		// //String ran2 = null;
		// //String EXISTarg = null;
		// //String EXIST = null;
		// String ORDERBY = "?date";
		// //String LIMIT = null;
		//
		// SparqlUtil query = new SparqlUtil(repo);
		// //TupleQueryResult itr = query.Query(prefix, Args, ANDargs, ORargs,
		// OPTIONAL, RANarg, ran1, ran2, EXISTarg, EXIST, ORDERBY, LIMIT);
		//
		// TupleQueryResult itr = query.Query(prefix, Args, ANDargs, null, null,
		// null, null, null, null, null, ORDERBY, null);
		/***************************************** 某人发出的所有评论和评论的时间 **********************************/

		/***************************************** 与某人互粉的用户 **********************************/
		// String[] prefix = {"property:<http://weibo.com/property/>",
		// "uid:<http://weibo.com/userID/>"};
		// String[] Args = {"y"};
		// //String[] ANDargs = {"uid:1979814003 property:follower ?y",
		// "uid:1979814003 property:friend ?y"};
		// String[] ORargs = {"uid:1979814003 property:follower ?y",
		// "uid:1979814003 property:friend ?y"};
		// //String[] OPTIONAL = null;
		// //String RANarg = null;
		// //String ran1 = null;
		// //String ran2 = null;
		// //String EXISTarg = null;
		// //String EXIST = null;
		// //String ORDERBY = "?date";
		// //String LIMIT = null;
		//
		// SparqlUtil query = new SparqlUtil(repo);
		// //TupleQueryResult itr = query.Query(prefix, Args, ANDargs, ORargs,
		// OPTIONAL, RANarg, ran1, ran2, EXISTarg, EXIST, ORDERBY, LIMIT);
		//
		// TupleQueryResult itr = query.Query(prefix, Args, /*ANDargs*/null,
		// ORargs/*null*/, null, null, null, null, null, null, null, null);
		/***************************************** 与某人互粉的用户 ***********************************/

		while (itr.hasNext()) {
			BindingSet bf = itr.next();
			//
			System.out.println(bf.getValue("y"));
			System.out.println(bf.getValue("date"));
		}
		itr.close();

		System.out.println("Program exit.");
		
	}
}
