/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.osaki.osol.api.servicedao.sms.meterreading;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import jp.co.osaki.osol.entity.TCommand;
import jp.co.osaki.osol.entity.TCommandPK_;
import jp.co.osaki.osol.entity.TCommand_;
import jp.skygroup.enl.webap.base.BaseServiceDao;

/**
 * コマンド送信 検索 ServiceDaoクラス
 *
 * @author kobayashi.sho
 */
public class CommandServiceDaoImpl implements BaseServiceDao<TCommand> {

    /** 検索条件: デバイスID. */
    public static final String DEV_ID = "devId";

    /** 通信コマンド:コンセントレーター一覧要求. */
    public static final String COMMMAND_CONCENT_LIST = "concent-list";      // ※1
    /** 通信コマンド:メーター一覧要求. */
    public static final String COMMMAND_METER_LIST = "meter-list";
    /** 通信コマンド:メーター情報個別要求. */
    private static final String COMMMAND_GET_METERINFO = "get-meterinfo";   // ※2
    /** 通信コマンド:メーター負荷制限状態確認. */
    private static final String COMMMAND_GET_METERCTRL = "get-meterctrl";   // ※2
    /** 通信コマンド:自動検針月日時要求. */
    public static final String COMMMAND_GET_AUTOINSP = "get-autoinsp";      // ※1
    /** 通信コマンド:スマートパルス入力端末設定内容要求. */
    private static final String COMMMAND_GET_SPITCONF = "get-spitconf";     // ※2
    /** 通信コマンド:中継装置（無線）一覧要求. */
    private static final String COMMMAND_REPEATER_LIST = "repeater-list";
    // ※1 装置IDがコンセントレーター(装置IDの先頭2文字 "XR" or "XS")の場合は、設定一括収集 処理の対象外
    // ※2 「メーター一覧要求」が正常しないとき、設定一括収集 処理が行えない。(警告を出す)

    // 収集コマンド ※TAG is null 限定ではないもの。SRV_ENT の初期値は 1：処理待ち
    private static final List<String> COMMAND_LIST = Arrays.asList(COMMMAND_CONCENT_LIST, COMMMAND_METER_LIST, COMMMAND_GET_AUTOINSP, COMMMAND_REPEATER_LIST);
    // 「メーター情報個別要求、メーター負荷制限状態確認、スマートパルス入力端末設定内容要求」  ※TAG is null 限定のもの。SRV_ENT の初期値は 5:一括処理待ち
    public static final List<String> COMMAND_TAG_IS_NULL_LIST = Arrays.asList(COMMMAND_GET_METERINFO, COMMMAND_GET_METERCTRL, COMMMAND_GET_SPITCONF);

    // 複数件数取得DBアクセス処理
    @Override
    public List<TCommand> getResultList(Map<String, List<Object>> map, EntityManager em) {
        List<Object> devIdList = map.get(DEV_ID);

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = builder.createQuery(TCommand.class);
        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(root.get(TCommand_.id).get(TCommandPK_.devId).in(devIdList));
        whereList.add(builder.or(
            root.get(TCommand_.id).get(TCommandPK_.command).in(COMMAND_LIST),
            builder.and(
                root.get(TCommand_.id).get(TCommandPK_.command).in(COMMAND_TAG_IS_NULL_LIST),
                builder.isNull(root.get(TCommand_.tag)))));

        query = query.select(root)
            .where(builder.and(whereList.toArray(new Predicate[]{})))
            .orderBy(
                builder.asc(root.get(TCommand_.id).get(TCommandPK_.devId)),
                builder.asc(root.get(TCommand_.id).get(TCommandPK_.command)),
                builder.desc(root.get(TCommand_.id).get(TCommandPK_.recDate))
            );

        List<TCommand> entityList = em.createQuery(query).getResultList();

        // divId, command 毎に、重複行を削除
        List<TCommand> resultList = new ArrayList<TCommand>();
        String devIdBak = null;
        String commandBak = null;
        for(TCommand row : entityList) { // ※ソート順: divId, command, recDate(降順)
            if (row.getId().getDevId().equals(devIdBak) && row.getId().getCommand().equals(commandBak)) {
                continue; // 前行とキーが同じ(データが重複している) → スキップ
            }
            resultList.add(row);

            devIdBak = row.getId().getDevId();
            commandBak = row.getId().getCommand();
        }

        return resultList;
    }

    // 検索条件: 装置ID(DEV_ID), 通信コマンド(COMMAND) を条件に rec_date が最大の行を取得
    // @return 取得レコード ※該当データがない場合、null を返す
    @Override
    public TCommand find(TCommand target, EntityManager em) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<TCommand> query = builder.createQuery(TCommand.class);
        Root<TCommand> root = query.from(TCommand.class);

        List<Predicate> whereList = new ArrayList<>();
        whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.devId), target.getId().getDevId()));
        whereList.add(builder.equal(root.get(TCommand_.id).get(TCommandPK_.command), target.getId().getCommand()));
        if (COMMAND_TAG_IS_NULL_LIST.contains(target.getId().getCommand())) {
            whereList.add(builder.isNull(root.get(TCommand_.tag)));
        }

        query = query.select(root)
            .where(builder.and(whereList.toArray(new Predicate[]{})))
            .orderBy(builder.desc(root.get(TCommand_.id).get(TCommandPK_.recDate)));

        List<TCommand> resultList = em.createQuery(query)
                .setMaxResults(1) // limit 1
                .getResultList();
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }

        return resultList.get(0);
    }

    // 新規登録処理
    @Override
    public void persist(TCommand target, EntityManager em) {
        em.persist(target);
    }

    // 削除処理
    @Override
    public void remove(TCommand target, EntityManager em) {
        em.remove(target);
    }

    // 更新処理
    @Override
    public TCommand merge(TCommand target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数件数取得DBアクセス処理
    @Override
    public List<TCommand> getResultList(TCommand target, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // 複数レコード更新削除実行処理.
    @Override
    public int executeUpdate(Map<String, List<Object>> map, EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TCommand> getResultList(List<TCommand> list,
            EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<TCommand> getResultList(EntityManager em) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
