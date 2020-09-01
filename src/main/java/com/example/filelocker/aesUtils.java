package com.example.filelocker;
import android.os.Build;
import androidx.annotation.RequiresApi;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.HashMap;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
public class aesUtils
{
    @RequiresApi(api = Build.VERSION_CODES.O)
    public SecretKeySpec generateSecretkeySpec(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        byte[] sb = Base64.getDecoder().decode(salt);
        final PBEKeySpec key = new PBEKeySpec(password.toCharArray(), sb, 12000, 256);
        final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        final byte[] kb = skf.generateSecret(key).getEncoded();
        return new SecretKeySpec(kb, "AES");
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public IvParameterSpec generateIvSpec(String iv)
    {
        byte[] i = Base64.getDecoder().decode(iv.getBytes());
        return new IvParameterSpec(i);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public HashMap<String,String> encrypt(String ... arg) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        SecureRandom rand = new SecureRandom();
        byte[] s = new byte[256];
        rand.nextBytes(s);
        final String salt = Base64.getEncoder().encodeToString(s);
        final String password = arg[0];
        final SecretKeySpec keySpec = generateSecretkeySpec(password,salt);
        rand = new SecureRandom();
        byte[] i = new byte[16];
        rand.nextBytes(i);
        final String iv = Base64.getEncoder().withoutPadding().encodeToString(i);
        final IvParameterSpec ivSpec = generateIvSpec(iv);
        final String message=arg[1];
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        final byte[] emsg = cipher.doFinal(message.getBytes());
        final String emessage = Base64.getEncoder().encodeToString(emsg);
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("salt", salt);map.put("iv",iv);map.put("message", emessage);
        return map;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String decryt(String p,String s,String i,String m) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        //final String password = map.get("password");
        final String password = p;
        //final String salt = map.get("salt");
        final String salt = s;
        //final String iv = map.get("iv");
        final String iv = i;
        SecretKeySpec keySpec = generateSecretkeySpec(password, salt);
        IvParameterSpec ivSpec = generateIvSpec(iv);
        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] msg = Base64.getDecoder().decode(m.getBytes());
        final String message = new String(cipher.doFinal(msg));
        return message;
    }
}