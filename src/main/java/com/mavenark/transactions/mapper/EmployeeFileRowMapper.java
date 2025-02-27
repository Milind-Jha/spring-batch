package com.mavenark.transactions.mapper;

import com.mavenark.transactions.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class EmployeeFileRowMapper {

    public EmployeeDTO mapRow(Row row) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        if (row.getCell(0) != null) {
            employeeDTO.setId(UUID.randomUUID().toString());
            employeeDTO.setFirstName(extractStringData(row,1));
            employeeDTO.setLastName(extractStringData(row,2));
            employeeDTO.setEmail(extractStringData(row,3));
            if(employeeDTO.getFirstName()==null || employeeDTO.getLastName()==null || employeeDTO.getEmail()==null)
                return null;
            int age = extractNumericData(row,4);
            employeeDTO.setAge(age);
        }
        return employeeDTO;
    }

    private int extractNumericData(Row row,int cell) {
        if(row.getCell(cell) == null) {
            log.info("cell value is null");
            return 0;
        }
        switch (row.getCell(cell).getCellType()) {
            case NUMERIC:
                return (int) row.getCell(cell).getNumericCellValue();
            case STRING:
                try {
                    String ageStr = row.getCell(cell).getStringCellValue();
                    return Integer.parseInt(ageStr); // Try to parse as integer
                } catch (NumberFormatException e) {
                    return 0; // Default value if parsing fails
                }
            default:
                log.info("cell type encountered : {}",row.getCell(cell).getCellType().toString());
                return 0; // Default value for unexpected cell types
        }
    }

    private String extractStringData(Row row,int cell) {
        if(row.getCell(cell) == null) {
            log.info("cell value is null in place of string");
            return null;
        }

        return switch (row.getCell(cell).getCellType()) {
            case NUMERIC -> String.valueOf(row.getCell(cell).getNumericCellValue());
            case STRING, BOOLEAN, ERROR ->  row.getCell(cell).getStringCellValue();
            default -> defaultSituation(row,cell);
        };
    }
    private String defaultSituation(Row row, int cell){
        log.info("cell type : {}",row.getCell(cell).getCellType().toString());
        return null;
    }
}
