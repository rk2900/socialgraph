package haohao.example;

import haohao.FileOutputManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;

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
	private String access_token;
	private String uid;
	static private RepoUtil repo;
	private Users um;
	private Friendships fm;
	private Timeline tm;
	private Comments cm;
	private DateFormat sdf;
	private FileOutputManager manager;
	
	public weibo(String id, String session){
		uid = id;
		access_token = session;	
		
		um = new Users();
		um.client.setToken(access_token);
		
		fm = new Friendships();
		fm.client.setToken(access_token);
		
		tm = new Timeline();
		tm.client.setToken(access_token);
		
		cm = new Comments();
		cm.client.setToken(access_token);
		
		/*********notice that we need to fix the NS and type later********/
		String NS = "http://weibo.com/";
		repo.setNameSpace(NS);
		repo.setPredType("property");
		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}
	
	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
		
		repo = new RepoUtil();
		
		String haohaoID = "1979814003";
		String haohaoKey = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		
		weibo haohaoTest = new weibo(haohaoID,haohaoKey);
		haohaoTest.getWeiboData();
		
		String kankanID = "2128564862";
		String kankanKey = "2.001xOD1C0OM2YC6d102a5533yk1qLC";
		
		weibo kankanTest = new weibo(kankanID,kankanKey);
		kankanTest.getWeiboData();
		
		String dingdingID = "2411548261";
		String dingdingKey = "2.00hrbMdC0OM2YC9d8536d5a8FkHp3E";
		
		weibo dingdingTest = new weibo(dingdingID,dingdingKey);
		dingdingTest.getWeiboData();
		
		repo.saveRDFTurtle("./rkTest.n3", RDFFormat.N3, "");
		
//		String query = "PREFIX test:<http://weibo.com/userID/>" +
//				"PREFIX prop:<http://weibo.com/property/>" +
//				"SELECT ?comment ?weibo " +
//				"WHERE{" +
//				"test:1979814003 prop:createWeibo ?y ." +
//				"test:1979814003 prop:createComment ?c ." +
//				"?c prop:commentTo ?y ." +
//				"?c rdfs:comment ?comment ." +
//				"?y rdfs:Literal ?weibo" +
//				"}";
		
