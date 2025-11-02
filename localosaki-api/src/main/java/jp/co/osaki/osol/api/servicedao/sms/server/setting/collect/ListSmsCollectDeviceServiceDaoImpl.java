/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.collect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;

import jp.co.osaki.osol.OsolConstants;
import jp.co.osaki.osol.api.resultdata.sms.server.setting.collect.ListSmsCollectDeviceDetailResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 装置情報一覧取得  ServiceDaoクラス
 *
 * @author yoneda_y
 */
public class ListSmsCollectDeviceServiceDaoImpl implements BaseServiceDao<ListSmsCollectDeviceDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsCollectDeviceDetailResultData> getResultList(ListSmsCollectDeviceDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<ListSmsCollectDeviceDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsCollectDeviceDetailResultData> query = builder
                .createQuery(ListSmsCollectDeviceDetailResultData.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> whereList = new ArrayList<>();

        //MUDM2のみ取得(DEV_KINDが1)
        whereList.add(builder.equal(root.get(MDevPrm_.devKind), SmsConstants.DEVICE_PRM_KIND.MUDM2.getVal()));

        //ハンディ端末以外(MHから始まる装置ID以外)
        whereList.add(builder.notLike(root.get(MDevPrm_.devId), (SmsConstants.DEVICE_KIND.HANDY.getVal() + "%")));
        //AieLink装置以外(OCから始まる装置ID以外)
        whereList.add(builder.notLike(root.get(MDevPrm_.devId), (SmsConstants.DEVICE_KIND.OCR.getVal() + "%")));

        // 装置(IDまたは名)
        List<Object> targetDevIdOrNameList = map.get(OsolConstants.SEARCH_CONDITION_COLLECT_ID_OR_NAME);
        if (!CollectionUtils.isEmpty(targetDevIdOrNameList)) {
            for (Object s : targetDevIdOrNameList) {
                whereList.add(
                        builder.or(builder.like(root.get(MDevPrm_.devId), BaseUtility.addSqlWildcard((String) s)),
                                builder.like(root.get(MDevPrm_.name), BaseUtility.addSqlWildcard((String) s))));
            }
        }

        // 装置ID
        List<Object> targetDevIdList = map.get(OsolConstants.SEARCH_CONDITION_COLLECT_ID);
        if (!CollectionUtils.isEmpty(targetDevIdList)) {
            whereList.add(root.get(MDevPrm_.devId).in(targetDevIdList));
        }

        // 削除フラグ
        List<Object> targetDelFlgList = map.get(OsolConstants.SEARCH_CONDITION_COLLECT_STATUS);
        if (!CollectionUtils.isEmpty(targetDelFlgList)) {
            whereList.add(root.get(MDevPrm_.delFlg).in(targetDelFlgList));
        }

        query = query.select(builder.construct(ListSmsCollectDeviceDetailResultData.class,
                root.get(MDevPrm_.devId),
                root.get(MDevPrm_.devPw),
                root.get(MDevPrm_.name),
                root.get(MDevPrm_.memo),
                root.get(MDevPrm_.ipAddr),
                root.get(MDevPrm_.homeDirectory),
                root.get(MDevPrm_.examNoticeMonth),
                root.get(MDevPrm_.alertDisableFlg),
                root.get(MDevPrm_.revFlg),
                root.get(MDevPrm_.commInterval),
                root.get(MDevPrm_.delFlg),
                root.get(MDevPrm_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MDevPrm_.devId)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsCollectDeviceDetailResultData> getResultList(List<ListSmsCollectDeviceDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsCollectDeviceDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsCollectDeviceDetailResultData find(ListSmsCollectDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsCollectDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsCollectDeviceDetailResultData merge(ListSmsCollectDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsCollectDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
