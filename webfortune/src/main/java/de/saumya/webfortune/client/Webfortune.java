package de.saumya.webfortune.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Webfortune implements EntryPoint {
  
  protected final QuoteHTML quoteHtml = new QuoteHTML();
  protected final QuoteController controller = new QuoteController(quoteHtml);
  protected final QuoteButtons buttons = new QuoteButtons(controller);
  protected final TopicListPanel topicList = new TopicListPanel(controller, buttons);
  
  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {
    new TopicListLoader(topicList).load("topics.xml");
    RootPanel.get("topic-list").add(topicList);
    RootPanel.get("quote").add(quoteHtml);
    RootPanel.get("quote-buttons").add(buttons);
  }
  
 }
