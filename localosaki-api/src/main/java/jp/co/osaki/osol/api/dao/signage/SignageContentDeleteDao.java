package jp.co.osaki.osol.api.dao.signage;

import java.sql.Timestamp;

import javax.ejb.Stateless;
import javax.persistence.OptimisticLockException;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.parameter.signage.SignageContentDeleteParameter;
import jp.co.osaki.osol.api.result.signage.SignageContentDeleteResult;
import jp.co.osaki.osol.api.servicedao.entity.TSignageContentServiceDaoImpl;
import jp.co.osaki.osol.entity.TSignageContent;
import jp.co.osaki.osol.entity.TSignageContentPK;

/**
 * サイネージコンテンツ削除 Beanクラス
 *
 * @author d-komatsubara
 *
 */
@Stateless
public class SignageContentDeleteDao extends OsolApiDao<SignageContentDeleteParameter> {

    private final TSignageContentServiceDaoImpl tSignageContentServiceDaoImpl;

    public SignageContentDeleteDao() {
        tSignageContentServiceDaoImpl = new TSignageContentServiceDaoImpl();
    }

    @Override
    public SignageContentDeleteResult query(SignageContentDeleteParameter parameter) throws Exception {

        SignageContentDeleteResult result = new SignageContentDeleteResult();
        //登録時刻、更新時刻にセットする時刻を設定する
        Timestamp serverDateTime = getServerDateTime();

        //ログインユーザーIDを取得
        Long loginUserId = getMPerson(parameter.getLoginCorpId(), parameter.getLoginPersonId()).getUserId();

        //サイネージコンテンツの排他チェック
        TSignageContent selectParam = new TSignageContent();
        TSignageContentPK pkSelectParam = new TSignageContentPK();
        pkSelectParam.setCorpId(parameter.getOperationCorpId());
        pkSelectParam.setBuildingId(parameter.getBuildingId());
        pkSelectParam.setSignageContentsId(parameter.getSignageContentId());
        selectParam.setId(pkSelectParam);
        TSignageContent updateContent = find(tSignageContentServiceDaoImpl, selectParam);
        if (updateContent == null || !updateContent.getVersion().equals(parameter.getVersion())) {
            //対象のデータがないまたはversionが異なる場合、排他エラー
            throw new OptimisticLockException();
        }

        updateContent.setDelFlg(OsolConstants.FLG_ON);
        updateContent.setUpdateDate(serverDateTime);
        updateContent.setUpdateUserId(loginUserId);

        //更新後のデータを取得する
        TSignageContent newData = find(tSignageContentServiceDaoImpl, updateContent);
        result.setCorpId(newData.getId().getCorpId());
        result.setBuildingId(newData.getId().getBuildingId());
        result.setSignageContentId(newData.getId().getSignageContentsId());
        result.setDelFlg(newData.getDelFlg());
        result.setVersion(newData.getVersion());

        return result;
    }

}
