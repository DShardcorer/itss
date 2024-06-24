package entity.media;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import entity.db.AIMSDB;

//Violation:
// Single Responsibility Principle (SRP): This class handles both book attributes and database access logic.
// Reason:violation is intentional for convenience, combining data representation and data access within the same class to simplify code structure.
public class Book extends Media {

    // Book-specific attributes
    String author;
    String coverType;
    String publisher;
    Date publishDate;
    int numOfPages;
    String language;
    String bookCategory;

    public Book() throws SQLException {
        // Empty constructor for creating a Book object without initialization
    }

    public Book(int id, String title, String category, int price, int quantity, String type, String author,
                String coverType, String publisher, Date publishDate, int numOfPages, String language,
                String bookCategory) throws SQLException {
        super(id, title, category, price, quantity, type);
        this.author = author;
        this.coverType = coverType;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.numOfPages = numOfPages;
        this.language = language;
        this.bookCategory = bookCategory;
    }

    // Getters and Setters
    public int getId() {
        return this.id;
    }

    public String getAuthor() {
        return this.author;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getCoverType() {
        return this.coverType;
    }

    public Book setCoverType(String coverType) {
        this.coverType = coverType;
        return this;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public Book setPublisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public Date getPublishDate() {
        return this.publishDate;
    }

    public Book setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
        return this;
    }

    public int getNumOfPages() {
        return this.numOfPages;
    }

    public Book setNumOfPages(int numOfPages) {
        this.numOfPages = numOfPages;
        return this;
    }

    public String getLanguage() {
        return this.language;
    }

    public Book setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getBookCategory() {
        return this.bookCategory;
    }

    public Book setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
        return this;
    }

    @Override
    public Media getMediaById(int id) throws SQLException {
        // Constructs SQL query to join Book and Media tables
        String sql = "SELECT * FROM " +
                "aims.Book " +
                "INNER JOIN aims.Media " +
                "ON Media.id = Book.id " +
                "where Media.id = " + id + ";";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
        if (res.next()) {

            // Retrieves attributes from Media table
            String title = "";
            String type = res.getString("type");
            int price = res.getInt("price");
            String category = res.getString("category");
            int quantity = res.getInt("quantity");

            // Retrieves attributes from Book table
            String author = res.getString("author");
            String coverType = res.getString("coverType");
            String publisher = res.getString("publisher");
            Date publishDate = res.getDate("publishDate");
            int numOfPages = res.getInt("numOfPages");
            String language = res.getString("language");
            String bookCategory = res.getString("bookCategory");

            // Constructs and returns a new Book object
            return new Book(id, title, category, price, quantity, type,
                    author, coverType, publisher, publishDate, numOfPages, language, bookCategory);

        } else {
            throw new SQLException();
        }
    }

    @Override
    public List getAllMedia() {
        // Intentionally left unimplemented for simplicity
        return null;
    }

    @Override
    public String toString() {
        // Constructs a string representation of the Book object
        return "{" +
                super.toString() +
                " author='" + author + "'" +
                ", coverType='" + coverType + "'" +
                ", publisher='" + publisher + "'" +
                ", publishDate='" + publishDate + "'" +
                ", numOfPages='" + numOfPages + "'" +
                ", language='" + language + "'" +
                ", bookCategory='" + bookCategory + "'" +
                "}";
    }
}
