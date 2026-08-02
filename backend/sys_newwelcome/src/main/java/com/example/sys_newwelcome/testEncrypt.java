package com.example.sys_newwelcome;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

public class testEncrypt {
    public static void main(String[] args) {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        /**
         * 加密算法
         */
        config.setAlgorithm("PBEWithMD5AndDES");
        /**
         * 加密的密钥
         */
        config.setPassword("Angel");
        standardPBEStringEncryptor.setConfig(config);
        String plainText = "123456";
        String encryptedText = standardPBEStringEncryptor.encrypt(plainText);
        System.out.println(encryptedText);
    }
}
