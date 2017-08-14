package com.inapp.itext;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.inapp.itext.model.PageModel;
import com.inapp.itext.model.PdfFileDetails;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PdfManager implements Callable<PdfFileDetails> {
	private final static Logger logger = Logger.getLogger(PdfManager.class);
	Map<String, String> jsonObjectMap;
	PageModel page = new PageModel();

	String outPutDirectory;
	String tocPath;
	int indexID;
	List<String> tocTitle = new ArrayList<String>();

	public PdfManager(Map<String, String> fields, String outPutDirectory, int indexID) {
		this.jsonObjectMap = fields;
		this.outPutDirectory = outPutDirectory;
		this.indexID = indexID;
	}

	@Override
	public PdfFileDetails call() {
		try {
			
			for (Map.Entry<String, String> entry : jsonObjectMap.entrySet()) {
				if (entry.getKey().equals("orientation")) {
					page.setOrientation(entry.getValue() != null ? entry.getValue() : "");
				}
				else if (entry.getKey().equals("pageContent")) {
					page.setPageContent(entry.getValue() != null ? entry.getValue() : "");
					if (page.getPageContent() != null && page.getPageContent().length() > 0) {
						org.jsoup.nodes.Document doc = Jsoup.parse(page.getPageContent());
						Elements elements = doc.select("h3,h4,h5");
						for (Element element : elements) {
							tocTitle.add(element.toString());
							if (element.nodeName().equalsIgnoreCase("h3")) {
								Pattern parentPattern = Pattern.compile("<h3>(.+?)</h3>");
								Matcher parentMatcher = parentPattern.matcher(page.getPageContent());
								while (parentMatcher.find()) {
									// tocTitle.add(parentMatcher.group(1));
									tocTitle.add(parentMatcher.group(1));
								}
							} 
							else if (element.nodeName().equalsIgnoreCase("h4")) {
								Pattern childPattern = Pattern.compile("<h4>(.+?)</h4>");
								Matcher childMatcher = childPattern.matcher(page.getPageContent());
								while (childMatcher.find()) {
									tocTitle.add(childMatcher.group(1));
								}
							} 
							else if (element.nodeName().equalsIgnoreCase("h5")) {
								Pattern subChildPattern = Pattern.compile("<h4>(.+?)</h4>");
								Matcher subChildMatcher = subChildPattern.matcher(page.getPageContent());
								while (subChildMatcher.find()) {
									tocTitle.add(subChildMatcher.group(1));
								}
							}
						}

					}

				}
				else if (entry.getKey().equals("type")) {
					page.setType(entry.getValue() != null ? entry.getValue() : "");
				}
				else if (entry.getKey().equals("headerContent")) {
					page.setHeader(entry.getValue() != null ? entry.getValue() : "");
				}
				else if (entry.getKey().equals("footerContent")) {
					page.setFooter(entry.getValue() != null ? entry.getValue() : "");
				}
				else if (entry.getKey().equals("file")) {
					page.setFile(entry.getValue() != null ? entry.getValue() : "");
				}
			}

			return createPdf();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}

	public PdfFileDetails createPdf() {
		String headerContent = "";
		String footerContent = "";
		String filePath = "";
		String fileName = "";
		// Create document object with page properties
		PdfFileDetails pdfFileDetails = new PdfFileDetails();
        Orientation orientation =  new Orientation();
		try {
			Document document = new Document(PageSize.A4, 36, 36, 36, 72);
//			PdfNumber orientation = null;
			if (page != null) {
				// check JSON object type as HTML
				if (page.getType().equals("html") && !page.getPageContent().isEmpty()) {
					// create a PsfWriter object
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPutDirectory + indexID + ".pdf"));

					// check page orientation as landscape
					if (page.getOrientation() != null && page.getOrientation().equals("L")) {
						document.setPageSize(PageSize.A4.rotate());
						// check page orientation as portrait
					} 
					else if (page.getOrientation() != null && page.getOrientation().equals("P")) {
						
						document.newPage();
					}
					
					// get page Header
					if (page.getHeader() != null) {
						headerContent = page.getHeader();
					}

					// get page footer
					if (page.getFooter() != null) {
						footerContent = page.getFooter();
					}

					// set page header and footer
					writer.setPageEvent(new HeaderFooter(headerContent, footerContent));

					// set page orientation
					
					// open documet
					document.open();
					try{
	
					
					XMLWorkerHelper.getInstance().parseXHtml(writer,
							document, new ByteArrayInputStream(parserXHtml(page.getPageContent()).getBytes()));
					writer.setPageEvent(orientation);
					}catch(Exception e){
						logger.error("Error:invalid  html content detected!!");

					}
					document.close();
					/*
					 * String data = parserXHtml(page.getPageContent());
					 * PdfPTable table = new PdfPTable(1);
					 * 
					 * // disable table border
					 * table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
					 * 
					 * // create a pdf cell PdfPCell cell = new PdfPCell(); 
					 * // parse JSON page content as pdf document ElementList
					 * elementList = XMLWorkerHelper.parseToElementList(data, null);
					 *  // add content to cell
					 *   for (Element element :elementList) {
					 * 
					 * cell.addElement(element); 
					 * } 
					 * // disable // cell border
					 * cell.setBorder(Rectangle.NO_BORDER); 
					 * // add // cell into
					 * table table.addCell(cell); // add table to document
					 * document.add(table);
					 */
					document.close();

					// set filename and destination
					fileName = indexID + ".pdf";
					filePath = outPutDirectory;
					// check file type as external
				}
				else if (page.getType().equals("external")) {
					// get file name and path
					if (page.getFile() != null) {
						Path path = Paths.get(page.getFile());
						fileName = path.getFileName().toString();
						filePath = path.getParent() + File.separator;
					}
					else {
						Path path = Paths.get(page.getPageContent());
						fileName = path.getFileName().toString();
						filePath = path.getParent() + File.separator;
					}
				}
				pdfFileDetails.setPath(filePath + fileName);
				pdfFileDetails.setFileToc(tocTitle);
				pdfFileDetails.setFileIndexNo(indexID);

				return pdfFileDetails;
			}
		} catch (IOException | DocumentException e) {
			logger.error("Error:" + e.getMessage());
		}

		return null;
	}
	
	

	/*
	 * parsing htmlString to XHtml
	 * @param html
	 * @return string
	 */

	public String parserXHtml(String html) {
		org.jsoup.nodes.Document document = Jsoup.parseBodyFragment(html);
		document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);
		document.outputSettings().charset("UTF-8");
		return document.toString();
	}

}
