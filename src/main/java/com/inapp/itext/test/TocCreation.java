package com.inapp.itext.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.inapp.itext.model.TocModel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopy.PageStamp;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;

public class TocCreation {

	public TocCreation() {
		// TODO Auto-generated constructor stub
	}

	public void manipulatePdf(String outPutDirectory, List<TocModel> toc)
			throws DocumentException, IOException {
		/*
		 * PdfReader tocReader = new PdfReader("toc.pdf"); PdfStamper stamper =
		 * new PdfStamper(tocReader, new FileOutputStream(outPutDirectory +
		 * "toc1.pdf")); PdfImportedPage importedPage =
		 * stamper.getImportedPage(tocReader, 1); PageStamp stamp
		 * =copy.createPageStamp(page);
		 */

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, bos);
		PageStamp stamp;
		document.open();
		int noOfPages;
		PdfImportedPage page;
		Chunk chunk;
		PdfReader MergeReader;
		int pageCount = 1;
		// create Toc
		System.out.println("Starting TOC !!!");
		PdfReader tocReader = new PdfReader("toc.pdf");
		PdfStamper stamper = new PdfStamper(tocReader, new FileOutputStream(outPutDirectory + "toc1.pdf"));
		page = copy.getImportedPage(tocReader, 1);
		stamp = copy.createPageStamp(page);
		Paragraph paragraph;
		PdfAction action;
		PdfAnnotation link;
		float y = 770;
		Rectangle pagesize = tocReader.getPageSize(1);
		ColumnText colTxt = new ColumnText(stamp.getOverContent());
		colTxt.setSimpleColumn(36, 36, 559, y);
		for (TocModel tocModel : toc) {
			paragraph = new Paragraph(tocModel.getTitle());
			paragraph.add(new Chunk(new DottedLineSeparator()));
			paragraph.add(String.valueOf(tocModel.getPageNo()));
			colTxt.addElement(paragraph);
			try {
				colTxt.go();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// seting toc action
			action = PdfAction.gotoLocalPage("p" + tocModel.getPageNo(), false);
			link = new PdfAnnotation(copy, 36, colTxt.getYLine(), 559, y, action);
			stamp.addAnnotation(link);
			y = colTxt.getYLine();
		}

		try {
			int status = colTxt.go();
			// copy.addPage(page);
			int tocPageCount = 0;
			Rectangle rectPage2 = new Rectangle(36, 36, 559, 806);
			if (ColumnText.hasMoreText(status)) {
				System.out.println("Too much data found");
				page = loadPage(page, tocReader, stamper);
				triggerNewPage(stamper, pagesize, page, colTxt, rectPage2, ++tocPageCount);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		stamp.alterContents();
		// copy.addPage(page);
		document.close();
		System.out.println("Finished TOC !!!");
		tocReader = new PdfReader(bos.toByteArray());
		noOfPages = tocReader.getNumberOfPages();
		tocReader.selectPages(String.format("%d, 1-%d", noOfPages, noOfPages - 1));
		// stamper = new PdfStamper(tocReader, new
		// FileOutputStream(outPutDirectory + "merge.pdf"));
		stamper.close();
		System.out.println("TOC completed!!!");
	}

	public PdfImportedPage loadPage(
			PdfImportedPage page, PdfReader reader, PdfStamper stamper) {
		if (page == null) {
			return stamper.getImportedPage(reader, 1);
		}
		return page;
	}

	public void triggerNewPage(PdfStamper stamper, Rectangle pagesize,
			PdfImportedPage page, ColumnText column, Rectangle rect, int pagecount)
			throws DocumentException {
		stamper.insertPage(pagecount, pagesize);
		PdfContentByte canvas = stamper.getOverContent(pagecount);
		canvas.addTemplate(page, 0, 0);
		column.setCanvas(canvas);
		column.setSimpleColumn(rect);
		column.go();
	}

	public void manipulatePdf1(String outPutDirectory, List<TocModel> toc) throws DocumentException, IOException {
		Paragraph paragraph;
		PdfAction action;
		PdfAnnotation link;
		Document  document =  new Document();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		PdfCopy copy = new PdfCopy(document, bos);
		PdfReader reader = new PdfReader("toc.pdf");
		PdfImportedPage page = copy.getImportedPage(reader, 1);

		Rectangle pagesize = reader.getPageSize(1);
		
		PageStamp stamp = copy.createPageStamp(page);


		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPutDirectory + "new_toc.pdf"));
		ColumnText colTxt = new ColumnText(stamper.getOverContent(1));
		Rectangle rectPage1 = new Rectangle(36, 36, 559, 540);
		colTxt.setSimpleColumn(rectPage1);
		float y = 770;

		for (TocModel tocModel : toc) {
			paragraph = new Paragraph(tocModel.getTitle());
			paragraph.add(new Chunk(new DottedLineSeparator()));
			paragraph.add(String.valueOf(tocModel.getPageNo()));
			colTxt.addElement(paragraph);
			try {
				colTxt.go();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// seting toc action
			action = PdfAction.gotoLocalPage("p" + tocModel.getPageNo(), false);
			link = new PdfAnnotation(copy, 36, colTxt.getYLine(), 559, y, action);
			//stamp.addAnnotation(link);
			y = colTxt.getYLine();
		}
	//	copy.addPage(page);
		int pagecount = 1;
		Rectangle rectPage2 = new Rectangle(36, 36, 559, 806);
		int status = colTxt.go();
		
		while (ColumnText.hasMoreText(status)) {
			try {
				status = triggerNewPage(stamper, pagesize, colTxt, rectPage2, ++pagecount);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		stamper.setFormFlattening(true);
		stamper.close();
		//document.close();
		reader.close();

	}
	
	
	public int triggerNewPage(PdfStamper stamper, Rectangle pagesize, ColumnText column, Rectangle rect, int pagecount) throws DocumentException {
	    stamper.insertPage(pagecount, pagesize);
	    PdfContentByte canvas = stamper.getOverContent(pagecount);
	    column.setCanvas(canvas);
	    column.setSimpleColumn(rect);
	    return column.go();
	}
	
	public void finalMerge(List<InputStream> inputPdfList,String outPutDirectory) throws IOException, DocumentException{
		List<InputStream> inputPdfList1 = new ArrayList<InputStream>();
		inputPdfList1.add(new FileInputStream(outPutDirectory+"mergenew.pdf"));
		inputPdfList1.add(new FileInputStream(outPutDirectory+"new_toc.pdf"));
		OutputStream outputStream = 
                new FileOutputStream(outPutDirectory + "Final_Merge.pdf");
		
	    //Create document and pdfReader objects.
	    Document document = new Document();
	    List<PdfReader> readers = 
	            new ArrayList<PdfReader>();
	    int totalPages = 0;

	    //Create pdf Iterator object using inputPdfList.
	    Iterator<InputStream> pdfIterator = inputPdfList1.iterator();

	    // Create reader list for the input pdf files.
	    while (pdfIterator.hasNext()) {
	            InputStream pdf = pdfIterator.next();
	            PdfReader pdfReader = new PdfReader(pdf);
	            readers.add(pdfReader);
	            totalPages = totalPages + pdfReader.getNumberOfPages();
	    }

	    // Create writer for the outputStream
	    PdfWriter writer = PdfWriter.getInstance(document, outputStream);

	    //Open document.
	    document.open();

	    //Contain the pdf data.
	    PdfContentByte pageContentByte = writer.getDirectContent();

	    PdfImportedPage pdfImportedPage;
	    int currentPdfReaderPage = 1;
	    Iterator<PdfReader> iteratorPDFReader = readers.iterator();

	    // Iterate and process the reader list.
	    while (iteratorPDFReader.hasNext()) {
	            PdfReader pdfReader = iteratorPDFReader.next();
	            //Create page and add content.
	            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
	                  document.newPage();
	                  pdfImportedPage = writer.getImportedPage(
	                          pdfReader,currentPdfReaderPage);
	                  pageContentByte.addTemplate(pdfImportedPage, 0, 0);
	                  currentPdfReaderPage++;
	            }
	            currentPdfReaderPage = 1;
	    }

	    //Close document and outputStream.
	    outputStream.flush();
	    document.close();
	    outputStream.close();

	    System.out.println("Pdf files merged successfully.");
		
		
	}

}
