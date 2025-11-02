package jp.co.osaki.osol.mng.param;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
*
* デマンドスタート(設定) Param クラス
*
* @author t_sakamoto
*
*/
public class A200025Param extends BaseParam {

	/**
	 * 取得日時(設定変更日時)(週含む)
	 */
	private String settingDate;

	/**
	 * デマンドスタート命令
	 */
	@NotNull
	@Pattern(regexp = "[0-9]")
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
