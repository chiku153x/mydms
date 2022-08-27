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
import de.abas.erp.db.Query;
import de.abas.erp.db.infosystem.custom.ow1.DocHandler;
import de.abas.erp.db.schema.company.CompanyData;
import de.abas.erp.db.selection.Conditions;
import de.abas.erp.db.selection.SelectionBuilder;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;
import de.abas.erp.axi2.type.ScreenEventType;
import de.abas.erp.axi2.event.ScreenEvent;
import de.abas.erp.axi2.annotation.ScreenEventHandler;

@RunFopWith(EventHandlerRunner.class)
@EventHandler(head = DocHandler.class, row = DocHandler.Row.class)
public class DocHandlerEventHandler {

	@ButtonEventHandler(field = "start", type = ButtonEventType.AFTER)
	public void startButtonAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, DocHandler head)
			throws EventException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		// HttpPost uploadFile = new HttpPost("http://192.168.1.71:8080");
		HttpPost uploadFile = new HttpPost(head.getYmydmsurl());
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("payload", head.getYmydmsjson(), ContentType.TEXT_PLAIN);

		File f = new File(head.getYmydmsfilepath());
		try {
			builder.addBinaryBody("file", new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM, f.getName());
			HttpEntity multipart = builder.build();
			uploadFile.setEntity(multipart);
			CloseableHttpResponse response = httpClient.execute(uploadFile);
			HttpEntity responseEntity = response.getEntity();
		} catch (IOException e) {
			ctx.out().println(e.getMessage());
		}

	}

	@ScreenEventHandler(type = ScreenEventType.ENTER)
	public void screenEnter(ScreenEvent event, ScreenControl screenControl, DbContext ctx, DocHandler head)
			throws EventException {
		SelectionBuilder<CompanyData> selectionBuilder = SelectionBuilder.create(CompanyData.class);
		selectionBuilder.add(Conditions.eq(CompanyData.META.idno, "1"));
		Query<CompanyData> queryCompanyData = ctx.createQuery(selectionBuilder.build());
		for (CompanyData companyData : queryCompanyData) {
			head.setYmydmsurl(companyData.getYmydmsresturl());
		}
	}

}
