package weibo4j.examples.friendships;

import weibo4j.Friendships;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

public class GetFriendsChainFollowers {

	public static void main(String[] argv) {
		String args[] = {"2.00vDGzJC0OM2YC4ffc0d56f70hPqLU","1979814003"};
		String access_token = args[0];
		String uid = args[1];
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		try {
			UserWapper  users = fm.getFriendsChainFollowers(uid);
			for(User s : users.getUsers()){
				Log.logInfo(s.toString());
			}
			System.out.println(users.getNextCursor());
			System.out.println(users.getPreviousCursor());
			System.out.println(users.getTotalNumber());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
