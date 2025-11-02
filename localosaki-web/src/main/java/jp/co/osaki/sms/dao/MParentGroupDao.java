package jp.co.osaki.sms.dao;

import java.util.List;
import java.util.Objects;

import javax.ejb.Stateless;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.entity.MChildGroup;
import jp.co.osaki.osol.entity.MParentGroup;
import jp.co.osaki.osol.entity.MParentGroupPK;
import jp.co.osaki.osol.entity.TBuildingGroup;
import jp.co.osaki.osol.entity.TBuildingGroupPK;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MChildGroupServiceDaoImpl;
import jp.co.osaki.sms.servicedao.MParentGroupServiceDaoImpl;
import jp.co.osaki.sms.servicedao.TBuildingGroupEditServiceDaoImpl;

/**
 *
 * @author n-takada
 */
@Stateless
public class MParentGroupDao extends SmsDao {

    private final MParentGroupServiceDaoImpl pearentDaoImpl;
    private final MChildGroupServiceDaoImpl childDaoImpl;
    private final TBuildingGroupEditServiceDaoImpl buildingGroupDaoImpl;

    public MParentGroupDao() {
        pearentDaoImpl = new MParentGroupServiceDaoImpl();
        childDaoImpl = new MChildGroupServiceDaoImpl();
        buildingGroupDaoImpl = new TBuildingGroupEditServiceDaoImpl();
    }

