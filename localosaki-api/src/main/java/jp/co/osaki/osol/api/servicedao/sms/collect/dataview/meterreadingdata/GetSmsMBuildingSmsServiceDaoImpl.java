package jp.co.osaki.osol.api.servicedao.sms.collect.dataview.meterreadingdata;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import jp.co.osaki.osol.entity.MBuildingSms;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 建物情報(SMS用) 検索 ServiceDaoクラス
 * @author kobayashi.sho
 */
public class GetSmsMBuildingSmsServiceDaoImpl implements BaseServiceDao<MBuildingSms> {

    // 1件のみレコード取得するDBアクセス処理.
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public MBuildingSms find(MBuildingSms target, EntityManager em) {
        return em.find(MBuildingSms.class, target.getId());
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MBuildingSms> getResultList(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 削除処理
    @Override
    public void remove(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 更新処理
    @Override
    public MBuildingSms merge(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<MBuildingSms> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(List<MBuildingSms> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<MBuildingSms> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(MBuildingSms target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
