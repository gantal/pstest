package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;

public class Feladat3 extends TabItem {

	public Feladat3() {
		super("Feladat3");
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		final LayoutContainer container = new LayoutContainer();
		container.setSize(RootPanel.get().getOffsetWidth(), RootPanel.get().getOffsetHeight());

		container.addListener(Events.OnClick, new Listener<ComponentEvent>() {
			@Override
			public void handleEvent(ComponentEvent event) {
				try {
					if (event == null) {
						throw new IllegalArgumentException("Event értéke null");
					}

					int x = event.getClientX();
					int y = event.getClientY();

					Window window = new Window();
					window.setSize(200, 100);
					window.setHeading("Kattintási ablak fejléc");
					window.setPosition(x, y);
					window.show();
				} catch (Exception e) {
					GWT.log("Hiba történt az eseménykezelés során: ", e);
				}
			}
		});

		setLayout(new FitLayout());
		add(container);
	}
}
