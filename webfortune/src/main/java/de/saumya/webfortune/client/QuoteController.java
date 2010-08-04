package de.saumya.webfortune.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class QuoteController {

  protected final QuoteHTML quoteHTML;

  public QuoteController(final QuoteHTML quoteHTML) {
    this.quoteHTML = quoteHTML;
  }

  protected void setupQuote(final Quote quote, final String text) {
    GWT.log(quote.topic.displayName, null);
    this.quoteHTML.setHTML(text);
  }

  public void load(final Quote quote) {
    String url = GWT.getModuleBaseURL() + quote.file();
    RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, url);

    try {
      this.quoteHTML.setText("loading...");
      builder.sendRequest(null, new RequestCallback() {
        public void onError(final Request request, final Throwable exception) {
          QuoteController.this.quoteHTML.setHTML("<pre>"
              + exception.getMessage() + "</pre>");
        }

        public void onResponseReceived(final Request request,
            final Response response) {
          QuoteController.this.setupQuote(quote, response.getText());
        }
      });
    } catch (RequestException e) {
      this.quoteHTML.setHTML("<pre>" + e.getMessage() + "</pre>");
    }
  }

  public void hideQuotes() {
    this.quoteHTML.setVisible(false);
  }

  public void showQuotes() {
    this.quoteHTML.setVisible(true);
  }

}
