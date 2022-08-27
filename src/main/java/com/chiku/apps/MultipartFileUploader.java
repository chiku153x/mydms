package com.chiku.apps;

import java.io.File;
import java.io.IOException;
import java.util.List;

import de.abas.erp.db.DbContext;

public class MultipartFileUploader {
	public static void uploadFile(DbContext ctx, String filePath, String requestURL) {
		String charset = "UTF-8";
		File uploadFile = new File(filePath);

		try {
			MultipartUtility multipart = new MultipartUtility(requestURL, charset);

			multipart.addHeaderField("User-Agent", "CodeJava");
			multipart.addHeaderField("Test-Header", "Header-Value");

//			multipart.addFormField("file", "a.txt");
//			multipart.addFormField("keywords", "Java,upload,Spring");

			multipart.addFilePart("file", uploadFile);


			List<String> response = multipart.finish();

			ctx.out().println("SERVER REPLIED:");

			for (String line : response) {
				ctx.out().println(line);
			}
		} catch (IOException ex) {
			ctx.out().println(ex);
		}
	}
}
