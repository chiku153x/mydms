package com.chiku.apps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi2.type.ButtonEventType;
import de.abas.erp.db.DbContext;
import de.abas.erp.db.infosystem.custom.ow1.DocHandler;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

@RunFopWith(EventHandlerRunner.class)
@EventHandler(head = DocHandler.class, row = DocHandler.Row.class)
public class DocHandlerEventHandler {

	@ButtonEventHandler(field = "start", type = ButtonEventType.AFTER)
	public void startButtonAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, DocHandler head)
			throws EventException {
		ctx.out().print("XXX");
		// MultipartFileUploader.uploadFile(ctx, head.getYmydmsfilepath(),
		// "http://192.168.1.71:8080");

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost uploadFile = new HttpPost("http://192.168.1.71:8080");
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);

		// This attaches the file to the POST:
		File f = new File(head.getYmydmsfilepath());
		try {
			ctx.out().print("yyy");
			builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
			ctx.out().print("zzz" + " " + responseEntity.getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
