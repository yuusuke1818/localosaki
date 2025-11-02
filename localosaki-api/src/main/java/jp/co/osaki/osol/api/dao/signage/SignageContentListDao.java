/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.signage;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentListParameter;
import jp.co.osaki.osol.api.result.signage.SignageContentListResult;
import jp.co.osaki.osol.api.resultdata.signage.SignageApkFileListDetailResultData;
import jp.co.osaki.osol.api.servicedao.signage.SignageContentListServiceDaoImpl;

/**
 * サイネージコンテンツ一覧取得 Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class SignageContentListDao extends OsolApiDao<SignageContentListParameter> {

    private final SignageContentListServiceDaoImpl signageContentServiceDaoImpl;

    public SignageContentListDao() {
        signageContentServiceDaoImpl = new SignageContentListServiceDaoImpl();
    }

    @Override
    public SignageContentListResult query(SignageContentListParameter parameter) throws Exception {
        SignageContentListResult result = new SignageContentListResult();

        SignageApkFileListDetailResultData resultSet = new SignageApkFileListDetailResultData();
        resultSet.setCorpId(parameter.getOperationCorpId());
        resultSet.setBuildingId(parameter.getBuildingId());
        resultSet.setSignageContentsType(parameter.getSignageContentsType());

        List<SignageApkFileListDetailResultData> list = getResultList(signageContentServiceDaoImpl, resultSet);
        if (list == null || list.isEmpty()) {
            result.setSignageContentList(null);
            return result;
        }
        for (SignageApkFileListDetailResultData dbResult : list) {
            dbResult.setDisplayStartTime(conversionTime(dbResult.getDisplayStartTime()));
            dbResult.setDisplayEndTime(conversionTime(dbResult.getDisplayEndTime()));
        }

        result.setSignageContentList(list);
        return result;
    }

    /**
     * 文字列→時間（Time型）変換
     *
     * @param hour
     * @param minute
     * @return
     */
    private Date conversionTime(Date time) throws Exception {
        return new Date(time.getTime());
    }

}
