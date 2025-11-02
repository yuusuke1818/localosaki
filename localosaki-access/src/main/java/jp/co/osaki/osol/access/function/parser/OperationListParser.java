package jp.co.osaki.osol.access.function.parser;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jp.co.osaki.osol.access.function.model.OperationModel;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * 操作企業情報リスト パーサークラス
 *
 * @author take_suzuki
 */
public class OperationListParser implements XmlParserInterface<OperationModel> {

    /**
     * 操作企業情報リストファイル
     */
    public static final String XML_FILE = "operation.xml";

    /**
     * 操作企業情報タグ
     */
    private static final String OPERATION = "Operation";

    /**
     * 操作企業情報 企業種別タグ
     */
    private static final String CORP_TYPE = "CorpType";

    /**
     * 操作企業情報 権限種別タグ
     */
    private static final String AUTHORITY_TYPE = "AuthorityType";

    /**
     * 操作企業情報 担当者権限コードタグ
     */
    private static final String AUTHORITY_CD = "AuthorityCd";

    /**
     *
     * 操作企業情報リストのインスタンス取得
     *
     * 操作企業情報リストファイルをパースしインスタンス化する。
     *
     * @param xmlFile パースする操作企業情報リストファイル
     * @return 操作企業情報リスト
     */
    @Override
    public LinkedHashMap<String, OperationModel> parse(File xmlFile) {

        LinkedHashMap<String, OperationModel> tmpOperationList = new LinkedHashMap<>();
        OperationModel operation;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
            Element root = document.getDocumentElement();
            NodeList operationNodesList = root.getChildNodes();
            for (int i = 0; i < operationNodesList.getLength(); i++) {
                if (S_TEXT.equals(operationNodesList.item(i).getNodeName())) {
                    continue;
                }
                if (OPERATION.equals(operationNodesList.item(i).getNodeName())) {
                    operation = new OperationModel();
                    NamedNodeMap operationListAttr = operationNodesList.item(i).getAttributes();
                    for (int x = 0; x < operationListAttr.getLength(); x++) {
                        switch (operationListAttr.item(x).getNodeName()) {
                            case KEY:
                                operation.setKey(operationListAttr.item(x).getTextContent());
                                break;
                            case NAME:
                                operation.setName(operationListAttr.item(x).getTextContent());
                                break;
                            default:
                        }
                    }
                    NodeList operationNodes = operationNodesList.item(i).getChildNodes();
                    for (int j = 0; j < operationNodes.getLength(); j++) {
                        if (S_TEXT.equals(operationNodes.item(j).getNodeName())) {
                            continue;
                        }
                        if (CORP_TYPE.equals(operationNodes.item(j).getNodeName())) {
                            operation.setCorpType(operationNodes.item(j).getTextContent());
                        }
                        if (AUTHORITY_TYPE.equals(operationNodes.item(j).getNodeName())) {
                            operation.setAuthorityType(operationNodes.item(j).getTextContent());
                        }
                        if (AUTHORITY_CD.equals(operationNodes.item(j).getNodeName())) {
                            operation.setAuthorityCd(operationNodes.item(j).getTextContent());
                        }
                    }
                    tmpOperationList.put(operation.getKey(), operation);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        eventLogger.debug(this.getClass().getName() + " : " + xmlFile.getAbsolutePath() + " => Count : " + tmpOperationList.size());
        return tmpOperationList;
    }
}
