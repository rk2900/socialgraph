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

//		weibo test = new weibo("1979814003","2.00vDGzJC0OM2YC4ffc0d56f70hPqLU");
//		test.getWeiboData();
		
//		repo = new RepoUtil();
		


		System.out.println("Program exit.");
		
	}
}
