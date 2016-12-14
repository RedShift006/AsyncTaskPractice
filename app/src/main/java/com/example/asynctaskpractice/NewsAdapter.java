package com.example.asynctaskpractice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends BaseAdapter implements OnScrollListener{
	
	private List<NewsItem> mList;
	private LayoutInflater mInflater;
	private ListView mListView;
	private ImageLoader imageLoader;
	
	private int mStart;
	private int mEnd;
	private boolean isFirstIn;
	
	
	
	public NewsAdapter(Context context,List<NewsItem> data,ListView listView){
		
		mList=data;
		mInflater=LayoutInflater.from(context);
		mListView=listView;
		isFirstIn = true;
		
		imageLoader=new ImageLoader(mListView);
		imageLoader.mUrls = new String[mList.size()];
		for(int i=0;i<mList.size();i++){
			imageLoader.mUrls[i] = mList.get(i).getLink();
		}
		mListView.setOnScrollListener(this);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.item_imooc,null);
			viewHolder.ivImage=(ImageView) convertView.findViewById(R.id.iv_image);
			viewHolder.tvContent=(TextView) convertView.findViewById(R.id.tv_content);
			viewHolder.tvTitle=(TextView) convertView.findViewById(R.id.tv_tile);
			convertView.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder) convertView.getTag();
		}
		
		viewHolder.ivImage.setTag(mList.get(position).getLink());
		viewHolder.ivImage.setImageResource(R.drawable.ic_launcher);
		
		imageLoader.showImage(viewHolder.ivImage,mList.get(position).getLink());
		
		viewHolder.tvTitle.setText(mList.get(position).getTitle());
//		viewHolder.tvContent.setText(mList.get(position).getDescription());
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView ivImage;
		TextView tvTitle;
		TextView tvContent;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		if(scrollState==SCROLL_STATE_IDLE){
			imageLoader.loadImages(mStart,mEnd);
		}else{
			imageLoader.cancelAllAsyncTask();
		}
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		mStart=firstVisibleItem;
		mEnd=firstVisibleItem+visibleItemCount;
		
		if(isFirstIn&&visibleItemCount>0){
			imageLoader.loadImages(mStart,mEnd);
			isFirstIn=false;
		}
		
	}

}
