/**
 * ULandExchange.java Created May 30, 2011 by rickcharon.
 *
 */
package ptscontractinfos;

/**
 * Simple class to contain a list of underlying symbols and their corresponding exchange.
 * @author rickcharon
 */
public class ULandExchange {
  /**
   * The Underlying symbol.
   */
  private String ul;
  /**
   * The uderlying's exchange.
   */
  private String exchange;

  /**
   * 
   * @return exchange.
   */
  public String getExchange() {
    return exchange;
  }

  /**
   * 
   * @param exchange
   */
  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  /**
   * 
   * @return underlying
   */
  public String getUl() {
    return ul;
  }

  /**
   * 
   * @param ul - sets underlying
   */
  public void setUl(String ul) {
    this.ul = ul;
  }
  
  
  
}
