/**
 * 
 */
package de.saumya.webfortune.client;

public class Topic {

  public final String name;
  public final String linkText;
  public final String url;
  public final String urlTarget;
  public final String displayName;
  public final int max;

  Topic(final String name, final String displayName, final int max,
      final String linkText, final String url, final String urlTarget) {
    this.name = name;
    this.url = url;
    this.linkText = linkText;
    this.displayName = displayName;
    this.max = max;
    this.urlTarget = urlTarget;
  }

  Topic(final String name, final String displayName, final int max) {
    this.name = name;
    this.url = null;
    this.linkText = null;
    this.displayName = displayName;
    this.max = max;
    this.urlTarget = null;
  }

}