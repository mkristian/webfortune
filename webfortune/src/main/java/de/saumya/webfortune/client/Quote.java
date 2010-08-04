package de.saumya.webfortune.client;

public class Quote {

  public final Topic topic;

  private int id;

  public Quote(final Topic topic) {
    this.topic = topic;
  }

  public String file() {
    return this.topic.name + "/" + this.id + ".xml";
  }

  public void next() {
    this.id++;
    if (this.id == this.topic.max) {
      this.id = 0;
    }
  }

  public void previous() {
    this.id--;
    if (this.id == -1) {
      this.id = this.topic.max - 1;
    }
  }

  public void random() {
    do {
      this.id = (int) (Math.random() * this.topic.max);
    } while (this.topic.max == this.id);
  }

}
