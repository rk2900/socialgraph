package haohao.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import sun.rmi.runtime.Log;
import Utils.*;
import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

public class weibo{
	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		weibo test = new weibo();
		test.weiboData();
	}
	
	public void weiboData(){

		RepoUtil repo = new RepoUtil();	
		
		String access_token = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		String uid = "1979814003";
		
		Users um = new Users();
		um.client.setToken(access_token);
		
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		
		Comments cm = new Comments();
		cm.client.setToken(access_token);
		
		String NS = "http://weibo.com/";
		repo.setNameSpace(NS);
		repo.setPredType("property");
		
		//used for create date String
//		StringBuilder date = new StringBuilder();
		String dateStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		try {
			User user = um.showUserById(uid);
			repo.setSubjType("userID");
			
			repo.addRecord(uid, "name", user.getScreenName(), false);
			if(user.getDescription().length() != 0){
				repo.addRecord(uid, "description", user.getDescription(), false);
			}
	
			UserWapper users = fm.getFollowersById(uid);
//			UserWapper users = fm.getFollowersById(uid, count, cursor)
			for(User u : users.getUsers()){
				repo.setSubjType("userID");
				repo.setObjType("userID");
				
				repo.addRecord(uid, "follower", u.getId(), true);
				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
				if(u.getDescription().length() != 0){
					repo.addRecord(u.getId(), "description", u.getDescription(), false);
				}
			}
			
			users = fm.getFriendsByID(uid);
			for(User u : users.getUsers()){
				repo.addRecord(uid, "friend", u.getId(), true);
				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
				repo.addRecord(u.getId(), "description", u.getDescription(), false);
				if(u.getDescription().length() != 0){
					repo.addRecord(u.getId(), "description", u.getDescription(), false);
				}
			}
			
			Paging page = new Paging(6,100);
			Integer base_app = new Integer(0);
			Integer feature = new Integer(0);
			
			for(int i=1;i<=6;++i){
				page.setPage(i);
				StatusWapper status = tm.getUserTimelineByUid(uid, page, base_app, feature);
				for(Status s : status.getStatuses()){
					
					repo.setSubjType("userID");	
					repo.setObjType("weiboID");
					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
					
					repo.setSubjType("weiboID");
					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
				}
			}
			
			StatusWapper statusFriend = tm.getFriendsTimeline();
			for(Status s : statusFriend.getStatuses()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
				
				repo.setSubjType("weiboID");
				repo.addRecord(s.getId(), "weiboText", s.getText(),false);
				repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
			}
			
			StatusWapper statusBilateral = tm.getBilateralTimeline();
			for(Status s : statusBilateral.getStatuses()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
				
				repo.setSubjType("weiboID");
				repo.addRecord(s.getId(), "weiboText", s.getText(),false);
				repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
			}
			
			CommentWapper commentByMe = cm.getCommentByMe();
			for(Comment c : commentByMe.getComments()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
	
				repo.setSubjType("weiboID");
				repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
				repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
				repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
			}
			
			CommentWapper commentToMe = cm.getCommentToMe();
			for(Comment c :commentToMe.getComments()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
	
				repo.setSubjType("weiboID");
				repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
				repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
				
				 
				dateStr = sdf.format(c.getCreatedAt());  
	            System.out.println(dateStr);
				repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}

		System.out.println("----------output ends-------------");
	
		repo.saveRDFTurtle("./test.n3", RDFFormat.N3);

//		String query = "select ?y WHERE " +
//				"{<http://weibo.com/userID/1979814003> <http://weibo.com/property/follower> ?y ." +
//				"<http://weibo.com/userID/1979814003> <http://weibo.com/property/friend> ?y} ";

//		String query = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT  ?y WHERE { uid:1979814003 prop:createWeibo ?y . } ";
////		String query = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y ?date WHERE { uid:1979814003 prop:createWeibo ?y . ?y prop:weiboDate ?date } ORDER BY ?date";
//
////		String query = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> SELECT ?y ?date WHERE { uid:1979814003 prop:createComment ?y . ?y prop:commentDate ?date } ORDER BY ?date";
//
//
//		
//		TupleQueryResult itr = repo.getConnection().prepareTupleQuery(QueryLanguage.SPARQL, query).evaluate();
//		while (itr.hasNext()) {
//			BindingSet bf = itr.next();
//			
////			System.out.println(bf.getValue("p"));
//			System.out.println(bf.getValue("y"));
//		}
//		itr.close();
		
		System.out.println("Program exit.");
	}
}