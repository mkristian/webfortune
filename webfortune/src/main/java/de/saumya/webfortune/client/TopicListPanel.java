package de.saumya.webfortune.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TopicListPanel extends VerticalPanel {

    private final QuoteButtons    buttons;

    private final QuoteController quoteController;

    public TopicListPanel(final QuoteController quoteController,
            final QuoteButtons buttons) {
        setStyleName("webfortune-TopicListPanel");
        this.quoteController = quoteController;
        this.quoteController.hideQuotes();
        this.buttons = buttons;
        this.buttons.setVisible(false);
    }

    public void addRadioButton(final Quote quote) {
        final RadioButton rb = new RadioButton("topic", quote.topic.displayName);

        // valueChangeHandler does not work when clicking on label
        rb.addMouseDownHandler(new MouseDownHandler() {

            public void onMouseDown(final MouseDownEvent event) {
                if (!rb.getValue()) {
                    random(quote);
                }
            }
        });
        rb.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

            public void onValueChange(final ValueChangeEvent<Boolean> event) {
                if (rb.getValue()) {
                    random(quote);
                }
            }
        });
        add(rb);
    }

    private void random(final Quote quote) {
        quote.random();
        TopicListPanel.this.quoteController.showQuotes();
        TopicListPanel.this.quoteController.load(quote);
        TopicListPanel.this.buttons.setQuote(quote);
    }
}