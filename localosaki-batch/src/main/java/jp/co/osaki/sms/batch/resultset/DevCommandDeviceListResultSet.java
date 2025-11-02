package jp.co.osaki.sms.batch.resultset;

import java.math.BigDecimal;

/**
 * コマンド送信異常用装置情報 ResultSetクラス
 *
 * @author akr_iwamoto
 *
 */
public class DevCommandDeviceListResultSet {
    /** 装置ID */
    private String devId;

    /** 装置異常(0:正常復帰 1:異常発生) */
    private BigDecimal devSta;

    /** 装置名称 */
    private String name;

    /** ホームディレクトリ名 */
    private String homeDirectory;

    /** 逆潮対応機能(0:未使用 1:使用) */
    private String revFlg;

    /**
     * コンストラクタ
     *
     * @param devId
     * @param devSta
     * @param name
     * @param homeDirectory
     * @param revFlg
     */
    public DevCommandDeviceListResultSet(String devId, BigDecimal devSta, String name,
    		String homeDirectory, String revFlg) {
        this.devId = devId;
        this.devSta = devSta;
        this.name = name;
        this.homeDirectory = homeDirectory;
        this.revFlg = revFlg;
    }

    /**
     * @return devId
     */
    public String getDevId() {
        return devId;
    }

    /**
     * @param devId セットする devId
     */
    public void setDevId(String devId) {
        this.devId = devId;
    }

    /**
     * @return devSta
     */
	public BigDecimal getDevSta() {
		return this.devSta;
	}

    /**
     * @param devSta セットする devSta
     */
	public void setDevSta(BigDecimal devSta) {
		this.devSta = devSta;
	}

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name セットする name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return homeDirectory
     */
	public String getHomeDirectory() {
		return this.homeDirectory;
	}

    /**
     * @param homeDirectory セットする homeDirectory
     */
	public void setHomeDirectory(String homeDirectory) {
		this.homeDirectory = homeDirectory;
	}

    /**
     * @return revFlg
     */
	public String getRevFlg() {
		return this.revFlg;
	}

    /**
     * @param revFlg セットする revFlg
     */
	public void setRevFlg(String revFlg) {
		this.revFlg = revFlg;
	}
}
