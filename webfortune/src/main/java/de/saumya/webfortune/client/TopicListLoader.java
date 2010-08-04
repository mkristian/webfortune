package de.saumya.webfortune.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NamedNodeMap;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class TopicListLoader {

    private final TopicListPanel panel;

    public TopicListLoader(final TopicListPanel panel) {
        this.panel = panel;
    }

    public void load(final String file) {
        final HTML html = new HTML("loading...");
        this.panel.add(html);
        final String url = GWT.getModuleBaseURL() + file;
        final RequestBuilder builder = new RequestBuilder(RequestBuilder.GET,
                url);

        try {
            builder.sendRequest(null, new RequestCallback() {
                public void onError(final Request request,
                        final Throwable exception) {
                    html.setHTML("<pre>" + exception.getMessage() + "</pre>");
                }

                public void onResponseReceived(final Request request,
                        final Response response) {
                    // allow lower than 200 for file load without webserver
                    if (response.getStatusCode() <= 200) {
                        TopicListLoader.this.panel.remove(0);
                        TopicListLoader.this.setupPanel(response.getText());
                    }
                    else {
                        html.setHTML("<pre>error loading " + url + "\n"
                                + response.getStatusText() + " ("
                                + response.getStatusCode() + ")" + "</pre>");
                    }
                }
            });
        }
        catch (final RequestException e) {
            html.setHTML("<pre>" + e.getMessage() + "</pre>");
        }
    }

    private void setupPanel(String xml) {
        // remove xml processing instruction since some browser (i.e. Opera) can
        // not
        // handle it
        xml = xml.substring(xml.indexOf("<topics"));
        final Document doc = XMLParser.parse(xml);
        final NodeList list = doc.getFirstChild().getChildNodes();
        final List<Topic> topics = new ArrayList<Topic>();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                final NamedNodeMap attributes = list.item(i).getAttributes();
                final String directory = attributes.getNamedItem("directory")
                        .getNodeValue();
                final String displayname = attributes.getNamedItem("displayname")
                        .getNodeValue();
                final int max = Integer.parseInt(attributes.getNamedItem("size")
                        .getNodeValue());
                final Node url = attributes.getNamedItem("href");
                Topic topic;
                if (url != null) {
                    final String linkText = attributes.getNamedItem("linktext")
                            .getNodeValue();
                    final Node target = attributes.getNamedItem("target");
                    final String targetValue = target == null
                            ? null
                            : target.getNodeValue();
                    topic = new Topic(directory,
                            displayname,
                            max,
                            linkText,
                            url.getNodeValue(),
                            targetValue);
                }
                else {
                    topic = new Topic(directory, displayname, max);
                }
                topics.add(topic);
                this.panel.addRadioButton(new Quote(topic));
            }
        }
        int total = 0;
        for (final Topic topic : topics) {
            GWT.log(topic.displayName + " " + topic.max, null);
            total += topic.max;
        }
        int rand = Random.nextInt(total);
        int index = 0;
        for (final Topic topic : topics) {
            rand -= topic.max;
            if (rand < 0) {
                break;
            }
            index++;
        }
        ((RadioButton) this.panel.getWidget(index)).setValue(true, true);
    }
}
