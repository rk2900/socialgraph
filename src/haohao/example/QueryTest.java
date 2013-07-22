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

		String queryTemp = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y ?date WHERE { uid:1979814003 prop:createWeibo ?y . ?y prop:weiboDate ?date } ORDER BY ?date";
		String query1 = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y WHERE { uid:1979814003 prop:createWeibo ?y . } ";

		SparqlUtil sparql = new SparqlUtil(repo);
		String[] prefix = { "uid", "http://weibo.com/userID/", "prop",
				"http://weibo.com/property/" };
		TupleQueryResult itr = sparql.Query(prefix, "?y",
				"uid:1979814003 prop:createWeibo ?y", null, null);
		while (itr.hasNext()) {
			BindingSet bf = itr.next();
			System.out.println(bf.getValue("y"));
			// System.out.println(bf.getValue("date"));
		}
		itr.close();
		// }
		System.out.println("Program exit.");
	}
}
