package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.*;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.*;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import hu.gantal.ps.task.client.components.EditItemWindow;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feladat11 extends TabItem {

	private ListStore<ModelData> store;
	private Grid<ModelData> grid;

	public Feladat11() {
		super("Feladat11");
		setSize(600, 400);
		setLayout(new FitLayout());
		initGrid();
		add(createMainPanel());
		loadDataFromServer();
		layout();
	}

	private void initGrid() {
		store = new ListStore<ModelData>();

		List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
		configs.add(new ColumnConfig("id", "Azonosító", 50));
		configs.add(new ColumnConfig("product", "Termék", 150));
		configs.add(new ColumnConfig("quantity", "Mennyiség", 80));
		configs.add(new ColumnConfig("price", "Egységár", 100));
		configs.add(new ColumnConfig("orderDate", "Rendelés dátuma", 120));

		ColumnModel cm = new ColumnModel(configs);

		grid = new Grid<ModelData>(store, cm);
		grid.setBorders(true);
		grid.setAutoExpandColumn("product");
		grid.setSelectionModel(new GridSelectionModel<ModelData>());
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<BaseModelData>>() {
			@Override
			public void handleEvent(GridEvent<BaseModelData> be) {
				BaseModelData selectedItem = be.getModel();
				if (selectedItem != null) {
					openEditWindow(selectedItem);
				}
			}
		});
	}

	private ContentPanel createMainPanel() {
		ContentPanel panel = new ContentPanel();
		panel.setHeading("Feladat11");
		panel.setLayout(new FitLayout());
		panel.setTopComponent(createToolBar());
		panel.add(grid);
		return panel;
	}

	private ToolBar createToolBar() {
		ToolBar toolBar = new ToolBar();

		Button buttonCreate = new Button("Új", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				openCreateWindow();
			}
		});

		Button buttonDelete = new Button("Törlés", new SelectionListener<ButtonEvent>() {
			@Override
			public void componentSelected(ButtonEvent ce) {
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					store.remove(selected);
				}
			}
		});

		toolBar.add(buttonCreate);
		toolBar.add(buttonDelete);

		return toolBar;
	}

	private void loadDataFromServer() {
		String url = GWT.getHostPageBaseURL() + "feladat11_data.php";
		ModelType modelType = new ModelType();
		modelType.setRoot("data");
		modelType.addField("id", "id");
		modelType.addField("product", "product");
		modelType.addField("quantity", "quantity");
		modelType.addField("price", "price");
		modelType.addField("orderDate", "orderDate");

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
		HttpProxy<String> proxy = new HttpProxy<String>(builder);
		JsonReader<ListLoadResult<ModelData>> reader = new JsonLoadResultReader<ListLoadResult<ModelData>>(
				modelType);
		BaseListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy,
				reader);
		store = new ListStore<ModelData>(loader);
		grid.reconfigure(store, grid.getColumnModel());

		loader.load();
	}

	private void openCreateWindow() {
		final BaseModelData newItem = new BaseModelData();
		newItem.set("id", store.getCount() + 1);
		newItem.set("orderDate", new Date());

		EditItemWindow w = new EditItemWindow(newItem, "Új tétel hozzáadása");
		w.setSaveListener(new EditItemWindow.SaveListener() {
			@Override
			public void onSave(ModelData updatedModel) {
				store.add(updatedModel);
			}
		});
		w.show();
	}

	private void openEditWindow(final BaseModelData item) {
		EditItemWindow w = new EditItemWindow(item, "Tétel szerkesztése");
		w.setSaveListener(new EditItemWindow.SaveListener() {
			@Override
			public void onSave(ModelData updatedModel) {
				store.update(updatedModel);
			}
		});
		w.show();
	}
}