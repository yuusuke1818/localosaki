/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.bean.building;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import jp.co.osaki.osol.api.OsolApiBean;
import jp.co.osaki.osol.api.OsolApiResultCode;
import jp.co.osaki.osol.api.dao.building.GroupBuildingListDao;
import jp.co.osaki.osol.api.parameter.building.GroupBuildingListParameter;
import jp.co.osaki.osol.api.response.building.GroupBuildingListResponse;
import jp.co.osaki.osol.api.result.building.GroupBuildingListResult;
import jp.skygroup.enl.webap.base.api.BaseApiBean;

/**
 * 建物・テナント一覧取得（グループ指定） Beanクラス
 *
 * @author ya-ishida
 */
@Named(value = "GroupBuildingListBean")
@RequestScoped
public class GroupBuildingListBean extends OsolApiBean<GroupBuildingListParameter>
        implements BaseApiBean<GroupBuildingListParameter, GroupBuildingListResponse> {

    private GroupBuildingListParameter parameter = new GroupBuildingListParameter();

    private GroupBuildingListResponse response = new GroupBuildingListResponse();

    @EJB
    GroupBuildingListDao groupBuildingListDao;

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#getParameter()
     */
    @Override
    public GroupBuildingListParameter getParameter() {
        return parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#setParameter(jp.skygroup.enl.webap.base.api.BaseApiParameter)
     */
    @Override
    public void setParameter(GroupBuildingListParameter parameter) {
        this.parameter = parameter;
    }

    /* (非 Javadoc)
     * @see jp.skygroup.enl.webap.base.api.BaseApiBean#execute()
     */
    @Override
    public GroupBuildingListResponse execute() throws Exception {
        GroupBuildingListParameter param = new GroupBuildingListParameter();
        copyOsolApiParameter(this.parameter, param);
        param.setParentGroupId(this.parameter.getParentGroupId());
        param.setChildGroupId(this.parameter.getChildGroupId());
        param.setTotalTargetYm(this.parameter.getTotalTargetYm());

        if (this.validate(param).size() > 0) {
            response.setResultCode(OsolApiResultCode.API_ERROR_PARAMETER_VALID);
            return response;
        }

        GroupBuildingListResult result = groupBuildingListDao.query(param);
        response.setResultCode(OsolApiResultCode.API_OK);
        response.setResult(result);
        return response;
    }

}
