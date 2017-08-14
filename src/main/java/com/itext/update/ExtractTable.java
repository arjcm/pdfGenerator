package com.itext.update;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
























import org.jsoup.select.NodeVisitor;

import com.inapp.itext.PdfManager;
import com.inapp.itext.test.ParseHtmlTable2;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.Pfm2afm;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class ExtractTable {
	final static Logger logger = Logger.getLogger(ExtractTable.class);
	public ExtractTable() throws DocumentException, IOException {
				File input = new File("C:/Users/ArjunM/workspace/pdfGenerator/document/test.html");
		Document doc = null;
		String html = "<table><tr><td>first</td><td>second</td><td>third</td></tr><tr><td>ajdj</td></tr><td></td><td></td></table>gdfjgdjgjdfng";
		doc = Jsoup.parse(html, "UTF-8");
		
		/*File file = new File("C:/Users/ArjunM/workspace/pdfGenerator/document/test.html");
		String doc = FileUtils.readFileToString(file);*/
		
		com.itextpdf.text.Document document =new com.itextpdf.text.Document();
	        try {
				PdfWriter.getInstance(document, new FileOutputStream("output3.pdf"));
			} catch (FileNotFoundException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 
	        document.open();
	        
	        
	        for (Element table : doc.select("table")) {
	        	PdfPTable  ptable =  new PdfPTable(table.select("tr").size());
	        	 System.out.println(table.select("tr").size());
	            for (Element row : table.select("tr")) {
	               Elements tds = row.select("td");
	               ptable.addCell(tds.text());
	              
	               
	            }
	            
	            document.add(ptable);
	       }
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        
	        /*  Elements elements = doc.select("*");
	        
	        //iterate elements using enhanced for loo
	        
	        
	       doc.traverse(new NodeVisitor() {
	            public void head(Node node, int depth) {
	                System.out.println("Entering tag: " + node.nodeName());
	                Element element = (Element) node;
	               
	            }
	            public void tail(Node node, int depth) {
	                System.out.println("Exiting tag: " + node.nodeName());
	            }
	        });*/
	        
	        
	        
    /*   if(doc.select("table") != null){
			    Elements tables = doc.select("table");
			    for (Element element : tables) {
			    	 logger.info("elements="+element);
			
			    
			   
			    //tables.get(0);
			  
				try {
					PdfPTable table=	new ParseHtmlTable2().getTable( element.toString());
					document.add(table);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
	        } else {
	        	document.add(new Paragraph());
				
			}*/
	        
		   // System.out.println(tables);
	       /* List<PdfPTable>	tblList = null;
			try {
			tblList = parseTableFromHtmlString(doc);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    for(int  i = 0; i < tblList.size(); i++ ){
		    	//System.out.println("tbleCount = "+i);
		    	try {
		    		System.out.println("have data"+tblList.get(i));
		    		//document.add(new Paragraph("Hai"));
		    			document.add(tblList.get(i));
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    }
		   */
	       // parseTable(doc);
		    
		    document.close();
		
		
		
		
		
	}

	public static void main(String[] args) throws DocumentException {
		// TODO Auto-generated method stub
		try {
			new ExtractTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getItemCount(Elements elements){
		int count = 0;
		for (Element n : elements) {
		    count++;
		}
		return count;
	}
	
	
	  public List<PdfPTable> parseTable(String doc){
		  
		  /*String[] table = StringUtils.substringBetween(doc, "<title>", "</title>");
			System.out.println("title:" + title); // good
*/
		  String[] tds1 = StringUtils.substringsBetween(doc, "<table>", "</table>");
		  
		  for (String td : tds1) {
				System.out.println("td value:" + td); // good
			}  
		  
		  
		  
			String[] tds = StringUtils.substringsBetween(doc, "<td>", "</td>");
			for (String td : tds) {
				System.out.println("td value:" + td); // good
			}  
		  
		  
		  
		  
		return null;
			 
		 }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<PdfPTable> parseTableFromHtmlString(Document doc) throws IOException{
		//extract full table data from  string
		List<PdfPTable> tableList = new ArrayList<PdfPTable>();
		Elements tables = doc.select("table");
		//Iterate element and get table one by one
		for (Element table : tables) {
			int rowCount = getItemCount(table.select("tr"));
			int colCount = getItemCount(table.select("td"));
		   //Create table	
		   PdfPTable pdfPTable =  new PdfPTable(colCount);
		   PdfPCell cell = new PdfPCell();
		   //Fetch row string
		   for (Element tableRow : tables.select("tr")) {
			  System.out.println(tableRow.select("th").text());
			
				 pdfPTable.addCell(tableRow.select("td").text());
				
			
			   /*ElementList list1 = XMLWorkerHelper.parseToElementList(tableRow.select("th").text(), null);
			   for (com.itextpdf.text.Element element : list1) {
		            cell.addElement(element);
		        }*/
			   //pdfPTable.addCell(tableRow.select("th").text());
			   
		   }
		   pdfPTable.setHeaderRows(1);
		   tableList.add(pdfPTable);
		   
		}
		
		
		
		return tableList;
		
	}
}
