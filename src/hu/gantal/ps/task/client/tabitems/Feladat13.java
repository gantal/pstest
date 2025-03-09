package hu.gantal.ps.task.client.tabitems;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.extjs.gxt.ui.client.data.BaseDateFilterConfig;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BaseNumericFilterConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.HttpProxy;
import com.extjs.gxt.ui.client.data.JsonPagingLoadResultReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.LoadEvent;
import com.extjs.gxt.ui.client.data.Loader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelType;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.filters.DateFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.GridFilters;
import com.extjs.gxt.ui.client.widget.grid.filters.NumericFilter;
import com.extjs.gxt.ui.client.widget.grid.filters.StringFilter;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;
import com.extjs.gxt.ui.client.widget.grid.filters.Filter;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import hu.gantal.ps.task.client.components.EditItemWindow;
import hu.gantal.ps.task.client.components.EditItemWindow.SaveListener;
import hu.gantal.ps.task.client.utils.NumberUtil;

public class Feladat13 extends TabItem {
	private static final int PAGE_SIZE = 2;
	private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static final String DATA_URL = GWT.getHostPageBaseURL() + "feladat13_data.php";
	private static final String CREATE_URL = GWT.getHostPageBaseURL() + "feladat13_create.php";
	private static final String UPDATE_URL = GWT.getHostPageBaseURL() + "feladat13_update.php";

	private ColumnModel cm;
	private PagingLoader<PagingLoadResult<BaseModelData>> loader;
	private ListStore<ModelData> store;
	private Grid<ModelData> grid;
	private PagingToolBar pagingToolBar;
	private GridFilters gridFilters;

	public Feladat13() {
		super("Feladat13");
		setSize(600, 400);
		setLayout(new FitLayout());
		ContentPanel panel = createContentPanel();
		add(panel);
		loader.load();
		layout();
	}

	private ContentPanel createContentPanel() {
		ContentPanel panel = new ContentPanel();
		panel.setHeading("Feladat13");
		panel.setLayout(new FitLayout());

		List<ColumnConfig> columns = defineColumns();
		cm = new ColumnModel(columns);

		loader = setupLoader();
		store = new ListStore<ModelData>(loader);
		gridFilters = setupGridFilters();
		grid = setupGrid();

		ToolBar toolBar = createToolBar();
		pagingToolBar = new PagingToolBar(PAGE_SIZE);
		pagingToolBar.bind(loader);

		panel.setTopComponent(toolBar);
		panel.setBottomComponent(pagingToolBar);
		panel.add(grid);

		return panel;
	}

	private GridFilters setupGridFilters() {
		gridFilters = new GridFilters();
		gridFilters.addFilter(new StringFilter("product"));
		gridFilters.addFilter(new NumericFilter("quantity"));
		gridFilters.addFilter(new NumericFilter("price"));
		gridFilters.addFilter(new DateFilter("orderDate"));

		return gridFilters;
	}

