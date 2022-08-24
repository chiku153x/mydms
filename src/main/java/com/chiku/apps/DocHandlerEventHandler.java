package com.chiku.apps;

import de.abas.erp.axi2.annotation.EventHandler;
import de.abas.erp.axi2.EventHandlerRunner;
import de.abas.erp.jfop.rt.api.annotation.RunFopWith;

import de.abas.erp.db.infosystem.custom.ow1.DocHandler;
import de.abas.erp.axi.screen.ScreenControl;
import de.abas.erp.axi2.event.ButtonEvent;
import de.abas.erp.axi.event.EventException;
import de.abas.erp.axi2.annotation.ButtonEventHandler;
import de.abas.erp.db.DbContext;
import de.abas.erp.axi2.type.ButtonEventType;

@RunFopWith(EventHandlerRunner.class)
@EventHandler(head = DocHandler.class, row = DocHandler.Row.class)
public class DocHandlerEventHandler {

	@ButtonEventHandler(field="start", type = ButtonEventType.AFTER)
	public void startButtonAfter(ButtonEvent event, ScreenControl screenControl, DbContext ctx, DocHandler head) throws EventException {
		ctx.out().print("Ela ela");
	}
	
}
