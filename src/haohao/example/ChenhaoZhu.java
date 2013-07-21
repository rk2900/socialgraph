package haohao.example;

import haohao.FileOutputManager;
import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;

public class ChenhaoZhu {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String access_token = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		String uids = "1979814003";
		
		//user count
		Users um = new Users();
		um.client.setToken(access_token);
		
		FileOutputManager manager = new FileOutputManager("d:\\test\\weibo.txt", "UTF-8");
		try {
			JSONArray user = um.getUserCount(uids);
			Log.logInfo(user.toString());
			
			manager.write(user.toString());
			manager.write("\n");
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		//show user
		try {
			User user = um.showUserById(uids);
			Log.logInfo(user.toString());
			//System.out.println(user.toString());
			
			manager.write(user.toString());
			
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
		manager.close();
		
	}

}
