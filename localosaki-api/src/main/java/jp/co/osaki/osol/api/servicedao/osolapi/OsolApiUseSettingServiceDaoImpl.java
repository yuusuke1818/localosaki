package jp.co.osaki.osol.api.servicedao.osolapi;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.TApiUseSetting;
import jp.co.osaki.osol.entity.TApiUseSettingPK;
import jp.skygroup.enl.webap.base.BaseServiceDao;

public class OsolApiUseSettingServiceDaoImpl implements BaseServiceDao<TApiUseSetting> {

    @Override
    public int executeUpdate(Map<String, List<Object>> parameterMap, EntityManager em) {
        return 0;
    }

    @Override
    public List<TApiUseSetting> getResultList(TApiUseSetting target, EntityManager em) {
        return null;
    }

    @Override
    public List<TApiUseSetting> getResultList(Map<String, List<Object>> parameterMap, EntityManager em) {
        return null;
    }

    @Override
    public List<TApiUseSetting> getResultList(List<TApiUseSetting> entityList, EntityManager em) {
        return null;
    }

    @Override
    public List<TApiUseSetting> getResultList(EntityManager em) {
        return null;
    }

    @Override
    public TApiUseSetting find(TApiUseSetting target, EntityManager em) {

        // 主キー用エンティティ
        TApiUseSettingPK entityPK = new TApiUseSettingPK();
        entityPK.setCorpId(target.getId().getCorpId());
        entityPK.setApiKind(target.getId().getApiKind());

        // 検索用Entity
        TApiUseSetting entity = em.find(TApiUseSetting.class, entityPK);

        // ヒットしなければnullを返却
        if(entity == null){
            return null;
        }

        // 取得したレコード情報をResultクラスにセットする
        target.setClosingDaySettingFlg(entity.getClosingDaySettingFlg());
        target.setClosingDay(entity.getClosingDay());
        target.setPredictionVal(entity.getPredictionVal());
        target.setMaxVal(entity.getMaxVal());
        return target;
    }

    @Override
    public void persist(TApiUseSetting target, EntityManager em) {

    }

    @Override
    public TApiUseSetting merge(TApiUseSetting target, EntityManager em) {
        return null;
    }

    @Override
    public void remove(TApiUseSetting target, EntityManager em) {

    }
}
