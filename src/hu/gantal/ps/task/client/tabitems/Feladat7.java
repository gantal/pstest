package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.data.BaseModelData;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Feladat7 extends TabItem {

    private ListStore<BaseModelData> store;
    private Grid<BaseModelData> grid;

    public Feladat7() {
        super("Feladat7");
        setLayout(new FitLayout());
        initGrid();
        add(createMainPanel());
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
    }

    private VerticalPanel createMainPanel() {
        VerticalPanel panel = new VerticalPanel();
        panel.setLayout(new FitLayout());
        panel.setSpacing(5);
        panel.add(grid);
        panel.add(createToolBar());
        return panel;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        
        Button buttonCreate = new Button("Új", new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                BaseModelData newItem = new BaseModelData();
                newItem.set("id", store.getCount() + 1);
                newItem.set("product", "");
                newItem.set("quantity", 0);
                newItem.set("price", 0.0);
                newItem.set("orderDate", new Date());
                store.add(newItem);
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
}
