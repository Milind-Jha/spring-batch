package com.mavenark.transactions.mapper;

import com.mavenark.transactions.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Row;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EmployeeFileRowMapper {

    public EmployeeDTO mapRow(Row row) {
        EmployeeDTO employee = new EmployeeDTO();
        if (row.getCell(0) != null) {
            employee.setEmployeeId(row.getCell(0) == null ? UUID.randomUUID().toString() : row.getCell(0).getStringCellValue());
            employee.setFirstName(row.getCell(1) == null ? "" : row.getCell(1).getStringCellValue());
            employee.setLastName(row.getCell(2) == null ? "" : row.getCell(2).getStringCellValue());
            employee.setEmail(row.getCell(3) == null ? "" : row.getCell(3).getStringCellValue());

            // Handling cell type and conversion for age (column 4)
            if (row.getCell(4) != null) {
                switch (row.getCell(4).getCellType()) {
                    case NUMERIC:
                        employee.setAge((int) row.getCell(4).getNumericCellValue());
                        break;
                    case STRING:
                        try {
                            String ageStr = row.getCell(4).getStringCellValue();
                            employee.setAge(Integer.parseInt(ageStr)); // Try to parse as integer
                        } catch (NumberFormatException e) {
                            employee.setAge(0); // Default value if parsing fails
                        }
                        break;
                    default:
                        employee.setAge(0); // Default value for unexpected cell types
                }
            }
        }
        return employee;
    }

}
