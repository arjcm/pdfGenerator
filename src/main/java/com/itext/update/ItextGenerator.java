/**
 * 
 */
package com.itext.update;

/**
 * This class is used  for generate pdf with toc from JSON object
 * Merge two or more pdf using itext.
 * existing pdf file using iText jar.
 * JSON parsing using Jackson-xc.
 * HTMl to pdf parse using xmlworker.
 * 
 * @author ArjunM 
 *
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.inapp.itext.PdfManager;
import com.inapp.itext.model.PdfFileDetails;
import com.inapp.itext.model.TocModel;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfCopy.PageStamp;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class ItextGenerator {

	final static Logger logger = Logger.getLogger(ItextGenerator.class);
	// out put folder path
	static String outPutDirectory = "";

	// JSON file path
	static String jsonPath = "";

	int numberOfPage;
	Map<Integer, PdfFileDetails> pdfFiles = new TreeMap<Integer, PdfFileDetails>();

	// create an executerService
	public static ExecutorService executorService = Executors.newFixedThreadPool(1000);

	public ItextGenerator() throws NoSuchFileException {

	}

	public static void main(String[] args) {

		ItextGenerator app = null;
		try {
			app = new ItextGenerator();
		} catch (NoSuchFileException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<Future<PdfFileDetails>> futureList = new ArrayList<Future<PdfFileDetails>>();
		// Calculate execution time
		long startTime = System.currentTimeMillis();
		if (args.length != 0) {
			if (args[0] != null && args[1] != null) {
				outPutDirectory = args[0];
				jsonPath = args[1];
				// tocPath = args[2];

				try {
					File file = new File(outPutDirectory);
					file.getParentFile().mkdirs();
					logger.info("Start application");
					app.parseJson(jsonPath, futureList);
					app.waitForParsingCompleteAndMerge(futureList);
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (NoSuchFileException e) {
					e.printStackTrace();
					logger.error("invalid json  path");
				}
			}
		}
		else {
			logger.error("Invalid input arguments!");
			System.exit(0);
		}

		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		logger.info("execution time=" + elapsedTime);
		logger.info("Finishing Application !!!");

	}

	public void waitForParsingCompleteAndMerge(List<Future<PdfFileDetails>> futureList) {
		try {
			List<Future<PdfFileDetails>> finishedList = new ArrayList<Future<PdfFileDetails>>();
			while (!futureList.isEmpty()) {
				Iterator<Future<PdfFileDetails>> itr = futureList.iterator();
				while (itr.hasNext()) {
					Future<PdfFileDetails> future = itr.next();

					if (future.isDone()) {
						PdfFileDetails pdfDetails = future.get();
						if (pdfDetails != null) {

							pdfFiles.put(pdfDetails.getFileIndexNo(), pdfDetails);
						}
						finishedList.add(future);
					}
				}

				futureList.removeAll(finishedList);
				finishedList.clear();
			}
			// Shutdown executorService
			executorService.shutdown();

			// call mergeWithToc
			mergeWithToc();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * This method is ued for parse JSON object
	 * 
	 * @param JSON.path of json object
	 * 
	 * @return pdfData having JSON details
	 */
	public void parseJson(String jsonPath, List<Future<PdfFileDetails>> futureList) throws NoSuchFileException {

		int count = 0;
		try {
			JsonFactory f = new MappingJsonFactory();
			JsonParser parser = f.createParser(new File(jsonPath));

			Map<String, String> fields = null;
			JsonToken token;

			while ((token = parser.nextToken()) != JsonToken.END_ARRAY) {

				switch (token) {
				case START_OBJECT:
					fields = new HashMap<String, String>();
					break;

				// For each field-value pair, store it in the map 'fields'
				case FIELD_NAME:
					String field = parser.getCurrentName();
					token = parser.nextToken();
					String value = parser.getValueAsString();
					fields.put(field, value);
					break;
				case END_OBJECT:

					// Start executer service
					Future<PdfFileDetails> result = executorService.submit(new PdfManager(fields, outPutDirectory, count++));
					// add future object into future list
					if (result != null) {
						futureList.add(result);
					}

					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void mergeWithToc() throws DocumentException, IOException, URISyntaxException {

		// URL resource = this.getClass().getResource("toc.pdf");
		File file = new File("resources/toc.pdf");
		String absolutePath = file.getAbsolutePath();
		System.out.println("Path: " + absolutePath);
		logger.info("Start merging pdf files. !!!");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		List<TocModel> toc = new ArrayList<TocModel>();
		Document document = new Document();
		PdfCopy copy = new PdfCopy(document, bos);
		PageStamp stamp;
		document.open();
		int noOfPages;
		PdfImportedPage page;
		Chunk chunk;
		PdfReader MergeReader;
		int pageCount = 1;

		logger.info("Merging file size :" + pdfFiles.size());
		// merge pdf
		Set<Map.Entry<Integer, PdfFileDetails>> entrySet = pdfFiles.entrySet();
		for (Map.Entry<Integer, PdfFileDetails> entry : entrySet) {
			PdfFileDetails pdfFileDetails = (PdfFileDetails) entry.getValue();
			logger.info("Merging file path :" + pdfFileDetails.getPath());
			MergeReader = new PdfReader(pdfFileDetails.getPath());
			noOfPages = MergeReader.getNumberOfPages();
			logger.info("Merging file : " + pdfFileDetails.getFileIndexNo() + " Total Pages: " + noOfPages);
			for (int pageNo = 0; pageNo < noOfPages; pageNo++) {
				pageCount++;
				// import page existing pdf
				page = copy.getImportedPage(MergeReader, pageNo + 1);
				stamp = copy.createPageStamp(page);
				// set page number
				chunk = new Chunk(String.format("Page %d", pageCount));
				// parse pdf file
				PdfReaderContentParser parser = new PdfReaderContentParser(MergeReader);
				TextExtractionStrategy strategy;
				strategy = parser.processContent(pageNo + 1, new SimpleTextExtractionStrategy());
				for (int k = 0; k < pdfFileDetails.getFileToc().size(); k++) {
					// check TOC title
					if (strategy.getResultantText().contains(pdfFileDetails.getFileToc().get(k))) {
						TocModel tocModel = new TocModel();
						tocModel.setPageNo(pageCount);
						tocModel.setTitle(pdfFileDetails.getFileToc().get(k));
						tocModel.setParentToc(pdfFileDetails.getFileToc().get(k));
						// set destination
						chunk.setLocalDestination("p" + pageCount);
						toc.add(tocModel);
					}
				}
				ColumnText.showTextAligned(stamp.getUnderContent(), Element.ALIGN_RIGHT,
						new Phrase(chunk), 559, 810, 0);
				stamp.alterContents();
				// copying pages
				copy.addPage(page);
			}
		}

		logger.info("Finished merging pdf files. !!!");
		// create Toc
		 final PdfReader tocBackgroundReader = new PdfReader("toc.pdf");
		Document tocDocument = new Document(tocBackgroundReader.getCropBox(1));
		ByteArrayOutputStream tocBaos = new ByteArrayOutputStream();
		final PdfWriter tocWriter = PdfWriter.getInstance(tocDocument, tocBaos);
		tocWriter.setPageEvent(new PdfPageEventHelper() {
		    PdfImportedPage stationary = tocWriter.getImportedPage(tocBackgroundReader, 1);
		    @Override
		    public void onEndPage(PdfWriter writer, Document document)
		    {
		        writer.getDirectContentUnder().addTemplate(stationary, 20, 20);
		    }
		});
		tocDocument.open();
		for (TocModel tocModel : toc) {
		    PdfAction action = PdfAction.gotoLocalPage("p" + tocModel.getPageNo(), false);

		    Paragraph paragraph = new Paragraph();
		    Chunk tocChunk = new Chunk(tocModel.getTitle());
		    tocChunk.setAction(action);
		    paragraph.add(tocChunk);
		    tocChunk = new Chunk(new DottedLineSeparator());
		    tocChunk.setAction(action);
		    paragraph.add(tocChunk);
		    tocChunk = new Chunk(String.valueOf(tocModel.getPageNo()));
		    tocChunk.setAction(action);
		    paragraph.add(tocChunk);
		    tocDocument.add(paragraph);
		}
		tocDocument.close();

		PdfReader tocReader = new PdfReader(tocBaos.toByteArray());
		int tocPageCount = tocReader.getNumberOfPages();
		copy.addDocument(tocReader);

		document.close();
		logger.info("Finished TOC !!!");

        tocReader = new PdfReader(bos.toByteArray());
        noOfPages = tocReader.getNumberOfPages();
        tocReader.selectPages(String.format("%d-%d, 1-%d", noOfPages - tocPageCount + 1, noOfPages, noOfPages - tocPageCount));
        PdfStamper stamper = new PdfStamper(tocReader, new FileOutputStream(outPutDirectory + "merge.pdf"));

        stamper.close();
        logger.info("merging completed!!!");
	}

}
