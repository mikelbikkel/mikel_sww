package org.subwayworld.routeservice;

public class CityInfo {

  private String city_id, url, category, description, indexno;
  private boolean isValidUrl;

  public String getIndexno() {
    return indexno;
  }

  public void setIndexno(String indexno) {
    this.indexno = indexno;
  }

  public String getCity_id() {
    return city_id;
  }

  public void setCity_id(String city_id) {
    this.city_id = city_id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * @param city_id
   * @param url
   * @param category
   * @param description
   * @param isValidUrl
   */
  public CityInfo(String city_id, String url, String category,
      String description, boolean isValidUrl) {
    this.city_id = city_id;
    this.url = url;
    this.category = category;
    this.description = description;
    this.isValidUrl = isValidUrl;
  }

  public void setValidUrl(boolean isValidUrl) {
    this.isValidUrl = isValidUrl;
  }

  public boolean isValidUrl() {
    return !url.isEmpty() && isValidUrl;
  }

}
