package cn.itcast.douban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.itcast.douban.common.StatusEnum;
import cn.itcast.douban.domain.FactoryOption;
import cn.itcast.douban.domain.PageResult;
import cn.itcast.douban.domain.TourCommon;
import cn.itcast.douban.domain.TourDetail;
import cn.itcast.douban.domain.User;
import cn.itcast.douban.util.MyToast;
import cn.itcast.douban.util.NetUtils;

public class ViewTourActivity extends BaseActivity implements OnClickListener {

	private TextView txtDate;
	private TextView txtNumValue;
	private TextView txtMoneyValue;
	private TextView txtOptionLabel;
	private TextView txtOptionValue;
	private ListView lvOption;
	private Button btnCancel;
	
	private TourCommon tourCommon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.view_tour);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void findView() {
		lvOption = (ListView) this.findViewById(R.id.lvOption);
		txtDate = (TextView) findViewById(R.id.txtDate);
		txtNumValue = (TextView) findViewById(R.id.txtNumValue);
		txtMoneyValue = (TextView) findViewById(R.id.txtMoneyValue);
		btnCancel = (Button) this.findViewById(R.id.btnCancel);
	}

	@Override
	protected void setUpView() {
		btnCancel.setOnClickListener(this);
		fillData();
	}

	private void fillData() {
		String json = this.getIntent().getStringExtra("json");
		tourCommon = JSON.parseObject(json, TourCommon.class);
		txtDate.setText(tourCommon.getReportYear() + "年" 
				+ tourCommon.getReportMonth() + "月");
		txtNumValue.setText(tourCommon.getTotalPersonNum().toString());
		txtMoneyValue.setText(tourCommon.getTotalIncome().toString());
		
		lvOption.setAdapter(new TourOptionAdapter(tourCommon.getDetails()));

		// 设置lvOption自动适应高度
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
		params.height = totalHeight
				+ (lvOption.getDividerHeight() * (listAdapter.getCount() - 1)) + 10;
		((MarginLayoutParams) params).setMargins(10, 10, 10, 10);
		lvOption.setLayoutParams(params);

	}

	private class TourOptionAdapter extends BaseAdapter {

		private List<TourDetail> result;

		public TourOptionAdapter(List<TourDetail> result) {
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
						R.layout.option_item_view, null);
				holder = new ViewHolder();
				holder.txtOptionLabel = (TextView) view
						.findViewById(R.id.txtOptionLabel);
				holder.txtOptionValue = (TextView) view
						.findViewById(R.id.txtOptionValue);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.txtOptionLabel.setText(result.get(position).getName());
			holder.txtOptionValue.setText(result.get(position).getMoney().toString());
			return view;
		}
	}

	static class ViewHolder {
		TextView txtOptionLabel;
		TextView txtOptionValue;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCancel:
			finish();
			break;
		}
	}

}
