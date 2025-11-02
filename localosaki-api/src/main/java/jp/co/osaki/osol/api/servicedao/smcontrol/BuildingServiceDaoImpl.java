package jp.co.osaki.osol.api.servicedao.smcontrol;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.entity.TBuilding;
import jp.co.osaki.osol.entity.TBuildingPK;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class BuildingServiceDaoImpl implements BaseServiceDao<BuildingResult> {

    @Override
    public BuildingResult find(BuildingResult target, EntityManager em) {
        // 主キー用エンティティ
        TBuildingPK entityPK = new TBuildingPK();
        entityPK.setBuildingId(target.getBuildingId());
        entityPK.setCorpId(target.getCorpId());

        // 検索用Entity
        TBuilding entity = em.find(TBuilding.class, entityPK);

        // ヒットしなければNullを返却
        if(entity==null){
            return null;
        }

        // 取得したレコード情報をResultクラスにセットする
        target.setBuildingName(entity.getBuildingName());
        target.setBuildingId(entity.getId().getBuildingId());
        target.setBuildingNo(entity.getBuildingNo());
        return target;
    }

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingResult> getResultList(BuildingResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingResult> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingResult> getResultList(List<BuildingResult> entityList, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BuildingResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(BuildingResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BuildingResult merge(BuildingResult target, EntityManager em) {
        // 更新対象テーブルのPK用エンティティ
        TBuildingPK entityPk = new TBuildingPK();

        // 要企業TBLのServiceDAO作成+Resultsetの設定
        entityPk.setCorpId(target.getCorpId());
        entityPk.setBuildingId(target.getBuildingId());

        // 更新対象レコード
        TBuilding entity = em.find(TBuilding.class, entityPk);

        entity.setUpdateUserId(target.getUpdateUserId());
        entity.setUpdateDate(target.getUpdateDate());

        // 更新処理
        em.merge(entity);

        return target;
    }


    @Override
    public void remove(BuildingResult target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
