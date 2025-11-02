package jp.co.osaki.sms;

import java.io.File;

import javax.ejb.Stateless;
import javax.servlet.http.Part;

import jp.skygroup.enl.webap.base.BaseFileUpload;

/**
 *
 * ファイルアップロード
 *
 * ファイルアップロードがある画面Beanにて当クラスをinjectして使用する
 *
 * @author maruta.y
 */
@Stateless
public class SmsFileUpload extends BaseFileUpload {

    /**
     *
     * テナント一時ファイルディレクトリ削除
     *
     * @param loginUserId ログインユーザーID
     * @return true:成功 false:失敗
     */
    public boolean tenantBulkTempDirDelete(Long loginUserId) {

        return tempDirDelete(
                getWrapped().getInitParameter("METER_TENANT_BULK_FILE_UPLOAD_TEMP_DIR")
                .concat(File.separator).concat(loginUserId.toString()));
    }

    /**
     *
     * テナント一時ファイルアップロード
     *
     * @param filePart アップロードファイル情報
     * @param loginUserId ログインユーザーID
     * @return アップロード先物理ファイルパス
     */
    public String tenantBulkTempFileUpload(Part filePart, Long loginUserId) {

        return fileUpload(filePart,
                getWrapped().getInitParameter("METER_TENANT_BULK_FILE_UPLOAD_TEMP_DIR")
                .concat(File.separator).concat(loginUserId.toString()),
                getFileName(filePart));
    }

    /**
    *
    * メーター一括登録一時ファイルディレクトリ削除
    *
    * @param loginUserId ログインユーザーID
    * @return true:成功 false:失敗
    */
   public boolean meterBulkTempDirDelete(Long loginUserId) {

       return tempDirDelete(
               getWrapped().getInitParameter("METER_BULK_FILE_UPLOAD_TEMP_DIR")
               .concat(File.separator).concat(loginUserId.toString()));
   }

   /**
    *
    * メーター一括登録一時ファイルアップロード
    *
    * @param filePart アップロードファイル情報
    * @param loginUserId ログインユーザーID
    * @return アップロード先物理ファイルパス
    */
   public String meterBulkTempFileUpload(Part filePart, Long loginUserId) {

       return fileUpload(filePart,
               getWrapped().getInitParameter("METER_BULK_FILE_UPLOAD_TEMP_DIR")
               .concat(File.separator).concat(loginUserId.toString()),
               getFileName(filePart));
   }

   /**
   *
   * メーター一括登録一時ファイルアップロード(保存パス・ファイル名指定)
   *
   * @param filePart アップロードファイル情報
   * @param devId 装置ID
   * @param fileName 保存ファイル名
   * @return アップロード先物理ファイルパス
   */
  public String meterBulkTempFileUpload(Part filePart, String devId, String fileName) {

      return fileUpload(filePart,
              getWrapped().getInitParameter("METER_BULK_FILE_UPLOAD_TEMP_DIR")
                        .concat(File.separator).concat(devId), fileName);
  }
}
