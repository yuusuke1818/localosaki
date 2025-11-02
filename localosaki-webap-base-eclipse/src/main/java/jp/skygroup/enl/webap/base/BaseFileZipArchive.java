package jp.skygroup.enl.webap.base;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.jboss.logging.Logger;

/**
 *
 * Base zip,tar.gz 圧縮・展開クラス
 *
 * @author take_suzuki
 */
public class BaseFileZipArchive extends BaseConstants {

    /**
     * エラー用ログ
     */
    private static final Logger errorLogger = Logger.getLogger(LOGGER_NAME.ERROR.getVal());

    /**
     * 指定ディレクトリ内のファイルをzip圧縮して指定されたパスに配置
     *
     * @param inputDirectoryPath 圧縮するディレクトリ ( 例; C:/sample )
     * @param zipFilePath 圧縮後の出力ファイル名をフルパスで指定 ( 例: C:/sample.zip )
     * @param charset エンコード文字セット
     * @param compressionLevel 圧縮レベル
     *
     * @return 処理結果 true:成功 false:失敗
     */
    public static boolean compressDirectory(String inputDirectoryPath, String zipFilePath, Charset charset, int compressionLevel) {

        File inputDirectory = new File(inputDirectoryPath);
        File zipFile = new File(zipFilePath);
        ZipOutputStream zipOutputStream = null;
        try {
            // ZIPファイル出力オブジェクト作成
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile), charset);
            // 圧縮レベル設定
            zipOutputStream.setLevel(compressionLevel);
            // 指定ディレクトリ圧縮処理
            archiveDirectory(zipOutputStream, inputDirectory.getParentFile().getAbsolutePath(), inputDirectory, zipFile, charset);
        } catch (Exception ex) {
            // ZIP圧縮失敗
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        } finally {
            // ZIPエントリクローズ
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.closeEntry();
                    zipOutputStream.flush();
                    zipOutputStream.close();
                } catch (Exception ex) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 指定ArrayListのファイルをzip圧縮して指定されたパスに配置
     *
     * @param inputFileList 圧縮するファイルリスト ( 例; {C:/sample1.txt, C:/sample2.txt} )
     * @param zipFilePath 圧縮後のファイル名をフルパスで指定 ( 例: C:/sample.zip )
     * @param charset エンコード文字セット
     * @param compressionLevel 圧縮レベル
     *
     * @return 処理結果 true:圧縮成功 false:圧縮失敗
     */
    public static boolean compressFileList(List<String> inputFileList, String zipFilePath, Charset charset, int compressionLevel) {

        ZipOutputStream zipOutputStream = null;
        File zipFile = new File(zipFilePath);
        try {
            // ZIPファイル出力オブジェクト作成
            zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile), charset);
            // 圧縮レベル設定
            zipOutputStream.setLevel(compressionLevel);
            // 圧縮ファイルリストのファイルを連続圧縮
            for (int i = 0; i < inputFileList.size(); i++) {
                // ファイルオブジェクト作成
                File file = new File(inputFileList.get(i));
                // 圧縮処理
                String fileName = file.getName();
                if (charset.name().equals(Charset.forName("SJIS").name())) {
                	fileName = replaceChar(fileName);
                }
                fileName = new String(fileName.getBytes(charset), charset);
                archiveFile(zipOutputStream, file, fileName);
            }
        } catch (Exception ex) {
            // ZIP圧縮失敗
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        } finally {
            // ZIPエントリクローズ
            if (zipOutputStream != null) {
                try {
                    zipOutputStream.closeEntry();
                    zipOutputStream.flush();
                    zipOutputStream.close();
                } catch (Exception ex) {
                    errorLogger.error(BaseUtility.getStackTraceMessage(ex));
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * ディレクトリ圧縮のための再帰処理
     *
     * @param outZip zip出力ストリームクラス
     * @param file File 圧縮対象のファイル・ディレクトリ
     * @param zipFile 圧縮先のzipファイル
     * @param charset エンコード文字セット
     */
    private static void archiveDirectory(ZipOutputStream zipOutputStream, String inputDirectoryPath, File inputDirectory, File zipFile, Charset charset) {
        if (inputDirectory.isDirectory()) {
            File[] fileList = inputDirectory.listFiles();
            for (File file : fileList) {
                if (file.isDirectory()) {
                    archiveDirectory(zipOutputStream, inputDirectoryPath, file, zipFile, charset);
                } else if (!file.getAbsolutePath().equals(zipFile.getAbsolutePath())) {
                    String fileName = file.getAbsolutePath().replace(inputDirectoryPath, STR_EMPTY).substring(File.separator.length());
                    if (charset.name().equals(Charset.forName("SJIS").name())) {
                    	fileName = replaceChar(fileName);
                    }
                    fileName = new String(fileName.getBytes(charset), charset);
                    // 圧縮処理
                    archiveFile(zipOutputStream, file, fileName);
                }
            }
        }
    }

    /**
     * zip圧縮処理
     *
     * @param zipOutputStream zip出力ストリームクラス
     * @param targetFile 圧縮対象ファイル
     * @parma zipEntryPath zipファイル内のエントリパス
     *
     * @return 処理結果 true:圧縮成功 false:圧縮失敗
     */
    private static boolean archiveFile(ZipOutputStream zipOutputStream, File targetFile, String zipEntryPath) {

        try {
            // ZIPエントリ作成
            zipOutputStream.putNextEntry(new ZipEntry(zipEntryPath));
            // 圧縮ファイル読み込みストリーム取得
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(targetFile));
            // 圧縮ファイルをZIPファイルに出力
            int readSize = 0;
            byte buffer[] = new byte[1024]; // 読み込みバッファ
            while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
                zipOutputStream.write(buffer, 0, readSize);
            }
            // クローズ処理
            in.close();
            // ZIPエントリクローズ
            zipOutputStream.closeEntry();
        } catch (Exception ex) {
            // ZIP圧縮失敗
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            return false;
        }
        return true;
    }

    /**
     * 指定されたzipファイルを解凍・展開する。
     *
     * @param zipFilePath 展開するzipファイルのフルパス
     * @param targetDirectoryPath 展開先のディレクトリパス
     *
     * @return true:成功 false:失敗
     */
    public static boolean unZip(final String zipFilePath, final String targetDirectoryPath) {

        //ファイル存在チェック
        final File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            return false;
        }
        if (!zipFile.isFile()) {
            return false;
        }
        //解凍先ディレクトリ存在チェック
        final File targetDirectory = new File(targetDirectoryPath);
        if (!targetDirectory.exists()) {
            return false;
        }
        if (!targetDirectory.isDirectory()) {
            return false;
        }

        File createPath;
        File createDirectory;
        boolean errorFlg = true;
        try {
            //ディレクトリ作成
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            ZipInputStream zipInStream = new ZipInputStream(fileInputStream);
            for (;;) {
                ZipEntry zipEntry = zipInStream.getNextEntry();
                if (zipEntry == null) {
                    break;
                }
                createPath = new File(targetDirectory.getAbsolutePath().concat(File.separator).concat(zipEntry.getName()));
                createDirectory = new File(createPath.getParentFile().getAbsolutePath());
                //ディレクトリ作成
                if (!createDirectory.exists()) {
                    if (!createDirectory.mkdirs()) {
                        errorFlg = false;
                    }
                }
                //ファイル作成
                if (!zipEntry.isDirectory()) {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(createPath)) {
                        for (;;) {
                            int iRead = zipInStream.read();
                            if (iRead < 0) {
                                break;
                            }
                            fileOutputStream.write(iRead);
                        }
                        fileOutputStream.flush();
                    }
                }
            }
            zipInStream.closeEntry();
            zipInStream.close();
            fileInputStream.close();

        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            errorFlg = false;
        }
        return errorFlg;
    }

    /**
     * 指定されたtar.gzファイルを解凍・展開する。
     *
     * @param targzFilePath gzipファイルのフルパス
     * @param outputDirectoryPath 展開先のディレクトリ
     * @return true:成功 false:失敗
     */
    public static boolean unTargz(final String targzFilePath, final String outputDirectoryPath) {

        //ファイル存在チェック
        final File targzFile = new File(targzFilePath);
        if (!targzFile.exists()) {
            return false;
        }
        if (!targzFile.isFile()) {
            return false;
        }
        //解凍先ディレクトリ存在チェック
        final File outputDirectory = new File(outputDirectoryPath);
        if (!outputDirectory.exists()) {
            return false;
        }
        if (!outputDirectory.isDirectory()) {
            return false;
        }

        boolean errorFlg = true;
        try {
            //ディレクトリ作成
            FileInputStream fileInStream = new FileInputStream(targzFile);
            GZIPInputStream gzipInStream = new GZIPInputStream(fileInStream);
            TarArchiveInputStream tarInStream = new TarArchiveInputStream(gzipInStream);
            for (;;) {
                TarArchiveEntry tarArchiveEntry = tarInStream.getNextTarEntry();
                if (tarArchiveEntry == null) {
                    break;
                }
                File createPath = new File(outputDirectory.getAbsolutePath().concat(File.separator).concat(tarArchiveEntry.getName()));
                File createDirectory = new File(createPath.getParentFile().getAbsolutePath());
                //ディレクトリ作成
                if (!createDirectory.exists()) {
                    if (!createDirectory.mkdirs()) {
                        errorFlg = false;
                    }
                }
                //ファイル作成
                if (!tarArchiveEntry.isDirectory()) {
                    try (FileOutputStream fileOutputStream = new FileOutputStream(createPath)) {
                        for (;;) {
                            int iRead = tarInStream.read();
                            if (iRead < 0) {
                                break;
                            }
                            fileOutputStream.write(iRead);
                        }
                        fileOutputStream.flush();
                    }
                }
            }
            tarInStream.close();
            gzipInStream.close();
            fileInStream.close();

        } catch (Exception ex) {
            errorLogger.error(BaseUtility.getStackTraceMessage(ex));
            errorFlg = false;
        }
        return errorFlg;
    }

    /**
     * utf-8から、shift_jisへ変換できない文字を置き換える。
     * 
     * @param target
     * @return
     */
	private static String replaceChar(String target) {
		char[] charArray = target.toCharArray();
		String retString = "";
		for (char checkChar : charArray) {
			switch (checkChar) {
			// 波線
			case 0xFF5E:
				checkChar = 0x301C;
				break;
			// enダッシュ
			case 0xFF0D:
				checkChar = 0x2212;
				break;
			// セント
			case 0xFFE0:
				checkChar = 0x00A2;
				break;
			// ポンド
			case 0xFFE1:
				checkChar = 0x00A3;
				break;
			// 否定記号
			case 0xFFE2:
				checkChar = 0x00AC;
				break;
			// emダッシュ
			case 0x2015:
				checkChar = 0x2014;
				break;
			// 二重縦線
			case 0x2225:
				checkChar = 0x2016;
				break;
			default:
				break;
			}
			retString += checkChar;
		}
		return retString;
	}
}
