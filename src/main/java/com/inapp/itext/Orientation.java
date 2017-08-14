package com.inapp.itext;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class Orientation extends PdfPageEventHelper  {
	protected PdfNumber orientation ;
	public Orientation(PdfNumber orientation) {
		this.orientation = orientation;
		// TODO Auto-generated constructor stub
	}
	
	 public Orientation() {
		// TODO Auto-generated constructor stub
	}

	public void setOrientation(PdfNumber orientation) {
         this.orientation = orientation;
     }

     @Override
     public void onStartPage(PdfWriter writer, Document document) {
         writer.addPageDictEntry(PdfName.ROTATE, orientation);
     }

}
