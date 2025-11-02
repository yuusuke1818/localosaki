package jp.co.osaki.osol.api.dao.signage;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.signage.SignageApkFileListParameter;
import jp.co.osaki.osol.api.result.signage.SignageApkFileListResult;
import jp.co.osaki.osol.api.resultdata.signage.SignageApkFileListDetailResultData;
import jp.co.osaki.osol.api.servicedao.signage.SignageContentListServiceDaoImpl;

/**
 * サイネージファイル取得 Daoクラス
 *
 * @author d-komatsubara
 */
@Stateless
public class SignageApkFileListDao extends OsolApiDao<SignageApkFileListParameter> {

    private final SignageContentListServiceDaoImpl signageContentServiceDaoImpl;

    public SignageApkFileListDao() {
        signageContentServiceDaoImpl = new SignageContentListServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SignageApkFileListResult query(SignageApkFileListParameter parameter) throws Exception {
        SignageApkFileListResult result = new SignageApkFileListResult();

        List<SignageApkFileListDetailResultData> advertiseList = getResultSetList(parameter.getOperationCorpId(),
                parameter.getBuildingId(),
                ApiGenericTypeConstants.SIGNAGE_CONTENTS_TYPE.ADVERTISE.getVal());

        List<SignageApkFileListDetailResultData> businessReportList = getResultSetList(parameter.getOperationCorpId(),
                parameter.getBuildingId(), ApiGenericTypeConstants.SIGNAGE_CONTENTS_TYPE.REPORT.getVal());

        result.setAdvertiseList(advertiseList);
        result.setBusinessReportList(businessReportList);
        return result;
    }

    private List<SignageApkFileListDetailResultData> getResultSetList(
            String corpId, long buildingId, String signageContentsType) throws Exception {
        SignageApkFileListDetailResultData resultSet = new SignageApkFileListDetailResultData();
        resultSet.setCorpId(corpId);
        resultSet.setBuildingId(buildingId);
        resultSet.setSignageContentsType(signageContentsType);

        List<SignageApkFileListDetailResultData> list = getResultList(signageContentServiceDaoImpl, resultSet);
        if (list == null) {
            return list;
        }
        for (SignageApkFileListDetailResultData dbResult : list) {
            dbResult.setDisplayStartTime(conversionTime(dbResult.getDisplayStartTime()));
            dbResult.setDisplayEndTime(conversionTime(dbResult.getDisplayEndTime()));
        }
        return list;
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
