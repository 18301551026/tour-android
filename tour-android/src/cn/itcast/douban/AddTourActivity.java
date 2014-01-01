package cn.itcast.douban;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.itcast.douban.domain.FactoryOption;
import cn.itcast.douban.domain.TourCommon;
import cn.itcast.douban.domain.TourDetail;
import cn.itcast.douban.util.MyToast;
import cn.itcast.douban.util.NetUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

public class AddTourActivity extends BaseActivity implements OnClickListener {
	
	private TextView txtTitle;
	private EditText editMonth;
	private EditText editYear;
	private EditText editNum;
	private ListView lvOption;
	private EditText editOption;

	private Button btnSave;
	private Button btnCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.add_tour);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findView() {
		lvOption = (ListView) this.findViewById(R.id.lvOption);
		txtTitle = (TextView) this.findViewById(R.id.txtOptionLabel);
		editMonth = (EditText) this.findViewById(R.id.editMonth);
		editYear = (EditText) this.findViewById(R.id.editYear);
		editNum = (EditText) this.findViewById(R.id.editNum);
		
		btnSave = (Button) this.findViewById(R.id.btnSave);
		btnCancel = (Button) this.findViewById(R.id.btnCancel);
	}

	@Override
	protected void setUpView() {
		btnSave.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		fillOption();
	}
	
	private void fillOption() {
		new AsyncTask<Void, Void, List<FactoryOption>>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showLoading();
			}
			@Override
			protected void onPostExecute(List<FactoryOption> result) {
				super.onPostExecute(result);
				hideLoading();
				if (result != null) {
					lvOption.setAdapter(new TourOptionAdapter(result));
					
					
					//设置lvOption自动适应高度
					  ListAdapter listAdapter = lvOption.getAdapter();  
					    if (listAdapter == null) { 
					        return; 
					    } 

					    int totalHeight = 0; 
					    for (int i = 0; i < listAdapter.getCount(); i++) { 
					        View listItem = listAdapter.getView(i, null, lvOption); 
					        listItem.measure(0, 0); 
					        totalHeight += listItem.getMeasuredHeight(); 
					    } 

					    ViewGroup.LayoutParams params = lvOption.getLayoutParams();
					    params.height = totalHeight + (lvOption.getDividerHeight() * (listAdapter.getCount() - 1)) + 10;
					    ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
					    lvOption.setLayoutParams(params);
					    
				} else {
					MyToast.showToast(getApplicationContext(), "获取数据失败", 0);
				}
			}
			@Override
			protected List<FactoryOption> doInBackground(Void... params) {
				DoubanApplication myapp = (DoubanApplication) AddTourActivity.this.getApplication();
				String deptId = myapp.currentUser.getDept().getId().toString();

				Map<String, String> argument = new HashMap<String, String>();
				argument.put("deptId", deptId);

				String json = NetUtils.callWs("tourService", "findOptionWs", argument);
				List<FactoryOption> list = JSON.parseObject(json, new TypeReference<List<FactoryOption>>(){});				
				return list;
			}
		}.execute();
	}	
	
	private class TourOptionAdapter extends BaseAdapter {

		private List<FactoryOption> result;

		public TourOptionAdapter(List<FactoryOption> result) {
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
						R.layout.option_item, null);
				holder = new ViewHolder();
				holder.editOption = (EditText) view.findViewById(R.id.editOption);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.editOption.setHint(result.get(position).getName());
			return view;
		}
	}

	static class ViewHolder {
		EditText editOption;
	}		
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSave:
			String year = editYear.getText().toString();
			String month = editMonth.getText().toString();
			String personNum = editNum.getText().toString();
			if (TextUtils.isEmpty(year) || TextUtils.isEmpty(month) || TextUtils.isEmpty(personNum)) {
				MyToast.showToast(getApplicationContext(), "申报年或月或申报人数不能为空", 1);
				return;
			}
			Pattern p = Pattern.compile("\\d*");
			Matcher m = p.matcher(year);
			if (!m.matches() || year.trim().length() != 4) {
				MyToast.showToast(getApplicationContext(), "请输入4位年份", 1);
				return;				
			}
			
			m = p.matcher(month);
			if (!m.matches()) {
				MyToast.showToast(getApplicationContext(), "请输入正确月份", 1);
				return;				
			} 
			if (Integer.parseInt(month) <1 || Integer.parseInt(month) > 12) {
				MyToast.showToast(getApplicationContext(), "请输入正确月份", 1);
				return;				
			}
			
			m = p.matcher(personNum);
			if (!m.matches()) {
				MyToast.showToast(getApplicationContext(), "请输入正确人数", 1);
				return;				
			} 
			
			TourCommon c = new TourCommon();
			
			c.setReportMonth(Integer.parseInt(month));
			c.setReportYear(Integer.parseInt(year));
			c.setTotalPersonNum(Integer.parseInt(personNum));
			
			DoubanApplication myapp = (DoubanApplication) AddTourActivity.this.getApplication();
			c.setUser(myapp.currentUser);
			c.setType(myapp.currentUser.getDept().getDeptType());
			
			for (int i=0; i<lvOption.getCount(); i++) {
				TourDetail d = new TourDetail();
				EditText option = (EditText) lvOption.getChildAt(i).findViewById(R.id.editOption);
				d.setName(option.getHint().toString());
				String money = option.getText().toString();
				
				if (null == money || "".equals(money.trim())) {
					money = "0";
				}
				
				p = Pattern.compile("^[0-9]*(.[0-9]{1,})?$");
				m = p.matcher(money);
				if (!m.matches()) {
					MyToast.showToast(getApplicationContext(), "营业数据必须为数字", 1);
					return;				
				}				
				
				d.setMoney(Double.parseDouble(money));
				c.getDetails().add(d);
			}
			
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter("reportMonth", "reportYear", "totalPersonNum", "user", "id", "details", "name", "money", "type");
			final String json = JSON.toJSONString(c, filter);
			
			new AsyncTask<String, Void, Boolean>() {
				@Override
				protected void onPreExecute() {
					super.onPreExecute();
					showLoading();
				}

				@Override
				protected void onPostExecute(Boolean result) {
					super.onPostExecute(result);
					hideLoading();
					if (result) {
						finish();
						MyToast.showToast(getApplicationContext(), "数据录入成功", 0);
					} else {
						MyToast.showToast(getApplicationContext(), "存在相同月份数据，请选择其他时间录入", 1);
					}
				}

				@Override
				protected Boolean doInBackground(String... params) {
					Map<String, String> argument = new HashMap<String, String>();
					argument.put("json", json);
					String result = NetUtils.callWs("tourService", "addTourWs", argument);
					return Boolean.parseBoolean(result);
				}
			}.execute(json);

			break;
		case R.id.btnCancel:
			finish();
			break;
		}
	}

}
