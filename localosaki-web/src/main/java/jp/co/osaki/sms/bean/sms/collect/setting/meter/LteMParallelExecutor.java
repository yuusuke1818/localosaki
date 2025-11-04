package jp.co.osaki.sms.bean.sms.collect.setting.meter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

import jp.co.osaki.osol.api.result.sms.collect.setting.meter.LteMMeterExecResult;
import jp.co.osaki.osol.entity.MMeterInfo;
import jp.co.osaki.osol.utility.DateUtility;
import jp.co.osaki.sms.bean.common.HttpRequester;
import jp.co.osaki.sms.deviceCtrl.dao.MMeterInfoDao;
import jp.skygroup.enl.webap.base.BaseConstants.LOGGER_NAME;

/**
 * LTE-M 非同期実行用クラス.
 *
 * @author y.nakamura
 */
@ApplicationScoped
public class LteMParallelExecutor {

    /**
     * イベントログ
     */
    protected static Logger eventLogger = Logger.getLogger(LOGGER_NAME.EVENT.getVal());

    @Resource
    private ManagedExecutorService managedExecutorService;
    @Resource
    private ManagedScheduledExecutorService managedScheduledExecutorService;

    @Inject
    private LteMApiService lteMApiService;

    private static final String DATE_FMT = DateUtility.DATE_FORMAT_YYYYMMDDHHmmss_SLASH;

    /** 開閉制御APIのみ実行か判定するフラグ true:開閉制御APIのみ実行  false:開閉制御API以外も実行 */
    private static boolean execOnlySwitchApiFlg = false;

    /**
     * 非同期でAPI実行
     *
     * @param targetMeterList 対象のメーターリスト
     * @param input 更新対象値
     * @param lteMApiType 更新
     * @param requestId リクエストID
     * @param personId 現在ログインしている担当者の担当者ID
     * @param mMeterInfoDao メーター詳細情報 Dao
     * @return 結果リスト
     */
    public List<LteMMeterExecResult> executeAsyncForMeters(
            List<MeterInfo> targetMeterList,
            MeterManagementInputProperty input,
            String lteMApiType,
            String requestId,
            String personId,
            MMeterInfoDao mMeterInfoDao
    ) {
        // 全件失敗を生成
        final Function<String, List<LteMMeterExecResult>> failAll =
                (msg) -> targetMeterList.stream().map(m -> {
                    LteMMeterExecResult r = new LteMMeterExecResult(m.getMeterMngId());
                    r.fail(msg);
                    return r;
                }).collect(Collectors.toList());

        // ログにエラー表示させる
        if (managedExecutorService == null) {
            logError("ManagedExecutorService not injected");
            return failAll.apply("非同期実行 未初期化(ManagedExecutorService is null)");
        }

        long perRequestTimeoutSec = 0;
        long apiCount = 0;

        // メーターの開閉コードの判定
        MMeterInfo mMeterInfo = new MMeterInfo();
        mMeterInfo.setMeterId(input.getMeterId());
        List<MMeterInfo> mMeterInfoList = mMeterInfoDao.getMMeterInfoList(mMeterInfo);

        // 機能なしの場合
        final BigDecimal NONE = BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.NONE.getCode());

        // 早期リターン用の結果リスト（APIを呼ばない失敗を格納）
        final List<LteMMeterExecResult> earlyResults = new ArrayList<>();

        // 対象meterId一覧
        List<String> ids = targetMeterList.stream()
            .map(MeterInfo::getMeterId)
            .filter(Objects::nonNull)
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .distinct()
            .collect(Collectors.toList());

        // DBより定格情報を取得
        Map<String, List<MMeterInfo>> byMeterId = new LinkedHashMap<>();
        for (String mid : ids) {
            MMeterInfo cond = new MMeterInfo();
            cond.setMeterId(mid);
            byMeterId.put(mid, mMeterInfoDao.getMMeterInfoList(cond));
        }

        // 定格情報が取得不可（0件）
        Set<String> missingMeterIds = ids.stream()
            .filter(mid -> {
                List<MMeterInfo> list = byMeterId.get(mid);
                return list == null || list.isEmpty();
            })
            .collect(Collectors.toCollection(LinkedHashSet::new));

        // 定格情報が重複（2件以上）
        Set<String> duplicatedMeterIds = ids.stream()
            .filter(mid -> {
                List<MMeterInfo> list = byMeterId.get(mid);
                return list != null && list.size() > 1;
            })
            .collect(Collectors.toCollection(LinkedHashSet::new));

