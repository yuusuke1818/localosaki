/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.master;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.master.CommonWeatherCityParameter;
import jp.co.osaki.osol.api.result.master.CommonWeatherCityResult;
import jp.co.osaki.osol.api.resultdata.master.CommonWeatherCityDetailResultData;
import jp.co.osaki.osol.api.servicedao.master.CommonWeatherCityServiceDaoImpl;

/**
 * 天気情報市町村マスタ検索 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class CommonWeatherCityDao extends OsolApiDao<CommonWeatherCityParameter> {

    private final CommonWeatherCityServiceDaoImpl commonWeatherCityServiceDaoImpl;

    public CommonWeatherCityDao() {
        commonWeatherCityServiceDaoImpl = new CommonWeatherCityServiceDaoImpl();
    }

    @Override
    public CommonWeatherCityResult query(CommonWeatherCityParameter parameter) throws Exception {
        CommonWeatherCityResult result = new CommonWeatherCityResult();
        CommonWeatherCityDetailResultData searchResultSet = new CommonWeatherCityDetailResultData();

        if (parameter.getCommonWeatherParameter() != null
                && parameter.getCommonWeatherParameter().getPrefectureCdList() != null
                && !parameter.getCommonWeatherParameter().getPrefectureCdList().isEmpty()) {
            searchResultSet.setPrefectureCdList(parameter.getCommonWeatherParameter().getPrefectureCdList());
        }
        searchResultSet.setCityName(parameter.getCityName());
        List<CommonWeatherCityDetailResultData> list = getResultList(commonWeatherCityServiceDaoImpl, searchResultSet);
        result.setDetailList(list);
        return result;
    }

}
