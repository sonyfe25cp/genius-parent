package com.genius.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.JFreeChart;
import org.springframework.web.servlet.ModelAndView;

import com.genius.dao.QueryLogDaoImpl;
import com.genius.model.QueryCount;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Chapter;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Section;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

/**
 * @author C.M
 *
 */
public class PDFUtils {
		
		private static int height = 500;
		private static int width = 700;
		private static String chartName = "Genius 统计";
		
		private static String fontPath="msyh.ttf";
	
//		private static DLDELogger logger=new DLDELogger();
		
		public static ModelAndView pDFModelAndView(String name, Date currentime, Date startime, String head, String email, 
				String title, String address, String telephone, QueryLogDaoImpl queryLogDao) throws IOException{
			ModelAndView mav = new ModelAndView(name);
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
			String currentimex = dateformat.format(currentime);
			String startimex = dateformat.format(startime);
			QueryCount[] querycount = queryLogDao.initial(startime, currentime);
			JFreeChart piechart = ChartUtils.createPieChart(chartName, querycount);
			JFreeChart barchart = ChartUtils.createBarChart(chartName, querycount);
			String oPath = PDFUtils.class.getResource("/").getPath();
			String savePath = oPath.replace("WEB-INF/classes/", "cloud/");
			File pieimage = ChartUtils.saveChart(piechart, currentime, "day", "pie",
					width, height, savePath);
			File barimage = ChartUtils.saveChart(barchart, currentime, "day", "bar",
					width, height, savePath);
			File pdf = createPDF(barimage, querycount, currentime, startime, head, title, email, address, telephone);
			String path = "/cloud/" + pdf.getName();
			mav.addObject("path", path).addObject("querycount", querycount).
						addObject("pieimage", pieimage).addObject("barimage", barimage).
						addObject("title", title).addObject("head",head).addObject("address", address).
						addObject("email", email).addObject("telephone", telephone)
						.addObject("pieimage", "/cloud/"+pieimage.getName())
						.addObject("barimage", "/cloud/"+barimage.getName()).
						addObject("currentime", currentimex).addObject("startime", startimex);	
			return mav;
		}
		
