package haohao.example;

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
	private RepoUtil repo;
	private Users um;
	private Friendships fm;
	private Timeline tm;
	private Comments cm;
	private DateFormat sdf;
	
	public weibo(String id, String session){
		uid = id;
		access_token = session;
		
		repo = new RepoUtil();	
		
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
		weibo test = new weibo("1979814003","2.00vDGzJC0OM2YC4ffc0d56f70hPqLU");
		test.getWeiboData();
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
	public void getFollowerData(){
		UserWapper users;
		try {
			users = fm.getFollowersById(uid, new Integer(200), new Integer(0));
			for(User u : users.getUsers()){
				repo.setSubjType("userID");
				repo.setObjType("userID");
				
				repo.addRecord(uid, "follower", u.getId(), true);
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
	
	public void getFriendData(){
		UserWapper users;
		try {
			/*
			 * Here I only take 200 data, the third left
			 */
			users = fm.getFriendsById(uid, new Integer(200), new Integer(0));
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
	 */
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
					
					repo.setSubjType("userID");	
					repo.setObjType("weiboID");
					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
					
					repo.setSubjType("weiboID");
					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
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
					repo.setSubjType("userID");
					repo.setObjType("weiboID");
					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
					
					repo.setSubjType("weiboID");
					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
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
					repo.setSubjType("userID");
					repo.setObjType("weiboID");
					repo.addRecord(s.getUser().getId(), "createWeibo", s.getId(), true);
					
					repo.setSubjType("weiboID");
					repo.addRecord(s.getId(), "weiboText", s.getText(),false);
					repo.addRecord(s.getId(), "weiboDate", sdf.format(s.getCreatedAt()), false);
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
	 */
	public void getCommentByme(int pageNum, int countNum){
		Paging page = new Paging(pageNum,countNum);
		CommentWapper commentByMe;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				commentByMe = cm.getCommentByMe(page, new Integer(0));
				for(Comment c : commentByMe.getComments()){
					repo.setSubjType("userID");
					repo.setObjType("weiboID");
					repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);

					repo.setSubjType("weiboID");
					repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
					repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
					repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void getCommentTome(int pageNum, int countNum){
		Paging page = new Paging(1,countNum);
//		Integer base_app = new Integer(0);
//		Integer feature = new Integer(0);
		CommentWapper commentToMe;
		
		for(int i=1;i<=pageNum;++i){
			page.setPage(i);
			try {
				commentToMe = cm.getCommentToMe(page, new Integer(0), new Integer(0));
				for(Comment c :commentToMe.getComments()){
					repo.setSubjType("userID");
					repo.setObjType("weiboID");
					repo.addRecord(c.getUser().getId(), "createComment", c.getIdstr(), true);
		
					repo.setSubjType("weiboID");
					repo.addRecord(c.getIdstr(), "commentTo", c.getStatus().getId(), true);
					repo.addRecord(c.getIdstr(), "commentContext", c.getText(), false);
					repo.addRecord(c.getIdstr(), "commentDate", sdf.format(c.getCreatedAt()), false);
				}
			} catch (WeiboException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	public void getWeiboData(){
		
		getUserData();
		getFollowerData();
		getFriendData();
		getUserTimeline(1,100);
		getFriendTimeline(1,100);
		getBilateralTimeline(1,100);
		getCommentByme(2,100);
		getCommentTome(2,100);

		System.out.println("----------output ends-------------");
		repo.saveRDFTurtle("./test.n3", RDFFormat.N3);
//		repo.saveRDFTurtle("./xml.rdf", RDFFormat.RDFXML);
		System.out.println("----------Program exit------------");
	}
}