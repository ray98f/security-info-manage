package com.security.info.manage.utils;

import com.alibaba.fastjson.JSONObject;
import com.security.info.manage.entity.PhysicalResult;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ReadWordTable {

    /**
     * 保存生成HTML时需要被忽略的单元格
     */
    private List<String> omitCellsList = new ArrayList<>();

    /**
     * 生成忽略的单元格列表中的格式
     *
     * @param row
     * @param col
     * @return
     */
    public String generateOmitCellStr(int row, int col) {
        return row + ":" + col;
    }

    /**
     * 获取当前单元格的colspan（列合并）的列数
     *
     * @param tcPr 单元格属性
     * @return
     */
    public int getColspan(CTTcPr tcPr) {
        // 判断是否存在列合并
        CTDecimalNumber gridSpan = null;
        if ((gridSpan = tcPr.getGridSpan()) != null) { // 合并的起始列
            // 获取合并的列数
            BigInteger num = gridSpan.getVal();
            return num.intValue();
        } else { // 其他被合并的列或正常列
            return 1;
        }
    }

    /**
     * 获取当前单元格的rowspan（行合并）的行数
     *
     * @param table 表格
     * @param row 行值
     * @param col 列值
     * @return
     */
    public int getRowspan(XWPFTable table, int row, int col) {

        XWPFTableCell cell = table.getRow(row).getCell(col);
        // 正常独立单元格
        if (!isContinueRow(cell) && !isRestartRow(cell)) {
            return 1;
        }
        // 当前单元格的宽度
        int cellWidth = getCellWidth(table, row, col);
        // 当前单元格距离左侧边框的距离
        int leftWidth = getLeftWidth(table, row, col);

        // 用户保存当前单元格行合并的单元格数-1（因为不包含自身）
        List<Boolean> list = new ArrayList<>();
        getRowspan(table, row, cellWidth, leftWidth, list);

        return list.size() + 1;
    }

    private void getRowspan(XWPFTable table, int row, int cellWidth, int leftWidth,
                            List<Boolean> list) {
        // 已达到最后一行
        if (row + 1 >= table.getNumberOfRows()) {
            return;
        }
        row = row + 1;
        int colsNum = table.getRow(row).getTableCells().size();
        // 因为列合并单元格可能导致行合并的单元格并不在同一列，所以从头遍历列，通过属性、宽度以及距离左边框间距来判断是否是行合并
        for (int i = 0; i < colsNum; i++) {
            XWPFTableCell testTable = table.getRow(row).getCell(i);
            // 是否为合并单元格的中间行（包括结尾行）
            if (isContinueRow(testTable)) {
                // 是被上一行单元格合并的单元格
                if (getCellWidth(table, row, i) == cellWidth
                        && getLeftWidth(table, row, i) == leftWidth) {
                    list.add(true);
                    // 被合并的单元格在生成html时需要忽略
                    addOmitCell(row, i);
                    // 去下一行继续查找
                    getRowspan(table, row, cellWidth, leftWidth, list);
                    break;
                }
            }
        }
    }

    /**
     * 判断是否是合并行的起始行单元格
     *
     * @param tableCell
     * @return
     */
    public boolean isRestartRow(XWPFTableCell tableCell) {
        CTTcPr tcPr = tableCell.getCTTc().getTcPr();
        if (tcPr.getVMerge() == null) {
            return false;
        }
        if (tcPr.getVMerge().getVal() == null) {
            return false;
        }
        if (tcPr.getVMerge().getVal().toString().equalsIgnoreCase("restart")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是合并行的中间行单元格（包括结尾的最后一行的单元格）
     *
     * @param tableCell
     * @return
     */
    public boolean isContinueRow(XWPFTableCell tableCell) {
        CTTcPr tcPr = tableCell.getCTTc().getTcPr();
        if (tcPr.getVMerge() == null) {
            return false;
        }
        if (tcPr.getVMerge().getVal() == null) {
            return true;
        }
        return false;
    }

    public int getLeftWidth(XWPFTable table, int row, int col) {
        int leftWidth = 0;
        for (int i = 0; i < col; i++) {
            leftWidth += getCellWidth(table, row, i);
        }
        return leftWidth;
    }

    public int getCellWidth(XWPFTable table, int row, int col) {
        BigInteger width = table.getRow(row).getCell(col).getCTTc().getTcPr().getTcW().getW();
        return width.intValue();
    }

    /**
     * 添加忽略的单元格(被行合并的单元格，生成HTML时需要忽略)
     *
     * @param row
     * @param col
     */
    public void addOmitCell(int row, int col) {
        String omitCellStr = generateOmitCellStr(row, col);
        omitCellsList.add(omitCellStr);
    }

    public boolean isOmitCell(int row, int col) {
        String cellStr = generateOmitCellStr(row, col);
        return omitCellsList.contains(cellStr);
    }

    public List<PhysicalResult> readTable(List<XWPFTable> tables) throws IOException {
        List<PhysicalResult> physicalResults = new ArrayList<>();
        for (XWPFTable table : tables) {
            int tableRowsSize = table.getRows().size();
            for (int i = 0; i < tableRowsSize; i++) {
                if ("序 号".equals(table.getRow(i).getCell(0).getText()) || "".equals(table.getRow(i).getCell(1).getText())) {
                    continue;
                }
                PhysicalResult physicalResult = new PhysicalResult();
                physicalResult.setId(TokenUtil.getUuId());
                physicalResult.setName(table.getRow(i).getCell(1).getText());
                physicalResult.setSex(table.getRow(i).getCell(2) == null ? 0 : "男".equals(table.getRow(i).getCell(2).getText()) ? 1 : 2);
                physicalResult.setAge(Integer.valueOf(table.getRow(i).getCell(3).getText()));
                physicalResult.setWorkYear(Integer.valueOf(table.getRow(i).getCell(4).getText()));
                physicalResult.setWorkType(table.getRow(i).getCell(5).getText());
                physicalResult.setHazardFactorName(table.getRow(i).getCell(6).getText());
                physicalResult.setWarningIndex(table.getRow(i).getCell(7).getText());
                physicalResult.setConclusion(table.getRow(i).getCell(8).getText());
                physicalResult.setHandlingOpinions(table.getRow(i).getCell(9) == null ? null : table.getRow(i).getCell(9).getText());
                physicalResult.setMedicalAdvice(table.getRow(i).getCell(10) == null ? table.getRow(i).getCell(7).getText() : table.getRow(i).getCell(10).getText());
                physicalResult.setResult(i + 1);
                physicalResults.add(physicalResult);
            }
        }
        clearTableInfo();
        return physicalResults;
    }

    public List<PhysicalResult> readTable(Elements tables) throws IOException {
        List<PhysicalResult> physicalResults = new ArrayList<>();
        for (int i = 0; i < tables.size(); i++) {
            Elements rows = tables.get(i).select("tbody").select("tr");
            if (i < tables.size() - 1) {
                for (Element row : rows) {
                    if (row.select("td").size() < 11 ||
                            (!"".equals(HtmlUtil.removeHtmlTag(row.select("td").first().toString())) &&
                                    "序 号".equals(HtmlUtil.removeHtmlTag(row.select("td").first().toString())))) {
                        continue;
                    }
                    PhysicalResult physicalResult = new PhysicalResult();
                    physicalResult.setId(TokenUtil.getUuId());
                    physicalResult.setName(HtmlUtil.removeHtmlTag(row.select("td").get(1).toString()));
                    physicalResult.setSex("".equals(HtmlUtil.removeHtmlTag(row.select("td").get(2).toString())) ? 0 : "男".equals(HtmlUtil.removeHtmlTag(row.select("td").get(2).toString())) ? 1 : 2);
                    physicalResult.setAge(Integer.valueOf(HtmlUtil.removeHtmlTag(row.select("td").get(3).toString())));
                    physicalResult.setWorkYear(Integer.valueOf(HtmlUtil.removeHtmlTag(row.select("td").get(4).toString())));
                    physicalResult.setWorkType(HtmlUtil.removeHtmlTag(row.select("td").get(5).toString()));
                    physicalResult.setHazardFactorName(HtmlUtil.removeHtmlTag(row.select("td").get(6).toString()));
                    physicalResult.setWarningIndex(HtmlUtil.removeHtmlTag(row.select("td").get(7).toString()));
                    physicalResult.setConclusion(HtmlUtil.removeHtmlTag(row.select("td").get(8).toString()));
                    physicalResult.setHandlingOpinions("".equals(HtmlUtil.removeHtmlTag(row.select("td").get(9).toString())) ? null : HtmlUtil.removeHtmlTag(row.select("td").get(9).toString()));
                    physicalResult.setMedicalAdvice("".equals(HtmlUtil.removeHtmlTag(row.select("td").get(10).toString())) ? null : HtmlUtil.removeHtmlTag(row.select("td").get(10).toString()));
                    physicalResult.setResult(i + 1);
                    physicalResults.add(physicalResult);
                }
            } else {
                for (Element row : rows) {
                    if (row.select("td").size() < 9 ||
                            (!"".equals(HtmlUtil.removeHtmlTag(row.select("td").first().toString())) &&
                                    "序 号".equals(HtmlUtil.removeHtmlTag(row.select("td").first().toString())))) {
                        continue;
                    }
                    PhysicalResult physicalResult = new PhysicalResult();
                    physicalResult.setId(TokenUtil.getUuId());
                    physicalResult.setName(HtmlUtil.removeHtmlTag(row.select("td").get(1).toString()));
                    physicalResult.setSex("".equals(HtmlUtil.removeHtmlTag(row.select("td").get(2).toString())) ? 0 : "男".equals(HtmlUtil.removeHtmlTag(row.select("td").get(2).toString())) ? 1 : 2);
                    physicalResult.setAge(Integer.valueOf(HtmlUtil.removeHtmlTag(row.select("td").get(3).toString())));
                    physicalResult.setWorkYear(Integer.valueOf(HtmlUtil.removeHtmlTag(row.select("td").get(4).toString())));
                    physicalResult.setWorkType(HtmlUtil.removeHtmlTag(row.select("td").get(5).toString()));
                    physicalResult.setHazardFactorName(HtmlUtil.removeHtmlTag(row.select("td").get(6).toString()));
                    physicalResult.setWarningIndex(HtmlUtil.removeHtmlTag(row.select("td").get(7).toString()));
                    physicalResult.setConclusion(HtmlUtil.removeHtmlTag(row.select("td").get(8).toString()));
                    physicalResult.setHandlingOpinions(null);
                    physicalResult.setMedicalAdvice(HtmlUtil.removeHtmlTag(row.select("td").get(7).toString()));
                    physicalResult.setResult(i + 1);
                    physicalResults.add(physicalResult);
                }
            }
        }
        return physicalResults;
    }

    public void clearTableInfo() {
        omitCellsList.clear();
    }

    public static void main(String[] args) {
        ReadWordTable readWordTable = new ReadWordTable();

        try (FileInputStream fileInputStream = new FileInputStream("C:\\Users\\40302\\Desktop\\111.docx");
             XWPFDocument document = new XWPFDocument(fileInputStream)) {
            List<XWPFTable> tables = document.getTables();
            System.out.println(JSONObject.toJSONString(readWordTable.readTable(tables)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}