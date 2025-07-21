# PowerShell Testing Guide for Employee API

## PowerShell Commands (Windows)

### Option 1: Using Invoke-WebRequest

```powershell
# Test Debug Endpoint
$formData = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    email = "john@example.com"
    contactNumber = "9876543210"
    altNumber = " 9966612345"
    dob = "1990-01-01"
    role = "Manager"
    accessStatus = "Active"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/employees/debug" -Method POST -Form $formData
```

### Option 2: Using curl.exe (if available)

```powershell
# Install curl if not available
# Download from: https://curl.se/windows/

# Test Debug Endpoint
curl.exe -X POST http://localhost:8080/api/employees/debug `
  -F "salutation=Mr" `
  -F "firstName=John" `
  -F "lastName=Doe" `
  -F "email=john@example.com" `
  -F "contactNumber=9876543210" `
  -F "altNumber= 9966612345" `
  -F "dob=1990-01-01" `
  -F "role=Manager" `
  -F "accessStatus=Active"
```

### Option 3: Using PowerShell with JSON

```powershell
# Test with JSON (for non-file uploads)
$body = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    gender = "Male"
    nationality = "Indian"
    dob = "1990-01-01"
    contactNumber = "9876543210"
    email = "john@example.com"
    country = "India"
    state = "Telangana"
    district = "Hyderabad"
    role = "Manager"
    accessStatus = "Active"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/employees" -Method POST -Body $body -ContentType "application/json"
```

## Complete Test Script

```powershell
# Test Employee Creation with PowerShell
Write-Host "Testing Employee Creation..." -ForegroundColor Green

# Test 1: Debug Endpoint
Write-Host "`n1. Testing Debug Endpoint..." -ForegroundColor Yellow
$debugForm = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    email = "john@example.com"
    contactNumber = "9876543210"
    altNumber = " 9966612345"
    dob = "1990-01-01"
    role = "Manager"
    accessStatus = "Active"
}

try {
    $debugResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/employees/debug" -Method POST -Form $debugForm
    Write-Host "Debug Response: $($debugResponse.Content)" -ForegroundColor Green
} catch {
    Write-Host "Debug Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test 2: Minimal Create
Write-Host "`n2. Testing Minimal Create..." -ForegroundColor Yellow
$createForm = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    gender = "Male"
    nationality = "Indian"
    dob = "1990-01-01"
    contactNumber = "9876543210"
    email = "john@example.com"
    country = "India"
    state = "Telangana"
    district = "Hyderabad"
    role = "Manager"
    accessStatus = "Active"
}

try {
    $createResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/employees" -Method POST -Form $createForm
    Write-Host "Create Response: $($createResponse.Content)" -ForegroundColor Green
} catch {
    Write-Host "Create Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Error Details: $($_.Exception.Response)" -ForegroundColor Red
}

# Test 3: Get All Employees
Write-Host "`n3. Testing Get All Employees..." -ForegroundColor Yellow
try {
    $getResponse = Invoke-WebRequest -Uri "http://localhost:8080/api/employees" -Method GET
    Write-Host "Get Response: $($getResponse.Content)" -ForegroundColor Green
} catch {
    Write-Host "Get Error: $($_.Exception.Message)" -ForegroundColor Red
}
```

## Alternative: Using Postman

If PowerShell is giving you trouble, you can use **Postman**:

1. **Download Postman**: https://www.postman.com/downloads/
2. **Create a new request**:
   - Method: `POST`
   - URL: `http://localhost:8080/api/employees`
   - Body: `form-data`
   - Add fields:
     - `salutation`: `Mr`
     - `firstName`: `John`
     - `lastName`: `Doe`
     - `gender`: `Male`
     - `nationality`: `Indian`
     - `dob`: `1990-01-01`
     - `contactNumber`: `9876543210`
     - `email`: `john@example.com`
     - `country`: `India`
     - `state`: `Telangana`
     - `district`: `Hyderabad`
     - `role`: `Manager`
     - `accessStatus`: `Active`

## Quick Test Commands

### Test if server is running:
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/employees" -Method GET
```

### Test debug endpoint:
```powershell
$form = @{ salutation = "Mr"; firstName = "John"; lastName = "Doe"; email = "test@test.com"; contactNumber = "9876543210"; dob = "1990-01-01"; role = "Manager"; accessStatus = "Active" }
Invoke-WebRequest -Uri "http://localhost:8080/api/employees/debug" -Method POST -Form $form
```

## Expected Results

### Success Response:
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

### Debug Response:
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