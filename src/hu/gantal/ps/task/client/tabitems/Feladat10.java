package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.Events;
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
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import hu.gantal.ps.task.client.components.EditItemWindow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feladat10 extends TabItem {

    private ListStore<BaseModelData> store;
    private Grid<BaseModelData> grid;

    public Feladat10() {
        super("Feladat10");
        setSize(600, 400);
        setLayout(new FitLayout());
        initGrid();
        add(createMainPanel());
        loadDataFromServer();
        layout();
    }

    private void initGrid() {
        store = new ListStore<BaseModelData>();

        List<ColumnConfig> configs = new ArrayList<ColumnConfig>();
        configs.add(new ColumnConfig("id", "Azonosító", 50));
        configs.add(new ColumnConfig("product", "Termék", 150));
        configs.add(new ColumnConfig("quantity", "Mennyiség", 80));
        configs.add(new ColumnConfig("price", "Egységár", 100));
        configs.add(new ColumnConfig("orderDate", "Rendelés dátuma", 120));

        ColumnModel cm = new ColumnModel(configs);

        grid = new Grid<BaseModelData>(store, cm);
        grid.setBorders(true);
        grid.setAutoExpandColumn("product");
        grid.setSelectionModel(new GridSelectionModel<BaseModelData>());
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
        panel.setHeading("Feladat10");
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
                BaseModelData selected = grid.getSelectionModel().getSelectedItem();
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
        String url = GWT.getHostPageBaseURL() + "feladat10_data.php";

        RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);
        try {
            builder.sendRequest(null, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == 200) {
                        String jsonText = response.getText();
                        parseAndLoadJson(jsonText);
                    } else {
                        System.out.println("Hiba: HTTP " + response.getStatusCode());
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    exception.printStackTrace();
                }
            });
        } catch (RequestException e) {
            e.printStackTrace();
        }
    }

    private void parseAndLoadJson(String jsonText) {
        JSONValue value = JSONParser.parseLenient(jsonText);
        if (value != null && value.isArray() != null) {
            JSONArray arr = value.isArray();
            for (int i = 0; i < arr.size(); i++) {
                JSONObject obj = arr.get(i).isObject();
                if (obj != null) {
                    BaseModelData model = new BaseModelData();
                    model.set("id", getInt(obj, "id"));
                    model.set("product", getString(obj, "product"));
                    model.set("quantity", getInt(obj, "quantity"));
                    model.set("price", getDouble(obj, "price"));
                    model.set("orderDate", getString(obj, "orderDate"));
                    store.add(model);
                }
            }
        }
    }

    private Integer getInt(JSONObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).isNumber() != null) {
            return (int) obj.get(key).isNumber().doubleValue();
        }
        return null;
    }
    private Double getDouble(JSONObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).isNumber() != null) {
            return obj.get(key).isNumber().doubleValue();
        }
        return null;
    }
    private String getString(JSONObject obj, String key) {
        if (obj.containsKey(key) && obj.get(key).isString() != null) {
            return obj.get(key).isString().stringValue();
        }
        return null;
    }

    private void openCreateWindow() {
        final BaseModelData newItem = new BaseModelData();
        newItem.set("id", store.getCount() + 1); 
        newItem.set("orderDate", new Date());

        EditItemWindow w = new EditItemWindow(newItem, "Új tétel hozzáadása");
        w.setSaveListener(new EditItemWindow.SaveListener() {
            @Override
            public void onSave(BaseModelData updatedModel) {
                store.add(updatedModel);
            }
        });
        w.show();
    }

    private void openEditWindow(final BaseModelData item) {
        EditItemWindow w = new EditItemWindow(item, "Tétel szerkesztése");
        w.setSaveListener(new EditItemWindow.SaveListener() {
            @Override
            public void onSave(BaseModelData updatedModel) {
                store.update(updatedModel);
            }
        });
        w.show();
    }
}
