/**
 * 
 */
package com.inapp.itext;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * @author ArjunM
 *
 */
public class HeaderFooter extends PdfPageEventHelper {

	 protected ElementList header;
     protected ElementList footer;
     String HEADER;
     String FOOTER;
     public HeaderFooter(String HEADER,String FOOTER ) throws IOException {
         header = XMLWorkerHelper.parseToElementList(HEADER, null);
         footer = XMLWorkerHelper.parseToElementList(FOOTER, null);
     }
     @Override
     public void onEndPage(PdfWriter writer, Document document) {
         try {
             ColumnText ct = new ColumnText(writer.getDirectContent());
             ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
             for (Element e : header) {
                 ct.addElement(e);
             }
             ct.go();
             ct.setSimpleColumn(new Rectangle(36, 10, 559, 32));
             for (Element e : footer) {
                 ct.addElement(e);
             }
             ct.go();
         } catch (DocumentException de) {
             throw new ExceptionConverter(de);
         }
     }
   
   

}
