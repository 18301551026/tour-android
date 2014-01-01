package cn.itcast.douban;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.itcast.douban.common.StatusEnum;
import cn.itcast.douban.domain.PageResult;
import cn.itcast.douban.domain.TourCommon;
import cn.itcast.douban.util.MyToast;
import cn.itcast.douban.util.NetUtils;

import com.alibaba.fastjson.JSON;

public class IsConfirmTourActivity extends BaseActivity implements
		OnClickListener {

	private Integer startIndex;// 起始下标
	private Integer pageSize;// 每页行数
	private Integer rowCount;// 总行数
	private Integer pageCount;// 总页数

	private List<TourCommon> tourCommons;
	private ListView lv;

	private Button bt_pre;
	private Button bt_next;
	private Button bt_jump;
	private ImageButton refresh_button;
	private EditText et_page;
	private TextView tv_current_page;

	private boolean isloading;
	private boolean isView = false;

	private static final int VIEW_NOTE = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.tour);
		startIndex = 0;
		pageSize = 10;
		super.onCreate(savedInstanceState);
		isloading = false;
	}

	@Override
	protected void findView() {
		lv = (ListView) this.findViewById(R.id.tourlistview);

		bt_pre = (Button) this.findViewById(R.id.bt_pre);
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_jump = (Button) this.findViewById(R.id.bt_jump);
		et_page = (EditText) this.findViewById(R.id.et_page);
		tv_current_page = (TextView) this.findViewById(R.id.tv_current_page);
		refresh_button = (ImageButton) this.findViewById(R.id.refresh_button);

	}

	@Override
	protected void setUpView() {
		bt_jump.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_pre.setOnClickListener(this);
		bt_pre.setEnabled(false);
		
		refresh_button.setOnClickListener(this);
		

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isView = true;
				TourCommon tour = tourCommons.get(position);
				viewTour(tour.getId().toString());
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				isView = true;
				AlertDialog.Builder builder = new Builder(
						IsConfirmTourActivity.this);
				String[] items = { "查看营业数据"};
				builder.setItems(items, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						TourCommon tour = tourCommons.get(position);
						switch (which) {
						case 0:
							viewTour(tour.getId().toString());
							break;
						}

					}

				});
				builder.create().show();
				return false;
			}
		});
	}

	private void viewTour(String id) {
		
		new AsyncTask<String, Void, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				isloading = true;
				showLoading();				
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				isloading = false;
				hideLoading();
				if (result != null && !"".equals(result.trim())) {
					Intent intent = new Intent(IsConfirmTourActivity.this.getApplicationContext(),
							ViewTourActivity.class);
					intent.putExtra("json", result);
					startActivityForResult(intent, VIEW_NOTE);					
				} else {
					MyToast.showToast(getApplicationContext(), "营业数据查看失败", 0);
				}

			}

			@Override
			protected String doInBackground(String... params) {
				Map<String, String> argument = new HashMap<String, String>();
				argument.put("id", params[0]);				
				String json = NetUtils.callWs("tourService", "getTourWs", argument);
				return json;
				
			}

		}.execute(id);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (isView) {
			isView = false;
			return;
		}
		fillData();
	}

	private void fillData() {
		new AsyncTask<Void, Void, PageResult>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				isloading = true;
				showLoading();
			}

			@Override
			protected void onPostExecute(PageResult result) {
				super.onPostExecute(result);
				isloading = false;
				hideLoading();
				if (result != null) {
					tourCommons = result.getResult();
					lv.setAdapter(new TourCommonAdapter(result.getResult()));

				} else {
					MyToast.showToast(getApplicationContext(), "获取数据失败", 0);
				}
				isloading = false;
				int page = startIndex / pageSize + 1;
				rowCount = result.getRowCount();
				int m = rowCount % pageSize;
				pageCount = m == 0 ? rowCount / pageSize : (rowCount - m)
						/ pageSize + 1;
				tv_current_page.setText("第" + page + "页/共" + pageCount + "页");
				
				setPageButtonStatus();
			}

			@Override
			protected PageResult doInBackground(Void... params) {

				DoubanApplication myapp = (DoubanApplication) IsConfirmTourActivity.this
						.getApplication();
				String userId = myapp.currentUser.getId().toString();

				Map<String, String> argument = new HashMap<String, String>();
				argument.put("userId", userId);
				argument.put("status", StatusEnum.reported.getValue()
						.toString());
				argument.put("start", IsConfirmTourActivity.this.startIndex
						+ "");
				argument.put("pageSize",
						IsConfirmTourActivity.this.pageSize.toString());

				String json = NetUtils
						.callWs("tourService", "findWs", argument);
				PageResult result = JSON.parseObject(json, PageResult.class);
				return result;
			}

		}.execute();
	}

	public void onClick(View v) {
		if (isloading) {
			return;
		}

		switch (v.getId()) {
		case R.id.bt_jump:
			String strpage = et_page.getText().toString();
			if (strpage.contains(".")) {
				MyToast.showToast(getApplicationContext(), "页码只能是整数", 1);
				return;
			}
			int page = Integer.parseInt(strpage);
			if (page <= 0) {
				MyToast.showToast(getApplicationContext(), "页码是从1开始的", 1);
				return;
			} else {
				startIndex = (page - 1) * pageSize;
				fillData();
			}
			setPageButtonStatus();
			break;
		case R.id.bt_next:// 点击下一页
			startIndex += pageSize;
			bt_pre.setEnabled(true);
			fillData();
			setPageButtonStatus();
			break;
		case R.id.bt_pre:// 点击上一页
			startIndex -= pageSize;
			fillData();
			setPageButtonStatus();
			break;
		case R.id.refresh_button: // 刷新
			fillData();
			break;			
		}
	}

	private void setPageButtonStatus() {
		if (startIndex <= 0) {
			bt_pre.setEnabled(false);
			startIndex = 0;
		} else {
			bt_pre.setEnabled(true);
		}
		if ((startIndex >= (pageCount - 1) * pageSize)
				|| (rowCount <= pageSize)) {
			bt_next.setEnabled(false);
			startIndex = (pageCount - 1) * pageSize;
		} else {
			bt_next.setEnabled(true);
		}
	}

	private class TourCommonAdapter extends BaseAdapter {

		private List<TourCommon> result;

		public TourCommonAdapter(List<TourCommon> result) {
			this.result = result;
		}

		public int getCount() {
			return result.size();
		}

		public Object getItem(int position) {
			return result.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			final ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.tour_item, null);
				holder = new ViewHolder();
				holder.month_value = (TextView) view
						.findViewById(R.id.month_value);
				holder.num_label = (TextView) view.findViewById(R.id.num_label);
				holder.num_value = (TextView) view.findViewById(R.id.num_value);
				holder.money_label = (TextView) view
						.findViewById(R.id.money_label);
				holder.money_value = (TextView) view
						.findViewById(R.id.money_value);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			TourCommon tour = result.get(position);
			holder.month_value.setText(tour.getReportYear() + "年"
					+ tour.getReportMonth() + "月：");
			holder.num_label.setText("接待人数：");
			holder.num_value.setText(tour.getTotalPersonNum().toString());
			holder.money_label.setText("总收入：");
			holder.money_value.setText(tour.getTotalIncome().toString());
			return view;
		}
	}

	static class ViewHolder {
		TextView month_value;
		TextView num_label;
		TextView num_value;
		TextView money_label;
		TextView money_value;
	}

}
