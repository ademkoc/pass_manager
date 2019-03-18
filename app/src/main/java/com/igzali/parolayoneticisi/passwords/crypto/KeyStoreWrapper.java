package com.igzali.parolayoneticisi.passwords.crypto;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

public class KeyStoreWrapper {

    private Context mContext;
    private String mAlias;
    private KeyStore mKeyStore;
    private String TAG = KeyStoreWrapper.class.getSimpleName();

    public KeyStoreWrapper(Context context, String alias) {
        mContext=context;
        mAlias = alias;

        createKeyStore();
    }

    private void createKeyStore() {
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
            if (!mKeyStore.containsAlias(mAlias)) {
                createAndroidKeyStoreAsymmetricKey();
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private KeyPair createAndroidKeyStoreAsymmetricKey() {
        KeyPairGenerator generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");

            if (Build.VERSION.SDK_INT >= 23)
                initGeneratorWithKeyPairGeneratorSpec(generator);
            else
                initGeneratorWithKeyGenParameterSpec(generator);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return generator.generateKeyPair();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initGeneratorWithKeyGenParameterSpec(KeyPairGenerator generator) {
        try {
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(mAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1);
            generator.initialize(builder.build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    private void initGeneratorWithKeyPairGeneratorSpec(KeyPairGenerator generator){
        Date startDate = Calendar.getInstance().getTime();
        Date endDate = Calendar.getInstance().getTime();
        endDate.setYear(endDate.getYear() + 20);
        try {
            KeyPairGeneratorSpec.Builder builder = new KeyPairGeneratorSpec.Builder(mContext)
                    .setAlias(mAlias)
                    .setSerialNumber(BigInteger.ONE)
                    .setSubject(new X500Principal(String.format("CN=%s CA Certificate", mAlias)))
                    .setStartDate(startDate)
                    .setEndDate(endDate);
            generator.initialize(builder.build());
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public KeyPair getAndroidKeyStoreAsymmetricKeyPair() {
        PrivateKey privateKey = null;
        PublicKey publicKey = null;

        try {
            privateKey = (PrivateKey) mKeyStore.getKey(mAlias, null);
            publicKey = mKeyStore.getCertificate(mAlias).getPublicKey();

        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        if (privateKey == null || privateKey == null)
            return null;
        else
            return new KeyPair(publicKey, privateKey);
    }

    private void removeAndroidKeyStoreKey() {
        try {
            mKeyStore.deleteEntry(mAlias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }
}