//		String query = "PREFIX test:<http://weibo.com/userID/>" +
//				" PREFIX prop:<http://weibo.com/property/> " +
//				"SELECT ?reply ?comment ?weibo " +
//				"WHERE{" +
//				"test:1979814003 prop:createWeibo ?x ." +
//				"test:2128564862 prop:createComment ?c ." +
//				"?c prop:commentTo ?x ." +
//				"?y prop:commentReplyTo ?c ." +
//				"?x rdfs:Literal ?weibo ." +
//				"?c rdfs:comment ?comment ." +
//				"?y rdfs:comment ?reply ." +
//				"}";
	}
	
	/*
	 * This function is used to get the current user's information
	 */
	public void getUserData(){
		User user;
		try {
			user = um.showUserById(uid);
			repo.setSubjType("userID");
			
			repo.addRecord(uid, "name", user.getScreenName(), false);
			if(user.getDescription().length() > 0){
				repo.addRecord(uid, "description", user.getDescription(), false);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This function is used to get the current user's followers. namely, FANS
	 * Here I only take 200 data.
	 */
	public void getAllFollowerData(){
		UserWapper users;
		int nextCursor = 0;
		try {
			do{
				users = fm.getFollowersById(uid, new Integer(200), new Integer(nextCursor));
				nextCursor = (int) users.getNextCursor();
				for(User u : users.getUsers()){
					repo.setSubjType("userID");
					repo.setObjType("userID");
					
					repo.addRecord(uid, "follower", u.getId(), true);
					repo.addRecord(u.getId(), "name", u.getScreenName(), false);
					
					if(u.getDescription().length() > 0){
						repo.addRecord(u.getId(), "description", u.getDescription(), false);
					}
				}
			}while(nextCursor != 0);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getAllFriendData(){
		UserWapper users;
		int nextCursor = 0;
		try {
			do{
				users = fm.getFriendsById(uid, new Integer(200), new Integer(nextCursor));
				nextCursor = (int) users.getNextCursor();
				for(User u : users.getUsers()){
					repo.setSubjType("userID");
					repo.setObjType("userID");
					repo.addRecord(uid, "friend", u.getId(), true);			
					repo.addRecord(u.getId(), "name", u.getScreenName(), false);
					
					if(u.getDescription().length() > 0){
						repo.addRecord(u.getId(), "description", u.getDescription(), false);
					}
				}
				
			}while(nextCursor != 0);

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*------------return RecentfriendNum wich <= 200----------------*/
	public void getRecentFriendData(int friendNum){
		UserWapper users;
		try {
			if(friendNum > 200) friendNum = 200;
			users = fm.getFriendsById(uid, new Integer(friendNum), new Integer(0));
			for(User u : users.getUsers()){
				repo.setSubjType("userID");
				repo.setObjType("userID");
				repo.addRecord(uid, "friend", u.getId(), true);			
				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
				
				if(u.getDescription().length() > 0){
					repo.addRecord(u.getId(), "description", u.getDescription(), false);
				}
			}
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * to the first three below 
	 * pageNum >= 1 && if countNum > 100, it is considered to be 100 as default
	 * function addStatus is used
	 */
	public void addS(Status s){
		repo.setSubjType("userID");	
		repo.setObjType("weiboID");
		repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
		
		repo.setSubjType("weiboID");
		repo.addRecord(s.getId(), "weiboText", s.getText(),false);
		repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
	}
	
	public void addStatus(Status s){
		addS(s);
		if(s.getRetweetedStatus() != null){
			repo.setSubjType("weiboID");
			repo.setObjType("weiboID");
			repo.addRecord(s.getId(), "reweetFrom", s.getRetweetedStatus().getId(), true);
			
			//if the original weibo has been deleted.
			if(s.getRetweetedStatus().getUser() != null){
				addS(s.getRetweetedStatus());
			}			
		}	
	}
	
	public void getUserTimeline(int pageNum,int countNum){
		Paging page = new Paging(1,countNum);
		Integer base_app = new Integer(0);
		Integer feature = new Integer(0);
		StatusWapper status;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);			
			try {
				status = tm.getUserTimelineByUid(uid, page, base_app, feature);
				for(Status s : status.getStatuses()){
					addStatus(s);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getFriendTimeline(int pageNum, int countNum){
		Paging page = new Paging(1,countNum);
		Integer base_app = new Integer(0);
		Integer feature = new Integer(0);
		StatusWapper statusFriend;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				statusFriend = tm.getFriendsTimeline(base_app, feature, page);
				for(Status s : statusFriend.getStatuses()){
					addStatus(s);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getBilateralTimeline(int pageNum, int countNum){
		Paging page = new Paging(1,countNum);
		Integer base_app = new Integer(0);
		Integer feature = new Integer(0);
		StatusWapper statusBilateral;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				statusBilateral = tm.getBilateralTimeline(page, base_app, feature);
				for(Status s : statusBilateral.getStatuses()){
					addStatus(s);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * to the first two below
	 * pageNum >=1
	 * function addComment is used
	 */
	public void addC(Comment c){
		repo.setSubjType("userID");
		repo.setObjType("commentID");
		repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);

		repo.setSubjType("commentID");
		repo.setObjType("weiboID");
		
		repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
		repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
		repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
		manager.write(c.getText() + "\t" + c.getStatus().getText() + "\n");
	}
	
	public void addComment(Comment c){
		addC(c);
		if(c.getReplycomment() != null){
			repo.setSubjType("commentID");
			repo.setObjType("commentID");
			repo.addRecord(c.getIdstr(), "commentReplyTo", c.getReplycomment().getIdstr(), true);
		}
	}
	
	public void getCommentByme(int pageNum, int countNum){
		Paging page = new Paging(pageNum,countNum);
		CommentWapper commentByMe;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				commentByMe = cm.getCommentByMe(page, new Integer(0));
				for(Comment c : commentByMe.getComments()){
					addComment(c);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getCommentTome(int pageNum, int countNum){
		Paging page = new Paging(1,countNum);
		CommentWapper commentToMe;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				commentToMe = cm.getCommentToMe(page, new Integer(0), new Integer(0));
				for(Comment c :commentToMe.getComments()){
					addComment(c);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public void getWeiboData(){
		
		getUserData();
		getAllFollowerData();
		getAllFriendData();
		getUserTimeline(1,100);
		getFriendTimeline(1,100);
		getBilateralTimeline(1,100);
		getCommentByme(2,100);
		getCommentTome(2,100);

		System.out.println("----------output ends-------------");
		System.out.println("----------Program exit------------");
	}
}
//package haohao.example;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.rio.RDFFormat;
//
//import Utils.*;
//import weibo4j.Comments;
//import weibo4j.Friendships;
//import weibo4j.Timeline;
//import weibo4j.Users;
//import weibo4j.model.Comment;
//import weibo4j.model.CommentWapper;
//import weibo4j.model.Paging;
//import weibo4j.model.Status;
//import weibo4j.model.StatusWapper;
//import weibo4j.model.User;
//import weibo4j.model.UserWapper;
//import weibo4j.model.WeiboException;
//
//public class weibo{
//	private String access_token;
//	private String uid;
//	private RepoUtil repo;
//	private Users um;
//	private Friendships fm;
//	private Timeline tm;
//	private Comments cm;
//	private DateFormat sdf;
//	
//	public weibo(String id, String session){
//		uid = id;
//		access_token = session;
//		
////		repo = new RepoUtil("http://localhost:8080/openrdf-sesame/", "my_repo");
//		repo = new RepoUtil();
//		
//		um = new Users();
//		um.client.setToken(access_token);
//		
//		fm = new Friendships();
//		fm.client.setToken(access_token);
//		
//		tm = new Timeline();
//		tm.client.setToken(access_token);
//		
//		cm = new Comments();
//		cm.client.setToken(access_token);
//		
//		/*********notice that we need to fix the NS and type later********/
//		String NS = "http://weibo.com/";
//		repo.setNameSpace(NS);
//		repo.setPredType("property");
//		sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//	}
//	
//	public static void main(String[] args) throws QueryEvaluationException, RepositoryException, MalformedQueryException {
//		String haohaoid = "1979814003";
//		String haohaokey = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
////		String kankanid = "2128564862";
////		String kankankey = "2.001xOD1C0OM2YC6d102a5533yk1qLC";
//		
////		weibo testKankan = new weibo(kankanid,kankankey);
//		weibo testHaohao = new weibo(haohaoid,haohaokey);
//		
//		testHaohao.getWeiboData();
////		testKankan.getWeiboData();
//	}
//	
//	/*
//	 * This function is used to get the current user's information
//	 */
//	public void getUserData(){
//		User user;
//		try {
//			user = um.showUserById(uid);
//			repo.setSubjType("userID");
//			
//			repo.addRecord(uid, "name", user.getScreenName(), false);
//			if(user.getDescription().length() > 0){
//				repo.addRecord(uid, "description", user.getDescription(), false);
//			}
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/*
//	 * This function is used to get the current user's followers. namely, FANS
//	 * Here I only take 200 data.
//	 */
//	public void getFollowerData(){
//		UserWapper users;
//		try {
//			users = fm.getFollowersById(uid, new Integer(200), new Integer(0));
//			for(User u : users.getUsers()){
//				repo.setSubjType("userID");
//				repo.setObjType("userID");
//				
//				repo.addRecord(uid, "follower", u.getId(), true);
//				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
//				
//				if(u.getDescription().length() > 0){
//					repo.addRecord(u.getId(), "description", u.getDescription(), false);
//				}
//			}
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void getFriendData(){
//		UserWapper users;
//		try {
//			/*
//			 * Here I only take 200 data, the third left
//			 */
//			users = fm.getFriendsById(uid, new Integer(200), new Integer(0));
//			for(User u : users.getUsers()){
//				repo.setSubjType("userID");
//				repo.setObjType("userID");
//				repo.addRecord(uid, "friend", u.getId(), true);			
//				repo.addRecord(u.getId(), "name", u.getScreenName(), false);
//				
//				if(u.getDescription().length() > 0){
//					repo.addRecord(u.getId(), "description", u.getDescription(), false);
//				}
//			}
//		} catch (WeiboException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/*
//	 * to the first three below
//	 * pageNum >= 1 && if countNum > 100, it is considered to be 100 as default
//	 */
//	public void getUserTimeline(int pageNum,int countNum){
//		Paging page = new Paging(1,countNum);
//		Integer base_app = new Integer(0);
//		Integer feature = new Integer(0);
//		StatusWapper status;
//		
//		for(int i=1;i<=pageNum;++i){
//			page.setPage(i);			
//			try {
//				status = tm.getUserTimelineByUid(uid, page, base_app, feature);
//				for(Status s : status.getStatuses()){
//					
//					repo.setSubjType("userID");	
//					repo.setObjType("weiboID");
//					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
//					
//					repo.setSubjType("weiboID");
//					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
//					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
//				}
//			} catch (WeiboException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public void getFriendTimeline(int pageNum, int countNum){
//		Paging page = new Paging(1,countNum);
//		Integer base_app = new Integer(0);
//		Integer feature = new Integer(0);
//		StatusWapper statusFriend;
//		
//		for(int i=1;i<=pageNum;++i){
//			page.setPage(i);
//			try {
//				statusFriend = tm.getFriendsTimeline(base_app, feature, page);
//				for(Status s : statusFriend.getStatuses()){
//					repo.setSubjType("userID");
//					repo.setObjType("weiboID");
//					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
//					
//					repo.setSubjType("weiboID");
//					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
//					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
//				}
//			} catch (WeiboException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public void getBilateralTimeline(int pageNum, int countNum){
//		Paging page = new Paging(1,countNum);
//		Integer base_app = new Integer(0);
//		Integer feature = new Integer(0);
//		StatusWapper statusBilateral;
//		
//		for(int i=1;i<=pageNum;++i){
//			page.setPage(i);
//			try {
//				statusBilateral = tm.getBilateralTimeline(page, base_app, feature);
//				for(Status s : statusBilateral.getStatuses()){
//					repo.setSubjType("userID");
//					repo.setObjType("weiboID");
//					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
//					
//					repo.setSubjType("weiboID");
//					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
//					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
//				}
//			} catch (WeiboException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	/*
//	 * to the first two below
//	 * pageNum >=1
//	 */
//	public void getCommentByme(int pageNum, int countNum){
//		Paging page = new Paging(pageNum,countNum);
//		CommentWapper commentByMe;
//		
//		for(int i=1;i<=pageNum;++i){
//			page.setPage(i);
//			try {
//				commentByMe = cm.getCommentByMe(page, new Integer(0));
//				for(Comment c : commentByMe.getComments()){
//					repo.setSubjType("userID");
//					repo.setObjType("commentID");
//					repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
//
//					repo.setSubjType("commentID");
//					repo.setObjType("weiboID");
//					
//					repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
//					repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
//					repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
//				}
//			} catch (WeiboException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	public void getCommentTome(int pageNum, int countNum){
//		Paging page = new Paging(1,countNum);
//		CommentWapper commentToMe;
//		
//		for(int i=1;i<=pageNum;++i){
//			page.setPage(i);
//			try {
//				commentToMe = cm.getCommentToMe(page, new Integer(0), new Integer(0));
//				for(Comment c :commentToMe.getComments()){
//					repo.setSubjType("userID");
//					repo.setObjType("commentID");
//					repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
//		
//					repo.setSubjType("commentID");
//					repo.setObjType("weiboID");
//					repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
//					repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
//					repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
//				}
//			} catch (WeiboException e) {
//				e.printStackTrace();
//			}	
//		}
//	}
//	
//	public void getWeiboData(){
//		
//		getUserData();
//		getFollowerData();
//		getFriendData();
//		getUserTimeline(1,100);
//		getFriendTimeline(1,100);
//		getBilateralTimeline(1,100);
//		getCommentByme(2,100);
//		getCommentTome(2,100);
//
//		repo.saveRDFTurtle("./rkTest.n3", RDFFormat.N3, "");
//		
//		System.out.println("----------output ends-------------");
//		System.out.println("----------Program exit------------");
//	}
//}