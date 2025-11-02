package jp.co.osaki.osol.mng.param;

/**
 *
 * デマンドスタート(取得) Param クラス
 *
 * @autho t_sakamoto
 *
 */
public class A200024Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)(週含む)
	 */
	private String settingDate;

	/**
	 * デマンドスタート命令
	 */
	private String demandStartOrder;

	public String getSettingDate() {
		return settingDate;
	}

	public void setSettingDate(String settingDate) {
		this.settingDate = settingDate;
	}

	public String getDemandStartOrder() {
		return demandStartOrder;
	}

	public void setDemandStartOrder(String demandStartOrder) {
		this.demandStartOrder = demandStartOrder;
	}


}
