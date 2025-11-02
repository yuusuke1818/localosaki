package jp.co.osaki.sms.bean.sms.aggregate;

import java.io.Serializable;

import javax.enterprise.context.Dependent;
import javax.inject.Named;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import jp.co.osaki.sms.bean.building.info.Condition;
import jp.skygroup.enl.webap.base.BaseSearchBeanProperty;

/**
 * 検針データダウンロードの検索条件保持クラス
 * このクラスを用いてJSON変換をやりやすくする
 *
 * @author 22702kansaku
 */

@Named(value = "smsAggregateMeterReadingDataSearchCondition")
@Dependent
public class MeterReadingDataSearchCondition extends BaseSearchBeanProperty<Condition> implements Serializable {

    private static final long serialVersionUID = 5098939796642213799L;

    /** 現在ログインしているアカウントの担当者種別 */
    @SerializedName("loginPersonType")
    @Expose
    public String loginPersonType;


    /** 現在ログインしているアカウントの企業ID */
    @SerializedName("loginCorpId")
    @Expose
    public String loginCorpId;


    /** 現在ログインしているアカウントの担当者ID */
    @SerializedName("loginPersonId")
    @Expose
    public String loginPersonId;


    /** 現在ログインしているアカウントの企業種別 */
    @SerializedName("loginCorpType")
    @Expose
    public String loginCorpType;


    /** 現在ログインしているアカウントの権限 */
    @SerializedName("personAuths")
    @Expose
    public String personAuths;


    /** 選択された検針種別 */
    @SerializedName("inspTypes")
    @Expose
    public String inspTypes;


    /** 検索対象のおしりの日付 */
    @SerializedName("endDate")
    @Expose
    public String endDate;


    /** 検索対象のあたまの日付 */
    @SerializedName("startDate")
    @Expose
    public String startDate;


    /** 最大デマンド値を出力するかしないか（"true"/"false"） */
    @SerializedName("outputMaxDemand")
    @Expose
    public String outputMaxDemand;


    /** 前年同月データを出力するかしないか（"true"/"false"） */
    @SerializedName("outputPrivYearAndSameMonth")
    @Expose
    public String outputPrivYearAndSameMonth;


    /** 企業検索に追加した条件 */
    @SerializedName("corpParam")
    @Expose
    public String corpParam;


    /** 建物検索に追加した条件 */
    @SerializedName("buildingParam")
    @Expose
    public String buildingParam;

}
