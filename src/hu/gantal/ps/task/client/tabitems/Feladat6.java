package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;

public class Feladat6 extends TabItem {

	private TextField<String> textNumber1;
	private TextField<String> textNumber2;
	private TextField<String> textOperation;
	private TextField<String> textResult;
	private Button buttonSendGet;
	private Button buttonSendPost;

	public Feladat6() {
		super("Feladat6");
		initFieldsAndAddToPanel();
		addEventListenersToButtons();
	}

	private void initFieldsAndAddToPanel() {
		textNumber1 = new TextField<String>();
		textNumber1.setFieldLabel("Első szám:");

		textNumber2 = new TextField<String>();
		textNumber2.setFieldLabel("Második szám (nem negatív):");

		textOperation = new TextField<String>();
		textOperation.setFieldLabel("Művelet (+, -, *, %):");

		textResult = new TextField<String>();
		textResult.setFieldLabel("Eredmény:");
		textResult.setReadOnly(true);

		buttonSendGet = new Button("GET Küldés");
		buttonSendPost = new Button("POST Küldés");

		ContentPanel panel = new ContentPanel();
		panel.add(new LabelField("Első szám:"));
		panel.add(textNumber1);
		panel.add(new LabelField("Második szám:"));
		panel.add(textNumber2);
		panel.add(new LabelField("Művelet:"));
		panel.add(textOperation);
		panel.add(buttonSendGet);
		panel.add(buttonSendPost);
		panel.add(new LabelField("Eredmény:"));
		panel.add(textResult);
		add(panel);
	}

	private void addEventListenersToButtons() {
		buttonSendGet.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (validateInputs()) {
					sendGetRequest();
				}
			}
		});

		buttonSendPost.addSelectionListener(new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				if (validateInputs()) {
					sendPostRequest();
				}
			}
		});
	}

	private boolean validateInputs() {
		String s1 = textNumber1.getValue();
		String s2 = textNumber2.getValue();

		if (s1 == null || s1.trim().isEmpty()) {
			Window.alert("Az első szám kitöltése kötelező!");
			return false;
		}
		if (s2 == null || s2.trim().isEmpty()) {
			Window.alert("A második szám kitöltése kötelező!");
			return false;
		}
		try {
			double value = Double.parseDouble(s2.trim());
			if (value < 0) {
				Window.alert("A második szám nem lehet negatív!");
				return false;
			}
		} catch (NumberFormatException e) {
			Window.alert("Érvénytelen számformátum a második mezőben!");
			return false;
		}
		String op = textOperation.getValue();
		if (op != null && !op.trim().isEmpty()) {
			if (!op.matches("[+\\-\\*%]")) {
				Window.alert("Érvénytelen műveleti karakter! Csak +, -, *, % engedélyezett.");
				return false;
			}
		}
		return true;
	}

	private void sendGetRequest() {
		String num1 = textNumber1.getValue().trim();
		String num2 = textNumber2.getValue().trim();
		String op = textOperation.getValue().trim();

		String url = GWT.getHostPageBaseURL() + "szamologep.php?number1=" + URL.encode(num1) + "&number2="
				+ URL.encode(num2) + "&operator=" + URL.encodeQueryString(op);

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request req, Response res) {
					textResult.setValue(res.getText());
				}

				@Override
				public void onError(Request req, Throwable exception) {
					Window.alert("Hiba a kérés során: " + exception.getMessage());
				}
			});
		} catch (RequestException ex) {
			Window.alert("Kérés küldése sikertelen: " + ex.getMessage());
		}
	}

	private void sendPostRequest() {
		String num1 = textNumber1.getValue().trim();
		String num2 = textNumber2.getValue().trim();
		String op = (textOperation.getValue() != null) ? textOperation.getValue().trim() : "";

		String postData = "number1=" + URL.encode(num1) + "&number2=" + URL.encode(num2);
		if (!op.isEmpty()) {
			postData += "&operator=" + URL.encodeQueryString(op);
		}

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, GWT.getHostPageBaseURL() + "szamologep.php");
		builder.setHeader("Content-Type", "application/x-www-form-urlencoded");

		try {
			builder.sendRequest(postData, new RequestCallback() {
				@Override
				public void onResponseReceived(Request req, Response res) {
					if (res.getStatusCode() == Response.SC_OK) {
						textResult.setValue(res.getText());
					} else {
						Window.alert("Hibás válaszkód: " + res.getStatusCode());
					}
				}

				@Override
				public void onError(Request req, Throwable exception) {
					Window.alert("Hiba a kérés során: " + exception.getMessage());
				}
			});
		} catch (RequestException ex) {
			Window.alert("Kérés küldése sikertelen: " + ex.getMessage());
		}
	}

}
