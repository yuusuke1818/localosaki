package jp.skygroup.enl.webap.base;

import java.security.Key;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.jboss.logging.Logger;

/**
 * Baseユーティリティクラス
 *
 * Baseクラスのみで使用するメソッド提供するクラス
 * 
 * @author d-komatsubara
 */
public abstract class BaseUtility extends BaseConstants {

    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * 前と後ろに%をつけて返却する
     *
     * @param org 対象文字列
     * @return 前後に%が付与された文字列
     */
    public static String addSqlWildcard(String org) {
        return "%".concat(org).concat("%");
    }

    /**
     * BigDecimalクラスの足し算を行う。<br>
     * 引数にNULLが含まれた場合0に変換し計算を行う。<br>
     *
     * @param first
     * @param second
     * @return
     */
    public static BigDecimal addBigDecimal(BigDecimal first, BigDecimal second) {
        if (first == null) {
            first = BigDecimal.ZERO;
        }
        if (second == null) {
            second = BigDecimal.ZERO;
        }
        return first.add(second);
    }

    /**
     * スタックトレースメッセージを一行にする
     *
     * @param ex Exception
     * @return スタックトレースメッセージ
     */
    public static String getStackTraceMessage(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String str = "";
        try {
            ex.printStackTrace(pw);
            pw.flush();
            str = sw.toString();
            pw.close();
            sw.close();
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * スタックトレースメッセージを一行にする
     *
     * @param ex Throwable
     * @return スタックトレースメッセージ
     */
    public static String getStackTraceMessage(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        String str = "";
        try {
            ex.printStackTrace(pw);
            pw.flush();
            str = sw.toString();
            pw.close();
            sw.close();
        } catch (Exception e) {
        }
        return str;
    }

    /**
     * リクエストトークン生成
     *
     * @return リクエストトークン
     */
    public static String createRequestToken() {

        byte token[] = new byte[32];
        StringBuilder buf = new StringBuilder();
        SecureRandom random = null;

        try {
            random = SecureRandom.getInstance(BaseConstants.REQUEST_TOKEN.SHA1.getVal());
            random.nextBytes(token);

            for (int i = 0; i < token.length; i++) {
                buf.append(String.format(BaseConstants.REQUEST_TOKEN.FORMAT.getVal(), token[i]));
            }

        } catch (NoSuchAlgorithmException ex) {
            errorLogger.error(getStackTraceMessage(ex));
        }

        return buf.toString();
    }
    /**
     * 文字列暗号化
     * 
     * @param inputString      暗号化する文字列（null不可）
     * @param keyValue         暗号化キー
     * @return 暗号化された文字列(16進数)
     */
    @Deprecated
    public static String encryptionString(String inputString, String keyValue) {

        if(inputString != null && !inputString.equals(STR_EMPTY)){
            try {
                // 秘密鍵を生成 MD5で暗号化
                byte[] password = keyValue.getBytes();
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update(password);
                Arrays.fill(password, (byte) 0x00);
                byte[] pssKey = md.digest();
                DESKeySpec desKeySpec = new DESKeySpec(pssKey);
                Arrays.fill(pssKey, (byte) 0x00);
                SecretKeyFactory desKeyFac = SecretKeyFactory.getInstance("DES");
                SecretKey desKey = desKeyFac.generateSecret(desKeySpec);

                // 暗号化準備
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.ENCRYPT_MODE, desKey);

                // 文字列をbyte配列に変換
                byte input[] = inputString.getBytes();

                // 暗号化
                byte encrypted[] = c.doFinal(input);

                return asHex(encrypted);

            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                errorLogger.error(getStackTraceMessage(ex));
            }
        }
        return inputString;
    }
    /**
     * 文字列復号化
     *
     * @param inputString      復号化する文字列（null不可）
     * @param keyValue         復号化キー
     * @return 復号化された文字列
     */
    @Deprecated
    public static String decryptionString(String inputString, String keyValue) {

        if(inputString != null && !inputString.equals(STR_EMPTY)){
            try {
                // 秘密鍵を生成 MD5で暗号化
                byte[] password = keyValue.getBytes();
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update(password);
                Arrays.fill(password, (byte) 0x00);
                byte[] pssKey = md.digest();
                DESKeySpec desKeySpec = new DESKeySpec(pssKey);
                Arrays.fill(pssKey, (byte) 0x00);
                SecretKeyFactory desKeyFac = SecretKeyFactory.getInstance("DES");
                SecretKey desKey = desKeyFac.generateSecret(desKeySpec);

                // 復合化準備
                Cipher c = Cipher.getInstance("DES");
                c.init(Cipher.DECRYPT_MODE, desKey);

                // 暗号文字列をbyte配列に変換
                byte input[] = asByteArray(inputString);

                // 復合化
                byte output[] = c.doFinal(input);

                return new String(output);

            } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
                errorLogger.error(getStackTraceMessage(ex));
            }
        }
        // nullが返却される場合はExceptionをさすようなメッセージ出力が必要
        return inputString;
    }
    /**
     * 暗号化
     * @param inputString 文字列
     * @param keyValue    秘密鍵
     * @return 暗号化文字列
     */
    public static String encryptionStringAES(String inputString, String keyValue) {

        if(inputString != null && !inputString.equals(STR_EMPTY)){
            try {
                // 秘密鍵をMD5ハッシュ値取得
                byte[] password = keyValue.getBytes();
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update(password);
                Arrays.fill(password, (byte) 0x00);
                byte[] pssKey = md.digest();

                // 暗号化準備
                Key skey = new SecretKeySpec(pssKey,"AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, skey);

                // 文字列をbyte配列に変換
                byte input[] = inputString.getBytes();

                // 暗号化
                byte encrypted[] = cipher.doFinal(input);

                return asHex(encrypted);

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }
        return inputString;
    }
    /**
     * 復号化
     * @param inputString 暗号化文字列
     * @param keyValue    秘密鍵
     * @return 複合化文字列
     */
    public static String decryptionStringAES(String inputString, String keyValue) {
        
        if(inputString != null && !inputString.equals(STR_EMPTY)){
            try {
                // 秘密鍵をMD5ハッシュ値取得
                byte[] password = keyValue.getBytes();
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update(password);
                Arrays.fill(password, (byte) 0x00);
                byte[] pssKey = md.digest();

                // 複合化準備
                Key skey = new SecretKeySpec(pssKey,"AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, skey);

                // 暗号文字列をbyte配列に変換
                byte input[] = asByteArray(inputString);

                // 復合化
                byte output[] = cipher.doFinal(input);

                return new String(output);

            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                throw new RuntimeException(e);
            }
        }
        return inputString;
    }
    /**
     * 
     * SHA256ハッシュ値の取得
     * 
     * @param inputString 対象文字列
     * @param solt    ソルト
     * @return SHA256ハッシュ値
     */
    public static String getSHA256(String inputString, String solt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(inputString.concat(solt).getBytes());
            return asHex(md.digest());
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    /**
     * バイト配列を16進数の文字列に変換する。
     *
     * @param bytes バイト配列
     * @return 16進数の文字列
     */
    private static String asHex(byte bytes[]) {

        // バイト配列の２倍の長さの文字列バッファを生成。
        StringBuilder strbuf = new StringBuilder(bytes.length * 2);

        // バイト配列の要素数分、処理を繰り返す。
        for (int index = 0; index < bytes.length; index++) {
            // バイト値を自然数に変換。
            int bt = bytes[index] & 0xff;

            // バイト値が0x10以下か判定。
            if (bt < 0x10) {
                // 0x10以下の場合、文字列バッファに0を追加。
                strbuf.append("0");
            }

            // バイト値を16進数の文字列に変換して、文字列バッファに追加。
            strbuf.append(Integer.toHexString(bt));
        }

        /// 16進数の文字列を返す。
        return strbuf.toString();
    }
    /**
     * 16進数の文字列をバイト配列に変換する。
     *
     * @param hex 16進数の文字列
     * @return バイト配列
     */
    private static byte[] asByteArray(String hex) {
        // 文字列長の1/2の長さのバイト配列を生成。
        byte[] bytes = new byte[hex.length() / 2];

        // バイト配列の要素数分、処理を繰り返す。
        for (int index = 0; index < bytes.length; index++) {
            // 16進数文字列をバイトに変換して配列に格納。
            bytes[index]
                    = (byte) Integer.parseInt(
                            hex.substring(index * 2, (index + 1) * 2),
                            16);
        }

        // バイト配列を返す。
        return bytes;
    }
}
