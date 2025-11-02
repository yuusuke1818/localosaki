/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.dao.signage;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentWriteParameter;
import jp.co.osaki.osol.api.request.signage.SignageContentWriteRequest;
import jp.co.osaki.osol.api.result.signage.SignageContentWriteResult;
import jp.co.osaki.osol.api.resultdata.signage.SignageApkFileListDetailResultData;
import jp.co.osaki.osol.api.servicedao.entity.TSignageContentServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.signage.SignageContentListServiceDaoImpl;
import jp.co.osaki.osol.entity.TSignageContent;
import jp.co.osaki.osol.entity.TSignageContentPK;

/**
 * サイネージコンテンツ書き込み Daoクラス
 *
 * @author n-takada
 */
@Stateless
public class SignageContentWriteDao extends OsolApiDao<SignageContentWriteParameter> {

    private final TSignageContentServiceDaoImpl tSignageContentServiceDaoImpl;
    private final SignageContentListServiceDaoImpl signageContentServiceDaoImpl;

    public SignageContentWriteDao() {
        tSignageContentServiceDaoImpl = new TSignageContentServiceDaoImpl();
        signageContentServiceDaoImpl = new SignageContentListServiceDaoImpl();
    }

    @Override
    public SignageContentWriteResult query(SignageContentWriteParameter parameter) throws Exception {
        SignageContentWriteResult result = new SignageContentWriteResult();
        List<TSignageContent> updateList = new ArrayList<>();
        Boolean newFlg;

        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //JSON⇒Resultに変換
        SignageContentWriteRequest resultSet = parameter.getSignageContentResultSet();

        if (resultSet == null || resultSet.getSignageContentList() == null
                || resultSet.getSignageContentList().isEmpty()) {
            return new SignageContentWriteResult();
        }

        //サイネージコンテンツの排他チェック
        for (SignageApkFileListDetailResultData detail : resultSet.getSignageContentList()) {
            if (detail.getSignageContentsId() == null) {
                updateList.add(null);
            } else {
                TSignageContent exParam = new TSignageContent();
                TSignageContentPK pkExParam = new TSignageContentPK();
                pkExParam.setCorpId(detail.getCorpId());
                pkExParam.setBuildingId(detail.getBuildingId());
                pkExParam.setSignageContentsId(detail.getSignageContentsId());
                exParam.setId(pkExParam);
                TSignageContent updateData = find(tSignageContentServiceDaoImpl, exParam);
                if (updateData == null) {
                    //対象データがnullの場合は、新規登録とみなす（コンテンツIDを先に取得してから登録するケースがある）
                    updateList.add(null);
                } else if (!updateData.getVersion().equals(detail.getVersion())) {
                    //versionが異なる場合は排他エラー
                    throw new OptimisticLockException();
                }
                updateList.add(updateData);
            }
        }

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //サイネージコンテンツの更新
        int i = 0;
        for (SignageApkFileListDetailResultData detail : resultSet.getSignageContentList()) {
            TSignageContent updateData = new TSignageContent();
            if (updateList.get(i) == null) {
                //新規登録
                newFlg = Boolean.TRUE;
                TSignageContentPK pkUpdateData = new TSignageContentPK();
                pkUpdateData.setCorpId(detail.getCorpId());
                pkUpdateData.setBuildingId(detail.getBuildingId());
                pkUpdateData
                        .setSignageContentsId(createId(OsolConstants.ID_SEQUENCE_NAME.SIGNAGE_CONTENTS_ID.getVal()));
                updateData.setId(pkUpdateData);
                updateData.setCreateDate(serverDateTime);
                updateData.setCreateUserId(loginUserId);
            } else {
                //更新
                updateData = updateList.get(i);
                newFlg = Boolean.FALSE;
            }

            updateData.setSignageContentsType(detail.getSignageContentsType());
            updateData.setDisplayStartTime(new Time(detail.getDisplayStartTime().getTime()));
            updateData.setDisplayEndTime(new Time(detail.getDisplayEndTime().getTime()));
            updateData.setDisplayOrder(detail.getDisplayOrder());
            updateData.setSpecifySunday(detail.getSpecifySunday());
            updateData.setSpecifyMonday(detail.getSpecifyMonday());
            updateData.setSpecifyTuesday(detail.getSpecifyTuesday());
            updateData.setSpecifyWednesday(detail.getSpecifyWednesday());
            updateData.setSpecifyThursday(detail.getSpecifyThursday());
            updateData.setSpecifyFriday(detail.getSpecifyFriday());
            updateData.setSpecifySaturday(detail.getSpecifySaturday());
            updateData.setTitle(detail.getTitle());
            updateData.setMessage(detail.getMessage());
            updateData.setFontSize(detail.getFontSize());
            updateData.setFontColorCode(detail.getFontColorCode());
            updateData.setImageFilePath(detail.getImageFilePath());
            updateData.setImageFileName(detail.getImageFileName());
            updateData.setDelFlg(detail.getDelFlg());
            updateData.setVersion(detail.getVersion());
            updateData.setUpdateUserId(loginUserId);
            updateData.setUpdateDate(serverDateTime);

            if (newFlg) {
                persist(tSignageContentServiceDaoImpl, updateData);
            } else {
                merge(tSignageContentServiceDaoImpl, updateData);
            }

            i++;
        }

        //更新後の最新情報を取得する
        SignageApkFileListDetailResultData newDataParam = new SignageApkFileListDetailResultData();
        newDataParam.setCorpId(parameter.getOperationCorpId());
        newDataParam.setBuildingId(parameter.getBuildingId());
        newDataParam.setSignageContentsType(resultSet.getSignageContentList().get(0).getSignageContentsType());

        List<SignageApkFileListDetailResultData> list = getResultList(signageContentServiceDaoImpl, newDataParam);
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
