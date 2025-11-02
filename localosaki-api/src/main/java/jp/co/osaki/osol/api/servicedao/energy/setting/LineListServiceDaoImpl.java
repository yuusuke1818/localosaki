/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.energy.setting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.entity.MLine;
import jp.co.osaki.osol.entity.MLinePK_;
import jp.co.osaki.osol.entity.MLineType;
import jp.co.osaki.osol.entity.MLineType_;
import jp.co.osaki.osol.entity.MLine_;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * 系統取得処理 ServiceDaoクラス
 *
 * @author ya-ishida
 */
public class LineListServiceDaoImpl implements BaseServiceDao<LineListDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineListDetailResultData> getResultList(LineListDetailResultData t, EntityManager em) {
        String corpId = t.getCorpId();
        Long lineGroupId = t.getLineGroupId();
        String lineNo = t.getLineNo();
        Integer lineEnableFlg = t.getLineEnableFlg();
        Integer eventValidFlg = t.getEventValidFlg();
        Integer dmValidFlg = t.getDmValidFlg();

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<LineListDetailResultData> query = builder.createQuery(LineListDetailResultData.class);

        Root<MLine> root = query.from(MLine.class);

        Join<MLine, MLineType> joinLineType = root.join(MLine_.MLineType);

        List<Predicate> whereList = new ArrayList<>();

        //企業ID
        whereList.add(builder.equal(root.get(MLine_.id).get(MLinePK_.corpId), corpId));
        //系統グループID
        if (lineGroupId != null) {
            whereList.add(builder.equal(root.get(MLine_.id).get(MLinePK_.lineGroupId), lineGroupId));
        }
        //系統番号
        if (!CheckUtility.isNullOrEmpty(lineNo)) {
            whereList.add(builder.equal(root.get(MLine_.id).get(MLinePK_.lineNo), lineNo));
        }
        //系統有効フラグ
        if (lineEnableFlg != null) {
            whereList.add(builder.equal(root.get(MLine_.lineEnableFlg), lineEnableFlg));
        }
        //イベント検証フラグ
        if(eventValidFlg != null){
            whereList.add(builder.equal(joinLineType.get(MLineType_.eventValidFlg), eventValidFlg));
        }
        //デマンド検証フラグ
        if(dmValidFlg != null){
            whereList.add(builder.equal(joinLineType.get(MLineType_.dmValidFlg), dmValidFlg));
        }

        //削除フラグ
        whereList.add(builder.equal(root.get(MLine_.delFlg), OsolConstants.FLG_OFF));

        query = query.select(builder.construct(LineListDetailResultData.class,
                root.get(MLine_.id).get(MLinePK_.corpId),
                root.get(MLine_.id).get(MLinePK_.lineGroupId),
                root.get(MLine_.id).get(MLinePK_.lineNo),
                root.get(MLine_.lineName),
                root.get(MLine_.lineUnit),
                root.get(MLine_.lineTarget),
                joinLineType.get(MLineType_.lineType),
                joinLineType.get(MLineType_.dmValidFlg),
                joinLineType.get(MLineType_.eventValidFlg),
                joinLineType.get(MLineType_.airValidFlg),
                root.get(MLine_.lineEnableFlg),
                root.get(MLine_.inputEnableFlg),
                root.get(MLine_.delFlg),
                root.get(MLine_.version))).
                where(builder.and(whereList.toArray(new Predicate[]{})));

        return em.createQuery(query).getResultList();
    }

    @Override
    public List<LineListDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineListDetailResultData> getResultList(List<LineListDetailResultData> list, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LineListDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineListDetailResultData find(LineListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(LineListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public LineListDetailResultData merge(LineListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(LineListDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
