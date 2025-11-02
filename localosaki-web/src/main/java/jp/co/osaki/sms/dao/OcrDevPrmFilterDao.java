package jp.co.osaki.sms.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.access.filter.dao.CorpPersonAuthDao;
import jp.co.osaki.osol.access.filter.dao.DevPrmDataFilterDao;
import jp.co.osaki.osol.access.filter.param.BuildingPersonDevDataParam;
import jp.co.osaki.osol.access.filter.param.CorpPersonAuthParam;
import jp.co.osaki.osol.access.filter.resultset.CorpPersonAuthResultSet;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MPerson;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.resultset.MDevPrmListResultSet;
import jp.co.osaki.sms.servicedao.MDevPrmListServiceDaoImpl;

@Stateless
public class OcrDevPrmFilterDao extends SmsDao {

    private final MDevPrmListServiceDaoImpl mDevPrmListServiceDaoImpl;

    /**
     * 装置フィルター
     */
    @EJB
    private DevPrmDataFilterDao devPrmDataFilterDao;

    /**
     * 権限フィルター
     */
    @EJB
    private CorpPersonAuthDao corpPersonAuthDao;

    private final String ocrAuthCd = "013";

    public OcrDevPrmFilterDao() {
        mDevPrmListServiceDaoImpl = new MDevPrmListServiceDaoImpl();
    }

    /**
     * 装置情報検索.
     * OCR権限があればOCR装置のみを返却
     * OCR権限がなければOCR装置以外を返却する
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @return 装置情報List
     */
    public List<MDevPrm> getDevPrmList(String corpId, Long buildingId, CorpPersonAuthParam corpPersonAuthParam) {
        return getDevPrmList(corpId, buildingId, corpPersonAuthParam, false);
    }

    /**
     * 装置情報検索.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報List
     */
    public List<MDevPrm> getDevPrmList(String corpId, Long buildingId,  CorpPersonAuthParam corpPersonAuthParam, boolean isTenant) {
        MDevPrmListResultSet dbParam = new MDevPrmListResultSet();
        dbParam.setCorpId(corpId);
        dbParam.setBuildingId(buildingId);
        dbParam.setTenant(isTenant);

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(MDevPrmListServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));

        List<MDevPrm> devList = getResultList(mDevPrmListServiceDaoImpl, parameterMap);

        // ログイン担当者情報取得
        MPerson person = (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());

        // ログインユーザーの企業情報を取得
        String corpType = person.getMCorp().getCorpType();

        // 大崎権限の場合は絞り込まない
        if(corpType.equals("0")) {
            return devList;
        }

        // 担当者が権限のある装置に絞り込む
        devList = devPrmDataFilterDao.applyDataFilter(devList, new BuildingPersonDevDataParam(
                corpId, buildingId, person.getId().getCorpId(), person.getId().getPersonId()));

        List<CorpPersonAuthResultSet> corpPersonAuthList = corpPersonAuthDao.getCorpPersonAuth(corpPersonAuthParam);
        List<MDevPrm> ocrFilterDevList = new ArrayList<>();
        boolean ocrAuth = false;
        for(CorpPersonAuthResultSet auth : corpPersonAuthList) {
            if(auth.getOperationAuthorityCd().equals(ocrAuthCd) && auth.getOperationAuthorityFlg().equals(OsolConstants.FLG_ON)) {
                ocrAuth = true;
            }
        }

        ocrFilterDevList = addOcrFilterDevList(devList, ocrAuth);

        return ocrFilterDevList;
    }

    /**
     * OCR装置以外の情報検索.
     *
     * @param corpId 企業ID
     * @param buildingId 建物ID
     * @param isTenant テナントフラグ
     * @return 装置情報List
     */
    public List<MDevPrm> getNotOcrDevPrmList(String corpId, Long buildingId,  CorpPersonAuthParam corpPersonAuthParam, boolean isTenant) {
        MDevPrmListResultSet dbParam = new MDevPrmListResultSet();
        dbParam.setCorpId(corpId);
        dbParam.setBuildingId(buildingId);
        dbParam.setTenant(isTenant);

        Map<String, List<Object>> parameterMap = new HashMap<>();
        parameterMap.put(MDevPrmListServiceDaoImpl.PARAM_KEY, new ArrayList<>(Arrays.asList(dbParam)));

        List<MDevPrm> devList = getResultList(mDevPrmListServiceDaoImpl, parameterMap);

        // ログイン担当者情報取得
        MPerson person = (MPerson) getSessionParameter(OsolConstants.LOGIN_USER_SESSION_KEY.PERSON.getVal());

        // 担当者が権限のある装置に絞り込む
        devList = devPrmDataFilterDao.applyDataFilter(devList, new BuildingPersonDevDataParam(
                corpId, buildingId, person.getId().getCorpId(), person.getId().getPersonId()));

        List<MDevPrm> ocrFilterDevList = new ArrayList<>();
        boolean ocrAuth = false;

        ocrFilterDevList = addOcrFilterDevList(devList, ocrAuth);

        return ocrFilterDevList;
    }


    private List<MDevPrm> addOcrFilterDevList(List<MDevPrm> devList, boolean ocrAuth){
        List<MDevPrm> ocrDevList = new ArrayList<>();
        if(ocrAuth) {
            for(MDevPrm devPrm : devList) {
                if(devPrm.getDevId().startsWith("OC")) {
                    ocrDevList.add(devPrm);
                }
            }
        }else {
            for(MDevPrm devPrm : devList) {
                if(!devPrm.getDevId().startsWith("OC")) {
                    ocrDevList.add(devPrm);
                }
            }
        }
        return ocrDevList;
    }

    /**
     * 装置情報取得.
     *
     * @param devId 装置ID
     * @return 装置情報
     */
    public MDevPrm getDevPrm(String devId) {
        MDevPrm dbParam = new MDevPrm();
        dbParam.setDevId(devId);

        return find(mDevPrmListServiceDaoImpl, dbParam);
    }

}
