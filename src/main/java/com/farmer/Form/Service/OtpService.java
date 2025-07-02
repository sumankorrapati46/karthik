package com.farmer.Form.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
 
import java.util.Map;
import java.util.Set;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
 
@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
 
    private final FirebaseAuth firebaseAuth;
 
    // OTP store: emailOrPhone -> otp
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
 
    // Verified email/phones
    private final Set<String> verifiedUsers = ConcurrentHashMap.newKeySet();
 
    // ✅ Firebase token verification
    public boolean verifyOtp(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            log.info("Firebase token verified for user: {}", decodedToken.getEmail());
            return decodedToken != null;
        } catch (FirebaseAuthException e) {
            log.warn("Firebase token verification failed: {}", e.getMessage());
            return false;
        }
    }
 
    // ✅ Manual OTP verification
    public boolean verifyOtp(String emailOrPhone, String otp) {
        String storedOtp = otpStore.get(emailOrPhone);
        boolean isValid = storedOtp != null && storedOtp.equals(otp);
 
        log.info("Verifying OTP for {}: provided={}, expected={}, result={}",
                emailOrPhone, otp, storedOtp, isValid);
 
        if (isValid) {
            otpStore.remove(emailOrPhone); // OTP is single-use
            verifiedUsers.add(emailOrPhone); // ✅ Mark as verified
            log.info("OTP verified and added to verified set for {}", emailOrPhone);
        }
 
        return isValid;
    }
 
    // ✅ Check if email/phone is verified
    public boolean isVerified(String emailOrPhone) {
        return verifiedUsers.contains(emailOrPhone);
    }
 
    // ✅ Clear verification (after registration)
    public void clearVerification(String emailOrPhone) {
        verifiedUsers.remove(emailOrPhone);
        log.info("Cleared verification state for {}", emailOrPhone);
    }
 
    // ✅ Extract email from Firebase token
    public String getUserEmailOrPhoneFromToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String emailOrPhone = decodedToken != null ? decodedToken.getEmail() : null;
            log.info("Extracted user identifier from token: {}", emailOrPhone);
            return emailOrPhone;
        } catch (FirebaseAuthException e) {
            log.error("Failed to extract user identifier from token: {}", e.getMessage());
            return null;
        }
    }
 
    // ✅ Generate + send OTP (email/SMS)
    public String generateAndSendOtp(String emailOrPhone) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(emailOrPhone, otp);
 
        boolean isEmail = emailOrPhone.contains("@");
        log.info("Generated OTP for {}: {}", isEmail ? "email" : "phone", otp);
 
        if (isEmail) {
            sendOtpEmail(emailOrPhone, otp);
        } else {
            sendOtpSms(emailOrPhone, otp);
        }
 
        return otp;
    }
 
    // ✅ Simulate email OTP sending
    private void sendOtpEmail(String email, String otp) {
        log.info("OTP email sent to {}: {}", email, otp);
        // Integrate JavaMailSender here
    }
 
    // ✅ Simulate SMS OTP sending
    private void sendOtpSms(String phoneNumber, String otp) {
        log.info("OTP SMS sent to {}: {}", phoneNumber, otp);
        // Integrate Twilio or Firebase SMS here
    }
 
    // ✅ Optional alias
    public boolean verifyOtpCode(String phoneNumber, String otp) {
        return verifyOtp(phoneNumber, otp);
    }
}
 
