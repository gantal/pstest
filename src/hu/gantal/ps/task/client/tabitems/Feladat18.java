package hu.gantal.ps.task.client.tabitems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.DNDEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.DateTimePropertyEditor;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.RowEditor;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class Feladat18 extends TabItem {
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

	private static final String SOURCE_GRID_URL = GWT.getHostPageBaseURL() + "feladat16_source_grid.php";
	private static final String TARGET_GRID_URL = GWT.getHostPageBaseURL() + "feladat16_target_grid.php";

	private ListStore<ModelData> leftStore;
	private ListStore<ModelData> rightStore;
	private Grid<ModelData> leftGrid;
	private Grid<ModelData> rightGrid;
	private boolean isOperationInProgress = false;

	public Feladat18() {
		super("Feladat18");
		setSize(1000, 600);
		setLayout(new FitLayout());
		add(createMainPanel());
		loadData(SOURCE_GRID_URL, leftStore);
		loadData(TARGET_GRID_URL, rightStore);
		layout();
	}

	private ContentPanel createMainPanel() {
		ContentPanel panel = new ContentPanel();
		panel.setHeading("Feladat18");
		panel.setLayout(new RowLayout(Orientation.HORIZONTAL));

		leftStore = new ListStore<ModelData>();
		rightStore = new ListStore<ModelData>();

		leftGrid = setupGrid(leftStore, "Bal tábla");
		rightGrid = setupGrid(rightStore, "Jobb tábla");

		panel.add(leftGrid, new RowData(0.5, 1, new Margins(6)));
		panel.add(rightGrid, new RowData(0.5, 1, new Margins(6, 6, 6, 0)));

		enableDragAndDrop(leftGrid, rightGrid, SOURCE_GRID_URL, TARGET_GRID_URL);
		enableDragAndDrop(rightGrid, leftGrid, TARGET_GRID_URL, SOURCE_GRID_URL);

		panel.setTopComponent(createToolBar());

		return panel;
	}

	private Grid<ModelData> setupGrid(ListStore<ModelData> store, String title) {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();

		columns.add(new ColumnConfig("id", "ID", 50));

		TextField<String> productField = new TextField<String>();
		ColumnConfig productColumn = new ColumnConfig("product", "Termék", 150);
		productColumn.setEditor(new CellEditor(productField));
		columns.add(productColumn);

		NumberField quantityField = new NumberField();
		quantityField.setPropertyEditorType(Integer.class);
		ColumnConfig quantityColumn = new ColumnConfig("quantity", "Mennyiség", 80);
		quantityColumn.setEditor(new CellEditor(quantityField));
		columns.add(quantityColumn);

		NumberField priceField = new NumberField();
		priceField.setPropertyEditorType(Double.class);
		ColumnConfig priceColumn = new ColumnConfig("price", "Egységár", 100);
		priceColumn.setEditor(new CellEditor(priceField));
		columns.add(priceColumn);

		DateField orderDateField = new DateField();
		orderDateField.setPropertyEditor(new DateTimePropertyEditor(DATE_FORMAT));
		ColumnConfig orderDateColumn = new ColumnConfig("orderDate", "Dátum", 120);
		orderDateColumn.setEditor(new CellEditor(orderDateField));
		columns.add(orderDateColumn);

		ColumnModel cm = new ColumnModel(columns);
		final Grid<ModelData> grid = new Grid<ModelData>(store, cm);
		grid.setSize(500, 600);
		grid.setBorders(true);
		RowEditor<ModelData> rowEditor = new RowEditor<ModelData>();
		rowEditor.setClicksToEdit(EditorGrid.ClicksToEdit.TWO);
		grid.addPlugin(rowEditor);

		return grid;
	}

	private void enableDragAndDrop(final Grid<ModelData> sourceGrid, final Grid<ModelData> targetGrid,
			final String sourceUrl, final String targetUrl) {

		new GridDragSource(sourceGrid);
		GridDropTarget target = new GridDropTarget(targetGrid);
		target.setAllowSelfAsSource(false);

		target.addListener(Events.Drop, new Listener<DNDEvent>() {
			@Override
			public void handleEvent(DNDEvent event) {
				if (isOperationInProgress) {
					MessageBox.alert("Hiba", "Másik művelet folyamatban van!", null);
					return;
				}

				List<ModelData> items = event.getData();
				if (items == null || items.isEmpty())
					return;

				final ModelData item = items.get(0);
				isOperationInProgress = true;

				deleteItemFromDB(sourceUrl, item, new RequestCallback() {
					public void onResponseReceived(Request request, Response response) {
						if (response.getStatusCode() == 200) {
							JSONValue jsonValue = JSONParser.parseLenient(response.getText());
							JSONObject jsonObject = jsonValue.isObject();

							if (jsonObject != null && jsonObject.containsKey("success")) {
								sourceGrid.getStore().remove(item);
								saveItemToDB(targetUrl, item, targetGrid);
							} else {
								MessageBox.alert("Hiba", "Törlés sikertelen!", null);
								isOperationInProgress = false;
							}
						} else {
							MessageBox.alert("Hiba", "Szerverhiba törléskor: " + response.getStatusText(), null);
							isOperationInProgress = false;
						}
					}

					public void onError(Request request, Throwable exception) {
						MessageBox.alert("Hiba", "Hálózati hiba történt a törlés során!", null);
						isOperationInProgress = false;
					}
				});
			}
		});
	}

	private void openCreateWindow() {
		final Window window = new Window();
		window.setHeading("Új tétel hozzáadása");
		window.setModal(true);
		window.setSize(350, 250);
		window.setLayout(new FitLayout());

		final FormPanel form = new FormPanel();
		form.setFrame(true);
		form.setHeaderVisible(false);
		form.setLabelWidth(100);

		final TextField<String> productField = new TextField<String>();
		productField.setFieldLabel("Termék");
		productField.setAllowBlank(false);

		final NumberField quantityField = new NumberField();
		quantityField.setFieldLabel("Mennyiség");
		quantityField.setAllowBlank(false);
		quantityField.setAllowNegative(false);

		final NumberField priceField = new NumberField();
		priceField.setFieldLabel("Egységár");
		priceField.setAllowBlank(false);
		priceField.setAllowNegative(false);

		final DateField orderDateField = new DateField();
		orderDateField.setFieldLabel("Rendelés dátuma");
		orderDateField.setAllowBlank(false);

		Button cancelButton = new Button("Mégse", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				window.hide();
			}
		});

		Button saveButton = new Button("Hozzáadás", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				for (Field<?> f : form.getFields()) {
					f.validate();
				}

				if (!form.isValid()) {
					return;
				}

				BaseModelData newItem = new BaseModelData();
				newItem.set("id", leftStore.getCount() + 1);
				newItem.set("product", productField.getValue());
				newItem.set("quantity", quantityField.getValue().intValue());
				newItem.set("price", priceField.getValue().doubleValue());
				newItem.set("orderDate", DATE_FORMAT.format(orderDateField.getValue()));

				leftStore.add(newItem);
				saveItemToDB(SOURCE_GRID_URL, newItem, leftGrid);

				refreshGrid();

				window.hide();
			}
		});

		form.add(productField);
		form.add(quantityField);
		form.add(priceField);
		form.add(orderDateField);
		form.addButton(cancelButton);
		form.addButton(saveButton);
		window.add(form);
		window.show();
	}

	private void refreshGrid() {
		leftStore.commitChanges();
		leftGrid.getView().refresh(false);
		rightStore.commitChanges();
		rightGrid.getView().refresh(false);
	}

	private ToolBar createToolBar() {
		ToolBar toolBar = new ToolBar();
		Button addButton = new Button("Új tétel", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				openCreateWindow();
			}
		});
		toolBar.add(addButton);
		return toolBar;
	}

	private List<ModelData> parseJson(String jsonText) {
		List<ModelData> items = new ArrayList<ModelData>();

		JSONValue jsonValue = JSONParser.parseLenient(jsonText);
		JSONArray jsonArray = jsonValue.isArray();

		if (jsonArray == null) {
			MessageBox.alert("Hiba", "JSON formátum érvénytelen!", null);
			return items;
		}

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = jsonArray.get(i).isObject();
			if (jsonObject != null) {
				BaseModelData model = new BaseModelData();

				model.set("id", (int) jsonObject.get("id").isNumber().doubleValue());
				model.set("product", jsonObject.get("product").isString().stringValue());

				if (jsonObject.get("quantity").isNumber() != null) {
					model.set("quantity", (int) jsonObject.get("quantity").isNumber().doubleValue());
				} else {
					model.set("quantity", 0);
				}

				if (jsonObject.get("price").isNumber() != null) {
					model.set("price", jsonObject.get("price").isNumber().doubleValue());
				} else {
					model.set("price", 0.0);
				}

				String dateString = jsonObject.get("orderDate").isString().stringValue();
				Date date = DATE_FORMAT.parse(dateString);

				model.set("orderDate", date);

				items.add(model);
			}
		}

		return items;
	}

	private void loadData(String url, final ListStore<ModelData> store) {
		leftGrid.mask("Betöltés..");
		rightGrid.mask("Betöltés..");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					leftGrid.unmask();
					rightGrid.unmask();
					if (response.getStatusCode() == 200) {
						store.removeAll();
						List<ModelData> parsedData = parseJson(response.getText());
						if (!parsedData.isEmpty()) {
							store.add(parsedData);
							leftGrid.getView().refresh(false);
							rightGrid.getView().refresh(false);
						} else {
							MessageBox.alert("Figyelem", "Nincsenek betöltött adatok!", null);
						}
					} else {
						MessageBox.alert("Hiba", "Adatbetöltési hiba: " + response.getStatusText(), null);
					}
				}

				public void onError(Request request, Throwable exception) {
					leftGrid.unmask();
					rightGrid.unmask();
					MessageBox.alert("Hiba", "Hálózati hiba történt!", null);
				}
			});
		} catch (RequestException e) {
			leftGrid.unmask();
			rightGrid.unmask();
			MessageBox.alert("Hiba", "Hálózati hiba történt!", null);
		}
	}

	private String createJsonFromModel(ModelData item) {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("id", new JSONNumber(((Integer) item.get("id")).doubleValue()));
		jsonObject.put("product", new JSONString((String) item.get("product")));
		jsonObject.put("quantity", new JSONNumber(((Integer) item.get("quantity")).doubleValue()));
		jsonObject.put("price", new JSONNumber(((Number) item.get("price")).doubleValue()));
		jsonObject.put("orderDate", new JSONString((String) item.get("orderDate")));

		return jsonObject.toString();
	}

	public void saveItemToDB(final String url, final ModelData item, final Grid<ModelData> targetGrid) {
		targetGrid.mask("Mentés folyamatban..");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/json");

		try {
			String jsonData = createJsonFromModel(item);

			builder.sendRequest(jsonData, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						JSONValue jsonValue = JSONParser.parseLenient(response.getText());
						JSONObject jsonObject = jsonValue.isObject();

						if (jsonObject != null && jsonObject.containsKey("success")) {
							MessageBox.alert("Siker", "Adat mentése sikeres a cél adatbázisba!", null);
							loadData(TARGET_GRID_URL, rightStore);
						} else {
							MessageBox.alert("Hiba", "Mentés sikertelen! Szerver visszautasította az adatot.", null);
						}
					} else {
						MessageBox.alert("Hiba", "Szerverhiba mentéskor: " + response.getStatusText(), null);
					}
				}

				public void onError(Request request, Throwable exception) {
					targetGrid.unmask();
					MessageBox.alert("Hiba", "Hálózati hiba történt a mentés során!", null);
				}
			});
		} catch (RequestException e) {
			targetGrid.unmask();
			MessageBox.alert("Hiba", "Hálózati hiba történt a mentés során!", null);
		}
	}

	private void deleteItemFromDB(final String url, final ModelData item, final RequestCallback callback) {
		leftGrid.mask("Törlés folyamatban...");
		RequestBuilder builder = new RequestBuilder(RequestBuilder.DELETE, url + "?id=" + item.get("id"));
		try {
			builder.sendRequest(null, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						JSONValue jsonValue = JSONParser.parseLenient(response.getText());
						JSONObject jsonObject = jsonValue.isObject();

						if (jsonObject != null && jsonObject.containsKey("success")) {
							leftGrid.unmask();
							callback.onResponseReceived(request, response);
						} else {
							MessageBox.alert("Hiba", "Törlés sikertelen! Az elem nem található az adatbázisban.", null);
						}
					} else {
						MessageBox.alert("Hiba", "Szerverhiba törléskor: " + response.getStatusText(), null);
					}
					isOperationInProgress = false;
				}

				public void onError(Request request, Throwable exception) {
					leftGrid.unmask();
					MessageBox.alert("Hiba", "Hálózati hiba történt a törlés során!", null);
					isOperationInProgress = false;
				}
			});
		} catch (RequestException e) {
			MessageBox.alert("Hiba", "Hálózati hiba történt a törlés során!", null);
			isOperationInProgress = false;
		}
	}

}
