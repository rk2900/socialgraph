package weibo4j.examples.comment;

import weibo4j.Comments;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.WeiboException;

public class GetCommentToMe {

	public static void main(String[] args) {
//		String access_token = args[0];
		//98
		String access_token = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		Comments cm = new Comments();
		cm.client.setToken(access_token);
		try {
			CommentWapper comment = cm.getCommentToMe();
			for(Comment c :comment.getComments()){
				Log.logInfo(c.toString());
			}
			System.out.println(comment.getTotalNumber());
			System.out.println(comment.getNextCursor());
			System.out.println(comment.getNextCursor());
			System.out.println(comment.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
