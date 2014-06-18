package com.ablesky.pdftool.command;

import java.io.File;

import com.ablesky.pdftool.util.PdfUtil;
import com.ablesky.pdftool.validator.DestinationFolderValidator;
import com.ablesky.pdftool.validator.SourceFileValidator;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.beust.jcommander.validators.PositiveInteger;

@Parameters(separators = "=", commandDescription = "split a multi-page pdf file into small segments")
public class SplitCommand extends AbstractCommand {
	
	public SplitCommand() {
		super("split");
	}
	
	@Parameter(names = {"-h", "--help"}, help = true)
	public boolean help;

	@Parameter(names = {"-seg", "--segmentSize"}, validateWith = PositiveInteger.class,  description = "the page number of every segment after split, default is 10")
	public Integer segmentSize = 10;
	
	@Parameter(names = {"-c", "--cover"}, description = "extract the first page as a single file")
	public Boolean cover = false; 
	
	@Parameter(
		names = {"-src", "--sourceFile"},
		validateValueWith = SourceFileValidator.class,
		description = "the source pdf file to split"
	)
	public File srcFile;			// 源文件路径
	
	@Parameter(
		names = {"-dest", "--destFolder"}, 
		validateValueWith = DestinationFolderValidator.class, 
		description = "the destination folder to save newly splitted pdf files. will use current folder as default"
	)
	public File destFolder;		// 默认使用当前路径
	
	@Override
	public void invoke() {
		int pageNum = PdfUtil.splitPdfIntoSmallPiece(srcFile.getAbsolutePath(), destFolder.getAbsolutePath(), segmentSize, cover);
		if(pageNum == PdfUtil.ERROR_PDF_ENCRYPTED) {
			System.err.println("can not split encrypted pdf!");
		} else if (pageNum == PdfUtil.ERROR_PDF_SPLIT_FAILED) {
			System.err.println("failed to split pdf into small segments!");
		} else {
			System.out.println("split operation success!");
		}
	}
	
}
