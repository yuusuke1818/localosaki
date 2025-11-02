package jp.co.osaki.osol.api.utility.smcontrol;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Named;

import org.beanio.StreamFactory;
import org.jboss.logging.Logger;

import jp.co.osaki.osol.mng.constants.SmControlConstants;
import jp.co.osaki.osol.mng.constants.SmControlConstants.BEANIO_MAPPING_API_ID;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 * BeanIOLoader Utiltiyクラス
 *
 * @author shimizu
 */
@Named(value = "factoryLoaderUtility")
@ApplicationScoped
public class FactoryLoaderUtility {

	/**
	 * エラー用ログ
	 */
	private static Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

	/**
	 * 固定長変換ファクトリーMap
	 */
	private Map<String, StreamFactory> factoryMap = new HashMap<>();

	/**
	 * 初期化 サーバー起動時マッピングファイルを全件取得する
	 *
	 * @param event
	 */
	public void handle(@Observes @Initialized(ApplicationScoped.class) Object event) throws Exception {
		// マッピングファイルの読み込み
		for (BEANIO_MAPPING_API_ID id :SmControlConstants.BEANIO_MAPPING_API_ID.values()) {
			try {
				StreamFactory factory = StreamFactory.newInstance();
				String resourcePath = SmControlConstants.MAPPING_FILE_RESOURCES_FOLDER + id.toString() + SmControlConstants.MAPPING_FILE_EXTENSION;
				factory.load(getClass().getResourceAsStream(resourcePath));

				// 機器制御API 対象機器
				String[] productCdArray = {
						SmControlConstants.PRODUCT_CD_FV2,
						SmControlConstants.PRODUCT_CD_FVP_D,
						SmControlConstants.PRODUCT_CD_FVP_ALPHA_D,
						SmControlConstants.PRODUCT_CD_FVP_ALPHA_G2,
						SmControlConstants.PRODUCT_CD_E_ALPHA,
						SmControlConstants.PRODUCT_CD_E_ALPHA_2};

				// 対象機器のプロダクトコードに対してチェック
				for(String productCd : productCdArray) {
					String reqStreamName = productCd + SmControlConstants.TO_FIXEDSTRING_STREAM_NAME_POSTFIX;
					String resStreamName = productCd + SmControlConstants.FROM_FIXEDSTRING_STREAM_NAME_POSTFIX;
					if(!factory.isMapped(reqStreamName) || !factory.isMapped(resStreamName)) {
						throw new Exception("XML不正");
					}
				}
				factoryMap.put(id.toString(), factory);
			} catch (Exception e) {
				errorLogger.error(BaseUtility.getStackTraceMessage(e));
				throw e;

			}
		}
	}

	/**
	 * BeanIO 固定長-Bean変換ファクトリー取得
	 *
	 * @param apiId
	 * @return BeanIO StreamFacrotyクラス
	 */
	public StreamFactory getFacrory(String apiId) {
		return factoryMap.get(apiId);
	}

}
