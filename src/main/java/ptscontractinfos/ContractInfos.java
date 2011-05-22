/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ptscontractinfos;

//import com.ib.client.Contract;

import com.ib.client.Contract;
import com.ib.client.ContractDetails;
import java.sql.*;
import javax.swing.JOptionPane;
//import org.jfree.data.time.RegularTimePeriod;
//import org.jfree.date.MonthConstants;
import ptsutils.PtsDBops;
import ptsutils.PtsIBConnectionManager;
import ptsutils.PtsIBWrapperAdapter;
import ptsutils.PtsMySocket;

//import com.ib.client.ContractDetails;
//import com.ib.client.EClientSocket;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.swing.JOptionPane;
//import petrasys.ContractInfoDialog;
//import petrasys.utils.DBops;
//import petrasys.utils.IBWrapperAdapter;
//import petrasys.utils.MsgBox;

/**
 *
 * @author rickcharon
 */
public class ContractInfos extends PtsIBWrapperAdapter implements Runnable {

  private PtsMySocket socket;
  private ContractInfoDialog contractInfoDlg;
  private Statement pgStmtForContractDetails;
  private Connection pgConnectionForContractDetails;
  private Contract contract;
  int orderID;
  int contractCount;

  public ContractInfos() {
    contractCount = 0;
  }

  public void setParams(ContractInfoDialog cidIn, Contract contractIn) {
    contractInfoDlg = cidIn;
    contract = contractIn;
    orderID = PtsIBConnectionManager.getTickerId();
  }

  public void requestContractDetails() {
    PtsIBConnectionManager.setPort(7496);
    socket = PtsIBConnectionManager.connect(this);
    socket.reqContractDetails(orderID, contract);
  }

  public void setContractDetailsConnection() {
    try {
      pgConnectionForContractDetails = PtsDBops.setuptradesConnection();
      pgStmtForContractDetails = pgConnectionForContractDetails.createStatement();
    } catch (SQLException ex) {
      //MsgBox.err2(ex);
      System.err.println("exception in setContractDetailsConnection: " + ex.getMessage());
    }

  }

  /**
   * @param reqId
   * @param cds
   */
  @Override
  public void contractDetails(int reqId, ContractDetails cds) {
    try {
      Contract ct = cds.m_summary;
      int priceMag = cds.m_priceMagnifier;
      int expir = Integer.parseInt(ct.m_expiry.trim());
      String insStr = "insert into futuresContractDetails "
              + "(symbol, expiry,multiplier,priceMagnifier, exchange,minTick,fullName)"
              + " values( '" + ct.m_symbol + "', " + Integer.parseInt(ct.m_expiry.trim())
              + ", " + ct.m_multiplier + ", " + priceMag + ", '" + ct.m_exchange + "', " + cds.m_minTick + ", '"
              + cds.m_longName + "')";
      pgStmtForContractDetails.executeUpdate(insStr);
      contractCount++;
    } catch (SQLException ex) {
      System.err.println("exception in contractDetails: " + ex.getMessage());
    }
  }

  /**
   *
   * @param reqId
   */
  @Override
  public void contractDetailsEnd(int reqId) {
    contractInfoDlg.setVisible(false);
    JOptionPane.showMessageDialog(null, contractCount + " Contracts Processed.");
    contractInfoDlg.dispose();
    socket.disConnect();
    try {
      pgStmtForContractDetails.close();
      pgConnectionForContractDetails.close();
    } catch (SQLException ex) {
            System.err.println("exception in contractDetailsEnd: " + ex.getMessage());

    } finally {
      System.exit(1);
    }
  }

  public void run() {
    try {
      setContractDetailsConnection();
      requestContractDetails();
    } catch (Exception ex) {
           System.err.println("exception in ContractInfos.run(): " + ex.getMessage());
    }
  }

  public static void main(String[] args) {
    //RegularTimePeriod rtp;
    //MonthConstants mt;
    System.out.println("");
    PtsDBops.setuptradesConnection();
    int j = 3;
  }
}


