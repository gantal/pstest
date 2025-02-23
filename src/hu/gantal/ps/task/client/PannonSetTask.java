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
import hu.gantal.ps.task.client.tabitems.Feladat2;

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
		cp.add(tp);
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				resize();
			}
		});
		resize();

		RootPanel.get("gwt").add(cp);
	}

	private void resize() {
		cp.setHeight(Window.getClientHeight());
	}
}