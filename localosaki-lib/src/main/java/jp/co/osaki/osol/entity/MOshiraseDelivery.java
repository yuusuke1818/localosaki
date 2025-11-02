package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_oshirase_delivery database table.
 *
 */
@Entity
@Table(name="m_oshirase_delivery")
@NamedQuery(name="MOshiraseDelivery.findAll", query="SELECT m FROM MOshiraseDelivery m")
public class MOshiraseDelivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private MOshiraseDeliveryPK id;

    @Column(name="create_date", nullable=false)
    private Timestamp createDate;

    @Column(name="create_user_id", nullable=false)
    private Long createUserId;

    @Column(name="delivery_use_flg", nullable=false)
    private Integer deliveryUseFlg;

    @Column(name="update_date", nullable=false)
    private Timestamp updateDate;

    @Column(name="update_user_id", nullable=false)
    private Long updateUserId;

    @Version
    @Column(nullable=false)
    private Integer version;

    //bi-directional many-to-one association to TOshirase
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name="corp_id", referencedColumnName="corp_id", nullable=false, insertable=false, updatable=false),
        @JoinColumn(name="oshirase_id", referencedColumnName="oshirase_id", nullable=false, insertable=false, updatable=false)
        })
    private TOshirase TOshirase;

    public MOshiraseDelivery() {
    }

    public MOshiraseDeliveryPK getId() {
        return this.id;
    }

    public void setId(MOshiraseDeliveryPK id) {
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

    public Integer getDeliveryUseFlg() {
        return this.deliveryUseFlg;
    }

    public void setDeliveryUseFlg(Integer deliveryUseFlg) {
        this.deliveryUseFlg = deliveryUseFlg;
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

    public TOshirase getTOshirase() {
        return this.TOshirase;
    }

    public void setTOshirase(TOshirase TOshirase) {
        this.TOshirase = TOshirase;
    }

}