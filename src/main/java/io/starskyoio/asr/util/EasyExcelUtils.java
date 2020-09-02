package io.starskyoio.asr.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.WriteHandler;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import org.apache.poi.ss.usermodel.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class EasyExcelUtils {
    public static void write(String outFile, List<? extends BaseRowModel> data, Class<? extends BaseRowModel> outClass) {
        try (OutputStream out = new FileOutputStream(outFile)) {
            ExcelWriter writer = EasyExcelFactory.getWriterWithTempAndHandler(null, out, ExcelTypeEnum.XLSX, true, new WriteHandler() {
                private Workbook workbook;

                @Override
                public void sheet(int i, org.apache.poi.ss.usermodel.Sheet sheet) {
                    workbook = sheet.getWorkbook();
                }

                @Override
                public void row(int i, Row row) {

                }

                @Override
                public void cell(int i, Cell cell) {
                    CellStyle cellStyle = workbook.createCellStyle();
                    DataFormat dataFormat = workbook.createDataFormat();
                    cellStyle.setDataFormat(dataFormat.getFormat("@"));
                    cell.setCellStyle(cellStyle);
                }
            });
            Sheet sheet = new Sheet(1, 0, outClass);
            writer.write(data, sheet);
            writer.finish();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
