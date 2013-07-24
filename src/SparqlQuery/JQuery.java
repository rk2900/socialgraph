package SparqlQuery;
import org.json.*;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

import Utils.RepoUtil;

import java.io.*;

public class JQuery {
	private JSONObject Sentence = new JSONObject();
	private RepositoryConnection repoConn;

	
	public JQuery(RepoUtil repo) throws JSONException{
		JSONArray temparray = new JSONArray();
		JSONArray tempselect = new JSONArray();
		JSONArray tempwhere = new JSONArray();
		JSONArray temporderby = new JSONArray();
		
		Sentence.put("PREFIX", temparray);
		Sentence.put("SELECT", tempselect);
		Sentence.put("WHERE", tempwhere);
		
		
		JSONObject and = new JSONObject("{\"AND\":[]}");
		Sentence.getJSONArray("WHERE").put(and);

		JSONObject or = new JSONObject("{\"OR\":[]}");
		Sentence.getJSONArray("WHERE").put(or);

		JSONObject ranmin = new JSONObject("{\"RANMIN\":[]}");
		Sentence.getJSONArray("WHERE").put(ranmin);

		JSONObject ranmax = new JSONObject("{\"RANMAX\":[]}");
		Sentence.getJSONArray("WHERE").put(ranmax);

		JSONObject regex = new JSONObject("{\"REGEX\":[]}");
		Sentence.getJSONArray("WHERE").put(regex);

		
		Sentence.put("ORDER BY", temporderby);

		JSONObject asc = new JSONObject("{\"ASC\":[]}");
		Sentence.getJSONArray("ORDER BY").put(asc);

		JSONObject dsc = new JSONObject("{\"DSC\":[]}");
		Sentence.getJSONArray("ORDER BY").put(dsc);
		
		repoConn = repo.getConnection();

	}
	
