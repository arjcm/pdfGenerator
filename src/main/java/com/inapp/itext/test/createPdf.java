package com.inapp.itext.test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.FileOutputStream;
import java.security.acl.Owner;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import com.itextpdf.text.pdf.PdfPTable;

public class createPdf {

	/** Path to the resulting PDF file. */
	public static final String RESULT = "splitHeader.pdf";

	/**
	 * Creates a PDF file: hello.pdf
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args)
			throws DocumentException, IOException {
		new createPdf().makePdf(RESULT);
	}

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void makePdf(String filename)
			throws DocumentException, IOException {
		// step 1
		Document document = new Document();
		// step 2
	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
	
	String html = "<table><tr>kkljjkjjkjk</tr><tr></tr><td></td><td></td></table>gdfjgdjgjdfng";
	org.jsoup.nodes.Document doc = Jsoup.parseBodyFragment(html);
	document.open();
	
	
	   Elements elements = doc.select("*");
    
    //iterate elements using enhanced for loo
    
    
   /*doc.traverse(new NodeVisitor() {
	   int tableRowCount = 0;
	   int tableColumnCount = 0;
		@Override
		public void head(Node node, int arg1) {
			// TODO Auto-generated method stub
			 Element element = (Element) node;
			 if(element.tagName().equals("tr")){
				 tableRowCount ++;
				 
			 }
			 System.out.println("row Count = "+ tableRowCount);
		}
		@Override
		public void tail(Node node, int arg1) {
			// TODO Auto-generated method stub
			 System.out.println("Exiting tag: " + node.nodeName());
		}
    });*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*Elements element = doc.select("tr");
	   System.out.println("Number of Rows are : "+element.size()); */
   /* for (Element table : doc.select("table")) {
    	int rowCount =0;
    	int colCount=0;
    	String rowValue = null;
    	
        for (Element row : table.select("tr")) {
        	rowValue = row.text();
        	rowCount = getItemCount(table.select("tr"));
        	//System.out.println(row);
            for (Element tds : row.select("td")) {
            	colCount = getItemCount( row.select("td"));	
            }
        }
        
        System.out.println("rowCount="+rowCount +"colCount="+colCount);
       
        pTable.addCell(rowValue);
        document.add(pTable);
        
    }*/
    
	
	
	
	
		// step 3
		
		PdfPTable table = new PdfPTable(2);
		// header row:
		table.addCell("Header");
		table.addCell("Header Value");
		table.setHeaderRows(1);
		// many data rows:
		for (int i = 1; i < 51; i++) {
		    table.addCell("key: " + i);
		    table.addCell("value: " + i);
		}
		
		//XMLWorkerHelper.getInstance().parseXHtml(writer, document);
		// step 4
		//document.add(new Paragraph("Hello World!"));
		// step 5
		document.add(table);
		document.close();
		
		
		
		
	}
	
	public int getItemCount(Elements elements){
		int count = 0;
		for (Element n : elements) {
		    count++;
		}
		return count;
	}
}