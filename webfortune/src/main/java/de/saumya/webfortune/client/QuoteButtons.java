/**
 * 
 */
package de.saumya.webfortune.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class QuoteButtons extends HorizontalPanel {

    private Quote        quote;

    private final Button next;

    private final Button previous;

    private final Button random;

    public QuoteButtons(final QuoteController quoteController) {
        this(quoteController,
                new Button("previous"),
                new Button("random"),
                new Button("next"));
    }

    public QuoteButtons(final QuoteController quoteController,
            final Button previous, final Button random, final Button next) {
        this.previous = previous;
        this.random = random;
        this.next = next;
        this.setStyleName("webfortune-QuoteButtons");
        this.random.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                QuoteButtons.this.quote.random();
                quoteController.load(QuoteButtons.this.quote);
            }
        });
        this.next.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                QuoteButtons.this.quote.next();
                quoteController.load(QuoteButtons.this.quote);
            }
        });
        this.previous.addClickHandler(new ClickHandler() {

            public void onClick(final ClickEvent event) {
                QuoteButtons.this.quote.previous();
                quoteController.load(QuoteButtons.this.quote);
            }
        });
        add(this.previous);
        add(this.random);
        add(this.next);
    }

    void setQuote(final Quote quote) {
        this.quote = quote;
        setVisible(this.quote != null);
    }
}