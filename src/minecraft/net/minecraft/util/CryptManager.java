package net.minecraft.util;

import cpw.mods.fml.common.asm.ReobfuscationMarker;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.modes.CFBBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@ReobfuscationMarker
public class CryptManager
{
    @SideOnly(Side.CLIENT)

    /**
     * Generate a new shared secret AES key from a secure random source
     */
    public static SecretKey createNewSharedKey()
    {
        CipherKeyGenerator cipherkeygenerator = new CipherKeyGenerator();
        cipherkeygenerator.init(new KeyGenerationParameters(new SecureRandom(), 128));
        return new SecretKeySpec(cipherkeygenerator.generateKey(), "AES");
    }

    public static KeyPair createNewKeyPair()
    {
        try
        {
            KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
            keypairgenerator.initialize(1024);
            return keypairgenerator.generateKeyPair();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            System.err.println("Key pair generation failed!");
            return null;
        }
    }

    /**
     * Compute a serverId hash for use by sendSessionRequest()
     */
    public static byte[] getServerIdHash(String par0Str, PublicKey par1PublicKey, SecretKey par2SecretKey)
    {
        try
        {
            return digestOperation("SHA-1", new byte[][] {par0Str.getBytes("ISO_8859_1"), par2SecretKey.getEncoded(), par1PublicKey.getEncoded()});
        }
        catch (UnsupportedEncodingException unsupportedencodingexception)
        {
            unsupportedencodingexception.printStackTrace();
            return null;
        }
    }

    /**
     * Compute a message digest on arbitrary byte[] data
     */
    private static byte[] digestOperation(String par0Str, byte[] ... par1ArrayOfByte)
    {
        try
        {
            MessageDigest messagedigest = MessageDigest.getInstance(par0Str);
            byte[][] abyte = par1ArrayOfByte;
            int i = par1ArrayOfByte.length;

            for (int j = 0; j < i; ++j)
            {
                byte[] abyte1 = abyte[j];
                messagedigest.update(abyte1);
            }

            return messagedigest.digest();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
            return null;
        }
    }

    /**
     * Create a new PublicKey from encoded X.509 data
     */
    public static PublicKey decodePublicKey(byte[] par0ArrayOfByte)
    {
        try
        {
            X509EncodedKeySpec x509encodedkeyspec = new X509EncodedKeySpec(par0ArrayOfByte);
            KeyFactory keyfactory = KeyFactory.getInstance("RSA");
            return keyfactory.generatePublic(x509encodedkeyspec);
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
        }
        catch (InvalidKeySpecException invalidkeyspecexception)
        {
            invalidkeyspecexception.printStackTrace();
        }

        System.err.println("Public key reconstitute failed!");
        return null;
    }

    /**
     * Decrypt shared secret AES key using RSA private key
     */
    public static SecretKey decryptSharedKey(PrivateKey par0PrivateKey, byte[] par1ArrayOfByte)
    {
        return new SecretKeySpec(decryptData(par0PrivateKey, par1ArrayOfByte), "AES");
    }

    @SideOnly(Side.CLIENT)

    /**
     * Encrypt byte[] data with RSA public key
     */
    public static byte[] encryptData(Key par0Key, byte[] par1ArrayOfByte)
    {
        return cipherOperation(1, par0Key, par1ArrayOfByte);
    }

    /**
     * Decrypt byte[] data with RSA private key
     */
    public static byte[] decryptData(Key par0Key, byte[] par1ArrayOfByte)
    {
        return cipherOperation(2, par0Key, par1ArrayOfByte);
    }

    /**
     * Encrypt or decrypt byte[] data using the specified key
     */
    private static byte[] cipherOperation(int par0, Key par1Key, byte[] par2ArrayOfByte)
    {
        try
        {
            return createTheCipherInstance(par0, par1Key.getAlgorithm(), par1Key).doFinal(par2ArrayOfByte);
        }
        catch (IllegalBlockSizeException illegalblocksizeexception)
        {
            illegalblocksizeexception.printStackTrace();
        }
        catch (BadPaddingException badpaddingexception)
        {
            badpaddingexception.printStackTrace();
        }

        System.err.println("Cipher data failed!");
        return null;
    }

    /**
     * Creates the Cipher Instance.
     */
    private static Cipher createTheCipherInstance(int par0, String par1Str, Key par2Key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance(par1Str);
            cipher.init(par0, par2Key);
            return cipher;
        }
        catch (InvalidKeyException invalidkeyexception)
        {
            invalidkeyexception.printStackTrace();
        }
        catch (NoSuchAlgorithmException nosuchalgorithmexception)
        {
            nosuchalgorithmexception.printStackTrace();
        }
        catch (NoSuchPaddingException nosuchpaddingexception)
        {
            nosuchpaddingexception.printStackTrace();
        }

        System.err.println("Cipher creation failed!");
        return null;
    }

    /**
     * Create a new BufferedBlockCipher instance
     */
    private static BufferedBlockCipher createBufferedBlockCipher(boolean par0, Key par1Key)
    {
        BufferedBlockCipher bufferedblockcipher = new BufferedBlockCipher(new CFBBlockCipher(new AESFastEngine(), 8));
        bufferedblockcipher.init(par0, new ParametersWithIV(new KeyParameter(par1Key.getEncoded()), par1Key.getEncoded(), 0, 16));
        return bufferedblockcipher;
    }

    public static OutputStream encryptOuputStream(SecretKey par0SecretKey, OutputStream par1OutputStream)
    {
        return new CipherOutputStream(par1OutputStream, createBufferedBlockCipher(true, par0SecretKey));
    }

    public static InputStream decryptInputStream(SecretKey par0SecretKey, InputStream par1InputStream)
    {
        return new CipherInputStream(par1InputStream, createBufferedBlockCipher(false, par0SecretKey));
    }

    static
    {
        Security.addProvider(new BouncyCastleProvider());
    }
}
