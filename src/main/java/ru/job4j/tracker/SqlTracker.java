package ru.job4j.tracker;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store {

    private Connection cn;

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    private void init() {
        try (InputStream in = SqlTracker.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws SQLException {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
            try (PreparedStatement ps = cn.prepareStatement("INSERT INTO items (name, created) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, item.getName());
                ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
                ps.execute();
                try (ResultSet genKeys = ps.getGeneratedKeys()) {
                    if (genKeys.next()) {
                        item.setId(genKeys.getInt(1));
                    } else {
                        throw new SQLException("Cant generate ID");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return item;
        }

        @Override
        public boolean replace(int id, Item item) {
            boolean rsl = false;
                try (PreparedStatement ps = cn.prepareStatement("UPDATE items SET name = ?, created = ? WHERE id = ?")) {
                    ps.setString(1, item.getName());
                    ps.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
                    ps.setInt(3, id);
                    int check = ps.executeUpdate();
                    if (check > 0) {
                        rsl = true;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return rsl;
            }

            @Override
            public boolean delete(int id) {
                boolean rsl = false;
                    try (PreparedStatement ps = cn.prepareStatement("DELETE FROM items WHERE id = ?")) {
                        ps.setInt(1, id);
                        int check = ps.executeUpdate();
                        if (check > 0) {
                            rsl = true;
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return rsl;
                }

                @Override
                public List<Item> findAll() {
                    List<Item> items = new ArrayList<>();
                        try (PreparedStatement ps = cn.prepareStatement("SELECT id, name, TO_CHAR(created::timestamp, 'YYYY-MM-DD HH24:MI:SS') as formatted_created FROM items;")) {
                            ResultSet rs = ps.executeQuery();
                            while (rs.next()) {
                                int id = rs.getInt("id");
                                String name = rs.getString("name");
                                String createdStr = rs.getString("formatted_created");
                                LocalDateTime created = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                items.add(new Item(id, name, created));
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return items;
                    }

                    @Override
                    public List<Item> findByName(String key) {
                        List<Item> items = new ArrayList<>();
                            try (PreparedStatement ps = cn.prepareStatement("SELECT id, name, TO_CHAR(created::timestamp, 'YYYY-MM-DD HH24:MI:SS') as formatted_created FROM items WHERE name = ?")) {
                                ps.setString(1, key);
                                ResultSet rs = ps.executeQuery();
                                while (rs.next()) {
                                    int id = rs.getInt("id");
                                    String name = rs.getString("name");
                                    String createdStr = rs.getString("formatted_created");
                                    LocalDateTime created = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                    items.add(new Item(id, name, created));
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            return items;
                        }

                        @Override
                        public Item findById(int id) {
                            Item item = null;
                                try (PreparedStatement ps = cn.prepareStatement("SELECT id, name, TO_CHAR(created::timestamp, 'YYYY-MM-DD HH24:MI:SS') as formatted_created FROM items WHERE id = ?")) {
                                    ps.setInt(1, id);
                                    ResultSet rs = ps.executeQuery();
                                    if (rs.next()) {
                                        int itemId = rs.getInt("id");
                                        String name = rs.getString("name");
                                        String createdStr = rs.getString("formatted_created");
                                        LocalDateTime created = LocalDateTime.parse(createdStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                                        item = new Item(itemId, name, created);
                                    }
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                                return item;
                            }
                        }