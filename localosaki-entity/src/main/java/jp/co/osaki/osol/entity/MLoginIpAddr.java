package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Formula;


/**
 * The persistent class for the m_login_ip_addr database table.
 *
 */
@Entity
@Table(name="m_login_ip_addr")
@NamedQuery(name="MLoginIpAddr.findAll", query="SELECT m FROM MLoginIpAddr m")
public class MLoginIpAddr implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MLoginIpAddrPK id;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="del_flg", nullable=false)
    private Integer delFlg;

    @Column(name="ip_address", nullable=false, length=15)
    private String ipAddress;

    @Column(name="login_permit_status", nullable=false, length=6)
    private String loginPermitStatus;

    @Column(name="login_permit_target", nullable=false, length=6)
    private String loginPermitTarget;

    @Column(nullable=false, length=2000)
    private String memo;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

        @Formula(value = "split_part(ip_address,'.',1)")
        private String ipAddress1;

        @Formula(value = "split_part(ip_address,'.',2)")
        private String ipAddress2;

        @Formula(value = "split_part(ip_address,'.',3)")
        private String ipAddress3;

        @Formula(value = "split_part(ip_address,'.',4)")
        private String ipAddress4;

        @Formula(value = "lpad(split_part(ip_address,'.',1), 3, '0') || '.' || lpad(split_part(ip_address,'.',2), 3, '0') || '.' ||lpad(split_part(ip_address,'.',3), 3, '0') || '.' ||lpad(split_part(ip_address,'.',4), 3, '0')")
        private String sortIpAddress;

    //bi-directional many-to-one association to MCorp
    @ManyToOne
    @JoinColumn(name="corp_id", nullable=false, insertable=false, updatable=false)
    private MCorp MCorp;

    public MLoginIpAddr() {
    }

    public MLoginIpAddrPK getId() {
        return this.id;
    }

    public void setId(MLoginIpAddrPK id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Long getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Integer getDelFlg() {
        return this.delFlg;
    }

    public void setDelFlg(Integer delFlg) {
        this.delFlg = delFlg;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLoginPermitStatus() {
        return this.loginPermitStatus;
    }

    public void setLoginPermitStatus(String loginPermitStatus) {
        this.loginPermitStatus = loginPermitStatus;
    }

    public String getLoginPermitTarget() {
        return this.loginPermitTarget;
    }

    public void setLoginPermitTarget(String loginPermitTarget) {
        this.loginPermitTarget = loginPermitTarget;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Timestamp getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Integer getVersion() {
        return this.version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public MCorp getMCorp() {
        return this.MCorp;
    }

    public void setMCorp(MCorp MCorp) {
        this.MCorp = MCorp;
    }

        public String getIpAddress1() {
            return ipAddress1;
        }

        public void setIpAddress1(String ipAddress1) {
            this.ipAddress1 = ipAddress1;
        }

        public String getIpAddress2() {
            return ipAddress2;
        }

        public void setIpAddress2(String ipAddress2) {
            this.ipAddress2 = ipAddress2;
        }

        public String getIpAddress3() {
            return ipAddress3;
        }

        public void setIpAddress3(String ipAddress3) {
            this.ipAddress3 = ipAddress3;
        }

        public String getIpAddress4() {
            return ipAddress4;
        }

        public void setIpAddress4(String ipAddress4) {
            this.ipAddress4 = ipAddress4;
        }

        public String getSortIpAddress() {
            return sortIpAddress;
        }

        public void setSortIpAddress(String sortIpAddress) {
            this.sortIpAddress = sortIpAddress;
        }

}