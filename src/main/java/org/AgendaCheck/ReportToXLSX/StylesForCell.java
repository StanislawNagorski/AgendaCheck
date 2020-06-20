package org.AgendaCheck.ReportToXLSX;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

public class StylesForCell {



    public static Map<String, CellStyle> createCellStyles(XSSFWorkbook report){

        Map<String, CellStyle> styleMap = new HashMap<>();

        DataFormat dataFormat = report.createDataFormat();
        XSSFFont boldFont = report.createFont();
        CellStyle defaultCellStyle = report.createCellStyle();
        styleMap.put("defaultCellStyle", defaultCellStyle);

        CellStyle defaultDoubleCellStyle = report.createCellStyle();
        defaultDoubleCellStyle.setDataFormat(dataFormat.getFormat("#.#"));
        styleMap.put("defaultDoubleCellStyle", defaultDoubleCellStyle);

        CellStyle boldedDoubleWithTopBorder = report.createCellStyle();
        boldedDoubleWithTopBorder.setDataFormat(dataFormat.getFormat("#"));
        boldFont.setBold(true);
        boldedDoubleWithTopBorder.setFont(boldFont);
        boldedDoubleWithTopBorder.setBorderTop(BorderStyle.MEDIUM);
        boldedDoubleWithTopBorder.setAlignment(HorizontalAlignment.CENTER);
        styleMap.put("boldedDoubleWithTopBorder", boldedDoubleWithTopBorder);

        CellStyle titleBoldedWithBotBorder = report.createCellStyle();
        boldFont.setBold(true);
        titleBoldedWithBotBorder.setFont(boldFont);
        titleBoldedWithBotBorder.setBorderBottom(BorderStyle.MEDIUM);
        titleBoldedWithBotBorder.setAlignment(HorizontalAlignment.CENTER);
        styleMap.put("titleBoldedWithBotBorder",titleBoldedWithBotBorder);

        CellStyle percentageStyle = report.createCellStyle();
        percentageStyle.setDataFormat(dataFormat.getFormat("0.00%"));
        styleMap.put("percentageStyle",percentageStyle);

        CellStyle polishZlotyStyle = report.createCellStyle();
        polishZlotyStyle.setDataFormat(dataFormat.getFormat("###0,00\\ \"zł\";-###0,00\\ \"zł\""));
        styleMap.put("polishZlotyStyle",polishZlotyStyle);

        CellStyle polishZlotyStyleBoldedWithBotBorder = report.createCellStyle();
        polishZlotyStyleBoldedWithBotBorder.setDataFormat(dataFormat.getFormat("###0,00\\ \"zł\";-###0,00\\ \"zł\""));
        boldFont.setBold(true);
        polishZlotyStyleBoldedWithBotBorder.setFont(boldFont);
        polishZlotyStyleBoldedWithBotBorder.setBorderTop(BorderStyle.MEDIUM);
        polishZlotyStyleBoldedWithBotBorder.setAlignment(HorizontalAlignment.CENTER);
        styleMap.put("polishZlotyStyleBoldedWithBotBorder", polishZlotyStyleBoldedWithBotBorder);

        return styleMap;

    }


}
