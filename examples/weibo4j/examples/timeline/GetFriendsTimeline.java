package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class GetFriendsTimeline {

	public static void main(String[] args) {
//		String access_token = args[0];
		String access_token = "2.00vDGzJC0OM2YC4ffc0d56f70hPqLU";
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			StatusWapper status = tm.getFriendsTimeline();
			for(Status s : status.getStatuses()){
				Log.logInfo(s.toString());
			}
			System.out.println(status.getNextCursor());
			System.out.println(status.getPreviousCursor());
			System.out.println(status.getTotalNumber());
			System.out.println(status.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
