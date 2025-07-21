# Debug Employee Creation Issue

## Current Problem
The frontend is getting a 400 Bad Request error with validation failure for `altNumber` field.

## Root Cause
The `altNumber` field was still being validated even though it should be optional. I've now removed the validation pattern.

## Changes Made

### 1. Removed altNumber Validation
```java
// Before (causing validation errors)
@Pattern(regexp = "^\\s*[6-9]\\d{9}\\s*$", message = "Enter a valid 10-digit alternate number")
private String altNumber;

// After (completely optional)
private String altNumber;
```

### 2. Added Debug Logging
- Added detailed console logging in `createEmployee` method
- Enhanced debug endpoint with more information
- Added validation error logging

## Testing Steps

### Step 1: Test Debug Endpoint
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

### Step 2: Test Minimal Create
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

### Step 3: Test with altNumber
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
  -F "role=Manager" \
  -F "accessStatus=Active"
```

## Expected Results

### Debug Endpoint Response
```json
{
  "message": "Form data received",
  "salutation": "Mr",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "contactNumber": "9876543210",
  "altNumber": " 9966612345",
  "altNumberLength": 11,
  "altNumberTrimmed": "9966612345",
  "dob": "1990-01-01",
  "role": "Manager",
  "accessStatus": "Active"
}
```

### Success Response
```json
{
  "message": "Employee created successfully",
  "employee": {
    "id": 3,
    "firstName": "John",
    "lastName": "Doe",
    "photoUrl": "/uploads/photos/1704067200000.jpg"
  }
}
```

## Console Logs to Watch

### Debug Logs
```
=== Employee Creation Debug ===
Salutation: Mr
FirstName: John
LastName: Doe
Email: john@example.com
ContactNumber: 9876543210
AltNumber: ' 9966612345'
Role: Manager
AccessStatus: Active
DOB: 1990-01-01
================================
```

### Validation Error Logs (if any)
```
=== Validation Errors ===
fieldName: error message
========================
```

## Frontend Testing

### 1. Test Debug Endpoint First
Change your frontend to call `/api/employees/debug` instead of `/api/employees` to see what data is being sent.

### 2. Check Network Tab
- Open browser DevTools
- Go to Network tab
- Submit the form
- Check the request payload and response

### 3. Common Frontend Issues
- **Date format**: Must be `YYYY-MM-DD`
- **Phone numbers**: Must be 10 digits starting with 6-9
- **Email**: Must be valid email format
- **Required fields**: salutation, firstName, lastName, gender, nationality, dob, contactNumber, email, country, state, district, role, accessStatus

## Next Steps

1. **Test the debug endpoint** to see what data is being sent
2. **Test the minimal create endpoint** without altNumber
3. **Test with altNumber** to ensure it's now optional
4. **Check frontend form** to ensure all required fields are being sent
5. **Monitor console logs** for detailed debugging information

## If Still Failing

### Check These:
1. **Database connection** - Ensure database is running
2. **File upload directory** - Ensure uploads directory exists
3. **Authentication** - Check if JWT token is valid
4. **CORS** - Ensure frontend origin is allowed
5. **Required fields** - Make sure all required fields are provided

### Debug Commands:
```bash
# Check if server is running
curl http://localhost:8080/api/employees

# Check database connection
curl http://localhost:8080/api/employees/1

# Test file upload directory
ls -la uploads/
``` 