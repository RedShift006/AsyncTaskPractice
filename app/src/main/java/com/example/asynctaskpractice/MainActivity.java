package com.example.asynctaskpractice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//博客地址：http://blog.csdn.net/dmk877/article/details/49366421
public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	ListView lvImooc;
	private String url="http://www.imooc.com/api/teacher?type=4&num=40";
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        lvImooc=(ListView) findViewById(R.id.lv_imooc);
        new NewsAsyncTask().execute(url);
    }
    
    private List<NewsBean> getJsonData(String url){
    	
    	List<NewsBean> newsBeanList=new ArrayList();
    	try {
			String jsonString = readStream(new URL(url).openStream());
			NewsBean newsBean;
			JSONObject jsonObject;
			
			try {
				jsonObject=new JSONObject(jsonString);
				JSONArray jsonArray = new JSONArray();
				jsonArray=jsonObject.getJSONArray("data");
				
				for(int i=0;i<jsonArray.length();i++){
					jsonObject = jsonArray.getJSONObject(i);
					newsBean=new NewsBean();
					newsBean.picSmall=jsonObject.getString("picSmall");
					newsBean.picBig=jsonObject.getString("picBig");
					newsBean.name=jsonObject.getString("name");
					newsBean.description=jsonObject.getString("description");
					newsBeanList.add(newsBean);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return newsBeanList;
    	
    }
    
    /**
     * 通过inputstream解析网页返回的数组
     * @param is
     * @return
     */
    private String readStream(InputStream is){
    	InputStreamReader isr;
    	StringBuffer result=new StringBuffer();
    	try {
    		String line="";
			isr=new InputStreamReader(is, "utf-8");
			BufferedReader br=new BufferedReader(isr);
			while((line=br.readLine())!=null){
				result.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	return result.toString();
    }
    
    /**
     * 实现网络的异步访问
     * @author Shimmer
     *
     */
    class NewsAsyncTask extends AsyncTask<String, Void, List<NewsItem>>{

		@Override
		protected List<NewsItem> doInBackground(String... params) {
			Document temp = HttpUtil.httpGetDoc(params[0]);

			List<NewsItem> list = JsoupUtil.getNewsItemList(0 , temp);

			return list;
		}
    	
		@Override
		protected void onPostExecute(List<NewsItem> result) {
			super.onPostExecute(result);
			
			NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this,result,lvImooc);
			lvImooc.setAdapter(newsAdapter);
			
		}
    }
    
}
