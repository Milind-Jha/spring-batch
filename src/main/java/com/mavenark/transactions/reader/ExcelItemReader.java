package com.mavenark.transactions.reader;

import com.mavenark.transactions.mapper.EmployeeFileRowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Iterator;

public class ExcelItemReader<T> implements ItemReader<T> {

    private final Resource fileResource;
    private final EmployeeFileRowMapper rowMapper;
    private Iterator<Row> rowIterator;

    public ExcelItemReader(Resource fileResource, EmployeeFileRowMapper rowMapper) throws Exception {
        this.fileResource = fileResource;
        this.rowMapper = rowMapper;
        initialize();
    }

    private void initialize() throws Exception {
        InputStream inputStream = fileResource.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        this.rowIterator = sheet.iterator();
        rowIterator.next(); // Skip Header Row
    }

    @Override
    public T read() throws Exception {
        if (rowIterator.hasNext()) {
            return (T)rowMapper.mapRow(rowIterator.next());
        }
        return null; // End of file
    }
}