    /**
     *
     * 親グループID採番
     *
     * @return 新規採番された親グループID
     */
    public Long createParentGroupId() {

        return super.createId(OsolConstants.ID_SEQUENCE_NAME.PARENT_GROUP_ID.getVal());

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

     public Long createParentGroupIdForDisp() {
         return createParentGroupId();
     }

     private void deleteBuildingGroup(String corpId, Long parentGroup, Long childGroup){
         TBuildingGroup target = new TBuildingGroup();
         TBuildingGroupPK id = new TBuildingGroupPK();
         target.setId(id);
         id.setCorpId(corpId);
         List<TBuildingGroup> buildingGroup = getResultList(buildingGroupDaoImpl, target);
         for(TBuildingGroup grp : buildingGroup){
             if(Objects.equals(grp.getId().getParentGroupId(), parentGroup)){
                 if(childGroup == -1){  //親グループの削除
                     if(Objects.equals(grp.getDelFlg(), OsolConstants.FLG_OFF)){
                        grp.setDelFlg(OsolConstants.FLG_ON);
                        merge(buildingGroupDaoImpl, grp);
                     }
                 }else{
                     if(Objects.equals(grp.getId().getChildGroupId(), childGroup)){
                         grp.setDelFlg(OsolConstants.FLG_ON);
                        if(Objects.equals(grp.getDelFlg(), OsolConstants.FLG_OFF)){
                           grp.setDelFlg(OsolConstants.FLG_ON);
                           merge(buildingGroupDaoImpl, grp);
                        }
                     }
                 }
             }
         }
     }

    // 2016/09/14 まとめて更新するように修正
    /**
     *
     * @param dlt_parent_groupList
     * @param updateChg_parent_groupList
     * @param parentGroupAddList
     * @param dltChildGroupList
     * @param chgChildGroupList
     * @param childGroupAddList
     * @return
     */
    public boolean execBuildingGroup(List<MParentGroup> dlt_parent_groupList, List<MParentGroup> updateChg_parent_groupList, List<MParentGroup> parentGroupAddList,
            List<MChildGroup> dltChildGroupList, List<MChildGroup> chgChildGroupList, List<MChildGroup> childGroupAddList) {

        // 親グループの処理
        // 削除更新
        for (MParentGroup deleteParentTarget : dlt_parent_groupList) {
            mergeParent(deleteParentTarget);
            if (isExceptionFlg()) {
                return false;
            }
            deleteBuildingGroup(deleteParentTarget.getId().getCorpId(), deleteParentTarget.getId().getParentGroupId(), -1L);
        }
        // 更新
        for (MParentGroup updateParentGroup : updateChg_parent_groupList) {
            mergeParent(updateParentGroup);
            if (isExceptionFlg()) {
                return false;
            }
        }
        // 登録用
        for (MParentGroup addParentGroup : parentGroupAddList) {
            // 自動採番の取得しセットする
            if (addParentGroup.getId().getParentGroupId() == null) {
                // ありえないはず
                addParentGroup.getId().setParentGroupId(createParentGroupId());
            }

            persistParent(addParentGroup);
            if (isExceptionFlg()) {
                return false;
            }
        }
        // 子グループの処理
        // 削除
        for (MChildGroup dltChildGroup : dltChildGroupList) {
            mergeChild(dltChildGroup);
            if (isExceptionFlg()) {
                return false;
            }
            deleteBuildingGroup(dltChildGroup.getId().getCorpId(), dltChildGroup.getId().getParentGroupId(), dltChildGroup.getId().getChildGroupId());
        }

        // 更新
        for (MChildGroup chgChildGroup : chgChildGroupList) {
            mergeChild(chgChildGroup);
            if (isExceptionFlg()) {
                return false;
            }
        }

        // 登録
        for (MChildGroup childGroupAdd : childGroupAddList) {
            if (findChild(childGroupAdd) != null) {
                childGroupAdd.getId().setChildGroupId(createChildGroupId());
            }
            persistChild(childGroupAdd);
            if (isExceptionFlg()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 企業IDが一致する未削除の親グループを取得
     *
     * @param corpId
     * @return
     */
    public List<MParentGroup> getCategoryList(String corpId) {

        MParentGroup target = new MParentGroup();
        MParentGroupPK id = new MParentGroupPK();
        id.setCorpId(corpId);
        target.setId(id);
        target.setDelFlg(0);
        return getResultList(pearentDaoImpl, target);
    }

    /**
     * 企業IDが一致する（削除含む）の親グループを取得
     *
     * @param corpId
     * @return
     */
    public List<MParentGroup> getParentGroupList(String corpId) {

        MParentGroup target = new MParentGroup();
        MParentGroupPK id = new MParentGroupPK();
        id.setCorpId(corpId);
        target.setId(id);
        target.setDelFlg(OsolConstants.FLG_OFF);   // 削除も含む場合とする
        return getResultList(pearentDaoImpl, target);
    }

    public List<MParentGroup> getResultList(MParentGroup mParentGroup) {
        return getResultList(pearentDaoImpl, mParentGroup);
    }

    /**
     * <p>
     * 親グループ 新規登録
     * </p>
     *
     * @param target
     */
    public void persistParent(MParentGroup target) {
        persist(pearentDaoImpl, target);
    }

    /**
     * <p>
     * 親グループ 更新処理
     * </p>
     *
     * @param target 親グループ
     * @return 更新結果()
     */
    public MParentGroup mergeParent(MParentGroup target) {
        MParentGroup result = merge(pearentDaoImpl, target);
        return result;
    }

    /**
     * 1件のみレコード取得する
     *
     * @param entity
     * @return
     */
    public MParentGroup find(MParentGroup entity) {
        return find(pearentDaoImpl, entity);
    }

    /**
     * <p>
     * 子グループ 新規登録
     * </p>
     *
     * @param target
     */
    public void persistChild(MChildGroup target) {
        persist(childDaoImpl, target);
    }

    /**
     * <p>
     * 子グループ 更新処理
     * </p>
     *
     * @param target 子グループ
     * @return 更新結果()
     */
    public MChildGroup mergeChild(MChildGroup target) {
        MChildGroup result = merge(childDaoImpl, target);
        return result;
    }

    /**
     * 1件のみレコード取得する
     *
     * @param entity
     * @return
     */
    public MChildGroup findChild(MChildGroup entity) {
        return find(childDaoImpl, entity);
    }
}
