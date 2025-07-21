# Backend Response Format Verification

## Expected Response Format

The backend should return this exact format for employee creation:

```json
{
  "id": 3,
  "salutation": "Mr",
  "firstName": "John",
  "middleName": "Michael", 
  "lastName": "Doe",
  "gender": "Male",
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
  "photoUrl": "/uploads/photos/employee_3.jpg"
}
```

## Testing Commands

### PowerShell Test (Port 8081)

```powershell
# Test 1: Debug endpoint to see what data is being sent
$debugForm = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    gender = "Male"
    nationality = "Indian"
    dob = "1990-05-15"
    contactNumber = "9876543210"
    email = "john.doe@example.com"
    relationType = "so"
    relationName = "John Doe Sr"
    altNumber = "9876543211"
    altNumberType = "Father"
    country = "India"
    state = "Telangana"
    district = "Hyderabad"
    block = "Madhapur"
    village = "Village A"
    zipcode = "500081"
    sector = "Technology"
    education = "Bachelor's Degree"
    experience = "5"
    bankName = "HDFC Bank"
    accountNumber = "1234567890"
    branchName = "Madhapur"
    ifscCode = "HDFC0001234"
    documentType = "aadharNumber"
    documentNumber = "123456789012"
    role = "manager"
    accessStatus = "active"
}

# Test debug endpoint
$debugResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/employees/debug" -Method POST -Form $debugForm
Write-Host "Debug Response: $($debugResponse.Content)"

# Test actual create endpoint
$createResponse = Invoke-WebRequest -Uri "http://localhost:8081/api/employees" -Method POST -Form $debugForm
Write-Host "Create Response: $($createResponse.Content)"
```

### curl Test (if available)

```bash
curl -X POST http://localhost:8081/api/employees \
  -F "salutation=Mr" \
  -F "firstName=John" \
  -F "lastName=Doe" \
  -F "gender=Male" \
  -F "nationality=Indian" \
  -F "dob=1990-05-15" \
  -F "contactNumber=9876543210" \
  -F "email=john.doe@example.com" \
  -F "relationType=so" \
  -F "relationName=John Doe Sr" \
  -F "altNumber=9876543211" \
  -F "altNumberType=Father" \
  -F "country=India" \
  -F "state=Telangana" \
  -F "district=Hyderabad" \
  -F "block=Madhapur" \
  -F "village=Village A" \
  -F "zipcode=500081" \
  -F "sector=Technology" \
  -F "education=Bachelor's Degree" \
  -F "experience=5" \
  -F "bankName=HDFC Bank" \
  -F "accountNumber=1234567890" \
  -F "branchName=Madhapur" \
  -F "ifscCode=HDFC0001234" \
  -F "documentType=aadharNumber" \
  -F "documentNumber=123456789012" \
  -F "role=manager" \
  -F "accessStatus=active"
```

## Backend Changes Made

### 1. Updated Employee Entity
- ✅ Added `photoUrl` field for frontend compatibility
- ✅ Added `@JsonFormat(pattern = "yyyy-MM-dd")` for date formatting
- ✅ All required fields are present

### 2. Updated EmployeeController
- ✅ Modified `createEmployee` to return the employee object directly
- ✅ Updated `formatPhotoUrls` to set both `photoFileName` and `photoUrl`
- ✅ Added comprehensive debug logging

### 3. Updated EmployeeDTO
- ✅ Removed validation from `altNumber` (now optional)
- ✅ Added proper date formatting annotation
- ✅ Made optional fields truly optional

## Verification Steps

### Step 1: Test Debug Endpoint
```powershell
$form = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    email = "test@test.com"
    contactNumber = "9876543210"
    dob = "1990-05-15"
    role = "manager"
    accessStatus = "active"
}

Invoke-WebRequest -Uri "http://localhost:8081/api/employees/debug" -Method POST -Form $form
```

### Step 2: Test Create Endpoint
```powershell
$createForm = @{
    salutation = "Mr"
    firstName = "John"
    lastName = "Doe"
    gender = "Male"
    nationality = "Indian"
    dob = "1990-05-15"
    contactNumber = "9876543210"
    email = "john.doe@example.com"
    country = "India"
    state = "Telangana"
    district = "Hyderabad"
    role = "manager"
    accessStatus = "active"
}

Invoke-WebRequest -Uri "http://localhost:8081/api/employees" -Method POST -Form $createForm
```

### Step 3: Verify Response Format
The response should contain:
- ✅ `id` field with numeric value
- ✅ All required fields populated
- ✅ `photoUrl` field (if photo uploaded)
- ✅ Proper date format (`yyyy-MM-dd`)
- ✅ No validation errors

## Expected Console Logs

When you submit the form, you should see:

```
=== Employee Creation Debug ===
Salutation: Mr
FirstName: John
LastName: Doe
Email: john.doe@example.com
ContactNumber: 9876543210
AltNumber: ' 9966612345'
Role: manager
AccessStatus: active
DOB: 1990-05-15
================================
```

## Frontend Integration

Once the backend returns the correct format, your frontend should:

1. ✅ Receive the response with `id` field
2. ✅ Navigate to `/view-employee/[id]`
3. ✅ Display all employee data correctly
4. ✅ Show photo if uploaded
5. ✅ Handle all field names properly

## Troubleshooting

### If response doesn't match expected format:
1. Check console logs for validation errors
2. Verify all required fields are being sent
3. Check if file uploads are working
4. Ensure database connection is stable

### If frontend still has issues:
1. Check browser network tab for actual response
2. Verify the `id` field is present in response
3. Check if photo URL is being set correctly
4. Ensure all field names match between frontend and backend 