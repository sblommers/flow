package com.vaadin.tests.components.datefield;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.tests.components.AbstractTestUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.VerticalLayout;

public class DateFieldRanges extends AbstractTestUI {

    @Override
    protected Integer getTicketNumber() {
        return 6241;
    }

    private Label label = new Label();
    private NativeSelect resoSelect = new NativeSelect("Resolution");
    private DateField fromRange = new DateField("Range start");
    private DateField toRange = new DateField("Range end");
    private DateField valueDF = new DateField("Value");
    private Button recreate = new Button("Recreate static datefields");
    private Button clearRangeButton = new Button("Clear range");

    private GridLayout currentStaticContainer;

    private DateField inlineDynamicDateField;
    private DateField dynamicDateField;

    private Calendar createCalendar() {
        Calendar c = Calendar.getInstance();
        c.set(2013, 3, 26, 6, 1, 12);
        return c;
    }

    private Date newDate() {
        return createCalendar().getTime();
    }

    private void initializeControlFields() {
        resoSelect.addItem(Resolution.MINUTE);
        resoSelect.addItem(Resolution.SECOND);
        resoSelect.addItem(Resolution.HOUR);
        resoSelect.addItem(Resolution.DAY);
        resoSelect.addItem(Resolution.MONTH);
        resoSelect.addItem(Resolution.YEAR);
        resoSelect.setValue(Resolution.DAY);
        resoSelect.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                Resolution r = (Resolution) resoSelect.getValue();
                inlineDynamicDateField.setResolution(r);
                dynamicDateField.setResolution(r);

            }
        });

        fromRange.setValue(null);
        fromRange.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                inlineDynamicDateField.setRangeStart(fromRange.getValue());
                dynamicDateField.setRangeStart(fromRange.getValue());

            }
        });

        toRange.setValue(null);
        toRange.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                inlineDynamicDateField.setRangeEnd(toRange.getValue());
                dynamicDateField.setRangeEnd(toRange.getValue());

            }
        });

        valueDF.setValue(null);
        valueDF.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {

                inlineDynamicDateField.setValue(valueDF.getValue());
                dynamicDateField.setValue(valueDF.getValue());

            }
        });

        recreate.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                GridLayout newContainer = createStaticFields();
                replace(currentStaticContainer, newContainer);
                currentStaticContainer = newContainer;
            }
        });

        clearRangeButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                fromRange.setValue(null);
                toRange.setValue(null);
            }
        });

        Calendar startCal = createCalendar();
        Calendar endCal = createCalendar();
        endCal.add(Calendar.DATE, 30);

        dynamicDateField = createDateField(startCal.getTime(), endCal.getTime(),
                null, Resolution.DAY, false);
        inlineDynamicDateField = createDateField(startCal.getTime(),
                endCal.getTime(), null, Resolution.DAY, true);

        resoSelect.setId("resoSelect");
        fromRange.setId("fromRange");
        toRange.setId("toRange");
        valueDF.setId("valueDF");
        recreate.setId("recreate");
        clearRangeButton.setId("clearRangeButton");
        dynamicDateField.setId("dynamicDateField");
        inlineDynamicDateField.setId("inlineDynamicDateField");

    }

    @Override
    protected void setup(VaadinRequest request) {
        setLocale(new Locale("en", "US"));
        getLayout().setWidth(100, Unit.PERCENTAGE);
        getLayout().setHeight(null);
        getLayout().setMargin(true);
        getLayout().setSpacing(true);

        initializeControlFields();

        GridLayout gl = new GridLayout(2, 2);
        gl.setSpacing(true);

        gl.addComponent(dynamicDateField);
        gl.addComponent(inlineDynamicDateField);

        HorizontalLayout hl = new HorizontalLayout();
        hl.setSpacing(true);
        hl.addComponent(resoSelect);
        hl.addComponent(fromRange);
        hl.addComponent(toRange);
        hl.addComponent(valueDF);
        hl.addComponent(recreate);
        hl.addComponent(clearRangeButton);
        add(hl);
        add(new Label("Dynamic DateFields"));
        add(gl);
        currentStaticContainer = createStaticFields();
        add(new Label("Static DateFields"));
        add(currentStaticContainer);

        add(label);

    }

    private GridLayout createStaticFields() {
        Calendar startCal = createCalendar();
        Calendar endCal = createCalendar();
        endCal.add(Calendar.DATE, 30);
        GridLayout gl = new GridLayout(2, 2);
        gl.setSpacing(true);
        DateField df = createDateField(startCal.getTime(), endCal.getTime(),
                null, Resolution.DAY, false);
        gl.addComponent(df);
        DateField inline = createDateField(startCal.getTime(), endCal.getTime(),
                null, Resolution.DAY, true);
        gl.addComponent(inline);
        inline.setId("staticInline");
        VerticalLayout vl = new VerticalLayout();

        return gl;
    }

    private DateField createDateField(Date rangeStart, Date rangeEnd,
            Date value, Resolution resolution, boolean inline) {

        DateField df = null;

        if (inline) {
            df = new InlineDateField();
        } else {
            df = new DateField();
        }

        final DateField gg = df;
        updateValuesForDateField(df);

        df.addValueChangeListener(new ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                label.setValue((gg.getValue() == null ? "Nothing"
                        : gg.getValue().toString()) + " selected. isValid: "
                        + gg.isValid());
            }
        });
        return df;
    }

    @Override
    protected String getTestDescription() {
        return "Not defined yet";

    }

    private void updateValuesForDateField(DateField df) {
        Date fromVal = fromRange.getValue();
        Date toVal = toRange.getValue();
        Date value = valueDF.getValue();
        Resolution r = (Resolution) resoSelect.getValue();

        df.setValue(value);
        df.setResolution(r);
        df.setRangeStart(fromVal);
        df.setRangeEnd(toVal);

    }

}