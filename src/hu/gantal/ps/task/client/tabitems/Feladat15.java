package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.dnd.GridDragSource;
import com.extjs.gxt.ui.client.dnd.GridDropTarget;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Feladat15 extends TabItem {
    private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

    private ListStore<ModelData> leftStore;
    private ListStore<ModelData> rightStore;
    private Grid<ModelData> leftGrid;
    private Grid<ModelData> rightGrid;
    private ColumnModel cm;

    public Feladat15() {
        super("Feladat15");
        setSize(1000, 600);
        setLayout(new FitLayout());
        add(createMainPanel());
        layout();
    }

    private ContentPanel createMainPanel() {
        ContentPanel panel = new ContentPanel();
        panel.setHeading("Feladat15");
        panel.setLayout(new RowLayout(Orientation.HORIZONTAL));
        cm = new ColumnModel(defineColumns());
        leftStore = new ListStore<ModelData>();
        gridSeeder(leftStore);
        rightStore = new ListStore<ModelData>();
        leftGrid = setupGrid(leftStore, "Forrás tábla");
        rightGrid = setupGrid(rightStore, "Cél tábla");
        panel.add(leftGrid, new RowData(.5, 1, new Margins(6)));
        panel.add(rightGrid, new RowData(.5, 1, new Margins(6, 6, 6, 0)));
        enableDragAndDrop(leftGrid, rightGrid);
        enableDragAndDrop(rightGrid, leftGrid);
        panel.setTopComponent(createToolBar());

        return panel;
    }

	private List<ColumnConfig> defineColumns() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		ColumnConfig idCol = new ColumnConfig("id", "Azonosító", 80);
		idCol.setNumberFormat(NumberFormat.getFormat("#"));
		columns.add(idCol);
		columns.add(new ColumnConfig("product", "Termék", 150));
		ColumnConfig quantityCol = new ColumnConfig("quantity", "Mennyiség", 80);
		quantityCol.setNumberFormat(NumberFormat.getFormat("#"));
		columns.add(quantityCol);
		ColumnConfig priceCol = new ColumnConfig("price", "Egységár", 100);
		priceCol.setNumberFormat(NumberFormat.getFormat("#.#"));
		columns.add(priceCol);

		ColumnConfig orderDateColumn = new ColumnConfig("orderDate", "Rendelés dátuma", 120);
		orderDateColumn.setRenderer(new GridCellRenderer<ModelData>() {
			@Override
			public Object render(ModelData model, String property, ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				Object value = model.get(property);
				if (value instanceof String) {
					try {
						Date parsedDate = DATE_FORMAT.parse((String) value);
						return DATE_FORMAT.format(parsedDate);
					} catch (Exception e) {
						return "Hibás dátum";
					}
				} else if (value instanceof Date) {
					return DATE_FORMAT.format((Date) value);
				}
				return "N/A";
			}
		});
		columns.add(orderDateColumn);

		return columns;
	}

    private void gridSeeder(ListStore<ModelData> store) {
        for (int i = 0; i <= 10; i++) {
            ModelData model = new BaseModelData();
            Random random = new Random();
            model.set("id", i + 1);
            model.set("product", "Termék " + (i + 1));
            model.set("quantity", random.nextInt(10) + 1);
            model.set("price", 00 + (random.nextDouble() * (10000 - 100)));
            model.set("orderDate", DATE_FORMAT.format(new Date()));

            store.add(model);
        }
    }

    private Grid<ModelData> setupGrid(ListStore<ModelData> store, String title) {
        Grid<ModelData> grid = new Grid<ModelData>(store, cm);
        grid.setBorders(true);
        grid.setStateId(title);
        grid.setStateful(true);
        return grid;
    }

    private void enableDragAndDrop(Grid<ModelData> sourceGrid, Grid<ModelData> targetGrid) {
        new GridDragSource(sourceGrid);
        GridDropTarget target = new GridDropTarget(targetGrid);
        target.setAllowSelfAsSource(false);
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
    }
}
