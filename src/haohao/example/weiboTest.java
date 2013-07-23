package haohao.example;

import haohao.FileOutputManager;
import weibo4j.Weibo;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.PostParameter;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

public class weiboTest extends Weibo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param args
	 * @throws WeiboException 
	 */
	public static void main(String[] args) throws WeiboException {
		// TODO Auto-generated method stub
		String access_token = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		String uids = "1979814003";
		//String uids = "2665167187";
		weiboTest test = new weiboTest();
		test.client.setToken(access_token);
		FileOutputManager manager = new FileOutputManager("D:\\test\\weibotest.txt", "UTF-8");
		
//		JSONArray userCount = test.client.get(WeiboConfig.getValue("baseURL") + "users/counts.json",
//				new PostParameter[] { new PostParameter("uids", uids)}).asJSONArray();
//		Log.logInfo(userCount.toString());
//		manager.write(userCount.toString());
//		manager.write("\n");
		
		JSONObject MyuserShow = test.client.get(WeiboConfig.getValue("baseURL") + "users/show.json",
				new PostParameter[] { new PostParameter("uid", uids)}).asJSONObject();
		Log.logInfo(MyuserShow.toString());
		manager.write(MyuserShow.toString());
		manager.write("\n");
		
		JSONObject friendshipsFollowersIDs = test.client.get(WeiboConfig.getValue("baseURL") + "friendships/followers/ids.json",
				new PostParameter [] { new PostParameter("uid", uids)}).asJSONObject();
		Log.logInfo(friendshipsFollowersIDs.toString());
		manager.write(friendshipsFollowersIDs.toString());
		manager.write("\n");
		
		try {
			JSONArray followerIDs = friendshipsFollowersIDs.getJSONArray("ids");
			String temp = followerIDs.toString().substring(1,followerIDs.toString().length()-1);
			String[] followerids = temp.split(",");
			for(String id: followerids){
				//Log.logInfo(id);
				JSONObject userShow = test.client.get(WeiboConfig.getValue("baseURL") + "users/show.json",
						new PostParameter[] { new PostParameter("uid", id)}).asJSONObject();
				Log.logInfo(userShow.toString());
				manager.write(userShow.toString());
				manager.write("\n");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JSONObject friendshipsFriendsIDs = test.client.get(WeiboConfig.getValue("baseURL") + "friendships/friends/ids.json",
//				new PostParameter [] { new PostParameter("uid", uids)}).asJSONObject();
//		Log.logInfo(friendshipsFriendsIDs.toString());
//		manager.write(friendshipsFriendsIDs.toString());
//		manager.write("\n");
		
//		JSONObject userTimeLineIDs = test.client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline/ids.json",
//				new PostParameter[] { new PostParameter("uid", uids)}).asJSONObject();
//		Log.logInfo(userTimeLineIDs.toString());
//		manager.write(userTimeLineIDs.toString());
//		manager.write("\n");		
//		
//		String id = "3599584163363772";
//		JSONObject weiboIDShow = test.client.get(WeiboConfig.getValue("baseURL") + "statuses/show.json",
//				new PostParameter[] { new PostParameter("id", id)}).asJSONObject();
//		Log.logInfo(weiboIDShow.toString());
//		manager.write(weiboIDShow.toString());
//		manager.write("\n");	
//		
//		JSONObject commentShow = test.client.get(WeiboConfig.getValue("baseURL") + "comments/show.json",
//				new PostParameter[] { new PostParameter("id", id)}).asJSONObject();
//		Log.logInfo(commentShow.toString());
//		manager.write(commentShow.toString());
//		manager.write("\n");
		
//		JSONObject commentsByMe = test.client.get(WeiboConfig.getValue("baseURL") + "comments/by_me.json").asJSONObject();
//		Log.logInfo(commentsByMe.toString());
//		manager.write(commentsByMe.toString());
//		manager.write("\n");
		
	}
}
