package com.example.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Locale;

public class Rank {

    public static ArrayList<ArrayList<String>> showAll(int level, int model) {

        Connection connection = null;
        PreparedStatement statement;
        ArrayList<ArrayList<String>> list = new ArrayList<>();

        String table = "";
        switch (model) {
            case 0:
                table = "score";
                break;
            case 1:
                table = "anti_gravity_score";
                break;
            case 2:
                table = "hidden_hole_score";
                break;
            case 3:
                table = "location_change_score";
                break;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                    "root", "132465798Mm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "select username,time from "+ table + " where level=? and inWorld=? order by time";


            if (connection != null) {

                statement = connection.prepareStatement(sql);

                connection.setAutoCommit(false);

                statement.setInt(1, level);
                statement.setString(2, "yes");

                ResultSet resultSet = statement.executeQuery();


                while (resultSet.next()) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(resultSet.getString("username"));
                    arrayList.add(String.format(Locale.ENGLISH ,"%02d : %02d", resultSet.getInt("time") / 60, resultSet.getInt("time") % 60 ));
                    list.add(arrayList);
                }

                connection.commit();
                resultSet.close();
                statement.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;

    }

    public static ArrayList<ArrayList<String>> showPersonal(int level, String username, int model) {

        Connection connection = null;
        PreparedStatement statement;
        ArrayList<ArrayList<String>> list = new ArrayList<>();

        String table = "";
        switch (model) {
            case 0:
                table = "score";
                break;
            case 1:
                table = "anti_gravity_score";
                break;
            case 2:
                table = "hidden_hole_score";
                break;
            case 3:
                table = "location_change_score";
                break;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                    "root", "132465798Mm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "select username,time from " + table + " where level=? and username=? order by time";

            if (connection != null) {

                statement = connection.prepareStatement(sql);

                connection.setAutoCommit(false);

                statement.setInt(1, level);
                statement.setString(2, username);

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(resultSet.getString("username"));
                    arrayList.add(String.format(Locale.ENGLISH ,"%02d : %02d", resultSet.getInt("time") / 60, resultSet.getInt("time") % 60 ));
                    list.add(arrayList);
                }

                connection.commit();
                resultSet.close();
                statement.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public static boolean[] addToRank(int level, int time, String username, int model) {

        Connection connection = null;
        PreparedStatement statement;
        PreparedStatement worldStatement;
        int number = 0;
        int worldNumber = 0;

        boolean toAdd = false;
        boolean worldRecord = false;

        boolean directAdd = false;
        boolean directAddWorld = false;

        String table = "";
        switch (model) {
            case 0:
                table = "score";
                break;
            case 1:
                table = "anti_gravity_score";
                break;
            case 2:
                table = "hidden_hole_score";
                break;
            case 3:
                table = "location_change_score";
                break;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                    "root", "132465798Mm");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String sql = "select username,time from " + table + " where level=? and username=? order by time";
            String worldSql = "select time,id from " + table + " where level=? and inWorld=? order by time";
            String insertSql = "insert into " + table + " (username,level,time,inWorld) values (?,?,?,?)";

            if (connection != null) {

                statement = connection.prepareStatement(sql);
                worldStatement = connection.prepareStatement(worldSql);

                connection.setAutoCommit(false);

                statement.setInt(1, level);
                statement.setString(2, username);

                worldStatement.setInt(1, level);
                worldStatement.setString(2, "yes");

                ResultSet resultSet = statement.executeQuery();
                ResultSet worldSet = worldStatement.executeQuery();

                while (resultSet.next()) {
                    number += 1;
                    if ( time < resultSet.getInt("time") ) {
                        toAdd = true;
                    }
                }
                connection.commit();

                while (worldSet.next()) {
                    worldNumber += 1;
                    if ( time < worldSet.getInt("time") ) {
                        worldRecord = true;
                    }
                }


                if (number < 10) {
//                    PreparedStatement statement1 = connection.prepareStatement(insertSql);
//
//                    statement1.setString(1, username);
//                    statement1.setInt(2, level);
//                    statement1.setInt(3, time);
//
//                    if (worldNumber < 10) {
//                        statement1.setString(4, "yes");
//                    }
//                    else {
//                        statement1.setString(4, "no");
//                    }
//
//                    statement1.addBatch();
//                    statement1.executeBatch();
//
//                    connection.commit();
//                    resultSet.close();
//                    statement.close();
//
//                    return true;
                    toAdd = true;
                    directAdd = true;
                    if (worldNumber < 10) {
                        worldRecord = true;
                        directAddWorld = true;
                    }

                }

                if (toAdd) {

                    if (!directAdd) {
                        resultSet.last();

                        String deleteSql = "DELETE FROM " + table + " WHERE id=?";

                        PreparedStatement statement3 = connection.prepareStatement(deleteSql);
                        statement3.setInt(1, resultSet.getInt("id"));

                        statement3.addBatch();
                        statement3.executeBatch();
                    }



                    PreparedStatement statement2 = connection.prepareStatement(insertSql);

                    statement2.setString(1, username);
                    statement2.setInt(2, level);
                    statement2.setInt(3, time);


                    if (worldRecord) {
                        if (!directAddWorld) {

                            worldSet.last();
                            String updateSql = "UPDATE " + table + " SET inWorld=? WHERE id=?";

                            PreparedStatement statement1 = connection.prepareStatement(updateSql);
                            statement1.setString(1, "no");
                            statement1.setInt(2, worldSet.getInt("id"));
                            statement1.addBatch();
                            statement1.executeBatch();
                        }

                        statement2.setString(4, "yes");

                    }
                    else {
                        statement2.setString(4, "no");
                    }

                    statement2.addBatch();
                    statement2.executeBatch();
                }

                connection.commit();
                worldSet.close();
                resultSet.close();
                statement.close();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new boolean[]{worldRecord, toAdd};

    }

}
