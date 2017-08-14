package com.inapp.itext.test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;


/**
 *
 * @author iText
 */

public class ParseHtmlTable2 {
    public static final String DEST = "C:/Users/ArjunM/workspace/pdfGeneratorhtml_table_2.pdf";
    public static final String CSS = "tr { text-align: center; } th { background-color: lightgreen; padding: 3px; } td {background-color: lightblue;  padding: 3px; }";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ParseHtmlTable2().createPdf(DEST);
    }
        
    /**
     * Creates a PDF with the words "Hello World"
     * @param file
     * @throws IOException
     * @throws DocumentException
     */
    public void createPdf(String file) throws IOException, DocumentException {
        // step 1
   /*     Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        // step 3
        document.open();
        // step 4
        document.add(new Paragraph("This is my HTML table:"));
        document.add(Chunk.NEWLINE);
        PdfPTable table = getTable();
        document.add(table);
        // step 5
        document.close();*/
    }
    
    public PdfPTable getTable( String str) throws IOException {
    	//List<PdfPTable> table =  new ArrayList<PdfPTable>();
        
        StringBuilder sb = new StringBuilder();
        sb.append(str);
      /*  sb.append("<table border=\"2\">");
        sb.append("<tr>");
        sb.append("<th>Sr. No.</th>");
        sb.append("<th>Text Data</th>");
        sb.append("<th>Number Data</th>");
        sb.append("</tr>");
        for (int i = 0; i < 10; ) {
            i++;
            sb.append("<tr>");
            sb.append("<td>");
            sb.append(i);
            sb.append("</td>");
            sb.append("<td>This is text data ");
            sb.append(i);
            sb.append("</td>");
            sb.append("<td>");
            sb.append(i);
            sb.append("</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");*/
        System.out.println(sb);
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(CSS.getBytes()));
        cssResolver.addCss(cssFile);
        
        // HTML
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(null);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline pdf = new ElementHandlerPipeline(elements, null);
        HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
        CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(css, true);
        XMLParser p = new XMLParser(worker);
        
        String data;
        if(sb.toString()== null){
        data= "not found";
        
        }else{
        	data =  sb.toString();
        }
        p.parse(new ByteArrayInputStream(data.getBytes()));
        
        return (PdfPTable)elements.get(0);
    }
}