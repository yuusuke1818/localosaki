package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineTypeListDetailResult;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統種別取得処理 ServiceDaoクラス
 * @author t_hirata
 */
public class LineTypeListServiceDaoImpl implements BaseServiceDao<LineTypeListDetailResult> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineTypeListDetailResult> getResultList(LineTypeListDetailResult t, EntityManager em) {

        String lineType = t.getLineType();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LineTypeListDetailResult> query = builder.createQuery(LineTypeListDetailResult.class);

        Root<MLineType> root = query.from(MLineType.class);
        List<Predicate> whereList = new ArrayList<>();
        // 系統種別
        if (lineType != null) {
            whereList.add(builder.equal(root.get(MLineType_.lineType), lineType));
        }
        //削除フラグ
        whereList.add(builder.equal(root.get(MLineType_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(LineTypeListDetailResult.class,
                root.get(MLineType_.lineType),
                root.get(MLineType_.lineTypeName),
                root.get(MLineType_.delFlg),
                root.get(MLineType_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MLineType_.displayOrder)), builder.asc(root.get(MLineType_.lineType)));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LineTypeListDetailResult> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineTypeListDetailResult> getResultList(List<LineTypeListDetailResult> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineTypeListDetailResult> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineTypeListDetailResult find(LineTypeListDetailResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(LineTypeListDetailResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineTypeListDetailResult merge(LineTypeListDetailResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(LineTypeListDetailResult t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
