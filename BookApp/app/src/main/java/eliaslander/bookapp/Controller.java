package eliaslander.bookapp;

import android.support.annotation.NonNull;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.ContentHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
            boolean bAuthor = false;
            String title, author;


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




                if (s2.equalsIgnoreCase("TITLE")) {
                    bTitle = true;
                }
                if (s2.equalsIgnoreCase("NAME")) {
                    bAuthor = true;
                }
            }

            @Override
            public void endElement(String s, String s1, String s2) throws SAXException {
                if (s2.equalsIgnoreCase("WORK")) {
                    Book book = new Book(title,author);
                    books.add(book);
                }
            }

            @Override
            public void characters(char[] chars, int start, int length) throws SAXException {

                if(bTitle) {
                    title = new String(chars, start,length);
                    bTitle = false;
                }
                if(bAuthor) {
                    title = new String(chars,start,length);
                    bAuthor = false;
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

        XMLReader myReader = XMLReaderFactory.createXMLReader();
        myReader.setContentHandler(handler);
        myReader.parse(new InputSource(url.openStream()));
        return books;
    }


}
