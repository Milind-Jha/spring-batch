package com.mavenark.transactions.reader;

import com.mavenark.transactions.mapper.EmployeeFileRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Iterator;

@Slf4j
public class ExcelItemReader<T> implements ItemReader<T> {

    private final Resource resource;
    private final EmployeeFileRowMapper rowMapper;
    private Iterator<Row> rowIterator;

    public ExcelItemReader(Resource resource, EmployeeFileRowMapper rowMapper) throws Exception {
        this.resource = resource;
        this.rowMapper = rowMapper;
        initializeReader();
    }

    private void initializeReader() throws Exception {
        InputStream inputStream = resource.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);  // Assuming first sheet
        this.rowIterator = sheet.iterator();

        // Skip header row if it exists
        if (rowIterator.hasNext()) {
            rowIterator.next();  // Skip header row
        }

        log.info("Initialized reader with file: {}", resource.getFilename());
    }

    @Override
    public T read() {
        if (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Check if the row contains enough cells and if they are non-null
            if (row.getCell(0) != null && row.getCell(1) != null && row.getCell(2) != null) {
                log.info("Reading valid row: {} {} {} {}", row.getRowNum(),row.getCell(0)
                        ,row.getCell(1),row.getCell(2));
                return (T) rowMapper.mapRow(row);
            } else {
                // If row is invalid (empty cells), log and skip
                log.warn("Skipping invalid row: {}", row.getRowNum());
            }
        }
        return null;
    }
}
