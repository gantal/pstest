package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Feladat8 extends TabItem {

    private ListStore<BaseModelData> store;
    private Grid<BaseModelData> grid;

    public Feladat8() {
        super("Feladat8");
		setSize(600, 400);
		setLayout(new FitLayout());
		initGrid();
		add(createMainPanel());
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
		grid.setHeight(300);
		grid.setAutoExpandColumn("product");
		grid.setSelectionModel(new GridSelectionModel<BaseModelData>());
		grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private ContentPanel createMainPanel() {
		ContentPanel panel = new ContentPanel();
		panel.setLayout(new FitLayout());
		panel.setTopComponent(createToolBar());
		panel.add(grid);
		panel.setSize("100%", "100%");
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

    private void openCreateWindow() {
        final Window window = new Window();
        window.setHeaderVisible(true);
        window.setHeading("Új tétel hozzáadása");
        window.setModal(true);
        window.setSize(300, 250);
        window.setLayout(new FitLayout());

        final FormPanel form = new FormPanel();
        form.setFrame(true);
        form.setLabelWidth(100);

        final TextField<String> productField = new TextField<String>();
        productField.setFieldLabel("Termék");
        productField.setAllowBlank(false);

        final NumberField quantityField = new NumberField();
        quantityField.setFieldLabel("Mennyiség");
        quantityField.setPropertyEditorType(Integer.class);
        quantityField.setAllowBlank(false);
        quantityField.setAllowNegative(false);
        quantityField.setMinValue(1);

        final NumberField priceField = new NumberField();
        priceField.setFieldLabel("Egységár");
        priceField.setPropertyEditorType(Double.class);
        priceField.setAllowBlank(false);
        priceField.setAllowNegative(false);
        priceField.setMinValue(1.0);

        final DateField orderDateField = new DateField();
        orderDateField.setFieldLabel("Rendelés dátuma");
        orderDateField.setAllowBlank(false);
        orderDateField.setValue(new Date());

        Button cancelButton = new Button("Mégse", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                window.hide();
            }
        });

        Button saveButton = new Button("Mentés", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                for (Field<?> f : form.getFields()) {
                    f.validate();
                }
                if (!form.isValid()) {
                    return;
                }

                BaseModelData newItem = new BaseModelData();
                newItem.set("id", store.getCount() + 1);
                newItem.set("product", productField.getValue());
                newItem.set("quantity", quantityField.getValue());
                newItem.set("price", priceField.getValue());
                newItem.set("orderDate", orderDateField.getValue());

                store.add(newItem);
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
}