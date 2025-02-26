package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;

public class Feladat5 extends TabItem {

	private TextArea textArea;
	private static final String WIDTH = "100%";
	private static final String HEIGHT = "100%";

	public Feladat5() {
		super("Feladat5");
		textArea = new TextArea();
		textArea.setSize(WIDTH, HEIGHT);
		textArea.setReadOnly(true);
		add(textArea);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		String url = GWT.getHostPageBaseURL() + "alapok.php";
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		builder.setCallback(new RequestCallback() {
			@Override
			public void onResponseReceived(Request req, Response res) {
				if (res.getStatusCode() == Response.SC_OK) {
					String processedText = res.getText().replace("<br/>", "\n");

					System.out.println("Szerver válasz: " + processedText);
					textArea.setValue(processedText);
				} else {
					System.out.println("Hibás válaszkód: " + res.getStatusCode());
				}
			}

			@Override
			public void onError(Request req, Throwable err) {
				System.out.println("Hiba történt a kérés során: " + err.getMessage());
				Window.alert("Hiba történt a kérés során: " + err.getMessage());
			}
		});

		try {
			builder.send();
		} catch (RequestException ex) {
			System.out.println("Nem sikerült elküldeni a kérést: " + ex.getMessage());
			Window.alert("Nem sikerült elküldeni a kérést: " + ex.getMessage());
		}
	}

}