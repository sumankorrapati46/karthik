# Test GET Employee Endpoint

## Current Status
✅ The GET `/api/employees/{employeeId}` endpoint is working correctly based on the logs.

## Test the Endpoint

### 1. Test with curl
```bash
curl -X GET http://localhost:8080/api/employees/2 \
  -H "Content-Type: application/json"
```

### 2. Expected Response Format
```json
{
  "id": 2,
  "salutation": "Mrs.",
  "firstName": "lakshmi",
  "middleName": "tinny",
  "lastName": "pussy",
  "gender": "Female",
  "dob": "1990-05-15",
  "nationality": "Indian",
  "contactNumber": "9876543210",
  "email": "john.doe@example.com",
  "relationType": "so",
  "relationName": "John Doe Sr",
  "altNumber": "9876543211",
  "altNumberType": "Father",
  "country": "India",
  "state": "Telangana",
  "district": "Hyderabad",
  "block": "Madhapur",
  "village": "Village A",
  "zipcode": "500081",
  "sector": "Technology",
  "education": "Bachelor's Degree",
  "experience": "5",
  "bankName": "HDFC Bank",
  "accountNumber": "1234567890",
  "branchName": "Madhapur",
  "ifscCode": "HDFC0001234",
  "documentType": "aadharNumber",
  "documentNumber": "123456789012",
  "role": "manager",
  "accessStatus": "active",
  "photoFileName": "/uploads/photos/employee_2.jpg",
  "photoUrl": "/uploads/photos/employee_2.jpg",
  "passbookFileName": "/uploads/passbooks/employee_2_passbook.pdf",
  "documentFileName": "/uploads/documents/employee_2_document.pdf"
}
```

## Logs Analysis
From the logs, we can see:
- ✅ Request reaches the controller: `GET "/api/employees/2"`
- ✅ Database query executes: `select e1_0.id,e1_0.access_status... from employee e1_0 where e1_0.id=?`
- ✅ Response is written: `Writing [Employee(id=2, salutation=Mrs., firstName=lakshmi...)]`
- ✅ Status 200 OK: `Completed 200 OK`

## Issues to Check

### 1. Frontend Issue
If the frontend is not receiving the data, check:
- CORS configuration
- Authentication headers
- Network tab in browser DevTools

### 2. Response Format
The response now includes both:
- `photoFileName`: For internal use
- `photoUrl`: For frontend compatibility

### 3. Database Schema
Make sure the database table matches the entity:
```sql
-- Check if employee with ID 2 exists
SELECT * FROM employee WHERE id = 2;

-- Check table structure
DESCRIBE employee;
```

## Debug Steps

### 1. Test with Postman
```http
GET http://localhost:8080/api/employees/2
Content-Type: application/json
```

### 2. Check Browser Network Tab
- Open DevTools
- Go to Network tab
- Make the request
- Check response headers and body

### 3. Add Debug Logging
```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    System.out.println("Fetching employee with ID: " + id);
    Employee employee = employeeService.getEmployeeById(id);
    System.out.println("Found employee: " + employee);
    if (employee == null) {
        return ResponseEntity.notFound().build();
    }
    formatPhotoUrls(employee);
    System.out.println("Formatted employee: " + employee);
    return ResponseEntity.ok(employee);
}
```

## Common Issues

### 1. CORS Issues
If you see CORS errors in browser console:
```java
// In SecurityConfig
config.setAllowedOrigins(List.of("http://localhost:3000"));
config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
```

### 2. Authentication Issues
If you see 401/403 errors:
- Check JWT token
- Verify authentication configuration

### 3. Database Issues
If you see 500 errors:
- Check database connection
- Verify employee exists with ID 2
- Check Hibernate logs

## Next Steps
1. Test the endpoint with curl
2. Check browser network tab
3. Verify response format matches frontend expectations
4. Add any missing fields to the response 