package com.chiku.apps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MultipartUtility {
	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;

	/**
	 * This constructor initializes a new HTTP POST request with content type is set
	 * to multipart/form-data
	 * 
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 */
	public MultipartUtility(String requestURL, String charset) throws IOException {
		this.charset = charset;

		// creates a unique boundary based on time stamp
		this.boundary = "===" + System.currentTimeMillis() + "===";

		URL url = new URL(requestURL);
		this.httpConn = (HttpURLConnection) url.openConnection();
		this.httpConn.setUseCaches(false);
		this.httpConn.setDoOutput(true); // indicates POST method
		this.httpConn.setDoInput(true);
		this.httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + this.boundary);
		this.httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
		this.httpConn.setRequestProperty("Test", "Bonjour");
		this.outputStream = this.httpConn.getOutputStream();
		this.writer = new PrintWriter(new OutputStreamWriter(this.outputStream, charset), true);
	}

	/**
	 * Adds a form field to the request
	 * 
	 * @param name  field name
	 * @param value field value
	 */
	public void addFormField(String name, String value) {
		this.writer.append("--" + this.boundary).append(LINE_FEED);
		this.writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		this.writer.append("Content-Type: text/plain; charset=" + this.charset).append(LINE_FEED);
		this.writer.append(LINE_FEED);
		this.writer.append(value).append(LINE_FEED);
		this.writer.flush();
	}

	/**
	 * Adds a upload file section to the request
	 * 
	 * @param fieldName  name attribute in <input type="file" name="..." />
	 * @param uploadFile a File to be uploaded
	 * @throws IOException
	 */
	public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		this.writer.append("--" + this.boundary).append(LINE_FEED);
		this.writer.append(
				"Contenthis.t-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		this.writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		this.writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		this.writer.append(LINE_FEED);
		this.writer.flush();

		try (FileInputStream inputStream = new FileInputStream(uploadFile);) {
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				this.outputStream.write(buffer, 0, bytesRead);
			}
			this.outputStream.flush();
			inputStream.close();

			this.writer.append(LINE_FEED);
			this.writer.flush();
		}

	}

	/**
	 * Adds a header field to the request.
	 * 
	 * @param name  - name of the header field
	 * @param value - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		this.writer.append(name + ": " + value).append(LINE_FEED);
		this.writer.flush();
	}

	/**
	 * Completes the request and receives response from the server.
	 * 
	 * @return a list of Strings as response in case the server returned status OK,
	 *         otherwise an exception is thrown.
	 * @throws IOException
	 */
	public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();

		this.writer.append(LINE_FEED).flush();
		this.writer.append("--" + this.boundary + "--").append(LINE_FEED);
		this.writer.close();

		// checks server's status code first
		int status = this.httpConn.getResponseCode();
		if (status == HttpURLConnection.HTTP_OK) {
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.httpConn.getInputStream()));) {
				String line = null;
				while ((line = reader.readLine()) != null) {
					response.add(line);
				}
				reader.close();
				this.httpConn.disconnect();
			}

		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}

		return response;
	}
}
