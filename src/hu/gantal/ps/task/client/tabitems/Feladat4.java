package hu.gantal.ps.task.client.tabitems;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.google.gwt.user.client.Element;
import java.util.ArrayList;
import java.util.List;

public class Feladat4 extends TabItem {
    public static final int GRID_SIZE = 3;
    public static final int BUTTON_SIZE = 100;

    private List<List<Button>> buttons = new ArrayList<List<Button>>();

    public Feladat4() {
        super("Feladat4");
    }

    private void updateBtnValues(int row, int col) {
        try {
            if (buttons == null || buttons.isEmpty()) {
                throw new IllegalStateException("Gombok listája nincs inicializálva!");
            }

            Button clickedBtn = buttons.get(row).get(col);
            if (clickedBtn == null) {
                throw new NullPointerException("A megnyomott gomb null értéket kapott!");
            }

            Integer value = (Integer) clickedBtn.getData("value");
            if (value == null) {
                value = 1; // Alapértelmezett érték beállítása
            }

            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if (i == row || j == col) {
                        Button btn = buttons.get(i).get(j);
                        if (btn == clickedBtn) {
                            continue;
                        }

                        Integer oldValue = (Integer) btn.getData("value");
                        if (oldValue == null) {
                            oldValue = 1;
                        }

                        int newValue = oldValue + value;
                        btn.setData("value", newValue);
                        btn.setText(String.valueOf(newValue));
                    }
                }
            }
        } catch (Exception e) {
            MessageBox.alert("Hiba", "Hiba történt a gombok frissítésekor: " + e.getMessage(), null);
        }
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);

        try {
            VBoxLayout vBox = new VBoxLayout();
            setLayout(vBox);

            for (int row = 0; row < GRID_SIZE; row++) {
                LayoutContainer rowContainer = new LayoutContainer();
                rowContainer.setLayout(new HBoxLayout());
                rowContainer.setSize("auto", "auto");
                List<Button> rowButtons = new ArrayList<Button>();

                for (int col = 0; col < GRID_SIZE; col++) {
                    Button btn = new Button("1");
                    btn.setSize(BUTTON_SIZE, BUTTON_SIZE);
                    btn.setData("value", 1);

                    final int currentRow = row;
                    final int currentCol = col;
                    btn.addListener(Events.OnClick, new Listener<ComponentEvent>() {
                        @Override
                        public void handleEvent(ComponentEvent event) {
                            updateBtnValues(currentRow, currentCol);
                        }
                    });

                    rowButtons.add(btn);
                    rowContainer.add(btn);
                }

                buttons.add(rowButtons);
                add(rowContainer);
            }
        } catch (Exception e) {
            MessageBox.alert("Hiba", "Hiba történt az UI betöltésekor: " + e.getMessage(), null);
        }
    }
}
