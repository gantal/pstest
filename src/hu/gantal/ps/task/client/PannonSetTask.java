package hu.gantal.ps.task.client;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

import hu.gantal.ps.task.client.tabitems.Feladat1;
import hu.gantal.ps.task.client.tabitems.Feladat10;
import hu.gantal.ps.task.client.tabitems.Feladat11;
import hu.gantal.ps.task.client.tabitems.Feladat12;
import hu.gantal.ps.task.client.tabitems.Feladat13;
import hu.gantal.ps.task.client.tabitems.Feladat14;
import hu.gantal.ps.task.client.tabitems.Feladat2;
import hu.gantal.ps.task.client.tabitems.Feladat3;
import hu.gantal.ps.task.client.tabitems.Feladat4;
import hu.gantal.ps.task.client.tabitems.Feladat5;
import hu.gantal.ps.task.client.tabitems.Feladat6;
import hu.gantal.ps.task.client.tabitems.Feladat7;
import hu.gantal.ps.task.client.tabitems.Feladat8;
import hu.gantal.ps.task.client.tabitems.Feladat9;

public class PannonSetTask implements EntryPoint {

	private ContentPanel cp;
	private TabPanel tp;

	@Override
	public void onModuleLoad() {
		cp = new ContentPanel();
		cp.setHeaderVisible(true);
		cp.setWidth("100%");
		cp.setFrame(true);
		cp.setHeading("TanProjekt");
		cp.setLayout(new FitLayout());

		tp = new TabPanel();
		tp.add(new Feladat1());
		tp.add(new Feladat2());
		tp.add(new Feladat3());
		tp.add(new Feladat4());
		tp.add(new Feladat5());
		tp.add(new Feladat6());
		tp.add(new Feladat7());
		tp.add(new Feladat8());
		tp.add(new Feladat9());
		tp.add(new Feladat10());
		tp.add(new Feladat11());
		tp.add(new Feladat12());
		tp.add(new Feladat13());
		tp.add(new Feladat14());
		cp.add(tp);
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				resize();
			}
		});
		
		resize();
		tp.setSelection(tp.getItem(tp.getItemCount() -1));
		RootPanel.get("gwt").add(cp);
	}

	private void resize() {
		cp.setHeight(Window.getClientHeight());
	}
}