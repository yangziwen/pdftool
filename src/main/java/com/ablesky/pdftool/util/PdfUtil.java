package com.ablesky.pdftool.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;

public class PdfUtil {
	
	private static final Properties COMMAND_CONFIG = ConfigUtil.loadConfigInfo("command.properties");

	private PdfUtil () {}
	
	/**
	 * 将整个pdf切分成10页一份的小pdf
	 * @return pdf文件的页数
	 */
	public static int splitPdfIntoSmallPiece(String sourceFilePath, String targetFolderPath) {
		return splitPdfIntoSmallPiece(sourceFilePath, targetFolderPath, 10, true);
	}
	
	public static final int ERROR_PDF_ENCRYPTED = -2;
	public static final int ERROR_PDF_SPLIT_FAILED = -1;
	
	public static int splitPdfIntoSmallPiece(String sourceFilePath, String targetFolderPath, int segmentSize, boolean needCover) {
		PdfReader reader = null;
		int pageNum = 0;
		Document document = null;
		PdfCopy copy = null;
		try {
			reader = new PdfReader(sourceFilePath);
			if(reader.isEncrypted()) {
				return ERROR_PDF_ENCRYPTED;
			}
			pageNum = reader.getNumberOfPages();
			
			if(needCover) {		// 单切一个封面出来
				document = new Document();
				copy = new PdfCopy(document, new FileOutputStream(FilenameUtils.concat(targetFolderPath, "cover.pdf")));
				document.open();
				copy.addPage(copy.getImportedPage(reader,  1));
				copy.close();
				document.close();
			}
			
			for(int i=1; i<=pageNum; i+=segmentSize) {
				String filePath = FilenameUtils.concat(targetFolderPath, i + ".pdf");
				document = new Document();
				copy = new PdfCopy(document, new FileOutputStream(filePath));
				document.open();
				for(int j=0; j<segmentSize && i+j <=pageNum; j++ ) {
					copy.addPage(copy.getImportedPage(reader,  i+j));
				}
				copy.close();
				document.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			return ERROR_PDF_SPLIT_FAILED;
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(copy != null) {
				try {
					copy.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(document != null) {
				try {
					document.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return pageNum;
	}
	
	public static boolean convertPdfToSwf(String sourceFilePath, String targetFilePath, boolean polyToBitmap) {
		String cmd = COMMAND_CONFIG.getProperty("command.pdfToSwf");
		if(polyToBitmap) {
			cmd += " -s poly2bitmap ";
		}
		Process process = CmdUtil.exec(cmd, sourceFilePath, targetFilePath);
		CmdUtil.waitFor(process);
		return CmdUtil.isSuccess(process.exitValue());
	}
	
	public static boolean convertPdfToSwf(File sourceFile, File targetFile, boolean polyToBitmap) {
		return convertPdfToSwf(sourceFile.getAbsolutePath(), targetFile.getAbsolutePath(), polyToBitmap);
	}
}
