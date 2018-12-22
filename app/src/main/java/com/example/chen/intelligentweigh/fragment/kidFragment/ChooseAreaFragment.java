package com.example.chen.intelligentweigh.fragment.kidFragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chen.intelligentweigh.BaseFragment;
import com.example.chen.intelligentweigh.R;
import com.example.chen.intelligentweigh.activity.NewCowActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseAreaActivity;
import com.example.chen.intelligentweigh.activity.kidActivity.ChooseHouseActivity;
import com.example.chen.intelligentweigh.bean.Area;
import com.example.chen.intelligentweigh.bean.House;
import com.example.chen.intelligentweigh.bean.NewCow;
import com.example.chen.intelligentweigh.bean.User;
import com.example.chen.intelligentweigh.fragment.NewCowFragment;
import com.example.chen.intelligentweigh.util.HttpUrlUtils;
import com.example.chen.intelligentweigh.util.SharedUtils;
import com.example.chen.intelligentweigh.util.TitleBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.litepal.LitePal;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chen
 * @date 2018/12/12.   21:16
 * description：
 */
public class ChooseAreaFragment extends BaseFragment {

    private ListView lv_choose_area;
    private String TAG = "ChooseAreaFragment";
    private String id ;
    private String framids;
    private String name;
    private NewCow cow;
    private List<String> listArea = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area_frag, container, false);
        initView(view);
        return view;
    }

    public static ChooseAreaFragment newInstances(String id,String name,NewCow cow){
        ChooseAreaFragment fragment = new ChooseAreaFragment();
        Bundle bundle = new Bundle();
        bundle.putString("houseName",name);
        bundle.putString("houseId",id);
        bundle.putSerializable("cowinfo",cow);
        fragment.setArguments(bundle);
        return fragment;
    }

    private void initView(View view) {
        lv_choose_area = (ListView) view.findViewById(R.id.lv_choose_area);
        initFragmentUI(view);
        initActivityUI(view);
    }

    /**
     * ActivityUI
     * @param view
     */
    private void initActivityUI(View view) {
        if(name!=null&&id!=null){
            new TitleBuilder(view).setTitleText("选择"+name+"分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
            initAreaList(id);
            AreaClickItemByActivity();

        }
    }

    private void AreaClickItemByActivity() {
        lv_choose_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listArea.get(position);
                cow.setHouseName(s);
                Intent intent = new Intent(getActivity(), NewCowActivity.class);
                intent.putExtra("cowInfo",cow);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /**
     * fragmentUI
     * @param view
     */
    private void initFragmentUI(View view) {
        if (getArguments() != null) {
            String houseName = getArguments().getString("houseName");
            String houseId = getArguments().getString("houseId");
            Log.e(TAG,"牧场名字"+houseName+"牧场ID"+houseId);
            new TitleBuilder(view).setTitleText("选择"+houseName+"分区").setLeftImage(R.drawable.arrowleft).setLeftOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getActivity()!=null) {
                        String phone = SharedUtils.getPhone(getActivity());
                        if (!phone.isEmpty()) {
                            List<User> users = LitePal.where("phone = ?", phone).find(User.class);
                            if (!users.isEmpty()) {
                                framids = users.get(0).getFarmids();
                            }
                        }
                        if (framids != null) {
                            ChooseHouseFragment fragment = ChooseHouseFragment.newInstance(framids,(NewCow) getArguments().getSerializable("cowinfo"));
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.other_content_frag, fragment);
                            ft.commit();
                        }
                    }


                }
            });
            initAreaList(houseId);
            AreaClickItem();
        }
    }

    /**
     * listView Item点击事件
     */
    private void AreaClickItem() {
        lv_choose_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listArea.get(position);
                NewCow cow = (NewCow)getArguments().getSerializable("cowinfo");
                cow.setHouseName(s);
                NewCowFragment fragment = NewCowFragment.newInstances(cow);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.other_content_frag,fragment).commit();
            }
        });
    }


    /**
     * 展示分区listview
     * @param id
     */
    private void initAreaList(String id) {
        OkHttpUtils.get()
                .url(HttpUrlUtils.ALL_HOUSE_AREA_URL)
                .addParams("farmid",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Request request, Exception e) {
                        Toast.makeText(getActivity(),"请检查网络连接",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        if("error".equals(response.toString())){
                            Toast.makeText(getActivity(),"数据出错",Toast.LENGTH_SHORT).show();
                        }else{
                            Type type = new TypeToken<List<Area>>(){}.getType();
                            List<Area> list = new Gson().fromJson(response, type);
                            for(Area area:list){
                                listArea.add(area.getArea());
                            }
                            Log.e(TAG,"activity list"+listArea);
                            ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,listArea);
                            lv_choose_area.setAdapter(adapter);

                        }
                    }
                });
    }



    @TargetApi(23)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    /*
     * Deprecated on API 23
     * Use onAttachToContext instead
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachToContext(activity);
        }
    }

    /*
     * Called when the fragment attaches to the context
     */
    protected void onAttachToContext(Context context) {
        if(context instanceof ChooseAreaActivity) {
             id = ((ChooseAreaActivity) context).setIdData();
             name = ((ChooseAreaActivity) context).setNameData();
             cow = ((ChooseAreaActivity) context).setCowData();
             Log.e(TAG,"id"+id+"name"+name);
        }
    }


}
