package com.inapp.itext.test;

import java.io.FileOutputStream;
import java.io.IOException;
 
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
public class HelloWorldNarrow {
	 
    /** Path to the resulting PDF file. */
    public static final String RESULT
        = "hello_narrow.pdf";
 
    /**
     * Creates a PDF file: hello_narrow.pdf
     * @param    args    no arguments needed
     */
    public static void main(String[] args)
         throws DocumentException, IOException {
        // step 1
    	// Using a custom page size
        Rectangle pagesize = new Rectangle(216f, 720f);
        Document document = new Document(pagesize, 36f, 72f, 108f, 180f);
        // step 2
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph(
            "Hello World! Hello People! " +
            "Hello Sky! Hello Sun! Hello Moon! Hello Stars!"));
        // step 5
        document.close();
    }
}