		public static  File createPDF(File barimage, 
				QueryCount[] querycount, Date currentime, Date startime, String head, String title, 
				String email, String address, String telephone) {
			
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd");
			String currentimex = dateformat.format(currentime);
			String startimex = dateformat.format(startime);
			
			String oPath = PDFUtils.class.getResource("/").getPath();
			String savePath = oPath.replace("WEB-INF/classes/", "cloud/");
			StringBuilder builder = new StringBuilder();
			builder.append(savePath);
			builder.append(ChartUtils.dateToInt(currentime));
			builder.append("-report.pdf");
			String path1 = builder.toString();
			
			Document doc = new Document(PageSize.A4);
			doc.addAuthor("Genius");
			doc.addCreationDate();
			File PDFFile = new File(path1);
			try {
				PdfWriter.getInstance(doc, new FileOutputStream(PDFFile));
				BaseFont fontChinese=BaseFont.createFont(fontPath,BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
				Font firsttitleFont = new Font(fontChinese, 22, Font.BOLD);
				Font secondtitleFont = new Font(fontChinese, 17, Font.BOLD);
				Font contentFont = new Font(fontChinese, 12);
				HeaderFooter header = new HeaderFooter(new Phrase(head, new Font(fontChinese, 10)), false);
				header.setBorder(2);
				header.setAlignment(1);
				header.setBorderColor(Color.red);
				doc.setHeader(header);
				
				HeaderFooter footer = new HeaderFooter(new Phrase("-"), new Phrase("-"));
				footer.setBorder(Rectangle.TOP);
				footer.setBorderColor(Color.red);
				footer.setAlignment(1);
				doc.setFooter(footer);
				doc.open();
				String lpath = PDFUtils.class.getResource("").getPath();
//				logger.info("logoPath:"+lpath);
				String logoPath = lpath.replace("WEB-INF/classes/com/genius/utils/", "images/logo_small.png");
//				logger.info("logoPath:"+logoPath);
				Image logoimage = Image.getInstance(logoPath);
				logoimage.setAlignment(Image.ALIGN_CENTER);
				logoimage.scaleToFit(300, 200);
				logoimage.setSpacingAfter(30);
				
				Paragraph firstpage = new Paragraph(new Chunk(title, firsttitleFont));
				firstpage.setAlignment(1);
				firstpage.setSpacingAfter(50);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Paragraph dateparagraph = new Paragraph(dateFormat.format(new Date()),  firsttitleFont);
				dateparagraph.setAlignment(1);
				doc.add(logoimage);
				doc.add(firstpage);
				doc.add(dateparagraph);
				
				doc.newPage();
				Chunk chunk =  new Chunk("简介", firsttitleFont);
				chunk.setBackground(Color.cyan);
				Paragraph intro = new Paragraph(chunk);
				intro.setSpacingAfter(30);
				
				Paragraph secondpage1 = new Paragraph(new Chunk("欢迎使用"+title+"。" +
						"在这份报表中您将会看到这一整段时间内所有用户进行查询的结果和我们进行检索出文档处理" +
						"的统计。",contentFont));
				secondpage1.setSpacingAfter(30);
				secondpage1.setFirstLineIndent(25);
				
				Paragraph secondpage2 = new Paragraph(new Chunk("对于一个搜索引擎，每一个用户的查询都会被" +
						"保存在服务器上" +"，将一定时间范围内所有用户的查询记录统计出来有利于分析特定" +
						"用户的搜索习惯，汇总时下热点事件，进行推荐操作等等。除了查询日志之外，可以同样的" +
						"对检索返回的文档进行如此操作。",contentFont));
				secondpage2.setSpacingAfter(30);
				secondpage2.setFirstLineIndent(25);
				
				Paragraph secondpage3 = new Paragraph(new Chunk("BIT Genius小组竭诚为您服务，如有任何意见或者建议" +
						"请直接与我们联系！", contentFont));
				secondpage3.setSpacingAfter(30);
				secondpage3.setFirstLineIndent(25);
				
				Table secondtable = new Table(15);
				secondtable.setWidth(102);
				Cell cell = new Cell("Email:");
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				cell = new Cell(email);
				cell.setColspan(10);
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				cell = new Cell(new Chunk("联系地址:",contentFont));
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				cell = new Cell(new Chunk(address,contentFont));
				cell.setColspan(10);			
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				cell = new Cell(new Chunk("联系电话:",contentFont));
				cell.setColspan(5);
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				cell = new Cell(telephone);
				cell.setColspan(10);
				cell.setBorder(Rectangle.NO_BORDER);
				secondtable.addCell(cell);
				secondtable.setBorder(Rectangle.NO_BORDER);
				secondtable.setPadding(3);
				secondtable.setSpacing(3);
				doc.add(intro);
				doc.add(secondpage1);
				doc.add(secondpage2);
				doc.add(secondpage3);
				doc.add(secondtable);
				
				doc.newPage();
				chunk =  new Chunk("详情", firsttitleFont);
				chunk.setBackground(Color.cyan);
				Paragraph title1 = new Paragraph(chunk);
				title1.setSpacingAfter(30);
				Chapter chapter = new Chapter(title1, 1);
				chapter.setNumberDepth(0);
				
				Paragraph thirdtitle = new Paragraph("统计概要",secondtitleFont);
				title1.setSpacingAfter(30);
				Section section1 = chapter.addSection(thirdtitle);
				Paragraph thirdpage1 = new Paragraph(new Chunk("您现在选择的" + startimex + "至" + currentimex +
						"这一段时间内查询记录的统计情况，统计的形式会分为图表和文字向您呈现。", contentFont));
				thirdpage1.setFirstLineIndent(20);
				section1.add(thirdpage1);
				
				Section section2 = chapter.addSection(new Paragraph(new Chunk("柱状图统计", secondtitleFont)));
				Image barImage = Image.getInstance(barimage.getPath());
				barImage.setAlignment(Image.MIDDLE);
				barImage.scalePercent(50);
				section2.add(barImage);
				
				Section section3 = chapter.addSection(new Paragraph(new Chunk("本周最热",secondtitleFont)));
				Paragraph thirdpage2 = new Paragraph(new Chunk("您看到的将是这周用户搜索频率最高的词汇！", secondtitleFont));
				thirdpage2.setFirstLineIndent(20);
				section3.add(thirdpage2);
				
				Table thirdtable = new Table(3);
				thirdtable.setWidth(100);
				thirdtable.setBorderColor(Color.orange);
				thirdtable.setSpacing(5);
				thirdtable.setBorderWidth(1);
				cell = new Cell(new Chunk("词项", contentFont));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setBackgroundColor(Color.gray);
				thirdtable.addCell(cell);
				cell = new Cell(new Chunk("计数", contentFont));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setColspan(2);
				thirdtable.addCell(cell);
				cell.setBackgroundColor(Color.gray);
				for(int i = 0; i<= querycount.length-1; i++)
				{
					QueryCount temp = querycount[i];
					String query = temp.getQuery();
					String count = String.valueOf(temp.getCount());
					cell = new Cell(new Chunk(String.valueOf(i+1) + ". " + query, contentFont));
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(1);
					thirdtable.addCell(cell);
					cell = new Cell(count);
					cell.setBorder(Rectangle.NO_BORDER);
					cell.setColspan(2);
					thirdtable.addCell(cell);
				}
				section3.add(thirdtable);
				doc.add(chapter);
				doc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (BadElementException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return PDFFile;
			
		}
	
}
