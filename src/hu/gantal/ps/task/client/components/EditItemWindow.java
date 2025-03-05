package hu.gantal.ps.task.client.components;

import java.util.Date;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

public class EditItemWindow extends Window {

    public interface SaveListener {
        void onSave(BaseModelData updatedModel);
    }

    private SaveListener saveListener;

    private final FormPanel form;
    private final TextField<String> productField;
    private final NumberField quantityField;
    private final NumberField priceField;
    private final DateField orderDateField;

    public EditItemWindow(final BaseModelData item, String title) {
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
        Object productValue = item.get("product");
        productField.setValue(productValue != null ? productValue.toString() : "");

        quantityField = new NumberField();
        quantityField.setFieldLabel("Mennyiség");
        quantityField.setAllowBlank(false);
        quantityField.setAllowNegative(false);
        Object quantityValue = item.get("quantity");
        quantityField.setValue(quantityValue != null ? Integer.parseInt(quantityValue.toString()) : 0);

        priceField = new NumberField();
        priceField.setFieldLabel("Egységár");
        priceField.setAllowBlank(false);
        priceField.setAllowNegative(false);
        Object priceValue = item.get("price");
        priceField.setValue(priceValue != null ? Double.parseDouble(priceValue.toString()) : 0.0);

        orderDateField = new DateField();
        orderDateField.setFieldLabel("Rendelés dátuma");
        orderDateField.setAllowBlank(false);

        Object orderDateValue = item.get("orderDate");
        if (orderDateValue instanceof String) {
            try {
                DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
                orderDateField.setValue(orderDateValue != null ? dateFormat.parse((String) orderDateValue) : new Date());
            } catch (IllegalArgumentException e) {
                System.out.println("Hibás dátumformátum: " + orderDateValue);
            }
        } else if (orderDateValue instanceof Date) {
            orderDateField.setValue(orderDateValue != null ? (Date) orderDateValue : new Date());
        } else {
            orderDateField.setValue(new Date());
        }

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

                DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(orderDateField.getValue());

                item.set("product", productField.getValue());
                item.set("quantity", quantityField.getValue());
                item.set("price", priceField.getValue());
                item.set("orderDate", formattedDate);

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
