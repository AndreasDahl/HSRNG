package io;

import util.Card;
import util.Rarity;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Andreas on 08-01-14.
 */
public class CardDatabase {
    private static final String DB_PATH = "res/db.s3db";
    private static CardDatabase instance = null;

    public static CardDatabase getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null)
            instance = new CardDatabase();
        return instance;
    }

    private CardDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
    }

    private Statement openStatement() throws SQLException {
        Connection c;
        c = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        c.setAutoCommit(false);
        return c.createStatement();

    }

    private void closeStatement(Statement s) throws SQLException {
        Connection c = s.getConnection();
        c.commit();
        s.close();
        c.close();
    }

    public ArrayList<Card> getCardsBy(String[] fields, String[][] criteria) throws SQLException{
        if (fields.length != criteria.length)
            throw new IllegalArgumentException("fields and criteria must match in length");
        String query = "SELECT * FROM card WHERE";
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) query += " AND";
            if (criteria[i].length > 1)
                query += " (";
            for (int j = 0; j < criteria[i].length; j++) {
                if (j > 0)
                    query += " OR";
                query += " " + fields[i] + "='" + criteria[i][j] + "'";
            }
            if (criteria[i].length > 1)
                query += ")";
        }
        ArrayList<Card> returnCards = new ArrayList<Card>();
        Statement s = openStatement();
        ResultSet rs = s.executeQuery(query);
        while (rs.next()) {
            Card card = new Card();
            card.name        = rs.getString(1);
            card.heroClass   = rs.getString(2);
            card.rarity      = Rarity.fromString(rs.getString(3));
            card.type        = rs.getString(4);
            card.race        = rs.getString(5);
            card.cost        = rs.getInt(6);
            card.atk         = rs.getInt(7);
            card.health      = rs.getInt(8);
            card.description = rs.getString(9);
            returnCards.add(card);
        }
        rs.close();
        closeStatement(s);
        return returnCards;
    }

    public ArrayList<Card> getCardsWithRarity(String rarity) throws SQLException {
        String[] fields = {"rarity"};
        String[][] criteria = {{rarity}};
        return getCardsBy(fields, criteria);
    }

}

