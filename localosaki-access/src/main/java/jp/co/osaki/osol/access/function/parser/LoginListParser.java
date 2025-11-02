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

import jp.co.osaki.osol.access.function.model.LoginModel;
import jp.skygroup.enl.webap.base.BaseUtility;

/**
 *
 * ログイン情報リスト パーサークラス
 *
 * @author take_suzuki
 */
public class LoginListParser implements XmlParserInterface<LoginModel> {

    /**
     * ログイン情報リストファイル
     */
    public static final String XML_FILE = "login.xml";

    /**
     * ログイン情報タグ
     */
    private static final String LOGIN = "Login";

    /**
     * ログイン情報 企業種別タグ
     */
    private static final String CORP_TYPE = "CorpType";

    /**
     * ログイン情報 担当者種別タグ
     */
    private static final String PERSON_TYPE = "PersonType";

    /**
     *
     * ログイン情報リスト取得
     *
     * ログイン情報リストファイルをパースしインスタンス化する。
     *
     * @param xmlFile パースするログイン情報リストファイル
     * @return ログイン情報リスト
     */
    @Override
    public LinkedHashMap<String, LoginModel> parse(File xmlFile) {

        LinkedHashMap<String, LoginModel> tmpLoginList = new LinkedHashMap<>();
        LoginModel login;

        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder(). parse(xmlFile);
            Element root = document.getDocumentElement();
            NodeList loginNodesList = root.getChildNodes();
            for (int i = 0; i < loginNodesList.getLength(); i++) {
                if (S_TEXT.equals(loginNodesList.item(i).getNodeName())) {
                    continue;
                }
                if (LOGIN.equals(loginNodesList.item(i).getNodeName())) {
                    login = new LoginModel();
                    NamedNodeMap loginAttr = loginNodesList.item(i).getAttributes();
                    for (int x = 0; x < loginAttr.getLength(); x++) {
                        switch (loginAttr.item(x).getNodeName()) {
                            case KEY:
                                login.setKey(loginAttr.item(x).getTextContent());
                                break;
                            case NAME:
                                login.setName(loginAttr.item(x).getTextContent());
                                break;
                            default:
                        }
                    }
                    NodeList loginNodes = loginNodesList.item(i).getChildNodes();
                    for (int j = 0; j < loginNodes.getLength(); j++) {
                        if (S_TEXT.equals(loginNodes.item(j).getNodeName())) {
                            continue;
                        }
                        if (CORP_TYPE.equals(loginNodes.item(j).getNodeName())) {
                            login.setCorpType(loginNodes.item(j).getTextContent());
                        }
                        if (PERSON_TYPE.equals(loginNodes.item(j).getNodeName())) {
                            login.setPersonType(loginNodes.item(j).getTextContent());
                        }
                    }
                    tmpLoginList.put(login.getKey(), login);
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
        }

        eventLogger.debug(this.getClass().getName() + " : " + xmlFile.getAbsolutePath() + " => Count : " + tmpLoginList.size());

        return tmpLoginList;
    }

}
