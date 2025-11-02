package jp.co.osaki.osol.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;


/**
 * The persistent class for the m_product_spec database table.
 *
 */
@Entity
@Table(name="m_product_spec")
@NamedQuery(name="MProductSpec.findAll", query="SELECT m FROM MProductSpec m")
public class MProductSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="product_cd", unique=true, nullable=false, length=2)
	private String productCd;

	@Column(name="create_date", nullable=false)
	private Timestamp createDate;

	@Column(name="create_user_id", nullable=false)
	private Long createUserId;

	@Column(name="del_flg", nullable=false)
	private Integer delFlg;

	@Column(name="load_control_output", nullable=false)
	private Integer loadControlOutput;

	@Column(name="measurement_point", nullable=false)
	private Integer measurementPoint;

	@Column(name="product_name", nullable=false, length=20)
	private String productName;

	@Column(name="product_type", nullable=false, length=20)
	private String productType;

	@Column(name="update_date", nullable=false)
	private Timestamp updateDate;

	@Column(name="update_user_id", nullable=false)
	private Long updateUserId;

	@Version
	@Column(nullable=false)
	private Integer version;

    //bi-directional many-to-one association to MProductControlLoad
    @OneToMany(mappedBy="MProductSpec")
    private List<MProductControlLoad> MProductControlLoads;

    //bi-directional many-to-one association to MSmPrm
	@OneToMany(mappedBy="MProductSpec")
	private List<MSmPrm> MSmPrms;

	//bi-directional many-to-one association to TSmConnectControlProduct
	@OneToMany(mappedBy="MProductSpec")
	private List<TSmConnectControlProduct> TSmConnectControlProducts;

    //bi-directional many-to-one association to MDemandCollectProduct
    @OneToMany(mappedBy="MProductSpec")
    private List<MDemandCollectProduct> MDemandCollectProducts;

    public MProductSpec() {
	}

	public String getProductCd() {
		return this.productCd;
	}

	public void setProductCd(String productCd) {
		this.productCd = productCd;
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

	public Integer getLoadControlOutput() {
		return this.loadControlOutput;
	}

	public void setLoadControlOutput(Integer loadControlOutput) {
		this.loadControlOutput = loadControlOutput;
	}

	public Integer getMeasurementPoint() {
		return this.measurementPoint;
	}

	public void setMeasurementPoint(Integer measurementPoint) {
		this.measurementPoint = measurementPoint;
	}

	public String getProductName() {
		return this.productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return this.productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
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

	public List<MProductControlLoad> getMProductControlLoads() {
		return this.MProductControlLoads;
	}

	public void setMProductControlLoads(List<MProductControlLoad> MProductControlLoads) {
		this.MProductControlLoads = MProductControlLoads;
	}

	public MProductControlLoad addMProductControlLoad(MProductControlLoad MProductControlLoad) {
		getMProductControlLoads().add(MProductControlLoad);
		MProductControlLoad.setMProductSpec(this);

		return MProductControlLoad;
	}

	public MProductControlLoad removeMProductControlLoad(MProductControlLoad MProductControlLoad) {
		getMProductControlLoads().remove(MProductControlLoad);
		MProductControlLoad.setMProductSpec(null);

		return MProductControlLoad;
	}

	public List<MSmPrm> getMSmPrms() {
		return this.MSmPrms;
	}

	public void setMSmPrms(List<MSmPrm> MSmPrms) {
		this.MSmPrms = MSmPrms;
	}

	public MSmPrm addMSmPrm(MSmPrm MSmPrm) {
		getMSmPrms().add(MSmPrm);
		MSmPrm.setMProductSpec(this);

		return MSmPrm;
	}

	public MSmPrm removeMSmPrm(MSmPrm MSmPrm) {
		getMSmPrms().remove(MSmPrm);
		MSmPrm.setMProductSpec(null);

		return MSmPrm;
	}

	public List<TSmConnectControlProduct> getTSmConnectControlProducts() {
		return this.TSmConnectControlProducts;
	}

	public void setTSmConnectControlProducts(List<TSmConnectControlProduct> TSmConnectControlProducts) {
		this.TSmConnectControlProducts = TSmConnectControlProducts;
	}

	public TSmConnectControlProduct addTSmConnectControlProduct(TSmConnectControlProduct TSmConnectControlProduct) {
		getTSmConnectControlProducts().add(TSmConnectControlProduct);
		TSmConnectControlProduct.setMProductSpec(this);

		return TSmConnectControlProduct;
	}

	public TSmConnectControlProduct removeTSmConnectControlProduct(TSmConnectControlProduct TSmConnectControlProduct) {
		getTSmConnectControlProducts().remove(TSmConnectControlProduct);
		TSmConnectControlProduct.setMProductSpec(null);

		return TSmConnectControlProduct;
	}

    public List<MDemandCollectProduct> getMDemandCollectProducts() {
        return this.MDemandCollectProducts;
    }

    public void setMDemandCollectProducts(List<MDemandCollectProduct> MDemandCollectProducts) {
        this.MDemandCollectProducts = MDemandCollectProducts;
    }

    public MDemandCollectProduct addMDemandCollectProduct(MDemandCollectProduct MDemandCollectProduct) {
        getMDemandCollectProducts().add(MDemandCollectProduct);
        MDemandCollectProduct.setMProductSpec(this);

        return MDemandCollectProduct;
    }

    public MDemandCollectProduct removeMDemandCollectProduct(MDemandCollectProduct MDemandCollectProduct) {
        getMDemandCollectProducts().remove(MDemandCollectProduct);
        MDemandCollectProduct.setMProductSpec(null);

        return MDemandCollectProduct;
    }

}