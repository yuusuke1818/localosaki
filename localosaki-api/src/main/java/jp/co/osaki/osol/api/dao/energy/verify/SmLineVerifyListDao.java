package jp.co.osaki.osol.api.dao.energy.verify;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.verify.SmLineVerifyListParameter;
import jp.co.osaki.osol.api.result.energy.verify.SmLineVerifyListResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.verify.SmLineVerifyListDetailResultData;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.entity.MSmLineVerifyServiceDaoImpl;
import jp.co.osaki.osol.entity.MSmLineVerify;
import jp.co.osaki.osol.entity.MSmLineVerifyPK;

/**
 * 機器系統検証 Daoクラス
 *
 * @author t_hirata
 */
@Stateless
public class SmLineVerifyListDao extends OsolApiDao<SmLineVerifyListParameter> {

    private static final Integer CHK_ON = 1;

    //TODO EntityServiceDaoを利用しない
    private final LineListServiceDaoImpl lineListServiceDaoImpl;
    private final MSmLineVerifyServiceDaoImpl smLineVerifyServiceDaoImpl;

    public SmLineVerifyListDao() {
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        smLineVerifyServiceDaoImpl = new MSmLineVerifyServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public SmLineVerifyListResult query(SmLineVerifyListParameter parameter) throws Exception {
        SmLineVerifyListResult result = new SmLineVerifyListResult();

        List<SmLineVerifyListDetailResultData> ret = new ArrayList<>();

        // 系統
        LineListDetailResultData param = new LineListDetailResultData();
        param.setCorpId(parameter.getOperationCorpId());
        param.setLineGroupId(parameter.getLineGroupId());
        List<LineListDetailResultData> list = getResultList(lineListServiceDaoImpl, param);
        if (list != null) {
            list.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                int lineNo1 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(rs1.getLineTarget())
                        ? Integer.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(rs1.getLineTarget())
                                ? Integer.MAX_VALUE
                                : Integer.parseInt(rs1.getLineNo());
                int lineNo2 = ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(rs2.getLineTarget())
                        ? Integer.MAX_VALUE - 1
                        : ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(rs2.getLineTarget())
                                ? Integer.MAX_VALUE
                                : Integer.parseInt(rs2.getLineNo());
                return lineNo1 - lineNo2;
            });
            for (LineListDetailResultData line : list) {
                // イベント検証フラグ
                if (CHK_ON.equals(line.getEventValidFlg())) {
                    SmLineVerifyListDetailResultData rs = new SmLineVerifyListDetailResultData();
                    rs.setCorpId(line.getCorpId());
                    rs.setLineGroupId(line.getLineGroupId());
                    rs.setLineNo(line.getLineNo());
                    rs.setLineName(line.getLineName());
                    rs.setLineTarget(line.getLineTarget());
                    rs.setLineUnit(line.getLineUnit());

                    // 機器系統検証
                    MSmLineVerify _param = new MSmLineVerify();
                    MSmLineVerifyPK id = new MSmLineVerifyPK();
                    id.setCorpId(parameter.getOperationCorpId());
                    id.setLineGroupId(parameter.getLineGroupId());
                    id.setLineNo(line.getLineNo());
                    id.setBuildingId(parameter.getBuildingId());
                    id.setSmId(parameter.getSmId());
                    _param.setId(id);
                    MSmLineVerify _sm = find(smLineVerifyServiceDaoImpl, _param);
                    if (_sm != null) {
                        rs.setBuildingId(_sm.getId().getBuildingId());
                        rs.setSmId(_sm.getId().getSmId());
                        rs.setBasicRateUnitPrice(_sm.getBasicRateUnitPrice());
                        rs.setAirVerifyType(_sm.getAirVerifyType());
                        rs.setCommodityChargeUnitPrice(_sm.getCommodityChargeUnitPrice());
                        rs.setReductionRateThreshold(_sm.getReductionRateThreshold());
                        rs.setReductionCorrectionRate(_sm.getReductionCorrectionRate());
                        rs.setReductionLowerRateMonth1(_sm.getReductionLowerRateMonth1());
                        rs.setReductionLowerRateMonth2(_sm.getReductionLowerRateMonth2());
                        rs.setReductionLowerRateMonth3(_sm.getReductionLowerRateMonth3());
                        rs.setReductionLowerRateMonth4(_sm.getReductionLowerRateMonth4());
                        rs.setReductionLowerRateMonth5(_sm.getReductionLowerRateMonth5());
                        rs.setReductionLowerRateMonth6(_sm.getReductionLowerRateMonth6());
                        rs.setReductionLowerRateMonth7(_sm.getReductionLowerRateMonth7());
                        rs.setReductionLowerRateMonth8(_sm.getReductionLowerRateMonth8());
                        rs.setReductionLowerRateMonth9(_sm.getReductionLowerRateMonth9());
                        rs.setReductionLowerRateMonth10(_sm.getReductionLowerRateMonth10());
                        rs.setReductionLowerRateMonth11(_sm.getReductionLowerRateMonth11());
                        rs.setReductionLowerRateMonth12(_sm.getReductionLowerRateMonth12());
                        rs.setReductionLowerAmountMonth1(_sm.getReductionLowerAmountMonth1());
                        rs.setReductionLowerAmountMonth2(_sm.getReductionLowerAmountMonth2());
                        rs.setReductionLowerAmountMonth3(_sm.getReductionLowerAmountMonth3());
                        rs.setReductionLowerAmountMonth4(_sm.getReductionLowerAmountMonth4());
                        rs.setReductionLowerAmountMonth5(_sm.getReductionLowerAmountMonth5());
                        rs.setReductionLowerAmountMonth6(_sm.getReductionLowerAmountMonth6());
                        rs.setReductionLowerAmountMonth7(_sm.getReductionLowerAmountMonth7());
                        rs.setReductionLowerAmountMonth8(_sm.getReductionLowerAmountMonth8());
                        rs.setReductionLowerAmountMonth9(_sm.getReductionLowerAmountMonth9());
                        rs.setReductionLowerAmountMonth10(_sm.getReductionLowerAmountMonth10());
                        rs.setReductionLowerAmountMonth11(_sm.getReductionLowerAmountMonth11());
                        rs.setReductionLowerAmountMonth12(_sm.getReductionLowerAmountMonth12());
                        rs.setDelFlg(_sm.getDelFlg());
                        rs.setVersion(_sm.getVersion());
                        rs.setProposalAmountUsedMonth1(_sm.getProposalAmountUsedMonth1());
                        rs.setProposalAmountUsedMonth2(_sm.getProposalAmountUsedMonth2());
                        rs.setProposalAmountUsedMonth3(_sm.getProposalAmountUsedMonth3());
                        rs.setProposalAmountUsedMonth4(_sm.getProposalAmountUsedMonth4());
                        rs.setProposalAmountUsedMonth5(_sm.getProposalAmountUsedMonth5());
                        rs.setProposalAmountUsedMonth6(_sm.getProposalAmountUsedMonth6());
                        rs.setProposalAmountUsedMonth7(_sm.getProposalAmountUsedMonth7());
                        rs.setProposalAmountUsedMonth8(_sm.getProposalAmountUsedMonth8());
                        rs.setProposalAmountUsedMonth9(_sm.getProposalAmountUsedMonth9());
                        rs.setProposalAmountUsedMonth10(_sm.getProposalAmountUsedMonth10());
                        rs.setProposalAmountUsedMonth11(_sm.getProposalAmountUsedMonth11());
                        rs.setProposalAmountUsedMonth12(_sm.getProposalAmountUsedMonth12());
                    }

                    ret.add(rs);
                }
            }
        }

        result.setDetailList(ret);
        return result;
    }

}
