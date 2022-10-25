package com.mxpioframework.excel.export.pdf;

import java.io.OutputStream;

import com.mxpioframework.excel.export.model.ReportTitle;
import com.mxpioframework.excel.export.pdf.model.ReportDataModel;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Component(PdfReportBuilder.BEAN_ID)
public class PdfReportBuilder extends AbstractPdfReportBuilder {
	
	public static final String BEAN_ID="panda.PdfReportBuilder";


	protected PdfReportBuilder() throws Exception {
		super();
	}

	public Document createDocument(ReportTitle reportTitle, OutputStream out) throws Exception {
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, out);
		if (reportTitle.isShowPageNo()) {
			PdfReportPageNumber event = new PdfReportPageNumber(chineseFont);
			writer.setPageEvent(event);
		}
		doc.open();
		Paragraph paragraph = this.createReportTitle(reportTitle);
		doc.add(paragraph);
		return doc;
	}

	public void addGridToDocument(Document doc, ReportTitle reportTitle, ReportDataModel reportDataModel) throws Exception {
		doc.add(createGridTable(reportDataModel, reportTitle.isRepeatHeader()));
	}

	public void addNewline(Document doc, int i) throws DocumentException {
		if (i > 0) {
			for (int j = 0; j < i; j++) {
				doc.add(Chunk.NEWLINE);
			}
		}
	}

}
