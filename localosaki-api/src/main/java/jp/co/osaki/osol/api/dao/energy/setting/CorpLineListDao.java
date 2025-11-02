package jp.co.osaki.osol.api.dao.energy.setting;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import jp.co.osaki.osol.access.filter.dao.CorpDataFilterDao;
import jp.co.osaki.osol.access.filter.param.PersonDataParam;
import jp.co.osaki.osol.api.OsolApiDao;
import jp.co.osaki.osol.api.constants.ApiGenericTypeConstants;
import jp.co.osaki.osol.api.parameter.energy.setting.CorpLineListParameter;
import jp.co.osaki.osol.api.result.energy.setting.CorpLineListResult;
import jp.co.osaki.osol.api.result.servicedao.CommonCorpExclusionResult;
import jp.co.osaki.osol.api.resultdata.energy.setting.CorpLineListDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineGroupSearchDetailResultData;
import jp.co.osaki.osol.api.resultdata.energy.setting.LineListDetailResultData;
import jp.co.osaki.osol.api.servicedao.common.CommonCorpExclusionServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineGroupSearchServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.energy.setting.LineListServiceDaoImpl;

/**
 * 企業系統一覧取得 Daoクラス
 * @author ya-ishida
 *
 */
@Stateless
public class CorpLineListDao extends OsolApiDao<CorpLineListParameter> {

    private final LineGroupSearchServiceDaoImpl lineGroupSearchServiceDaoImpl;

    private final LineListServiceDaoImpl lineListServiceDaoImpl;

    private final CommonCorpExclusionServiceDaoImpl commonCorpExclusionServiceDaoImpl;

    @EJB
    private CorpDataFilterDao corpDataFilterDao;

    public CorpLineListDao() {
        lineGroupSearchServiceDaoImpl = new LineGroupSearchServiceDaoImpl();
        lineListServiceDaoImpl = new LineListServiceDaoImpl();
        commonCorpExclusionServiceDaoImpl = new CommonCorpExclusionServiceDaoImpl();
    }

    /* (非 Javadoc)
     * @see jp.co.osaki.osol.api.OsolApiDao#query(jp.co.osaki.osol.api.OsolApiParameter)
     */
    @Override
    public CorpLineListResult query(CorpLineListParameter parameter) throws Exception {
        CorpLineListResult result = new CorpLineListResult();
        CorpLineListDetailResultData detailResult = new CorpLineListDetailResultData();

        //排他企業情報を取得する
        CommonCorpExclusionResult exParam = new CommonCorpExclusionResult();
        exParam.setCorpId(parameter.getOperationCorpId());
        List<CommonCorpExclusionResult> exList = getResultList(commonCorpExclusionServiceDaoImpl, exParam);

        //フィルター処理を行う
        exList = corpDataFilterDao.applyDataFilter(exList,
                new PersonDataParam(parameter.getLoginCorpId(), parameter.getLoginPersonId()));

        if (exList == null || exList.size() != 1) {
            return new CorpLineListResult();
        }

        //企業系統グループを取得する
        LineGroupSearchDetailResultData groupParam = new LineGroupSearchDetailResultData();
        groupParam.setCorpId(parameter.getOperationCorpId());
        groupParam.setLineGroupType(ApiGenericTypeConstants.LINE_GROUP_TYPE.CORPORATE_STANDARD.getVal());
        List<LineGroupSearchDetailResultData> groupList = getResultList(lineGroupSearchServiceDaoImpl, groupParam);

        if (groupList != null && groupList.size() == 1) {
            detailResult.setLineGroupId(groupList.get(0).getLineGroupId());
            detailResult.setLineGroupName(groupList.get(0).getLineGroupName());
            detailResult.setLineGroupType(groupList.get(0).getLineGroupType());
            detailResult.setDelFlg(groupList.get(0).getDelFlg());
            detailResult.setLineGroupVersion(groupList.get(0).getGroupVersion());

            //系統情報を取得する
            LineListDetailResultData lineParam = new LineListDetailResultData();
            lineParam.setCorpId(parameter.getOperationCorpId());
            lineParam.setLineGroupId(groupList.get(0).getLineGroupId());
            List<LineListDetailResultData> tempLineList = getResultList(lineListServiceDaoImpl, lineParam);

            if (tempLineList == null || tempLineList.isEmpty()) {
                detailResult.setLineList(new ArrayList<>());
                detailResult.setLineAll(new LineListDetailResultData());
                detailResult.setLineEtc(new LineListDetailResultData());
            } else {
                List<LineListDetailResultData> lineList = new ArrayList<>();
                LineListDetailResultData allLine = new LineListDetailResultData();
                LineListDetailResultData etcLine = new LineListDetailResultData();
                for (LineListDetailResultData line : tempLineList) {
                    if (ApiGenericTypeConstants.LINE_TARGET.ALL.getVal().equals(line.getLineTarget())) {
                        //全体の場合
                        allLine.setLineNo(line.getLineNo());
                        allLine.setLineType(line.getLineType());
                        allLine.setLineName(line.getLineName());
                        allLine.setLineTarget(line.getLineTarget());
                        allLine.setLineEnableFlg(line.getLineEnableFlg());
                        allLine.setDelFlg(line.getDelFlg());
                        allLine.setVersion(line.getVersion());
                    } else if (ApiGenericTypeConstants.LINE_TARGET.ETC.getVal().equals(line.getLineTarget())) {
                        //その他の場合
                        etcLine.setLineNo(line.getLineNo());
                        etcLine.setLineType(line.getLineType());
                        etcLine.setLineName(line.getLineName());
                        etcLine.setLineTarget(line.getLineTarget());
                        etcLine.setLineEnableFlg(line.getLineEnableFlg());
                        etcLine.setDelFlg(line.getDelFlg());
                        etcLine.setVersion(line.getVersion());
                    } else {
                        //上記以外の場合
                        lineList.add(line);
                    }
                }

                if (!lineList.isEmpty()) {
                    //ソートする
                    lineList.sort(
                            (LineListDetailResultData rs1, LineListDetailResultData rs2) -> rs1.getLineNo()
                                    .compareTo(rs2.getLineNo()));

                    lineList.sort((LineListDetailResultData rs1, LineListDetailResultData rs2) -> {
                        Integer l1 = Integer.parseInt(rs1.getLineNo());
                        Integer l2 = Integer.parseInt(rs2.getLineNo());
                        return l1.compareTo(l2);
                    });
                }

                detailResult.setLineList(lineList);
                detailResult.setLineAll(allLine);
                detailResult.setLineEtc(etcLine);
            }
        }

        result.setCorpId(exList.get(0).getCorpId());
        result.setVersion(exList.get(0).getVersion());
        result.setDetail(detailResult);

        return result;
    }

}
