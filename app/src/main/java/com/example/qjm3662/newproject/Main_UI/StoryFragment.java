package com.example.qjm3662.newproject.Main_UI;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.qjm3662.newproject.App;
import com.example.qjm3662.newproject.Data.Final_Static_data;
import com.example.qjm3662.newproject.Data.Story;
import com.example.qjm3662.newproject.Data.User;
import com.example.qjm3662.newproject.ListViewCompat;
import com.example.qjm3662.newproject.NetWorkOperator;
import com.example.qjm3662.newproject.R;
import com.example.qjm3662.newproject.Slide.SlideAdapter;
import com.example.qjm3662.newproject.StoryView.Edit_Acticity;
import com.example.qjm3662.newproject.StoryView.Main2Activity;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.sql.Date;

import okhttp3.Call;

public class StoryFragment extends Fragment implements OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {
	
	private ListViewCompat mListView;
	private View view;
	public static SlideAdapter slideAdapter;
	private ImageView img_add;
	private ImageView img_up_load;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_story, container, false);
		initView();



		return view;
	}
	
	private void initView() {
		mListView = (ListViewCompat) view.findViewById(android.R.id.list);
		slideAdapter = new SlideAdapter(getActivity());
		mListView.setAdapter(slideAdapter);
		img_add = (ImageView) view.findViewById(R.id.add_imageView_story);
		img_add.setOnClickListener(this);
		img_up_load = (ImageView) view.findViewById(R.id.cloud_imageview_story);
		img_up_load.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		refreshStoryListView();
	}

	public static void refreshStoryListView() {
		//更新数据（查询出所有的数据）
		slideAdapter.notifyDataSetChanged();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();

		//打开编辑界面，查看故事
		Intent i = new Intent(view.getContext(),Main2Activity.class);
		i.putExtra(Edit_Acticity.EDIT_TITLE, App.StoryList.get(position).getTitle());
		i.putExtra(Edit_Acticity.EDIT_CONTENT, App.StoryList.get(position).getContent());
		//加一个标记，true表示是已有的故事查看
		i.putExtra("JUDGE",true);
		i.putExtra("ID",App.StoryList.get(position).getLocal_id());
		i.putExtra("position",position);
		this.startActivity(i);
	}



	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.add_imageView_story:
				startActivityForResult(new Intent(view.getContext(),Edit_Acticity.class),5);
				break;
			case R.id.cloud_imageview_story:
				//NetWorkOperator.UpLoad_story(view.getContext(),App.StoryList.get(0));
//				new Thread(new Runnable() {
//					@Override
//					public void run() {
//						OkHttpUtils
//								.post()
//								.url(Final_Static_data.URL_GET_USER_STORIES)
//								.addHeader("LoginToken", User.getInstance().getLoginToken())
//								.addParams("userID", String.valueOf(User.getInstance().getId()))
//								.build()
//								.execute(new StringCallback() {
//									@Override
//									public void onError(Call call, Exception e) {
//										System.out.println("error");
//									}
//
//									@Override
//									public void onResponse(String response) {
//										System.out.println(response + " RES");
//
//									}
//								});
//					}
//				}).start();
//				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode){
			case 5:
				System.out.println("OK");
				slideAdapter.notifyDataSetChanged();
				break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		Story story = App.StoryList.get(position);
		Date date = new Date(System.currentTimeMillis());
		story.setUpdatedAt(date.toString());
		if(User.getInstance().getLoginToken() == null){
			Toast.makeText(view.getContext(), "请先登录", Toast.LENGTH_SHORT).show();
		}else {
			System.out.println("开始上传");
			NetWorkOperator.UpLoad_story(view.getContext(), story);
		}
		return true;
	}
}
