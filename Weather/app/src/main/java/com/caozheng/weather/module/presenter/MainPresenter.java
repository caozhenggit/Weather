package com.caozheng.weather.module.presenter;

import android.util.Log;

import com.caozheng.weather.App;
import com.caozheng.weather.bean.CityBean;
import com.caozheng.weather.bean.IpBean;
import com.caozheng.weather.bean.WeatherBean;
import com.caozheng.weather.db.AppRealm;
import com.caozheng.weather.db.CityModel;
import com.caozheng.weather.db.SaveCityModel;
import com.caozheng.weather.module.view.MainView;
import com.caozheng.weather.util.Api;
import com.caozheng.weather.util.Field;
import com.caozheng.xfastmvp.cache.SharedPref;
import com.caozheng.xfastmvp.mvp.BasePresenter;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author caozheng
 * @date 2017/11/6
 * <p>
 * describe:
 */

public class MainPresenter extends BasePresenter<MainView> {

    public MainPresenter(MainView view){
        attachView(view);
    }

    /** 同步城市列表 */
    public void syncCity(){
        Realm mRealm = AppRealm.getInstance().getRealm();
        RealmResults<CityModel> cityList = mRealm.where(CityModel.class).findAll();

        if(cityList.size() == 0){
            OkGo.<String>get(Api.WEATHER_API_CITY)
                    .headers(Field.FIELD_AUTHORIZATION, Field.FIELD_APPCODE + " " + Api.APP_CODE)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Gson gson = new Gson();
                            CityBean cityBean = gson.fromJson(response.body(), CityBean.class);

                            if(cityBean.getStatus() == 0){
                                insertCityToDb(cityBean.getResult());

                                mView.syncCityDone();
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);

                            mView.syncCityDone();
                        }
                    });
        }else {
            mView.syncCityDone();
        }
    }

    /** 获取当前所在城市 */
    public void getLocalCity(){
        OkGo.<String>get(Api.WEATHER_API_GET_IP)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        String data = body.substring(body.indexOf("{"), body.indexOf("}") + 1);

                        IpBean bean = new Gson().fromJson(data, IpBean.class);
                        getLocalCityName(bean);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        mView.getLocalCityDone();
                    }
                });
    }

    private void getLocalCityName(IpBean bean){
        Map<String, String> querys = new HashMap<String, String>();
        querys.put(Field.FIELD_IP, bean.getCip());
        querys.put(Field.FIELD_CITY, bean.getCname());

        OkGo.<String>get(Api.WEATHER_API_QUERY)
                .headers(Field.FIELD_AUTHORIZATION, Field.FIELD_APPCODE + " " + Api.APP_CODE)
                .params(querys, false)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        WeatherBean weatherBean = gson.fromJson(response.body(), WeatherBean.class);

                        if(weatherBean.getStatus() == 0){
                            insertLocalCityToDb(weatherBean.getResult());

                            SharedPref.getInstance(App.getAppContext())
                                    .putString(weatherBean.getResult().getCitycode(), response.body());
                        }else {
                            Log.i("获取当前城市天气失败", weatherBean.getMsg());

                            insertDefaultCityToDb();
                        }

                        mView.getLocalCityDone();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        mView.getLocalCityDone();
                    }
                });
    }

    private void insertCityToDb(List<CityBean.ResultBean> list){
        Realm mRealm = AppRealm.getInstance().getRealm();

        mRealm.beginTransaction();

        for (CityBean.ResultBean bean : list) {
            CityModel cityModel = mRealm.createObject(CityModel.class);

            cityModel.setCityId(bean.getCityid());
            cityModel.setCity(bean.getCity());
            cityModel.setCityCode(bean.getCitycode());
            cityModel.setParentId(bean.getParentid());
        }

        mRealm.commitTransaction();
    }
    
    private void insertLocalCityToDb(WeatherBean.ResultBean bean){
        Realm mRealm = AppRealm.getInstance().getRealm();

        RealmResults<SaveCityModel> cityList = mRealm.where(SaveCityModel.class).findAll();
        for (int i = 0; i < cityList.size(); i++) {
            SaveCityModel saveCityModel = cityList.get(i);
            if(saveCityModel.getCityId().equals(bean.getCityid())){
                mRealm.beginTransaction();
                cityList.deleteFromRealm(i);
                mRealm.commitTransaction();
            }
        }

        mRealm.beginTransaction();

        SaveCityModel cityModel = mRealm.createObject(SaveCityModel.class);

        cityModel.setCityId(bean.getCityid());
        cityModel.setCity(bean.getCity());
        cityModel.setCityCode(bean.getCitycode());
        cityModel.setType(1);

        mRealm.commitTransaction();
    }

    private void insertDefaultCityToDb(){
        Realm mRealm = AppRealm.getInstance().getRealm();

        RealmResults<SaveCityModel> cityList = mRealm.where(SaveCityModel.class).findAll();
        if(cityList.size() == 0){
            mRealm.beginTransaction();

            SaveCityModel cityModel = mRealm.createObject(SaveCityModel.class);

            cityModel.setCityId("1");
            cityModel.setCity("北京");
            cityModel.setCityCode("101010100");
            cityModel.setType(0);

            mRealm.commitTransaction();
        }
    }
}