	private String formatDateFilter(List<BaseDateFilterConfig> dateFilters) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < dateFilters.size(); i++) {
			BaseDateFilterConfig filter = dateFilters.get(i);
			Date date = (Date) filter.getValue();
			String formattedDate = DATE_FORMAT.format(date);

			if (i > 0) {
				result.append("_");
			}
			result.append(filter.getComparison()).append("=").append(formattedDate);
		}

		return result.toString();
	}

	private String formatNumericFilter(List<BaseNumericFilterConfig> numericFilters) {
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < numericFilters.size(); i++) {
			BaseNumericFilterConfig filter = numericFilters.get(i);
			String valueString = filter.getValue().toString();

			if (i > 0) {
				result.append("_");
			}
			result.append(filter.getComparison()).append("=").append(valueString);
		}

		return result.toString();
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
		columns.add(new ColumnConfig("price", "Egységár", 100));

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

	private PagingLoader<PagingLoadResult<BaseModelData>> setupLoader() {
		ModelType modelType = new ModelType();
		modelType.setRoot("data");
		modelType.setTotalName("total");
		modelType.addField("id", "id");
		modelType.addField("product", "product");
		modelType.addField("quantity", "quantity");
		modelType.addField("price", "price");
		modelType.addField("orderDate", "orderDate");

		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, DATA_URL);
		HttpProxy<String> proxy = new HttpProxy<String>(requestBuilder);
		JsonPagingLoadResultReader<ListLoadResult<BaseModelData>> reader = new JsonPagingLoadResultReader<ListLoadResult<BaseModelData>>(
				modelType);

		loader = new BasePagingLoader<PagingLoadResult<BaseModelData>>(proxy, reader) {
			protected Object newLoadConfig() {
				return new BaseFilterPagingLoadConfig();
			}
		};

		loader.setRemoteSort(true);
		loader.addListener(Loader.BeforeLoad, new Listener<LoadEvent>() {
		    @Override
		    public void handleEvent(LoadEvent event) {
		        StringBuilder sb = new StringBuilder();
		        List<Filter> activeFilters = gridFilters.getFilterData();
		        
		        for (Filter filter : activeFilters) {
		            sb.append(filter.getDataIndex()).append(":");
		            Object filterValue = filter.getValue();
		            String formattedValue = "";

		            if (filterValue instanceof List<?>) {
		                List<?> valuesList = (List<?>) filterValue;

		                if (!valuesList.isEmpty()) {
		                    Object firstElement = valuesList.get(0);

		                    if (firstElement instanceof BaseDateFilterConfig) {
		                        formattedValue = formatDateFilter((List<BaseDateFilterConfig>) valuesList);
		                    } else if (firstElement instanceof BaseNumericFilterConfig) {
		                        formattedValue = formatNumericFilter((List<BaseNumericFilterConfig>) valuesList);
		                    }
		                }
		            } else if (filterValue instanceof Number) {
		                formattedValue = "eq=" + filterValue.toString();
		            } else {
		                formattedValue = (filterValue != null) ? filterValue.toString() : "";
		            }

		            sb.append(formattedValue).append(",");
		        }

		        if (sb.length() > 0) {
		            sb.deleteCharAt(sb.length() - 1); // Utolsó vesszőt törölni kell
		        }

		        PagingLoadConfig config = event.getConfig();
		        config.set("filter", sb.toString());
		    }
		});


		return loader;
	}

	private Grid<ModelData> setupGrid() {
		grid = new Grid<ModelData>(store, cm);
		grid.setBorders(true);
		grid.setAutoExpandColumn("product");

		grid.addListener(Events.RowDoubleClick, new Listener<GridEvent<ModelData>>() {
			@Override
			public void handleEvent(GridEvent<ModelData> event) {
				ModelData selected = grid.getSelectionModel().getSelectedItem();
				if (selected != null) {
					openEditWindow(selected);
				}
			}
		});

		grid.addPlugin(gridFilters);

		return grid;
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

	private void sendDataToServer(String url, final ModelData item) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, url);
		builder.setHeader("Content-Type", "application/json");

		try {
			JSONObject jsonData = new JSONObject();
			jsonData.put("id", new JSONNumber(NumberUtil.getSafeInteger(item.get("id"))));
			jsonData.put("product", new JSONString((String) item.get("product")));
			jsonData.put("quantity", new JSONNumber(NumberUtil.getSafeInteger(item.get("quantity"))));
			jsonData.put("price", new JSONNumber(NumberUtil.getSafeDouble(item.get("price"))));
			jsonData.put("orderDate", new JSONString((String) item.get("orderDate")));

			builder.sendRequest(jsonData.toString(), new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() == 200) {
						loader.load();
					}
				}

				@Override
				public void onError(Request request, Throwable exception) {
					System.out.println("Hiba a szerverrel: " + exception.getMessage());
				}
			});

		} catch (RequestException e) {
			System.out.println("Hálózati hiba: " + e.getMessage());
		}
	}

	private void openCreateWindow() {
		final BaseModelData newItem = new BaseModelData();
		newItem.set("id", store.getCount() + 1);
		newItem.set("orderDate", DATE_FORMAT.format(new Date()));

		EditItemWindow w = new EditItemWindow(newItem, "Új tétel hozzáadása");
		w.setSaveListener(new SaveListener() {
			public void onSave(ModelData updatedModel) {
				sendDataToServer(CREATE_URL, updatedModel);
			}
		});
		w.show();
	}

	private void openEditWindow(final ModelData item) {
		EditItemWindow w = new EditItemWindow(item, "Tétel szerkesztése");
		w.setSaveListener(new SaveListener() {
			@Override
			public void onSave(ModelData updatedModel) {
				sendDataToServer(UPDATE_URL, updatedModel);
			}
		});
		w.show();
	}
}