package com.ablesky.pdftool.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.BooleanUtils;

import com.ablesky.pdftool.util.PdfUtil;
import com.ablesky.pdftool.validator.SourceFileValidator;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(separators = "=", commandDescription = "convert pdf to swf")
public class ConvertCommand extends AbstractCommand {
	
	public ConvertCommand() {
		super("convert");
	}
	
	@Parameter(names = {"-h", "--help"}, description = "print this message", help = true)
	public boolean help;

	@Parameter(
		names = {"-src", "--sourceFile"},
		validateValueWith = SourceFileValidator.class,
		description = "the source pdf file to convert",
		required = true
	)
	public File srcFile;			// 源文件路径
	
	@Parameter(
		names = {"-dest", "--destFile"}, 
		description = "the destination to save newly converted swf file. will use original pdf filename as default filename"
	)
	public File destFile;		// 默认使用pdf的文件名
	
	@Parameter(names = {"-p2b", "--poly2bitmap"}, description = "whether to use poly2bitmap option")
	public boolean polyToBitmap = false;
	
	
	@Override
	public void invoke(JCommander commander) {
		if(help) {
			commander.usage(this.getName());
			return;
		}
		if(destFile == null) {
			destFile = new File(FilenameUtils.removeExtension(srcFile.getAbsolutePath()) + ".swf");
		}
		if(!checkDestination(destFile)) {
			return;
		}
		FileUtils.deleteQuietly(destFile);
		PdfUtil.convertPdfToSwf(srcFile, destFile, polyToBitmap);
	}
	
	private boolean checkDestination(File destFile) {
		if(!destFile.exists()) {
			return true;
		}
		if(destFile.isDirectory()) {
			System.err.println("destination path is a folder!");
			return false;
		}
		boolean overwrite = prompt("\"" + destFile.getAbsolutePath() + "\" already exists, overwrite it? (yes or no [default no]): ", false);
		if(!overwrite) {
			System.err.println("destination path is not empty!");
			return false;
		}
		return true;
	}

	private static boolean prompt(String text, boolean defaultValue) {
		System.out.print(text);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		boolean result = defaultValue;
		try {
			result = BooleanUtils.toBoolean(reader.readLine().trim());
		} catch (Exception e) {}
		IOUtils.closeQuietly(reader);
		return result;
	}
	
}
