package jp.co.osaki.sms.dao;

import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MChildGroupPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MChildGroupServiceDaoImpl;

/**
 *
 * @author n-takada
 */
@Stateless
public class MChildGroupDao extends SmsDao {

    private final MChildGroupServiceDaoImpl daoImpl;

    public MChildGroupDao() {
        daoImpl = new MChildGroupServiceDaoImpl();
    }

    /**
     *
     * 子グループID採番
     *
     * @return 新規採番された子グループID
     */
    public Long createChildGroupId() {

        return super.createId(OsolConstants.ID_SEQUENCE_NAME.CHIID_GROUP_ID.getVal());

    }

    /**
     * 企業Id 親グループにひもづくグループIDを取得
     *
     * @param corpId 企業Id
     * @param parentGroupId 親グループId
     * @return
     */
    public List<MChildGroup> getGroupList(String corpId, long parentGroupId) {

        MChildGroup target = new MChildGroup();
        MChildGroupPK id = new MChildGroupPK();
        id.setCorpId(corpId);
        id.setParentGroupId(parentGroupId);
        target.setId(id);
        target.setDelFlg(OsolConstants.FLG_OFF);

        return getResultList(daoImpl, target);
    }

    /**
     * 企業IdにひもづくグループIDを取得
     *
     * @param corpId 企業Id
     * @return
     */
    public List<MChildGroup> getGroupList(String corpId) {

        MChildGroup target = new MChildGroup();
        MChildGroupPK id = new MChildGroupPK();
        id.setCorpId(corpId);
        target.setId(id);
        target.setDelFlg(0);
        return getResultList(daoImpl, target);
    }

    public List<MChildGroup> getResultList(MChildGroup mChildGroup) {
        return getResultList(daoImpl, mChildGroup);
    }

    /**
     * <p>
     * 子グループ 新規登録
     * </p>
     *
     * @param target
     */
    public void persist(MChildGroup target) {
        persist(daoImpl, target);
    }

    /**
     * <p>
     * 子グループ 更新処理
     * </p>
     *
     * @param target 子グループ
     * @return 更新結果()
     */
    public MChildGroup merge(MChildGroup target) {
        MChildGroup result = merge(daoImpl, target);
        return result;
    }

    /**
     * 1件のみレコード取得する
     *
     * @param entity
     * @return
     */
    public MChildGroup find(MChildGroup entity) {
        return find(daoImpl, entity);
    }

}
