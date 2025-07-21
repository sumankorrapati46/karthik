# Employee API Testing Guide

## Current Issue
The frontend is getting a 400 Bad Request error when submitting employee data. This guide helps debug and test the API.

## Debug Endpoint
Use this endpoint to check what data is being sent from the frontend:

```bash
curl -X POST http://localhost:8080/api/employees/debug \
  -F "salutation=Mr" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "email=john@example.com" \
  -F "contactNumber=9876543210" \
  -F "altNumber= 9966612345" \
  -F "dob=1990-01-01" \
  -F "role=Manager" \
  -F "accessStatus=Active"
```

## Fixed Validation Issues

### 1. Made Optional Fields
- `education` - No longer required
- `experience` - No longer required  
- `bankName` - No longer required
- `accountNumber` - No longer required
- `branchName` - No longer required
- `documentType` - No longer required
- `documentNumber` - No longer required

### 2. Fixed altNumber Pattern
- Changed from: `^[6-9]\\d{9}$`
- Changed to: `^\\s*[6-9]\\d{9}\\s*$`
- Now allows leading/trailing spaces

## Test Cases

### Test 1: Minimal Required Data
```bash
curl -X POST http://localhost:8080/api/employees \
  -F "salutation=Mr" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "gender=Male" \
  -F "nationality=Indian" \
  -F "dob=1990-01-01" \
  -F "contactNumber=9876543210" \
  -F "email=john@example.com" \
  -F "country=India" \
  -F "state=Telangana" \
  -F "district=Hyderabad" \
  -F "role=Manager" \
  -F "accessStatus=Active"
```

### Test 2: With Optional Fields
```bash
curl -X POST http://localhost:8080/api/employees \
  -F "salutation=Mr" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "gender=Male" \
  -F "nationality=Indian" \
  -F "dob=1990-01-01" \
  -F "contactNumber=9876543210" \
  -F "email=john@example.com" \
  -F "altNumber= 9966612345" \
  -F "country=India" \
  -F "state=Telangana" \
  -F "district=Hyderabad" \
  -F "education=Bachelor" \
  -F "experience=5 years" \
  -F "bankName=SBI" \
  -F "accountNumber=1234567890" \
  -F "branchName=Main Branch" \
  -F "ifscCode=SBIN0001234" \
  -F "documentType=Aadhar" \
  -F "documentNumber=123456789012" \
  -F "role=Manager" \
  -F "accessStatus=Active"
```

### Test 3: With Files
```bash
curl -X POST http://localhost:8080/api/employees \
  -F "salutation=Mr" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "gender=Male" \
  -F "nationality=Indian" \
  -F "dob=1990-01-01" \
  -F "contactNumber=9876543210" \
  -F "email=john@example.com" \
  -F "country=India" \
  -F "state=Telangana" \
  -F "district=Hyderabad" \
  -F "role=Manager" \
  -F "accessStatus=Active" \
  -F "photo=@photo.jpg" \
  -F "passbook=@passbook.pdf" \
  -F "documentFile=@document.pdf"
```

## Expected Responses

### Success Response
```json
{
  "message": "Employee created successfully",
  "employee": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "photoFileName": "/uploads/photos/1704067200000.jpg",
    "passbookFileName": "/uploads/passbooks/1704067200001.pdf",
    "documentFileName": "/uploads/documents/1704067200002.pdf"
  }
}
```

### Validation Error Response
```json
{
  "error": "Validation Error",
  "message": "Please check the form data and try again",
  "details": [
    "contactNumber: Enter a valid 10-digit mobile number",
    "email: Enter a valid email"
  ]
}
```

## Frontend Testing

### 1. Check Network Tab
- Open browser DevTools
- Go to Network tab
- Submit the form
- Check the request payload

### 2. Test Debug Endpoint
- Change the frontend to call `/api/employees/debug` instead of `/api/employees`
- This will show exactly what data is being sent

### 3. Common Issues
- **Date format**: Must be `YYYY-MM-DD`
- **Phone numbers**: Must be 10 digits starting with 6-9
- **Email**: Must be valid email format
- **Required fields**: salutation, firstName, lastName, gender, nationality, dob, contactNumber, email, country, state, district, role, accessStatus

## Next Steps
1. Test the debug endpoint to see what data is being sent
2. Fix any validation issues in the frontend
3. Test with the actual create endpoint
4. Check file upload functionality

## Logs to Monitor
- Application logs for validation errors
- Hibernate SQL logs for database queries
- Network tab in browser for request/response details 