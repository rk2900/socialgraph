package haohao.example;

import Utils.RepoUtil;
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
	public static void main(String[] args) {
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
		
		FileOutputManager manager = new FileOutputManager("d:\\test\\new.txt","UTF-8");
		RepoUtil repo = new RepoUtil();
//		SubjectUtil subj = new
		
		StringBuilder spo = new StringBuilder();
		
		try {
			User user = um.showUserById(uid);
			spo.append(uid);
			spo.append("\t");
			spo.append("rdf:label");
			spo.append("\t");
			spo.append(user.getScreenName());
			spo.append("\n");
			manager.write(spo.toString());
			spo.setLength(0);
			
			spo.append(uid);
			spo.append("\t");
			spo.append("rdf:description");
			spo.append("\t");
			spo.append(user.getDescription());
			spo.append("\n");
			manager.write(spo.toString());
			spo.setLength(0);
			
			UserWapper users = fm.getFollowersById(uid);
			for(User u : users.getUsers()){
				spo.append(uid);
				spo.append("\t");
				spo.append("rdf:follower");
				spo.append("\t");
				spo.append(u.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(u.getId());
				spo.append("\t");
				spo.append("rdf:label");
				spo.append("\t");
				spo.append(u.getScreenName());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(u.getId());
				spo.append("\t");
				spo.append("rdf:description");
				spo.append("\t");
				spo.append(u.getDescription());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
			}
			
			users = fm.getFriendsByID(uid);
			for(User u : users.getUsers()){
				spo.append(uid);
				spo.append("\t");
				spo.append("rdf:friend");
				spo.append("\t");
				spo.append(u.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(u.getId());
				spo.append("\t");
				spo.append("rdf:label");
				spo.append("\t");
				spo.append(u.getScreenName());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(u.getId());
				spo.append("\t");
				spo.append("rdf:description");
				spo.append("\t");
				spo.append(u.getDescription());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(uid + "\t" + "rdf:friend" + "\t" + u.getId() + "\n");
//				manager.write(u.getId() + "\t" + "rdf:label" + "\t" + u.getScreenName() + "\n");
//				manager.write(u.getId() + "\t" + "rdf:description" + "\t" + u.getDescription() + "\n");
			}
			
			StatusWapper status = tm.getUserTimeline();
			for(Status s : status.getStatuses()){
				spo.append(s.getUser().getId());
				spo.append("\t");
				spo.append("rdf:createWeibo");
				spo.append("\t");
				spo.append(s.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboText");
				spo.append("\t");
				spo.append(s.getText());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboDate");
				spo.append("\t");
				spo.append(s.getCreatedAt());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(s.getUser().getId() + "\t" + "rdf:createWeibo" + "\t" + s.getId() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboText" + "\t" + s.getText() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboDate" + "\t" + s.getCreatedAt() + "\n");
//				Log.logInfo(s.toString());
			}
			
			StatusWapper statusFriend = tm.getFriendsTimeline();
			for(Status s : statusFriend.getStatuses()){
				spo.append(s.getUser().getId());
				spo.append("\t");
				spo.append("rdf:createWeibo");
				spo.append("\t");
				spo.append(s.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboText");
				spo.append("\t");
				spo.append(s.getText());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboDate");
				spo.append("\t");
				spo.append(s.getCreatedAt());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(s.getUser().getId() + "\t" + "rdf:createWeibo" + "\t" + s.getId() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboText" + "\t" + s.getText() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboDate" + "\t" + s.getCreatedAt() + "\n");
//				Log.logInfo(s.toString());
			}
			
			StatusWapper statusBilateral = tm.getBilateralTimeline();
			for(Status s : statusBilateral.getStatuses()){
				spo.append(s.getUser().getId());
				spo.append("\t");
				spo.append("rdf:createWeibo");
				spo.append("\t");
				spo.append(s.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboText");
				spo.append("\t");
				spo.append(s.getText());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(s.getId());
				spo.append("\t");
				spo.append("rdf:weiboDate");
				spo.append("\t");
				spo.append(s.getCreatedAt());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(s.getUser().getId() + "\t" + "rdf:createWeibo" + "\t" + s.getId() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboText" + "\t" + s.getText() + "\n");
//				manager.write(s.getId() + "\t" + "rdf:weiboDate" + "\t" + s.getCreatedAt() + "\n");
//				Log.logInfo(s.toString());
			}
			
			CommentWapper commentByMe = cm.getCommentByMe();
			for(Comment c : commentByMe.getComments()){
				spo.append(c.getUser().getId());
				spo.append("\t");
				spo.append("rdf:createComment");
				spo.append("\t");
				spo.append(c.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentTo");
				spo.append("\t");
				spo.append(c.getStatus().getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentContext");
				spo.append("\t");
				spo.append(c.getText());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentData");
				spo.append("\t");
				spo.append(c.getCreatedAt());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(c.getUser().getId() + "\t" + "rdf:createComment" + "\t" + c.getId() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentTo" + "\t" + c.getStatus().getId() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentContext" + "\t" + c.getText() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentDate" + "\t" + c.getCreatedAt() + "\n");
//				Log.logInfo(c.toString());
			}
			
			CommentWapper commentToMe = cm.getCommentToMe();
			for(Comment c :commentToMe.getComments()){
				spo.append(c.getUser().getId());
				spo.append("\t");
				spo.append("rdf:createComment");
				spo.append("\t");
				spo.append(c.getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentTo");
				spo.append("\t");
				spo.append(c.getStatus().getId());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentContext");
				spo.append("\t");
				spo.append(c.getText());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
				
				spo.append(c.getId());
				spo.append("\t");
				spo.append("rdf:commentData");
				spo.append("\t");
				spo.append(c.getCreatedAt());
				spo.append("\n");
				manager.write(spo.toString());
				spo.setLength(0);
//				manager.write(c.getUser().getId() + "\t" + "rdf:createComment" + "\t" + c.getId() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentTo" + "\t" + c.getStatus().getId() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentContext" + "\t" + c.getText() + "\n");
//				manager.write(c.getId() + "\t" + "rdf:commentDate" + "\t" + c.getCreatedAt() + "\n");
////				Log.logInfo(c.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		manager.close();
	}
}