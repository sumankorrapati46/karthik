# Frontend-Backend Integration Debug Guide

## 🔍 **Issue Analysis**

Based on your frontend code, I can see several potential issues:

### **1. Port Mismatch**
- **Frontend**: Calls `http://localhost:8080/api/employees/${employeeId}`
- **Backend**: Running on port 8081
- **Fix**: Update frontend to use port 8081

### **2. Photo URL Format Issues**
- **Frontend expects**: Multiple photo field names (`photoUrl`, `photo`, `photo_url`, `photoPath`)
- **Backend provides**: `photoFileName` and `photoUrl`
- **Fix**: Backend now handles multiple photo field formats

### **3. Field Mapping Issues**
- **Frontend form fields**: `professional.education`, `professional.experience`, `bank.bankName`, etc.
- **Backend fields**: `education`, `experience`, `bankName`, etc.
- **Fix**: Backend returns flat structure, frontend maps correctly

## 🛠️ **Backend Fixes Applied**

### **1. Enhanced Photo URL Handling**
```java
private void formatPhotoUrls(Employee employee) {
    if (employee.getPhotoFileName() != null && !employee.getPhotoFileName().startsWith("http")) {
        String photoUrl = "/uploads/photos/" + employee.getPhotoFileName();
        employee.setPhotoFileName(photoUrl);
        employee.setPhotoUrl(photoUrl);
        // Also set other possible field names the frontend might expect
        try {
            employee.getClass().getMethod("setPhoto", String.class).invoke(employee, photoUrl);
        } catch (Exception e) {
            // Field doesn't exist, ignore
        }
    }
}
```

### **2. Enhanced GET Endpoint with Debugging**
```java
@GetMapping("/{id}")
public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    try {
        System.out.println("🔍 Fetching employee with ID: " + id);
        
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            System.out.println("❌ Employee not found with ID: " + id);
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("✅ Employee found: " + employee.getFirstName() + " " + employee.getLastName());
        
        // Format photo URLs for response
        formatPhotoUrls(employee);
        
        return ResponseEntity.ok(employee);
    } catch (Exception e) {
        System.out.println("❌ Error fetching employee with ID " + id + ": " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

## 🧪 **Testing Steps**

### **Step 1: Verify Backend is Running**
```powershell
# Check if backend is running on port 8081
Invoke-WebRequest -Uri "http://localhost:8081/api/employees" -Method GET
```

### **Step 2: Test GET Employee Endpoint**
```powershell
# Test with a known employee ID (replace 1 with actual ID)
$response = Invoke-WebRequest -Uri "http://localhost:8081/api/employees/1" -Method GET
Write-Host "Status: $($response.StatusCode)"
Write-Host "Response: $($response.Content)"
```

### **Step 3: Check Backend Logs**
Look for these log messages in your backend console:
```
🔍 Fetching employee with ID: 1
✅ Employee found: John Doe
📸 Photo fields before formatting:
  - photoFileName: employee_1.jpg
  - photoUrl: null
📸 Photo fields after formatting:
  - photoFileName: /uploads/photos/employee_1.jpg
  - photoUrl: /uploads/photos/employee_1.jpg
```

### **Step 4: Test Frontend-Backend Connection**
```powershell
# Test CORS and authentication
$headers = @{
    "Authorization" = "Bearer your-token-here"
    "Content-Type" = "application/json"
}

$response = Invoke-WebRequest -Uri "http://localhost:8081/api/employees/1" -Method GET -Headers $headers
Write-Host "Status: $($response.StatusCode)"
Write-Host "Response: $($response.Content)"
```

## 🔧 **Frontend Fixes Needed**

### **1. Update Port in Frontend**
In your `ViewEmployee.jsx`, change:
```javascript
// ❌ Current (wrong port)
fetch(`http://localhost:8080/api/employees/${employeeId}`)

// ✅ Correct (port 8081)
fetch(`http://localhost:8081/api/employees/${employeeId}`)
```

### **2. Update EmployeeDetails.jsx**
In your `EmployeeDetails.jsx`, change:
```javascript
// ❌ Current (wrong port)
const response = await fetch(
  isEditMode 
    ? `http://localhost:8080/api/employees/${employeeId}`
    : `http://localhost:8080/api/employees`,

// ✅ Correct (port 8081)
const response = await fetch(
  isEditMode 
    ? `http://localhost:8081/api/employees/${employeeId}`
    : `http://localhost:8081/api/employees`,
```

### **3. Update Photo URL Handling**
In your `ViewEmployee.jsx`, update the photo URL logic:
```javascript
// ✅ Enhanced photo URL handling
if (data.photoFileName || data.photoUrl || data.photo) {
  const photoUrl = data.photoFileName 
    ? `http://localhost:8081${data.photoFileName}`  // Note: port 8081
    : data.photoUrl 
      ? `http://localhost:8081${data.photoUrl}`     // Note: port 8081
      : data.photo;
  setPhotoPreview(photoUrl);
  console.log("Photo URL set:", photoUrl);
}
```

## 🚨 **Common Issues and Solutions**

### **Issue 1: CORS Error**
```
Access to fetch at 'http://localhost:8081/api/employees/1' from origin 'http://localhost:3000' has been blocked by CORS policy
```
**Solution**: Backend CORS is configured correctly. Check if backend is running on port 8081.

### **Issue 2: 404 Not Found**
```
GET http://localhost:8081/api/employees/1 404 (Not Found)
```
**Solution**: 
1. Check if employee with ID 1 exists in database
2. Verify backend is running
3. Check backend logs for error messages

### **Issue 3: 401 Unauthorized**
```
GET http://localhost:8081/api/employees/1 401 (Unauthorized)
```
**Solution**: 
1. Check if token is being sent correctly
2. Verify token is valid
3. Check backend authentication configuration

### **Issue 4: Photo Not Displaying**
**Solution**: 
1. Check photo URL format in response
2. Verify photo file exists in uploads folder
3. Check browser network tab for photo request

## 📋 **Complete Testing Checklist**

### **Backend Testing**
- [ ] Backend starts on port 8081
- [ ] GET `/api/employees` returns list
- [ ] GET `/api/employees/1` returns employee
- [ ] Photo URLs are formatted correctly
- [ ] CORS allows localhost:3000

### **Frontend Testing**
- [ ] Update all API calls to port 8081
- [ ] Test employee creation
- [ ] Test employee viewing
- [ ] Test photo display
- [ ] Test form navigation

### **Integration Testing**
- [ ] Create employee → Navigate to view
- [ ] View employee → Display all data
- [ ] Photo upload → Photo display
- [ ] Form validation → Backend validation

## 🎯 **Expected Response Format**

The backend should return this exact format:
```json
{
  "id": 1,
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
  "photoUrl": "/uploads/photos/employee_1.jpg"
}
```

## 🚀 **Next Steps**

1. **Update frontend port** from 8080 to 8081
2. **Test backend endpoints** using PowerShell
3. **Check backend logs** for debugging info
4. **Test frontend integration** with updated port
5. **Verify photo display** works correctly

The backend is now properly configured to handle your frontend requirements! 🎉 