	public void SetPrefix(String Prefix){
		try {
			Sentence.getJSONArray("PREFIX").put(Prefix);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void SetSelect(String Select){
		try {
			Sentence.getJSONArray("SELECT").put(Select);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void SetCount(String Count, String C){
		JSONObject temp;
		try {
			temp = new JSONObject("{COUNT:\"" + Count + "\", ARG:\"" + C + "\"}");
			Sentence.getJSONArray("SELECT").put(temp);
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	public void SetAndCondition(String S, String P, String O) throws JSONException{
		JSONObject temp = new JSONObject();
		temp.put("S", S);
		temp.put("P", P);
		temp.put("O", O);
//		JSONObject temp1 = new JSONObject();
//		temp1.append("AND", temp);
		Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").put(temp);
	}
	
	public void SetOrCondition(String S, String P, String O) throws JSONException{
		JSONObject temp = new JSONObject();
		temp.put("S", S);
		temp.put("P", P);
		temp.put("O", O);
//		JSONObject temp1 = new JSONObject();
//		temp1.append("OR", temp);
		Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").put(temp);
	}
	
	public void SetRangeMin(String RANArg, String Min) throws JSONException{
		JSONObject temp = new JSONObject();
		temp.put("ARG", RANArg);
		temp.put("MIN", Min);
		Sentence.getJSONArray("WHERE").getJSONObject(2).getJSONArray("RANMIN").put(temp);
	}
	
	public void SetRangeMax(String RANArg, String Max) throws JSONException{
		JSONObject temp = new JSONObject();
		temp.put("ARG", RANArg);
		temp.put("MAX", Max);
		Sentence.getJSONArray("WHERE").getJSONObject(3).getJSONArray("RANMAX").put(temp);
	}

	public void SetRegex(String Arg, String Regex, boolean ignore) throws JSONException{
		JSONObject temp = new JSONObject();
		temp.put("ARG", Arg);
		temp.put("Regex", Regex);
		temp.put("CASE", ignore);
		Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").put(temp);
	}
	
	public void SetOrderByAsc(String Arg) throws JSONException{
		Sentence.getJSONArray("ORDER BY").getJSONObject(0).getJSONArray("ASC").put(Arg);
	}
	
	public void SetOrderByDsc(String Arg) throws JSONException{
		Sentence.getJSONArray("ORDER BY").getJSONObject(1).getJSONArray("DSC").put(Arg);
	}
	
	public void SetLimit(String Limit) throws JSONException{
		Sentence.put("LIMIT", Limit);
	}
	
	public TupleQueryResult Query() throws QueryEvaluationException, RepositoryException, MalformedQueryException, JSONException{
		StringBuilder RESULT = new StringBuilder();
		
//		System.out.println(Sentence.toString());
//		System.out.println(x)
	
		if(Sentence.getJSONArray("PREFIX").length() != 0){
			for(int i = 0; i < Sentence.getJSONArray("PREFIX").length(); i ++){
				RESULT.append("PREFIX ");
				RESULT.append(Sentence.getJSONArray("PREFIX").getString(i) + " ");
			}
			
		}
		
		if(Sentence.getJSONArray("SELECT").length() != 0){
			RESULT.append("SELECT ");
			for(int i = 0 ; i < Sentence.getJSONArray("SELECT").length() ; i ++){
				if(Sentence.getJSONArray("SELECT").get(i).toString().startsWith("{")/*&& has("COUNT")*/){
					RESULT.append("(count(?" + 
									Sentence.getJSONArray("SELECT").getJSONObject(i).getString("COUNT") + 
									") as ?" + 
									Sentence.getJSONArray("SELECT").getJSONObject(i).getString("ARG") +
									") ");
				}
				else{
					RESULT.append("?" + Sentence.getJSONArray("SELECT").get(i) + " ");
				}
			}
		}
		
		if(Sentence.getJSONArray("WHERE").length() != 0){
			RESULT.append("WHERE{");
			if(Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").length() != 0){
				for(int i = 0; i < Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").length(); i++){
					RESULT.append(Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").getJSONObject(i).getString("S") + " ");
					RESULT.append(Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").getJSONObject(i).getString("P") + " ");
					RESULT.append(Sentence.getJSONArray("WHERE").getJSONObject(0).getJSONArray("AND").getJSONObject(i).getString("O") + " . ");
				}
			}
			
			if(Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").length() != 0){
				for(int i = 0; i < Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").length(); i++){
					RESULT.append("{" + Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").getJSONObject(i).getString("S") + " ");
					RESULT.append(Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").getJSONObject(i).getString("P") + " ");
					RESULT.append(Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").getJSONObject(i).getString("O") + " } ");
					if(i != Sentence.getJSONArray("WHERE").getJSONObject(1).getJSONArray("OR").length() - 1){
						RESULT.append("UNION ");
					}
				}
				RESULT.append(". ");
			}
			
			if(Sentence.getJSONArray("WHERE").getJSONObject(2).getJSONArray("RANMIN").length() != 0){
				for(int i = 0; i < Sentence.getJSONArray("WHERE").getJSONObject(2).getJSONArray("RANMIN").length(); i ++){
					RESULT.append("FILTER(?" + Sentence.getJSONArray("WHERE").getJSONObject(2).getJSONArray("RANMIN").getJSONObject(i).get("ARG"));
					RESULT.append(">" + Sentence.getJSONArray("WHERE").getJSONObject(2).getJSONArray("RANMIN").getJSONObject(i).get("MIN") + ") . ");
				}
			}
				
			if(Sentence.getJSONArray("WHERE").getJSONObject(3).getJSONArray("RANMAX").length() != 0){
				for(int i = 0; i < Sentence.getJSONArray("WHERE").getJSONObject(3).getJSONArray("RANMAX").length(); i ++){
					RESULT.append("FILTER(?" + Sentence.getJSONArray("WHERE").getJSONObject(3).getJSONArray("RANMAX").getJSONObject(i).get("ARG"));
					RESULT.append("<" + Sentence.getJSONArray("WHERE").getJSONObject(3).getJSONArray("RANMAX").getJSONObject(i).get("MAX") + ") . ");
				}
			}
			
			if(Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").length() != 0){
				for(int i = 0;i < Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").length(); i ++){
					RESULT.append("FILTER regex(?" + Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").getJSONObject(i).get("ARG") + ", ");
					RESULT.append("\"" + Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").getJSONObject(i).get("Regex") + "\"");
					if(Sentence.getJSONArray("WHERE").getJSONObject(4).getJSONArray("REGEX").getJSONObject(i).getBoolean("CASE")){
						RESULT.append(", \"i\") . ");
					}
					else{
						RESULT.append(") . ");
					}
				}
			}
			
			RESULT.append("} ");
		}
		
		if(Sentence.getJSONArray("ORDER BY").getJSONObject(0).getJSONArray("ASC").length() != 0
			|| Sentence.getJSONArray("ORDER BY").getJSONObject(0).getJSONArray("DSC").length() != 0){
			RESULT.append("ORDER BY ");
			for(int i = 0; i < Sentence.getJSONArray("ORDER BY").getJSONObject(0).getJSONArray("ASC").length(); i ++){
				RESULT.append("ASC(?" + Sentence.getJSONArray("ORDER BY").getJSONObject(0).getJSONArray("ASC").get(i).toString() + ") ");
			}
			
			for(int i = 0; i < Sentence.getJSONArray("ORDER BY").getJSONObject(1).getJSONArray("DSC").length(); i ++){
				RESULT.append("DESC(?" + Sentence.getJSONArray("ORDER BY").getJSONObject(1).getJSONArray("DSC").get(i).toString() + ") ");
			}
		}
		
		if(Sentence.has("LIMIT")){
			RESULT.append("LIMIT ");
			RESULT.append(Sentence.get("LIMIT"));
		}

		System.out.println(RESULT.toString());
		return repoConn.prepareTupleQuery(QueryLanguage.SPARQL, RESULT.toString()).evaluate();
//		return null;
	}
}