        // API対象（MMeterInfoが1件のmeterIdに紐づくMeterInfoのみ）
        List<MeterInfo> uniqueTargets = targetMeterList.stream()
            .filter(mi -> {
                String mid = mi.getMeterId();
                if (mid == null || mid.trim().isEmpty()) return false;
                if (missingMeterIds.contains(mid) || duplicatedMeterIds.contains(mid)) return false;
                List<MMeterInfo> list = byMeterId.get(mid);
                return list != null && list.size() == 1;
            })
            .collect(Collectors.toList());

        // 取得不可エラー（API実行しない = earlyResultsに失敗として格納）
        if (!missingMeterIds.isEmpty()) {
            String joined = String.join(", ", missingMeterIds);
            String msgLog = String.format("計器の定格電流の情報が取得できません。 計器ID:%s", joined);
            System.out.println(msgLog);
            eventLogger.error(msgLog);

            // 該当meterIdの情報を「API未実行の失敗」として早期リターンリストに格納
            targetMeterList.stream()
                .filter(mi -> missingMeterIds.contains(mi.getMeterId()))
                .forEach(mi -> {
                    LteMMeterExecResult r = new LteMMeterExecResult(mi.getMeterMngId());
                    r.fail(msgLog);
                    earlyResults.add(r);
                });
        }

        // 重複エラー（API実行しない = earlyResultsに失敗として格納）
        if (!duplicatedMeterIds.isEmpty()) {
            String joined = String.join(", ", duplicatedMeterIds);
            String msgLog = String.format("計器の定格電流の情報が重複しております。 計器ID:%s", joined);
            System.out.println(msgLog);
            eventLogger.error(msgLog);

            targetMeterList.stream()
                .filter(mi -> duplicatedMeterIds.contains(mi.getMeterId()))
                .forEach(mi -> {
                    LteMMeterExecResult r = new LteMMeterExecResult(mi.getMeterMngId());
                    r.fail(msgLog);
                    earlyResults.add(r);
                });
        }

        // API実行対象リストを取得
        List<MeterInfo> apiTargets = new ArrayList<>();
        for (MeterInfo mi : uniqueTargets) {
            List<MMeterInfo> list = byMeterId.get(mi.getMeterId()); // size==1確定
            MMeterInfo mm = list.get(0);
            BigDecimal sw = mm.getSwitchCode();

            if (sw != null && sw.compareTo(NONE) == 0) {
                // 開閉機能なし
                // 「開閉機能なし」は機能がないだけで正常と判定するためコメントアウト（今後異常判定にする際にはコメントアウト解除する）
//                String msg = String.format("【メーター管理番号:%d】 開閉機能なしのためAPI実行対象外 (計器ID:%s)",
//                        mi.getMeterMngId(), mi.getMeterId());
                LteMMeterExecResult r = new LteMMeterExecResult(mi.getMeterMngId());
//                r.fail(msg);
                r.setApiExecFlg(false);
                earlyResults.add(r);
            } else {
                // API実行対象リストに格納
                apiTargets.add(mi);
            }
        }

