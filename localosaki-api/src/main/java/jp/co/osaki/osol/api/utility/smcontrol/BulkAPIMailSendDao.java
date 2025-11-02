package jp.co.osaki.osol.api.utility.smcontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import jp.co.osaki.osol.api.dao.smcontrol.BaseSmControlDao;
import jp.co.osaki.osol.api.result.smcontrol.BuildingResult;
import jp.co.osaki.osol.api.result.smcontrol.BuildingSmResult;
import jp.co.osaki.osol.api.result.smcontrol.PersonResult;
import jp.co.osaki.osol.api.result.smcontrol.SmControlVerocityResult;
import jp.co.osaki.osol.api.resultdata.smcontrol.SmPrmResultData;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.BuildingSmServiceDaoImpl;
import jp.co.osaki.osol.api.servicedao.smcontrol.PersonServiceDaoImpl;
import jp.co.osaki.osol.utility.CheckUtility;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * BulkAPIMailSendDaoクラス
 *
 * @author takemura
 */
@Stateless
public class BulkAPIMailSendDao extends BaseSmControlDao {

    /**
     * エラー用ログ
     */
    private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    private final BuildingServiceDaoImpl buildingServiceDaoImpl;
    private final BuildingSmServiceDaoImpl buildingSmServiceDaoImpl;
    private final PersonServiceDaoImpl personServiceDaoImpl;


    public BulkAPIMailSendDao() {
        buildingSmServiceDaoImpl = new BuildingSmServiceDaoImpl();
        buildingServiceDaoImpl = new BuildingServiceDaoImpl();
        personServiceDaoImpl = new PersonServiceDaoImpl();
        }

    public List<SmControlVerocityResult> selectMailBody(List<SmControlVerocityResult> targetList) {

        // 返却用のリストを作成
        List<SmControlVerocityResult> resultList = new ArrayList<>();

        for(SmControlVerocityResult target:targetList) {

            // IPアドレス, SMアドレス取得
            SmPrmResultData smPrm = new SmPrmResultData();
            try {
                smPrm = findSmPrm(target.getSmId());
            } catch (Exception e) {
                // 一度取得しているSMIDなのでここでExceptionを拾う
                errorLogger.error(BaseUtility.getStackTraceMessage(e));
                e.printStackTrace();
            }

            target.setIpAddress(smPrm.getIpAddress());
            target.setSmAddress(smPrm.getSmAddress());

            // 建物テーブル参照用
            BuildingSmResult param = new BuildingSmResult();

            // 機器ID,企業IDから建物IDを取得
            param.setSmId(target.getSmId());
            param.setCorpId(target.getCorpId());
            List<BuildingSmResult> paramPkList;

            // 機器ID,企業IDだけでは主キーではないのでgetResultListを使用
            paramPkList = getResultList(buildingSmServiceDaoImpl, param);

            // 返却値が0の場合例外
            if(paramPkList.size() == 0 ) {
                daoLogger.error(this.getClass().getPackage().getName().concat(" Get PkList failed...  SmId=" + param.getSmId() + "  CorpId=" + param.getCorpId()));
            }

            // 建物情報取得
            for(BuildingSmResult paramPkelm:paramPkList) {
                BuildingResult buildingParam = new BuildingResult();
                buildingParam.setCorpId(paramPkelm.getCorpId());
                buildingParam.setBuildingId(paramPkelm.getBuildingId());
                // 取得処理
                buildingParam = find(buildingServiceDaoImpl,buildingParam);

                String buildingId = String.valueOf(buildingParam.getBuildingId());
                if (!CheckUtility.isNullOrEmpty(target.getBuildingId()) &&
                    target.getBuildingId().equals(buildingId)
                    ) {
                    // targetに設定
                    target.setBuildingName(buildingParam.getBuildingName());
                    target.setBuildingNo(buildingParam.getBuildingNo());
                }
            }

            // 担当者メールアドレス取得
            // 主キーセット
            PersonResult personParam = new PersonResult();
            personParam.setCorpId(target.getCorpId());
            personParam.setPersonId(target.getPersonId());

            // 取得処理
            PersonResult personResult = find(personServiceDaoImpl, personParam);

            // メールアドレスを設定
            target.setMailAddress(personResult.getMailAddress());
            // リストに追加
            resultList.add(target);
        }
        return resultList;
    }

}
