package haohao.example;

import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

import Utils.*;
import haohao.FileOutputManager;
import weibo4j.Comments;
import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

public class weibo{
	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		RepoUtil repo = new RepoUtil();
//		repo.addRecords("./result.n3", "", RDFFormat.N3);
		String NS = "http://weibo.com/";
		///////////////////////////////////////////////
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
		
		FileOutputManager manager = new FileOutputManager("d:\\test\\newweibo.txt","UTF-8");
		
		StringBuilder spo = new StringBuilder();
		
		repo.setNameSpace(NS);
		repo.setPredType("property");
		
		try {
			User user = um.showUserById(uid);
			repo.setSubjType("userID");
			
			repo.addRecord(uid, "name", user.getScreenName(), false);
			repo.addRecord(uid, "description", user.getDescription(), false);
			
			UserWapper users = fm.getFollowersById(uid);
			for(User u : users.getUsers()){
				repo.setSubjType("userID");
				repo.setObjType("userID");
				
				repo.addRecord(uid, "follower", u.getId(), true);
				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
				repo.addRecord(u.getId(), "description", u.getDescription(), false);
			}
			
			users = fm.getFriendsByID(uid);
			for(User u : users.getUsers()){
				repo.addRecord(uid, "friend", u.getId(), true);
				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
				repo.addRecord(u.getId(), "description", u.getDescription(), false);
			}
			
			StatusWapper status = tm.getUserTimeline();
			for(Status s : status.getStatuses()){
				repo.setObjType("weiboID");
				repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
				
				repo.setSubjType("weiboID");
				repo.addRecord(s.getId(), "weiboText", s.getText(),false);
				repo.addRecord(s.getId(), "weiboDate", s.getCreatedAt().toLocaleString(), false);
//				System.out.println(s.getCreatedAt().toLocaleString());
			}
			
			StatusWapper statusFriend = tm.getFriendsTimeline();
			for(Status s : statusFriend.getStatuses()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
				
				repo.addRecord(s.getId(), "weiboText", s.getText(),false);
				repo.addRecord(s.getId(), "weiboDate", s.getCreatedAt().toLocaleString(), false);
			}
			
			StatusWapper statusBilateral = tm.getBilateralTimeline();
			for(Status s : statusBilateral.getStatuses()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
				
				repo.setSubjType("weiboID");
				repo.addRecord(s.getId(), "weiboText", s.getText(),false);
				repo.addRecord(s.getId(), "weiboDate", s.getCreatedAt().toLocaleString(), false);
			}
			
			CommentWapper commentByMe = cm.getCommentByMe();
			for(Comment c : commentByMe.getComments()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
	
				repo.setSubjType("weiboID");
				repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
				repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
				repo.addRecord(c.getIdstr(), "commentData", c.getCreatedAt().toLocaleString(), false);
			}
			
			CommentWapper commentToMe = cm.getCommentToMe();
			for(Comment c :commentToMe.getComments()){
				repo.setSubjType("userID");
				repo.setObjType("weiboID");
				repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
	
				repo.setSubjType("weiboID");
				repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
				repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
				repo.addRecord(c.getIdstr(), "commentData", c.getCreatedAt().toLocaleString(), false);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		manager.close();
			System.out.println("----------output ends-------------");
			repo.saveRDFTurtle("./result.n3", RDFFormat.N3);
			//////////////////////////////////////////////////////////
			
//			String queryTemp = "PREFIX uid:<http://weibo.com/userID/> PREFIX prop:<http://weibo.com/property/> select ?y WHERE {uid:1979814003 prop:friend ?y . uid:1979814003 prop:follower ?y} ";
//		
//			SparqlUtil sparql = new SparqlUtil(repo);
//			String[] prefix = {"uid", "http://weibo.com/userID/",
//								"prop", "http://weibo.com/property/"};
//			TupleQueryResult itr = sparql.Query(prefix, "?y" ,"uid:1979814003 prop:friend ?y . uid:1979814003 prop:follower ?y", null, null);
//			while (itr.hasNext()) {
//				BindingSet bf = itr.next();
//				System.out.println(bf.getValue("y"));
//			}
//			itr.close();
//		}
		System.out.println("Program exit.");
	}
}