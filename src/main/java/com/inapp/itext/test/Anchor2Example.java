package com.inapp.itext.test;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**

 */
public class Anchor2Example {

  public static void main(String[] args) {

    Document document = new Document();

    try {
        PdfWriter.getInstance(document,
                new FileOutputStream("Anchor2.pdf"));

        document.open();

        
            Anchor anchor =
            new Anchor("Jump down to next paragraph");
            anchor.setReference("#linkTarget");
            Paragraph paragraph = new Paragraph();
            paragraph.add(anchor);
            document.add(paragraph);

            Anchor anchorTarget =
            new Anchor("This is the target of the link above");
            anchor.setName("linkTarget");
            Paragraph targetParagraph = new Paragraph();
            targetParagraph.setSpacingBefore(50);

            targetParagraph.add(anchorTarget);
            document.add(targetParagraph);
        

        document.close();
    } catch (DocumentException e) {
        e.printStackTrace();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

  }
}