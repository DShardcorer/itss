package entity.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.db.AIMSDB;


public class Media {
    // SRP Violation: This class handles both media properties and database operations.
    // OCP Violation: Due to the fact that the database schema is unlikely to change
    protected Statement stm;
    protected int id;
    protected String title;
    protected String category;
    protected int value; // the real price of product (eg: 450)
    protected int price; // the price which will be displayed on browser (eg: 500)
    protected int quantity;
    protected String type;
    protected String imageURL;

    // Random boolean to determine if the media supports rush order
    // SRP Violation: Business logic should not be mixed with data models.
    protected boolean isSupportedPlaceRushOrder = new Random().nextBoolean();

    // Constructor initializing the statement object.
    // SRP Violation: This constructor couples the media class to the database.
    public Media() throws SQLException{
        stm = AIMSDB.getConnection().createStatement();
    }

    // Constructor initializing media attributes
    public Media (int id, String title, String category, int price, int quantity, String type) throws SQLException{
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    // Constructor with additional imageURL parameter
    public Media(int id, String title, String category, int value, int price, int quantity, String type, String imageURL) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.imageURL = imageURL;
    }

    // Method to get the quantity of media
    // SRP Violation: Combines data retrieval with business logic.
    public int getQuantity() throws SQLException{
        int updated_quantity = getMediaById(id).quantity;
        this.quantity = updated_quantity;
        return updated_quantity;
    }

    // Method to get media by ID from the database
    public Media getMediaById(int id) throws SQLException{
        String sql = "SELECT * FROM Media";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        if(res.next()) {
            // Data coupling: the method depends on data from the ResultSet
            return new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setMediaURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
        }
        return null;
    }

    // Method to get all media from the database
    // Functional cohesion: It retrieves all media records.
    public List<Media> getAllMedia() throws SQLException{
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from Media");
        return _extractMediaFromResultSet(res);
    }

    // Method to get media by type from the database
    public List<Media> getMediaByType(String type) throws SQLException {
        String sql = "select * from Media where type = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, type);
        ResultSet res = stm.executeQuery();

        // Sequential cohesion: Processes a sequence of steps to achieve a result.
        ArrayList<Media> items = _extractMediaFromResultSet(res);
        for (Media media : items) {
            System.out.println(media.quantity);
        }
        return items;
    }

    // Method to update a field of a media record by ID
    // Stamp coupling: It relies on the table name and field name being correct.
    public void updateMediaFieldById(String tableName, int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        if (value instanceof String){
            value = "\"" + value + "\"";
        }
        stm.executeUpdate(" update " + tableName + " set " + field + "=" + value + " where id=" + id);
    }

    // Method to add a new media record to the database
    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        String sql = "insert into Media (title, type, category, imageUrl, price, quantity) values (?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, type);
        stm.setString(3, category);
        stm.setString(4, imgUrl);
        stm.setDouble(5, price);
        stm.setInt(6, quantity);
        stm.executeUpdate();
    }

    // Method to delete a media record by ID
    public void deleteMediaById(int id) throws SQLException {
        String sql = "delete from Media where id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);
        stm.executeUpdate();
    }

    // Private method to extract media objects from a ResultSet
    // Sequential cohesion: Processes the ResultSet to construct media objects.
    private ArrayList<Media> _extractMediaFromResultSet(ResultSet res) throws SQLException {
        ArrayList<Media> items = new ArrayList<>();
        while (res.next()) {
            Media media = new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setMediaURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
            items.add(media);
        }
        return items;
    }

    // Getters and setters for media attributes
    public int getId() {
        return this.id;
    }

    public Media setId(int id){
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Media setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getPrice() {
        return this.price;
    }

    public Media setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public Media setMediaURL(String url){
        this.imageURL = url;
        return this;
    }

    public Media setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Media setType(String type) {
        this.type = type;
        return this;
    }

    public boolean getIsSupportedPlaceRushOrder() {
        return this.isSupportedPlaceRushOrder;
    }

    public void setIsSupportedPlaceRushOrder(boolean b) { this.isSupportedPlaceRushOrder = b; }

    @Override
    public String toString() {
        return "{" +
                " id='" + id + "'" +
                ", title='" + title + "'" +
                ", category='" + category + "'" +
                ", price='" + price + "'" +
                ", quantity='" + quantity + "'" +
                ", type='" + type + "'" +
                ", imageURL='" + imageURL + "'" +
                "}";
    }

    // Method to update media details
    // Stamp coupling: Requires multiple attributes to be passed in.
    public void updateMediaDetails(int id, String title, String category, int price, int quantity) throws SQLException {
        String sql = "UPDATE Media SET title = ?, category = ?, price = ?, quantity = ? WHERE id = ?";
        try (PreparedStatement stmt = AIMSDB.getConnection().prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, category);
            stmt.setInt(3, price);
            stmt.setInt(4, quantity);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Error updating media details", e);
        }
    }
}
