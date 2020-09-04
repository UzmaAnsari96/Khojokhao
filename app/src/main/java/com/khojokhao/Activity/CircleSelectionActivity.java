package com.khojokhao.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CircleSelectionActivity extends Activity {
    public static String RESULT_CIRCLCODE = "circlecode";
    public static String RESULT_CIRCLEID = "circleid";
    public static String RESULT_CONTRYCODE = "countrycode";
    public static String RESULT_OPERATORID = "countryid";
    private ArrayList<Circles> circleList;
    String strOperatorName, strOperatorId, dt;
    //String circleJSON = "{\"result\":[{\"id\":\"1\",\"circle\":\"Andhra Pradesh\",\"circle_code\":\"13\"},{\"id\":\"2\",\"circle\":\"Assam\",\"circle_code\":\"24\"},{\"id\":\"3\",\"circle\":\"Bihar & Jharkhand\",\"circle_code\":\"17\"},{\"id\":\"4\",\"circle\":\"Chennai\",\"circle_code\":\"7\"},{\"id\":\"5\",\"circle\":\"Delhi\",\"circle_code\":\"5\"},{\"id\":\"6\",\"circle\":\"Gujarat\",\"circle_code\":\"12\"},{\"id\":\"7\",\"circle\":\"Haryana\",\"circle_code\":\"20\"},{\"id\":\"8\",\"circle\":\"Himachal Pradesh\",\"circle_code\":\"21\"},{\"id\":\"9\",\"circle\":\"Jammu & Kashmir\",\"circle_code\":\"25\"},{\"id\":\"10\",\"circle\":\"Karnataka\",\"circle_code\":\"9\"},{\"id\":\"11\",\"circle\":\"Kerala\",\"circle_code\":\"14\"},{\"id\":\"12\",\"circle\":\"Kolkata\",\"circle_code\":\"6\"},{\"id\":\"13\",\"circle\":\"Maharashtra\",\"circle_code\":\"4\"},{\"id\":\"14\",\"circle\":\"Madhya Pradesh & Chhattisgarh\",\"circle_code\":\"16\"},{\"id\":\"15\",\"circle\":\"Mumbai\",\"circle_code\":\"3\"},{\"id\":\"16\",\"circle\":\"North East\",\"circle_code\":\"26\"},{\"id\":\"17\",\"circle\":\"Orissa\",\"circle_code\":\"23\"},{\"id\":\"18\",\"circle\":\"Punjab\",\"circle_code\":\"1\"},{\"id\":\"19\",\"circle\":\"Rajasthan\",\"circle_code\":\"18\"},{\"id\":\"20\",\"circle\":\"Tamil Nadu\",\"circle_code\":\"8\"},{\"id\":\"21\",\"circle\":\"Uttar Pradesh - East\",\"circle_code\":\"10\"},{\"id\":\"22\",\"circle\":\"West Bengal\",\"circle_code\":\"2\"},{\"id\":\"23\",\"circle\":\"Uttar Pradesh - West\",\"circle_code\":\"11\"}]}";
    String circleJSON = "{\"circle\":[{\"circle_code\":\"13\",\"circle\":\"Andhra Pradesh\"},{\"circle_code\":\"24\",\"circle\":\"Assam\"},{\"circle_code\":\"17\",\"circle\":\"Bihar & Jharkhand\"},{\"circle_code\":\"7\",\"circle\":\"Chennai\"},{\"circle_code\":\"5\",\"circle\":\"Delhi\"},{\"circle_code\":\"12\",\"circle\":\"Gujarat\"},{\"circle_code\":\"20\",\"circle\":\"Haryana\"},{\"circle_code\":\"21\",\"circle\":\"Himachal Pradesh\"},{\"circle_code\":\"25\",\"circle\":\"Jammu & Kashmir\"},{\"circle_code\":\"9\",\"circle\":\"Karnataka\"},{\"circle_code\":\"14\",\"circle\":\"Kerala\"},{\"circle_code\":\"6\",\"circle\":\"Kolkata\"},{\"circle_code\":\"4\",\"circle\":\"Maharashtra\"},{\"circle_code\":\"16\",\"circle\":\"Madhya Pradesh & Chhattisgarh\"},{\"circle_code\":\"3\",\"circle\":\"Mumbai\"},{\"circle_code\":\"26\",\"circle\":\"North East\"},{\"circle_code\":\"23\",\"circle\":\"Orissa\"},{\"circle_code\":\"1\",\"circle\":\"Punjab\"},{\"circle_code\":\"18\",\"circle\":\"Rajasthan\"},{\"circle_code\":\"8\",\"circle\":\"Tamil Nadu\"},{\"circle_code\":\"10\",\"circle\":\"Uttar Pradesh - East\"},{\"circle_code\":\"2\",\"circle\":\"West Bengal\"},{\"circle_code\":\"11\",\"circle\":\"Uttar Pradesh - West\"}]}";
    //    ArrayAdapter<Circles> adapter;
    MyAdapter adapter;
    EditText txtSearch;
    ListView listViewCircle;
    String myURL, myKEY;

    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    SharedPref sessionManager;
    String uid="";
    TextView amount;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_selection);

        try {
            strOperatorName = getIntent().getExtras().getString("ghs");

            if (strOperatorName.equals("rc")) {
                myURL = "https://khojokhao.com/khojoveg/App_json/state_list";
                myKEY = "circle";
                strOperatorName = getIntent().getExtras().getString("operator");
                strOperatorId = getIntent().getExtras().getString("operatorid");
            } else {
                myURL = "https://khojokhao.com/khojoveg/App_json/state_list";
                myKEY = "operators";
            }

        } catch (Exception e) {
            myURL = "https://khojokhao.com/khojoveg/App_json/state_list";
            myKEY = "operators";
            e.printStackTrace();
        }

        uid="NA";

        circleList = new ArrayList<>();

        //new DownloadJSON().execute();
        new DownloadJSON2().execute();

        txtSearch = (EditText) findViewById(R.id.txtSearch_Circle);
        listViewCircle = (ListView) findViewById(R.id.listCircleSelection);

        adapter = new MyAdapter(CircleSelectionActivity.this, circleList);
        listViewCircle.setAdapter(adapter);
        listViewCircle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Circles c = adapter.getItem(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_CIRCLCODE, c.getName());
                returnIntent.putExtra(RESULT_CONTRYCODE, strOperatorName);
                returnIntent.putExtra(RESULT_OPERATORID, strOperatorId);
                returnIntent.putExtra(RESULT_CIRCLEID, c.getId());
                setResult(RESULT_OK, returnIntent);
                finish();
                /*String ghs=c.getId();
                Intent intent = new Intent(CircleSelectionActivity.this, CircleSelectionActivity2.class);
                intent.putExtra("sid",ghs);
                startActivity(intent);
                finish();*/


            }
        });


        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null) {
                    adapter.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void populateCircleList() {
        circleList = new ArrayList<Circles>();
        try {
            JSONObject jsonObject = new JSONObject(circleJSON);
            JSONArray jsonArray = jsonObject.getJSONArray("circle");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCircle = jsonArray.getJSONObject(i);
                circleList.add(new Circles("1", jsonObjectCircle.getString("circle"), jsonObjectCircle.getString("circle_code")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class MyAdapter extends BaseAdapter implements Filterable {

        //  private ArrayList<Product> mOriginalValues; // Original Values
        private ArrayList<Circles> mOriginalValues; // Original Values
        private ArrayList<Circles> mASd;    // Values to be displayed
        LayoutInflater inflater;

        public MyAdapter(Context context, ArrayList<Circles> mProductArrayList) {

            this.mASd = mProductArrayList;

            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mASd.size();
        }

        @Override
        public Circles getItem(int position) {
            return mASd.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView tvName;
            ImageView imgViw;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.activity_countrycode_row, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.name);
                holder.imgViw = (ImageView) convertView.findViewById(R.id.flag);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Circles obj = mASd.get(position);
            holder.tvName.setText(obj.getName());
            holder.imgViw.setVisibility(View.GONE);

            return convertView;
        }

        @Override
        public Filter getFilter() {

            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    // mDisplayedValues = (ArrayList<Product>) results.values; // has the filtered values
                    mASd = (ArrayList<Circles>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation com values
                    ArrayList<Circles> FilteredArrList = new ArrayList<Circles>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<Circles>(mASd); // saves the original data com mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String data = mOriginalValues.get(i).getName();
                            if (data.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new Circles(mOriginalValues.get(i).getName(), mOriginalValues.get(i).getName(), mOriginalValues.get(i).getId()));
                            }
                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }


    private class DownloadJSON2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            mProgressDialog = new ProgressDialog(CircleSelectionActivity.this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client2 = new OkHttpClient();
            client2 = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("type", "type")
                        .build();

                Request.Builder builder = new Request.Builder();
                builder.url(myURL);
                builder.post(formBody);
                Request request = builder.build();
                Response response = client2.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                CircleSelectionActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(CircleSelectionActivity.this, "Kindly Check Transaction Status", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;


        }

        @Override
        protected void onPostExecute(String result) {
            try {

                mProgressDialog.dismiss();
                arraylist = new ArrayList<HashMap<String, String>>();
                circleList = new ArrayList<Circles>();
                try {
                    JSONObject jsn = new JSONObject(result);
                    JSONArray posts = jsn.optJSONArray("result");
                    for (int i = 0; i < posts.length(); i++) {
                        HashMap<String, String> map = new HashMap<String, String>();
                        JSONObject jsonobject = posts.getJSONObject(i);
                        // Retrive JSON Objects
                        map.put("state_id", jsonobject.getString("state_id"));
                        map.put("state_name", jsonobject.getString("state_name"));
                        circleList.add(new Circles("1", jsonobject.getString("state_name"), jsonobject.getString("state_id")));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new MyAdapter(CircleSelectionActivity.this,circleList);
                listViewCircle.setAdapter(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void bk(View v)
    {
        CircleSelectionActivity.this.finish();
    }

}