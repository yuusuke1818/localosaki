package jp.co.osaki.sms.dao;

import javax.ejb.Stateless;

import jp.co.osaki.osol.entity.MAlertMailSetting;
import jp.co.osaki.sms.SmsDao;
import jp.co.osaki.sms.servicedao.MAlertMailSettingDaoImpl;

/**
 * 一括送信メールDao
 * @author hayashi_tak
 *
 */
@Stateless
public class MAlertMailSettingDao extends SmsDao {
    MAlertMailSettingDaoImpl daoImpl;

    public MAlertMailSettingDao() {
        daoImpl = new MAlertMailSettingDaoImpl();
    }

    public MAlertMailSetting find(MAlertMailSetting entity){
        return  super.find(daoImpl, entity);
      }

}
