package eliaslander.bookapp;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ContentHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by 11500585 on 24/10/2017.
 */

public class Controller {



    public static ArrayList<Book> SearchBooks(String query) throws SAXException, IOException {
        String adress = "https://www.goodreads.com/search/index.xml?key=rqDMOWLcyHZbWo3eeoN2g&q=";
        String charset = "UTF-8";
        ArrayList<Book> books = new ArrayList<>();
        URL url = new URL(adress+ URLEncoder.encode(query,charset));


        ContentHandler handler = new ContentHandler() {
            boolean bTitle = false;
            boolean bAuthorName = false;
            boolean bImageUrl = false;
            boolean bId = false;
            boolean bBestBook = false;
            boolean bAuthor = false;
            String title, author, imageUrl;
            int id;


            @Override
            public void setDocumentLocator(Locator locator) {

            }

            @Override
            public void startDocument() throws SAXException {

            }

            @Override
            public void endDocument() throws SAXException {

            }

            @Override
            public void startPrefixMapping(String s, String s1) throws SAXException {

            }

            @Override
            public void endPrefixMapping(String s) throws SAXException {

            }

            @Override
            public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {


                if (s2.equalsIgnoreCase("IMAGE_URL")) {
                    bImageUrl = true;
                }

                if (s2.equalsIgnoreCase("TITLE")) {
                    bTitle = true;
                }
                if (s2.equalsIgnoreCase("NAME")) {
                    bAuthorName = true;
                }
                if (s2.equalsIgnoreCase("ID")&& bBestBook && !bAuthor) {
                    bId = true;
                }
                if (s2.equalsIgnoreCase("BEST_BOOK")) {
                    bBestBook = true;
                }
                if (s2.equalsIgnoreCase("AUTHOR")) {
                    bAuthor = true;
                }
            }

            @Override
            public void endElement(String s, String s1, String s2) throws SAXException {
                if (s2.equalsIgnoreCase("WORK")) {
                    Book book = new Book(id,title,author);
                    book.setImageUrl(imageUrl);
                    books.add(book);
                }

                if (s2.equalsIgnoreCase("BEST_BOOK")) {
                    bBestBook = false;
                }
                if (s2.equalsIgnoreCase("AUTHOR")) {
                    bAuthor = false;
                }


            }

            @Override
            public void characters(char[] chars, int start, int length) throws SAXException {

                if(bTitle) {
                    title = new String(chars, start,length);
                    bTitle = false;
                }
                if(bAuthorName) {
                    author = new String(chars,start,length);
                    bAuthorName = false;
                }
                if(bImageUrl) {
                    imageUrl = new String(chars,start,length);
                    bImageUrl = false;
                }
                if(bId) {
                    id = Integer.parseInt(new String(chars,start,length));
                    bId = false;
                }



            }

            @Override
            public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {

            }

            @Override
            public void processingInstruction(String s, String s1) throws SAXException {

            }

            @Override
            public void skippedEntity(String s) throws SAXException {

            }
        };

        try {
            //System.setProperty( "org.xml.sax.driver", "javax.xml.parsers.SAXParser" );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser newSAXParser = saxParserFactory.newSAXParser();
            XMLReader myReader = newSAXParser.getXMLReader();
            //XMLReader myReader = XMLReaderFactory.createXMLReader();
            myReader.setContentHandler(handler);
            myReader.parse(new InputSource(url.openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    public static Book GetBookById(int id) throws MalformedURLException {
        String adress = "https://www.goodreads.com/book/show/"+id+".xml?key=rqDMOWLcyHZbWo3eeoN2g";
        Book book = new Book();
        URL url = new URL(adress);


        ContentHandler handler = new ContentHandler() {
            boolean bWorkTitle = false;
            boolean bWorkAuthorName = false;
            boolean bImageUrl = false;
            boolean bDescription = false;
            boolean bAuthor = false;
            boolean bWorkAuthor = true;
            boolean bRating = false;
            boolean bSeries = false;
            boolean bSimilarBooks = false;
            boolean bSimilarBook = false;

            boolean bSimilarTitle = false;
            boolean bSimilarAuthor = false;
            boolean bSimilarId = false;
            String title, author, imageUrl, description, similarTitle, similarAuthor;
            float rating;
            int similarId;
            List<Book> similarBooks = new ArrayList();

            @Override
            public void setDocumentLocator(Locator locator) {

            }

            @Override
            public void startDocument() throws SAXException {

            }

            @Override
            public void endDocument() throws SAXException {

            }

            @Override
            public void startPrefixMapping(String s, String s1) throws SAXException {

            }

            @Override
            public void endPrefixMapping(String s) throws SAXException {

            }

            @Override
            public void startElement(String s, String s1, String s2, Attributes attributes) throws SAXException {


                if (s2.equalsIgnoreCase("IMAGE_URL")&& !bSeries && !bSimilarBooks && !bAuthor) {
                    bImageUrl = true;
                }

                if (s2.equalsIgnoreCase("TITLE") &&!bSeries && !bSimilarBooks) {
                    bWorkTitle = true;
                }
                if (s2.equalsIgnoreCase("NAME")&& bAuthor &&bWorkAuthor && !bSeries && !bSimilarBooks) {
                    bWorkAuthorName = true;
                }
                if (s2.equalsIgnoreCase("DESCRIPTION")&& !bSeries) {
                    bDescription = true;
                }
                if (s2.equalsIgnoreCase("AUTHOR")) {
                    bAuthor = true;
                }
                if (s2.equalsIgnoreCase("AVERAGE_RATING") && !bSeries && !bSimilarBooks) {
                    bRating = true;
                }
                if (s2.equalsIgnoreCase("SERIES_WORKS")) {
                    bSeries = true;
                }
                if (s2.equalsIgnoreCase("SIMILAR-BOOKS")) {
                    bSimilarBooks = true;
                }


                if (s2.equalsIgnoreCase("BOOK")&& bSimilarBooks) {
                    bSimilarBook = true;
                }
                if (s2.equalsIgnoreCase("TITLE") && bSimilarBooks) {
                    bSimilarTitle = true;
                }
                if (s2.equalsIgnoreCase("NAME")&& bSimilarBooks) {
                    bSimilarAuthor = true;
                }



            }

            @Override
            public void endElement(String s, String s1, String s2) throws SAXException {
                if (s2.equalsIgnoreCase("SERIES_BOOKS")) {
                    bSeries = false;
                }

                if (s2.equalsIgnoreCase("AUTHOR")) {
                    bAuthor = false;
                    bWorkAuthor = false;
                }

                if (s2.equalsIgnoreCase("SIMILAR_BOOKS")) {
                    bSimilarBooks = false;
                }

                if (s2.equalsIgnoreCase("BOOK")&& bSimilarBooks) {
                    similarBooks.add(new Book(similarId,similarTitle,similarAuthor));
                    bSimilarBook = false;
                }

                if (s2.equalsIgnoreCase("BOOK")&& !bSimilarBooks) {
                    book.setId(id);
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setImageUrl(imageUrl);
                    book.setDescription(description);
                    book.setRating(rating);
                    book.setSimilarBooks(similarBooks);

                }


            }

            @Override
            public void characters(char[] chars, int start, int length) throws SAXException {

                if(bWorkTitle) {
                    title = new String(chars, start,length);
                    bWorkTitle = false;
                }
                if(bWorkAuthorName) {
                    author = new String(chars,start,length);
                    bWorkAuthorName = false;
                }
                if(bImageUrl) {
                    imageUrl = new String(chars,start,length);
                    bImageUrl = false;
                }
                if(bDescription){
                    description = new String(chars,start,length);
                    bDescription = false;
                }
                if(bRating){
                    rating = Float.parseFloat(new String(chars,start,length));
                    bRating = false;
                }

                if(bSimilarTitle) {
                    similarTitle = new String(chars, start,length);
                    bSimilarTitle = false;
                }
                if(bSimilarAuthor) {
                    similarAuthor = new String(chars, start,length);
                    bSimilarAuthor = false;
                }
                if(bSimilarId) {
                    similarId = Integer.parseInt(new String(chars, start,length));
                    bSimilarId = false;
                }








            }

            @Override
            public void ignorableWhitespace(char[] chars, int i, int i1) throws SAXException {

            }

            @Override
            public void processingInstruction(String s, String s1) throws SAXException {

            }

            @Override
            public void skippedEntity(String s) throws SAXException {

            }
        };

        try {
            //System.setProperty( "org.xml.sax.driver", "javax.xml.parsers.SAXParser" );
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser newSAXParser = saxParserFactory.newSAXParser();
            XMLReader myReader = newSAXParser.getXMLReader();
            //XMLReader myReader = XMLReaderFactory.createXMLReader();
            myReader.setContentHandler(handler);
            myReader.parse(new InputSource(url.openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }


}
