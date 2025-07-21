# Employee Management API Improvements

## Overview
This document outlines the improvements made to the Employee Management API to ensure proper file upload handling, consistent date formatting, and robust error handling.

## Key Improvements

### 1. Photo URL Formatting
- **Issue**: Photo URLs were not consistently formatted
- **Solution**: Added `formatPhotoUrls()` method in `EmployeeController`
- **Implementation**: 
  ```java
  private void formatPhotoUrls(Employee employee) {
      if (employee.getPhotoFileName() != null && !employee.getPhotoFileName().startsWith("http")) {
          employee.setPhotoFileName("/uploads/photos/" + employee.getPhotoFileName());
      }
      // Similar for passbook and document files
  }
  ```

### 2. File Upload Handling
- **Enhanced FileStorageService** with:
  - File type validation
  - Proper file extension handling
  - Better error messages
  - File existence checking
  - URL generation utilities

- **File Type Validation**:
  - Images: JPEG, JPG, PNG, GIF
  - Documents: PDF, DOC, DOCX, JPEG, JPG, PNG

### 3. Date Format Consistency
- **Employee Entity**: Added `@JsonFormat(pattern = "yyyy-MM-dd")`
- **EmployeeDTO**: Added `@JsonFormat(pattern = "yyyy-MM-dd")`
- **Application Properties**: Added Jackson date formatting configuration

### 4. Error Handling
- **Global Exception Handler**: Created `GlobalExceptionHandler` with:
  - Generic exception handling
  - File upload error handling
  - Multipart exception handling
  - Runtime exception handling

- **Consistent Error Responses**: All endpoints now return structured error responses:
  ```json
  {
    "error": "Error Type",
    "message": "Detailed error message",
    "timestamp": "2024-01-01T12:00:00",
    "status": 400
  }
  ```

### 5. CORS Configuration
- **Enhanced SecurityConfig** with:
  - Additional allowed headers
  - Exposed headers
  - Max age configuration
  - Better file upload support

### 6. File Storage Configuration
- **Application Properties**:
  - File size limits (10MB)
  - Multipart configuration
  - Temporary file location
  - Jackson date formatting

### 7. New File Management Endpoints
- **FileUploadController** provides:
  - `/api/files/upload/{directory}` - Upload files
  - `/api/files/download/{directory}/{fileName}` - Download files
  - `/api/files/view/{directory}/{fileName}` - View files
  - `/api/files/{directory}/{fileName}` - Delete files
  - `/api/files/exists/{directory}/{fileName}` - Check file existence

## API Endpoints

### Employee Management
- `POST /api/employees` - Create employee with file upload
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee

### File Management
- `POST /api/files/upload/{directory}` - Upload file to specific directory
- `GET /api/files/download/{directory}/{fileName}` - Download file
- `GET /api/files/view/{directory}/{fileName}` - View file
- `DELETE /api/files/{directory}/{fileName}` - Delete file
- `GET /api/files/exists/{directory}/{fileName}` - Check if file exists

## File Upload Directories
- `/uploads/photos/` - Employee photos
- `/uploads/passbooks/` - Bank passbooks
- `/uploads/documents/` - Employee documents

## Error Handling Examples

### File Upload Error
```json
{
  "error": "File Upload Error",
  "message": "Invalid image file type. Allowed types: JPEG, JPG, PNG, GIF",
  "timestamp": "2024-01-01T12:00:00",
  "status": 400
}
```

### Employee Not Found
```json
{
  "error": "Employee not found",
  "message": "Employee with ID 123 does not exist"
}
```

## Configuration

### Application Properties
```properties
# File upload configuration
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
spring.servlet.multipart.enabled=true
file.upload-dir=./uploads

# Date formatting
spring.jackson.date-format=yyyy-MM-dd
spring.jackson.time-zone=UTC
```

### CORS Configuration
```java
config.setAllowedOrigins(List.of("http://localhost:3000"));
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
config.setAllowCredentials(true);
```

## Testing

### Create Employee with Files
```bash
curl -X POST http://localhost:8080/api/employees \
  -F "photo=@photo.jpg" \
  -F "passbook=@passbook.pdf" \
  -F "documentFile=@document.pdf" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "email=john@example.com" \
  -F "dob=1990-01-01"
```

### Get Employee with Formatted URLs
```bash
curl http://localhost:8080/api/employees/1
```

Response will include properly formatted file URLs:
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "photoFileName": "/uploads/photos/1704067200000.jpg",
  "passbookFileName": "/uploads/passbooks/1704067200001.pdf",
  "documentFileName": "/uploads/documents/1704067200002.pdf"
}
```

## Security Considerations
- File type validation prevents malicious uploads
- File size limits prevent abuse
- Proper error handling prevents information leakage
- CORS configuration restricts cross-origin requests appropriately

## Future Enhancements
- Add file compression for images
- Implement file cleanup for unused files
- Add file metadata storage
- Implement file versioning
- Add file access logging 