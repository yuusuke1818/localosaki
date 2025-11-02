/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.server.setting.ocr;

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
import jp.co.osaki.osol.api.resultdata.sms.server.setting.ocr.ListSmsOcrDeviceDetailResultData;
import jp.co.osaki.osol.entity.MDevPrm;
import jp.co.osaki.osol.entity.MDevPrm_;
import jp.co.osaki.sms.SmsConstants;
import jp.skygroup.enl.webap.base.BaseServiceDao;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * 装置情報一覧取得（AieLinkのみ）  ServiceDaoクラス
 * 「OCR検針」→「AieLink」へ変更
 * @author iwasaki_y
 */
public class ListSmsOcrDeviceServiceDaoImpl implements BaseServiceDao<ListSmsOcrDeviceDetailResultData> {

    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsOcrDeviceDetailResultData> getResultList(ListSmsOcrDeviceDetailResultData t,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<ListSmsOcrDeviceDetailResultData> getResultList(Map<String, List<Object>> map, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<ListSmsOcrDeviceDetailResultData> query = builder
                .createQuery(ListSmsOcrDeviceDetailResultData.class);

        Root<MDevPrm> root = query.from(MDevPrm.class);

        List<Predicate> whereList = new ArrayList<>();

        //MUDM2のみ取得(DEV_KINDが1)
        whereList.add(builder.equal(root.get(MDevPrm_.devKind), SmsConstants.DEVICE_PRM_KIND.MUDM2.getVal()));

        // AieLink(OCから始まる装置ID)
        whereList.add(builder.like(root.get(MDevPrm_.devId), (SmsConstants.DEVICE_KIND.OCR.getVal() + "%")));

        // AieLink(番号または名)
        List<Object> targetDevIdOrNameList = map.get(OsolConstants.SEARCH_CONDITION_OCR_ID_OR_NAME);
        if (!CollectionUtils.isEmpty(targetDevIdOrNameList)) {
            for (Object s : targetDevIdOrNameList) {
                whereList.add(
                        builder.or(builder.like(root.get(MDevPrm_.devId), BaseUtility.addSqlWildcard((String) s)),
                                builder.like(root.get(MDevPrm_.name), BaseUtility.addSqlWildcard((String) s))));
            }
        }

        // 装置ID
        List<Object> targetDevIdList = map.get(OsolConstants.SEARCH_CONDITION_OCR_ID);
        if (!CollectionUtils.isEmpty(targetDevIdList)) {
            whereList.add(root.get(MDevPrm_.devId).in(targetDevIdList));
        }

        // 削除フラグ
        List<Object> targetDelFlgList = map.get(OsolConstants.SEARCH_CONDITION_OCR_STATUS);
        if (!CollectionUtils.isEmpty(targetDelFlgList)) {
            whereList.add(root.get(MDevPrm_.delFlg).in(targetDelFlgList));
        }

        query = query.select(builder.construct(ListSmsOcrDeviceDetailResultData.class,
                root.get(MDevPrm_.devId),
                root.get(MDevPrm_.name),
                root.get(MDevPrm_.examNoticeMonth),
                root.get(MDevPrm_.delFlg),
                root.get(MDevPrm_.version)))
                .where(builder.and(whereList.toArray(new Predicate[] {})))
                .orderBy(builder.asc(root.get(MDevPrm_.devId)));
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<ListSmsOcrDeviceDetailResultData> getResultList(List<ListSmsOcrDeviceDetailResultData> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ListSmsOcrDeviceDetailResultData> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsOcrDeviceDetailResultData find(ListSmsOcrDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void persist(ListSmsOcrDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListSmsOcrDeviceDetailResultData merge(ListSmsOcrDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void remove(ListSmsOcrDeviceDetailResultData t, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
