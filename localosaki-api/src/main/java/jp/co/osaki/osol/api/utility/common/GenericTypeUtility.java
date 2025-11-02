/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.utility.common;

import static jp.skygroup.enl.webap.base.BaseConstants.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.dao.generic.GenericTypeListDao;
import jp.co.osaki.osol.api.resultdata.generic.GenericTypeListDetailResultData;

/**
 * 汎用区分マスタ用Utilityクラス
 *
 * @author n-takada
 */
@Named(value = "genericTypeUtility")
@ApplicationScoped
public class GenericTypeUtility implements Serializable {

    private static final long serialVersionUID = -3182019373175707853L;

    @EJB
    private GenericTypeListDao genericTypeDao;

    /**
     * 汎用区分マスタのResultSetを格納するマップ
     */
    private Map<ApiGenericTypeConstants.GROUP_CODE, List<GenericTypeListDetailResultData>> genericTypeMasterMap;

    /**
     * 汎用区分マスタの名前解決用のマップ
     */
    private Map<ApiGenericTypeConstants.GROUP_CODE, Map<String, String>> kbnNameMap;

    /**
     * コンストラクタ<br>
     * mapに汎用区分マスタ情報を登録する
     */
    @PostConstruct
    public void init() {
        ApiGenericTypeConstants.GROUP_CODE[] groupCodeArray = ApiGenericTypeConstants.GROUP_CODE.values();
        genericTypeMasterMap = new HashMap<>();
        kbnNameMap = new HashMap<>();
        for (ApiGenericTypeConstants.GROUP_CODE groupCode : groupCodeArray) {
            List<GenericTypeListDetailResultData> genericTypeList = initGroupMap(groupCode);
            genericTypeMasterMap.put(groupCode, genericTypeList);
            // 名前解決用Mapにも登録
            putKbnNameMap(groupCode, genericTypeList);
        }
    }

    /**
     * 区分コードから区分名称が引けるようにmapを組み立てる
     *
     * @param groupCode
     * @param genericTypeList
     */
    private void putKbnNameMap(ApiGenericTypeConstants.GROUP_CODE groupCode,
            List<GenericTypeListDetailResultData> genericTypeList) {
        Map<String, String> groupMap = new HashMap<>();
        kbnNameMap.put(groupCode, groupMap);
        if (genericTypeList == null || genericTypeList.isEmpty()) {
            return;
        }
        for (GenericTypeListDetailResultData resultSet : genericTypeList) {
            groupMap.put(resultSet.getKbnCode(), resultSet.getKbnName());
        }
    }

    /**
     * 指定されたグループコードの汎用区分を取得する。
     *
     * @param groupCode
     * @return
     */
    private List<GenericTypeListDetailResultData> initGroupMap(ApiGenericTypeConstants.GROUP_CODE groupCode) {
        return genericTypeDao.getGenericTypeList(groupCode);
    }

    /**
     * 指定したグループコード、区分コードの区分名称を取得する
     *
     * @param groupCode kbnCodeに対応したグループコード
     * @param kbnCode 名称を取得したいkbnCode
     * @return
     */
    public String getKbnName(ApiGenericTypeConstants.GROUP_CODE groupCode, String kbnCode) {
        if (kbnCode == null || STR_EMPTY.equals(kbnCode)) {
            return STR_EMPTY;
        }
        Map<String, String> map = kbnNameMap.get(groupCode);
        if (map == null || map.isEmpty()) {
            return STR_EMPTY;
        }
        return map.get(kbnCode);
    }

    /**
     * groupCodeに対応した、汎用区分マスタを返す
     *
     * @param groupCode 取得したいグループコード
     * @return
     */
    public List<GenericTypeListDetailResultData> getKbnList(ApiGenericTypeConstants.GROUP_CODE groupCode) {
        return genericTypeMasterMap.get(groupCode);
    }

    /**
     * groupCodeに対応した、汎用区分マスタを返す
     *
     * @param groupCode 取得したいグループコード
     * @return
     */
    public List<GenericTypeListDetailResultData> getKbnList(String groupCode) {
        return genericTypeMasterMap.get(getGroupCode(groupCode));
    }

    /**
     * 文字列で受け取ったgroupCodeをGROUP_CODE型に変更する
     *
     * @param groupCode 取得したいグループコード
     * @return
     */
    private ApiGenericTypeConstants.GROUP_CODE getGroupCode(String groupCode) {
        ApiGenericTypeConstants.GROUP_CODE[] array = ApiGenericTypeConstants.GROUP_CODE.values();
        for (ApiGenericTypeConstants.GROUP_CODE part : array) {
            if (part.getVal().equals(groupCode)) {
                return part;
            }
        }
        return null;
    }
}
