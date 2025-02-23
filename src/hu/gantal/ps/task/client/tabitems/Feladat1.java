package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.user.client.Element;

public class Feladat1 extends TabItem {
	public Feladat1() {
		super("Feladat1");
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		Button b = new Button("Gomb");
		add(b);
	}
}