package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.widget.Info;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Feladat2 extends TabItem {
	public Feladat2() {
		super("Feladat2");

	}

	private String reverseString(String input) {
		if (input == null || input.isEmpty()) {
			return input;
		}
		char[] characters = input.toCharArray();
		StringBuilder reversed = new StringBuilder();
		for (int i = characters.length - 1; i >= 0; i--) {
			reversed.append(characters[i]);
		}
		return reversed.toString();
	}

	@Override
	protected void onRender(Element parent, int index) {
		super.onRender(parent, index);

		VerticalPanel panel = new VerticalPanel();
		panel.setSpacing(10);
		final TextBox textBox = new TextBox();
		panel.add(textBox);
		Button btn1 = new Button("MessageBox-al");
		btn1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MessageBox.alert("Üzenet", reverseString(textBox.getText()), null);
			}
		});
		panel.add(btn1);
		Button btn2 = new Button("Info-val");
		btn2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Info.display("Üzenet", reverseString(textBox.getText()));
			}
		});
		panel.add(btn2);
		Button btn3 = new Button("Window-al");
		btn3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window window = new Window();
				window.setHeading("Üzenet");
				window.add(new Label(reverseString(textBox.getText())));
				window.setSize(200, 100);
				window.show();
			}
		});
		panel.add(btn3);
		setLayout(new FitLayout());
		add(panel);
	}
}
