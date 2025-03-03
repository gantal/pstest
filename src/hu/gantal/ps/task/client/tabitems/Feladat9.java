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
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridSelectionModel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Feladat9 extends TabItem {

	private ListStore<BaseModelData> store;
	private Grid<BaseModelData> grid;

	public Feladat9() {
		super("Feladat9");
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

	public static class EditItemWindow extends Window {

		public interface SaveListener {
			void onSave(BaseModelData updatedModel);
		}

		private BaseModelData item;
		private SaveListener saveListener;

		private final FormPanel form;
		private final TextField<String> productField;
		private final NumberField quantityField;
		private final NumberField priceField;
		private final DateField orderDateField;

		public EditItemWindow(final BaseModelData item, String title) {
			this.item = item;

			setHeading(title);
			setModal(true);
			setSize(300, 250);
			setLayout(new FitLayout());

			form = new FormPanel();
			form.setFrame(true);
			form.setLabelWidth(100);

			productField = new TextField<String>();
			productField.setFieldLabel("Termék");
			productField.setAllowBlank(false);
			productField.setValue((String) item.get("product"));

			quantityField = new NumberField();
			quantityField.setFieldLabel("Mennyiség");
			quantityField.setAllowBlank(false);
			quantityField.setAllowNegative(false);
			quantityField.setValue((Number) item.get("quantity"));

			priceField = new NumberField();
			priceField.setFieldLabel("Egységár");
			priceField.setAllowBlank(false);
			priceField.setAllowNegative(false);
			priceField.setValue((Number) item.get("price"));

			orderDateField = new DateField();
			orderDateField.setFieldLabel("Rendelés dátuma");
			orderDateField.setAllowBlank(false);
			orderDateField.setValue((Date) item.get("orderDate"));

			form.add(productField);
			form.add(quantityField);
			form.add(priceField);
			form.add(orderDateField);

			Button cancelButton = new Button("Mégse", new SelectionListener<ButtonEvent>() {
				@Override
				public void componentSelected(ButtonEvent ce) {
					hide();
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

					item.set("product", productField.getValue());
					item.set("quantity", quantityField.getValue());
					item.set("price", priceField.getValue());
					item.set("orderDate", orderDateField.getValue());

					if (saveListener != null) {
						saveListener.onSave(item);
					}

					hide();
				}
			});

			form.addButton(cancelButton);
			form.addButton(saveButton);

			add(form);
		}

		public void setSaveListener(SaveListener listener) {
			this.saveListener = listener;
		}
	}
}