        // タイムアウト設定
        if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode())) {
            // 現在値取得の場合
            // 同期APIの場合、最大2回API(開閉制御API + 負荷制限API)を実行するためタイムアウトも2倍に設定
            apiCount = 2;
            perRequestTimeoutSec = MeterManagementConstants.TIMEOUT_SEC * 2L + MeterManagementConstants.TIMEOUT_SEC;

        } else if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
            // 設定変更や一括登録の場合
            if (mMeterInfoList != null && !mMeterInfoList.isEmpty()
                    && (mMeterInfoList.get(0).getSwitchCode().compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                    || mMeterInfoList.get(0).getSwitchCode().compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                // 開閉コード判定  開閉制御APIのみ実施する場合
                apiCount = 1;
                perRequestTimeoutSec = MeterManagementConstants.TIMEOUT_SEC + Math.round(MeterManagementConstants.TIMEOUT_SEC / 2.0);
                execOnlySwitchApiFlg = true;
            } else {
                // 負荷制限(loadlimit_mode)が0(無効)の場合、最大3回API(開閉制御API + 負荷制限API2回)を実行するためタイムアウトも3倍に設定
                apiCount =
                        (input.isSendFlgSwitch() ? 1 : 0)
                      + (input.isSendFlgLoadlimit() ? 1 : 0)
                      + (MeterManagementConstants.LOADLIMIT_MODE.DISABLED.getValue()
                             .equals(input.getLoadlimitMode()) ? 1 : 0);
                long delaySeconds = (input.isSendFlgSwitch() && input.isSendFlgLoadlimit())
                                    ? MeterManagementConstants.DELAY_TIME : 0;
                perRequestTimeoutSec = MeterManagementConstants.TIMEOUT_SEC * apiCount  + Math.round((MeterManagementConstants.TIMEOUT_SEC * apiCount) / 2.0) + delaySeconds;
            }
        } else {
            // その他の場合
            perRequestTimeoutSec = MeterManagementConstants.TIMEOUT_SEC + Math.round(MeterManagementConstants.TIMEOUT_SEC / 2.0);
        }

        // API実行対象が無い場合
        if (apiTargets.isEmpty()) {
            // API未実行分を格納
            List<LteMMeterExecResult> results = new ArrayList<>();
            results.addAll(earlyResults);
            return results;
        }

        System.out.println(String.format("【requestId:%s】 【personId:%s】 1メーターに対するタイムアウト設定：%d秒 API実行数:%d ※猶予時間(タイムアウト設定の半分)含む"
                , requestId, personId, perRequestTimeoutSec, apiCount));
        eventLogger.info(String.format("【requestId:%s】 【personId:%s】 1メーターに対するタイムアウト設定：%d秒 API実行数:%d ※猶予時間(タイムアウト設定の半分)含む"
                , requestId, personId, perRequestTimeoutSec, apiCount));
        System.out.println(String.format("今回実行メーター数:%d", apiTargets.size()));
        eventLogger.info(String.format("今回実行メーター数:%d", apiTargets.size()));
        // 合計タイムアウトを算出
        long totalTimeoutSec = Math.multiplyExact(perRequestTimeoutSec, (long) apiTargets.size());
        long totalApiCount   = Math.multiplyExact((long) apiCount, (long) apiTargets.size());
        System.out.println(String.format("【requestId:%s】 【personId:%s】 今回実行分合計のタイムアウト設定：%d秒 API実行数:%d ※猶予時間(タイムアウト設定の半分)含む"
                , requestId, personId, totalTimeoutSec, totalApiCount));
        eventLogger.info(String.format("【requestId:%s】 【personId:%s】 今回実行分合計のタイムアウト設定：%d秒 API実行数:%d ※猶予時間(タイムアウト設定の半分)含む"
                , requestId, personId, totalTimeoutSec, totalApiCount));

        final long timeout = perRequestTimeoutSec;
        List<CompletableFuture<LteMMeterExecResult>> futures = new ArrayList<CompletableFuture<LteMMeterExecResult>>();

        futures = apiTargets.stream()
                .map(meterInfo -> {
                    CompletableFuture<LteMMeterExecResult> cf =
                            CompletableFuture
                            .supplyAsync(() -> 0, managedExecutorService) // 非同期実行開始
                            .thenCompose(v -> {
                                CompletableFuture<LteMMeterExecResult> workFuture = 
                                    execForOneMeterBySwitchCode(meterInfo, input, execOnlySwitchApiFlg, mMeterInfoDao, lteMApiType);
                                checkTimeout(workFuture, timeout, TimeUnit.SECONDS, managedScheduledExecutorService);
                                return workFuture;
                            });

                    return cf.handle((res, ex) -> {
                        if (ex == null) {
                            if (res != null) return res;
                            LteMMeterExecResult lteMMeterExecResult = new LteMMeterExecResult(meterInfo.getMeterMngId());
                            lteMMeterExecResult.fail(String.format("【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 実行結果がnull", requestId, personId, meterInfo.getMeterMngId()));
                            return lteMMeterExecResult;
                        }
                        Throwable cause = unwrap(ex);
                        eventLogger.errorf(cause,
                            "【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 設定API例外 apiType=%s timeout=%d sec thread=%s",
                            requestId, personId, meterInfo.getMeterMngId(),
                            lteMApiType, timeout,
                            Thread.currentThread().getName());

                        String msg = (cause instanceof TimeoutException)
                            ? String.format("【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 タイムアウトしましたが設定は機器に送信されました。タイムアウト設定時間:%d秒 ※猶予時間(タイムアウト設定の半分)含む",
                                    requestId, personId, meterInfo.getMeterMngId(), timeout)
                            : String.format("【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 設定API例外: %s",
                                    requestId, personId, meterInfo.getMeterMngId(),
                                            (cause.getMessage() != null ? cause.getMessage() : cause.toString()));
                        LteMMeterExecResult result = new LteMMeterExecResult(meterInfo.getMeterMngId());
                        result.fail(msg);
                        return result;
                    });
                })
                .collect(Collectors.toList());

        // 全体はタイムアウトを設定せず完了まで待機（個別でタイムアウト設定済みのため）
        CompletableFuture<Void> all = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        all.join();

        // 非同期のAPI結果
        List<LteMMeterExecResult> asyncResults = futures.stream()
            .map(CompletableFuture::join)
            .collect(Collectors.toList());

        // API実行結果とAPI未実行分を統合
        List<LteMMeterExecResult> results = new ArrayList<>();
        results.addAll(earlyResults);
        results.addAll(asyncResults);

        // ログ出力
        results.forEach(r -> {
            String reason = (r.getMessages()==null || r.getMessages().isEmpty())
                ? "詳細なし"
                : r.getMessages().stream().filter(Objects::nonNull).map(String::trim)
                      .filter(s -> !s.isEmpty()).collect(Collectors.joining(" / "));
            if (r.isSuccess()) {
                logInfo("【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 API実行成功", requestId, personId, r.getMeterMngId());
            } else {
                logError("【requestId:%s】 【personId:%s】 【メーター管理番号:%d】 API実行失敗 理由:%s", requestId, personId, r.getMeterMngId(), reason);
            }
        });
        return results;
    }

    /**
     * 開閉コード(switch_code)に応じてAPI実行
     *
     * @param meterInfo 対象メーター情報
     * @param baseInput 更新対象値
     * @param mMeterInfoDao メーター詳細情報 Dao
     * @param execOnlySwitchApiFlg 開閉制御APIのみ実行か判定するフラグ true:開閉制御APIのみ実行  false:開閉制御API以外も実行
     * @param lteMApiType LTE-MのAPI種別
     * @return futureオブジェクト
     */
    private CompletableFuture<LteMMeterExecResult> execForOneMeterBySwitchCode(
            MeterInfo meterInfo,
            MeterManagementInputProperty baseInput,
            boolean execOnlySwitchApiFlg,
            MMeterInfoDao mMeterInfoDao,
            String lteMApiType) {

        // 開閉制御APIのみ実行か判定するフラグを初期化
        execOnlySwitchApiFlg = false;

        // メーターの開閉コード(switch_code)の取得
        BigDecimal code = null;
        MMeterInfo mMeterInfo = new MMeterInfo();
        mMeterInfo.setMeterId(meterInfo.getMeterId());
        List<MMeterInfo> mMeterInfoList = mMeterInfoDao.getMMeterInfoList(mMeterInfo);

        // メータID(計器ID)でのDB取得結果を判定
        if (mMeterInfoList != null && mMeterInfoList.size() == 1) {
            code = mMeterInfoList.get(0).getSwitchCode();
        }

        // 開閉コード(switch_code)が「null」の場合、API実行せず、失敗として返す
        if (code == null) {
            LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
            r.fail(String.format("計器IDの開閉コード情報が存在しないため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                    , meterInfo.getMeterMngId()
                    , meterInfo.getMeterId()
                    , code));
            return CompletableFuture.completedFuture(r);
        }

        // 開閉コード(switch_code)が「0:機能なし」の場合、API実行しない（成功扱いで即返す）
        if (code != null && code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.NONE.getCode())) == 0) {
            LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
            r.setSuccess(true); // スキップ＝失敗ではない
            return CompletableFuture.completedFuture(r);
        }

        MeterManagementInputProperty inputProperty = new MeterManagementInputProperty(baseInput);

        if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode())) {
            // 現在値取得の場合
            if (code != null && code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_LOADLIMIT.getCode())) == 0) {
                // 開閉コード(switch_code)が「2」の場合、開閉制御API＋負荷制限API実行
                inputProperty.setSendFlgSwitch(true);
                inputProperty.setSendFlgLoadlimit(true);

            } else if (code != null && (code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                    || code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                // 開閉コード(switch_code)が「1 or 3」の場合、 開閉制御APIのみ実行
                inputProperty.setSendFlgSwitch(true);
                inputProperty.setSendFlgLoadlimit(false);
                execOnlySwitchApiFlg = true;

            } else {
                // 想定外の開閉コード(switch_code)の場合
                LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
                r.fail(String.format("計器IDの開閉コード情報が想定外の値のため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                        , meterInfo.getMeterMngId()
                        , meterInfo.getMeterId()
                        , code));
                return CompletableFuture.completedFuture(r);
            }

        } else if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
            // 設定内容変更の場合
            if (code != null && code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_LOADLIMIT.getCode())) == 0) {
                // 開閉コード(switch_code)が「2」の場合、開閉制御API＋負荷制限API実行
                // 「開閉制御API」と「負荷制限API」の実行フラグは画面での設定値のまま

            } else if (code != null && (code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                    || code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                // 開閉コード(switch_code)が「1 or 3」の場合、 開閉制御APIのみ実行
                inputProperty.setSendFlgLoadlimit(false);
                execOnlySwitchApiFlg = true;

            } else {
                // 想定外の開閉コード(switch_code)の場合
                LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
                r.fail(String.format("計器IDの開閉コード情報が想定外の値のため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                        , meterInfo.getMeterMngId()
                        , meterInfo.getMeterId()
                        , code));
                return CompletableFuture.completedFuture(r);
            }
        } else {
            // その他(一括登録)の場合
            if (code != null && code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_LOADLIMIT.getCode())) == 0) {
                // 開閉コード(switch_code)が「2」の場合、開閉制御API＋負荷制限API実行
                inputProperty.setSendFlgSwitch(true);
                inputProperty.setSendFlgLoadlimit(true);

            } else if (code != null && (code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.BASIC.getCode())) == 0
                    || code.compareTo(BigDecimal.valueOf(MeterManagementConstants.SWITCH_CODE.WITH_TS.getCode())) == 0)) {
                // 開閉コード(switch_code)が「1 or 3」の場合
                LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
                r.fail(String.format("計器IDの開閉コード情報が想定外の値のため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                        , meterInfo.getMeterMngId()
                        , meterInfo.getMeterId()
                        , code));
                return CompletableFuture.completedFuture(r);

            } else {
                // 想定外の開閉コード(switch_code)の場合
                LteMMeterExecResult r = new LteMMeterExecResult(meterInfo.getMeterMngId());
                r.fail(String.format("計器IDの開閉コード情報が想定外の値のため設定内容変更できません。 メーター管理番号:%s 計器ID:%s 開閉コード:%s"
                        , meterInfo.getMeterMngId()
                        , meterInfo.getMeterId()
                        , code));
                return CompletableFuture.completedFuture(r);
            }
        }
        return execForOneMeter(meterInfo, inputProperty, execOnlySwitchApiFlg, lteMApiType);
    }

    /**
     * 指定時間で未完了の場合TimeoutException例外
     * <p>
     * 本メソッドは<em>非同期・非ブロッキング</em>であり、待機スレッドを占有しない。
     * タイムアウト監視は引数の{@code scheduler}にスケジュールしたタスクで行う。
     * 監視対象のFutureが先に正常／例外完了した場合は、スケジュール済みタスクをキャンセルする。
     * <p>
     * @param <T> 監視対象{@link CompletableFuture}の結果型
     * @param completableFuture タイムアウト監視する対象の Future
     * @param timeout タイムアウト値
     * @param unit{@code timeout}の時間単位
     * @param scheduler タイムアウト監視に用いる
     * @return 引数で渡した{@code completableFuture}自体（同一インスタンス）
     */
    public static <T> CompletableFuture<T> checkTimeout(
            CompletableFuture<T> completableFuture,
            long timeout, TimeUnit unit,
            ScheduledExecutorService scheduler) {

        final ScheduledFuture<?> killer = scheduler.schedule(
            () -> completableFuture.completeExceptionally(new TimeoutException(
                    "timeout after " + timeout + " " + unit.toString().toLowerCase())),
            timeout, unit);

        // 早期完了したらタイマーをキャンセルして漏れを防ぐ
        completableFuture.whenComplete((r, e) -> killer.cancel(false));
        return completableFuture; // 同じCompletableFutureを返す＝呼び出し元のオブジェクトをそのまま使用
    }

    /**
     * エラーログ出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logError(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.error(msg);
    }

    /**
     * イベントログ出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logInfo(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.info(msg);
    }

    /**
     * 例外のラップを剥がすユーティリティ
     *
     * @param ex 例外
     * @return ラップされていない例外
     */
    private static Throwable unwrap(Throwable ex) {
        while (ex instanceof CompletionException
                || ex instanceof ExecutionException) {
            if (ex.getCause() == null)
                break;
            ex = ex.getCause();
        }
        return ex;
    }

    /**
     * エラーログ出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logNotifyError(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.error(msg);
    }

    /**
     * イベントログ出力
     * @param format 出力文字列
     * @param args 出力パラメータ
     */
    public void logNotifyInfo(String format, Object... args) {
        String msg = String.format(format, args);
        System.out.println(msg);
        eventLogger.info(msg);
    }

    /**
     * メータ1台分の設定API実行
     *
     * @param meterInfo メーター取得結果
     * @param input 画面からの入力情報
     * @param execOnlySwitchApiFlg 開閉制御APIのみ実行か判定するフラグ  true:開閉制御APIのみ実行  false:開閉制御API以外も実行
     * @param lteMApiType API種別
     * @return Futureオブジェクト
     */
    private CompletableFuture<LteMMeterExecResult> execForOneMeter(
            MeterInfo meterInfo
            , MeterManagementInputProperty input
            , boolean execOnlySwitchApiFlg
            , String lteMApiType) {

        String apiTypeStr = null;
        if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
            apiTypeStr = "設定API";
        } else {
            apiTypeStr = "同期API";
        }

        System.out.println(String.format("【メーター管理番号:%d】 %s実行", meterInfo.getMeterMngId(), apiTypeStr));
        eventLogger.info(String.format("【メーター管理番号:%d】 %s実行", meterInfo.getMeterMngId(), apiTypeStr));

        Gson gson = new Gson();
        LteMMeterExecResult result = new LteMMeterExecResult(meterInfo.getMeterMngId());

        // 開閉制御を実行する時だけ開閉制御APIの開始時刻を持つ
        final Instant switchStartAt = input.isSendFlgSwitch() ? Instant.now() : null;

        // ---- 開閉制御 ----
        if (input.isSendFlgSwitch()) {
            HttpRequester switchApiHttp = new HttpRequester(LteMParallelExecutor.class.getPackage().getName());
            String setDateStr = null;

            boolean apiResultFlg = false;
            if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
                apiResultFlg = lteMApiService.execSwitchApiSet(switchApiHttp, meterInfo, input);
            } else {
                apiResultFlg = lteMApiService.execSwitchApiSync(switchApiHttp, meterInfo);
            }

            if (!apiResultFlg) {
                result.fail(String.format("【メーター管理番号:%d】 [開閉制御API] %s呼出し失敗", meterInfo.getMeterMngId(), apiTypeStr));
            } else {
                String dateHeader = switchApiHttp.getResponseHeaderValue("Date");
                if (isBlank(dateHeader)) {
                    result.fail(String.format("【メーター管理番号:%d】 [開閉制御API] Dateヘッダーなし", meterInfo.getMeterMngId()));
                } else {
                    try {
                        // RFC1123→Instant→Date
                        Instant serverInstant = ZonedDateTime.parse(
                                dateHeader, MeterManagementConstants.RFC1123).toInstant();
                        Date setDate = Date.from(serverInstant);
                        setDateStr = new SimpleDateFormat(DATE_FMT).format(setDate);
                    } catch (DateTimeParseException e) {
                        result.fail(String.format("【メーター管理番号:%d】 [開閉制御API] Date解析失敗: %s", meterInfo.getMeterMngId(), dateHeader));
                    }
                }
                String resBody = switchApiHttp.getResponseBody();
                if (setDateStr == null) {
                    result.fail(String.format("【メーター管理番号:%d】 [開閉制御API] 実行日時取得不可", meterInfo.getMeterMngId()));
                } else {
                    try {
                        SwitchApiResDto resDto = gson.fromJson(resBody, SwitchApiResDto.class);
                        resDto.setHttpResponseCode(switchApiHttp.getResponseCode());
                        result.setSuccess(true);
                    } catch (Exception e) {
                        result.fail(String.format("【メーター管理番号:%d】 [開閉制御API] 例外:%s", meterInfo.getMeterMngId(), e.getMessage()));
                    }
                }
            }
        }

        // ---- 負荷制限 ----
        // ---- 非ブロッキング遅延（delayedExecutor）で負荷制限ブロックをスケジュール ----
        if (input.isSendFlgLoadlimit() && !execOnlySwitchApiFlg) {
            long remainMs = 0L;
            if (switchStartAt != null) { // 開閉制御とセットで呼ばれた時だけ遅延する
                long delayMillis = TimeUnit.SECONDS.toMillis(MeterManagementConstants.DELAY_TIME);
                long elapsedMs = java.time.Duration.between(switchStartAt, Instant.now()).toMillis();
                remainMs = Math.max(0L, delayMillis - elapsedMs);
            }

            final long delay = remainMs;
            if (remainMs > 0L) {
                // 開閉制御APIを先行で実行している場合
                System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API] %dms 後にリクエストを遅延実行", meterInfo.getMeterMngId(), remainMs));
                eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API] %dms 後にリクエストを遅延実行", meterInfo.getMeterMngId(), remainMs));
            } else {
                // 負荷制限APIのみ実行の場合
                System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API] 遅延不要(遅延時間が0 もしくは 負荷制限APIのみ実行)のため即時実行", meterInfo.getMeterMngId()));
                eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API] 遅延不要(遅延時間が0 もしくは 負荷制限APIのみ実行)のため即時実行", meterInfo.getMeterMngId()));
            }

            // 負荷制限APIの実行
            return delay(delay).thenCompose(v -> loadlimitExecBlock(meterInfo, input, result, lteMApiType));
        }
        return CompletableFuture.completedFuture(result);
    }

    /**
     * 負荷制限API実行
     *
     * input.getLoadlimitMode() == "0" の場合、
     * input.setLoadlimitTarget("temp"), input.setLoadlimitMode("R") としてもう一度送る
     *
     * @param meterInfo メーター取得結果
     * @param input 画面からの入力情報
     * @param result 実行結果
     * @param lteMApiType API種別
     * @return CompletableFutureオブジェクト
     */
    private CompletableFuture<LteMMeterExecResult> loadlimitExecBlock(
            MeterInfo meterInfo,
            MeterManagementInputProperty input,
            LteMMeterExecResult result,
            String lteMApiType) {

        final String apiTypeStr =
                Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())
                ? "設定API"
                : "同期API";

        HttpRequester loadlimitApiHttp = new HttpRequester(LteMParallelExecutor.class.getPackage().getName());
        String setDateStr = null;

        boolean firstOk = false;
        final String baseLoadlimitMode = input.getLoadlimitMode();

        // ---- 1回目API実行----
        System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] %s実行 開始", meterInfo.getMeterMngId(), apiTypeStr));
        eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] %s実行 開始", meterInfo.getMeterMngId(), apiTypeStr));

        final Instant reqStart = Instant.now();
        boolean apiResult = false;
        if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
            apiResult = lteMApiService.execLoadlimitApiSet(loadlimitApiHttp, meterInfo, input);
        } else {
            apiResult = lteMApiService.execLoadlimitApiSync(loadlimitApiHttp, meterInfo);
        }

        if (!apiResult) {
            result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] %s呼出し失敗", meterInfo.getMeterMngId(), apiTypeStr));
        } else {
            String dateHeader = loadlimitApiHttp.getResponseHeaderValue("Date");
            if (isBlank(dateHeader)) {
                result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] Dateヘッダーなし", meterInfo.getMeterMngId()));
            } else {
                try {
                    Instant serverInstant = ZonedDateTime.parse(dateHeader, MeterManagementConstants.RFC1123).toInstant();
                    Date setDate = Date.from(serverInstant);
                    setDateStr = new SimpleDateFormat(DATE_FMT).format(setDate);
                } catch (DateTimeParseException e) {
                    result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] Date解析失敗: %s", meterInfo.getMeterMngId(), dateHeader));
                }
            }
            String resBody = loadlimitApiHttp.getResponseBody();
            if (setDateStr == null) {
                result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] 実行日時取得不可", meterInfo.getMeterMngId()));
            } else {
                try {
                    LoadlimitApiResDto resDto = new Gson().fromJson(resBody, LoadlimitApiResDto.class);
                    resDto.setHttpResponseCode(loadlimitApiHttp.getResponseCode());
                    firstOk = true;
                } catch (Exception e) {
                    result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] 例外:%s", meterInfo.getMeterMngId(), e.getMessage()));
                }
            }
        }
        System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] %s実行 終了", meterInfo.getMeterMngId(), apiTypeStr));
        eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 1回目] %s実行 終了", meterInfo.getMeterMngId(), apiTypeStr));

        // 同期APIの場合、1回のみ実行のため返却
        if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SYNC.getCode())) {
            return CompletableFuture.completedFuture(result);
        }

        // ---- 2回目API実行(設定時間分遅延実行）----
        if (firstOk && MeterManagementConstants.LOADLIMIT_MODE.DISABLED.getValue().equals(baseLoadlimitMode)) {
            System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %s実行 開始", meterInfo.getMeterMngId(), apiTypeStr));
            eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %s実行 開始", meterInfo.getMeterMngId(), apiTypeStr));

            MeterManagementInputProperty tempRelease = copyForTempRelease(input);

            long delayMillis = TimeUnit.SECONDS.toMillis(MeterManagementConstants.DELAY_TIME);
            long remainMs = Math.max(0L, delayMillis - Duration.between(reqStart, Instant.now()).toMillis());

            Executor exec = (remainMs <= 0L)
                    ? managedExecutorService
                    : (Runnable command) -> managedScheduledExecutorService.schedule(
                              () -> managedExecutorService.execute(command), remainMs, TimeUnit.MILLISECONDS);

            if (remainMs > 0L) {
                // 遅延時間がある場合
                System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %dms 後にリクエストを遅延実行", meterInfo.getMeterMngId(), remainMs));
                eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %dms 後にリクエストを遅延実行", meterInfo.getMeterMngId(), remainMs));
            } else {
                // 遅延時間がない場合
                System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] 遅延不要(遅延時間が0 もしくは 負荷制限APIのみ実行)のため即時実行", meterInfo.getMeterMngId()));
                eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] 遅延不要(遅延時間が0 もしくは 負荷制限APIのみ実行)のため即時実行", meterInfo.getMeterMngId()));
            }

            CompletableFuture<Boolean> second = CompletableFuture.supplyAsync(() -> {
                HttpRequester loadlimitApiHttpSecond = new HttpRequester(LteMParallelExecutor.class.getPackage().getName());
                boolean apiResultSecond = false;
                if (Objects.equals(lteMApiType, MeterManagementConstants.LTEM_API_TYPE.SET.getCode())) {
                    apiResultSecond = lteMApiService.execLoadlimitApiSet(loadlimitApiHttpSecond, meterInfo, tempRelease);
                } else {
                    apiResultSecond = lteMApiService.execLoadlimitApiSync(loadlimitApiHttpSecond, meterInfo);
                }

                if (!apiResultSecond) {
                    result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %s呼出し失敗", meterInfo.getMeterMngId(), apiTypeStr));
                    return false;
                }
                String dateHeaderSecond = loadlimitApiHttpSecond.getResponseHeaderValue("Date");
                if (dateHeaderSecond == null || dateHeaderSecond.isEmpty()) {
                    result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] Dateヘッダーなし", meterInfo.getMeterMngId()));
                    return false;
                }
                try {
                    String resBodySecond = loadlimitApiHttpSecond.getResponseBody();
                    LoadlimitApiResDto resDtoSecond = new Gson().fromJson(resBodySecond, LoadlimitApiResDto.class);
                    resDtoSecond.setHttpResponseCode(loadlimitApiHttpSecond.getResponseCode());
                    return true;
                } catch (Exception e) {
                    result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] 例外:%s", meterInfo.getMeterMngId(), e.getMessage()));
                    return false;
                }
            }, exec);

            second.whenComplete((ok, ex) -> {
                try {
                    if (ex != null) {
                        Throwable cause = unwrap(ex);
                        result.fail(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] 例外:%s",
                                meterInfo.getMeterMngId(),
                                cause.getMessage() != null ? cause.getMessage() : cause.toString()));
                    } else if (Boolean.TRUE.equals(ok)) {
                        result.setSuccess(true);
                    }
                } finally {
                    System.out.println(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %s実行 終了", meterInfo.getMeterMngId(), apiTypeStr));
                    eventLogger.info(String.format("【メーター管理番号:%d】 [負荷制限API 2回目] %s実行 終了", meterInfo.getMeterMngId(), apiTypeStr));
                }
            });
            // 2回目の完了を待つ CompletableFuture を返す
            return second.handle((ok, ex) -> result);

        } else if (firstOk) {
            result.setSuccess(true);
        }

        return CompletableFuture.completedFuture(result);
    }

    /**
     * 遅延時間をFutureへ変換する
     * @param ms 遅延時間
     * @return Futureオブジェクト
     */
    private CompletableFuture<Void> delay(long ms) {
        if (ms <= 0) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<Void> f = new CompletableFuture<>();
        managedScheduledExecutorService.schedule(() -> f.complete(null), ms, TimeUnit.MILLISECONDS);
        return f;
    }

    /**
     * MeterManagementInputPropertyをコピー
     *
     * @param srcProperty コピー元
     * @return コピー結果
     */
    private MeterManagementInputProperty copyForTempRelease(MeterManagementInputProperty srcProperty) {
        MeterManagementInputProperty targetProperty = new MeterManagementInputProperty(srcProperty);
        // 以下項目は上書き
        targetProperty.setLoadlimitTarget("temp");
        targetProperty.setLoadlimitMode("R");
        return targetProperty;
    }

    /**
     * null もしくは 空判定
     */
    private static boolean isBlank(String s) {
        return s == null || s.codePoints().allMatch(Character::isWhitespace);
    }

    /** 非同期実行に使うコンテナ管理Executorを公開する */
    public Executor getExecutor() {
        return managedExecutorService;
    }

}
