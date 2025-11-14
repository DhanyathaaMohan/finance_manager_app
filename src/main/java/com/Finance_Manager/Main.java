package com.finance_manager;   
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.finance_manager.util.DatabaseConnection;
import com.finance_manager.gui.MainTransactionGUI;
import com.finance_manager.model.Category;
import com.finance_manager.model.Transaction;

public class Main {
    public static void main(String[] args) {
        try{
            DatabaseConnection db_Connection = new DatabaseConnection();
            Connection sn=db_Connection.getConnection();
            System.out.println("The database connection successful!!!");
        }
        catch(SQLException e)
        {
            System.out.println("The database connection failed!!");
            System.exit(1);
        }
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            System.err.println("could not set look and feel"+e.getMessage());
        }
        SwingUtilities.invokeLater(
            ()->{
                try{
                new MainTransactionGUI().setVisible(true);
                }
                catch(Exception e)
                {
                    System.err.println("error starting the application"+e.getLocalizedMessage());
                }
            }
        );
    }
}
