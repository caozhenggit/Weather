package com.caozheng.weather.module.view;

import com.caozheng.weather.db.CityModel;
import com.caozheng.weather.db.SaveCityModel;
import com.caozheng.xfastmvp.mvp.BaseView;

import io.realm.RealmResults;

/**
 * @author caozheng
 * @date 2017/11/9
 * <p>
 * describe:
 */

public interface ManageCityView extends BaseView {

    void addCityDone();

    void deleteCityDone();

    void selectAllSaveCityDone(RealmResults<SaveCityModel> cityList);

    void selectAllCityDone(RealmResults<CityModel> cityList);

}
