package com.farmer.Form.Service;
 
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
 
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
 
/**
 * ✅ OTP Service for email verification.
 * Features:
 * - OTP: 6-digit numeric code
 * - Expiry: 10 minutes
 * - Cooldown: 30 seconds between requests
 * - Auto-cleanup after verification
 */
@Service
@Slf4j
public class OtpService {
 
    // ───── Configuration ─────
    private static final long OTP_EXPIRY_MS      = 10 * 60 * 1_000; // 10 minutes
    private static final long RESEND_COOLDOWN_MS = 30 * 1_000;      // 30 seconds
 
    // ───── Internal Store ─────
    private record OtpEntry(String otp, long issuedAt) {}
 
    private final Map<String, OtpEntry> otpStore = new ConcurrentHashMap<>();
    private final Set<String> verifiedEmails = ConcurrentHashMap.newKeySet();
 
    // ───── Public API ─────
 
    /**
     * ✅ Alias method to maintain backward compatibility
     */
    public String generateOtp(String rawEmail) {
        return generateAndSendOtp(rawEmail);
    }
 
    /**
     * Generates or re-sends OTP for the given email.
     * Throws error if cooldown hasn't passed.
     */
    public String generateAndSendOtp(String rawEmail) {
        if (rawEmail == null || rawEmail.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be empty.");
 
        String email = normalize(rawEmail);
        long now = Instant.now().toEpochMilli();
 
        // Enforce resend cooldown
        OtpEntry existing = otpStore.get(email);
        if (existing != null && (now - existing.issuedAt) < RESEND_COOLDOWN_MS) {
            long wait = (RESEND_COOLDOWN_MS - (now - existing.issuedAt)) / 1_000;
            throw new IllegalStateException("⏳ Please wait " + wait + "s before requesting a new OTP.");
        }
 
        // Generate fresh OTP
        String otp = String.format("%06d", ThreadLocalRandom.current().nextInt(0, 1_000_000));
        otpStore.put(email, new OtpEntry(otp, now));
        sendOtpEmail(email, otp);
 
        log.info("🔐 OTP [{}] issued for {}", otp, email);
        return otp;
    }
 
    /**
     * Verifies OTP and marks email as verified.
     */
    public boolean verifyOtp(String rawEmail, String otp) {
        if (rawEmail == null || otp == null)
            return false;
 
        String email = normalize(rawEmail);
        OtpEntry entry = otpStore.get(email);
        long now = Instant.now().toEpochMilli();
 
        boolean valid = entry != null
                     && entry.otp().equals(otp)
                     && (now - entry.issuedAt) < OTP_EXPIRY_MS;
 
        if (valid) {
            otpStore.remove(email);            // One-time use
            verifiedEmails.add(email);         // Mark as verified
            log.info("✅ OTP verified for {}", email);
        } else {
            log.warn("❌ OTP verification failed for {}", email);
        }
 
        return valid;
    }
 
    /**
     * Checks if email has already been OTP-verified.
     */
    public boolean isEmailOtpVerified(String rawEmail) {
        return verifiedEmails.contains(normalize(rawEmail));
    }
 
    /**
     * Clear verification once user is registered.
     */
    public void clearEmailVerification(String rawEmail) {
        verifiedEmails.remove(normalize(rawEmail));
        otpStore.remove(normalize(rawEmail));
    }
 
    // ───── Helpers ─────
 
    private String normalize(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
 
    /**
     * Stub: Replace this with actual email service (e.g., SendGrid, SMTP).
     */
    private void sendOtpEmail(String to, String otp) {
        log.info("📧 (stub) Sending OTP email to '{}': {}", to, otp);
    }
}
 
 