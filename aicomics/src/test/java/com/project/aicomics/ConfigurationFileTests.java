package com.project.aicomics;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigurationFileTests {
    ConfigurationFile cnf = ConfigurationFile.getInstance();

    @Test
    void testSingleton() {
        ConfigurationFile two = ConfigurationFile.getInstance();
        Assertions.assertEquals(cnf, two);
    }

    @Test
    void testLinks() {
        System.out.println(cnf.getCompURL());
        Assertions.assertEquals("https://api.openai.com/v1/chat/completions", cnf.getCompURL());
        
        System.out.println(cnf.getEmbedURL());
        Assertions.assertEquals("https://api.openai.com/v1/embeddings", cnf.getEmbedURL());

        System.out.println(cnf.getModelURL());
        Assertions.assertEquals("https://api.openai.com/v1/models", cnf.getModelURL());
    }

    @Test
    void testAPIinfo() {
        cnf.changeAPIConfig("org-55", "api-154y2", "gpt-4o-mini");

        System.out.println(cnf.getOrg());
        Assertions.assertEquals("org-55", cnf.getOrg());

        System.out.println(cnf.getAPIKey());
        Assertions.assertEquals("api-154y2", cnf.getAPIKey());

        System.out.println(cnf.getModel());
        Assertions.assertEquals("gpt-4o-mini", cnf.getModel());

    }

    @Test
    void testWroteToFile() {
        File fi = new File("src\\main\\resources\\apikey.txt");
        try(Scanner read = new Scanner(fi)) {
            Assertions.assertEquals("COMPLETIONS_URL https://api.openai.com/v1/chat/completions", read.nextLine());
            Assertions.assertEquals("EMBEDDINGS_URL https://api.openai.com/v1/embeddings", read.nextLine());
            Assertions.assertEquals("MODELS_URL https://api.openai.com/v1/models", read.nextLine());
            Assertions.assertEquals("ORG_KEY org-55", read.nextLine());
            read.nextLine();
            Assertions.assertEquals("MODEL gpt-4o-mini", read.nextLine());
        } catch (IOException e) {
            System.out.println("Error finding file in test" + e);
            // Doing this to ensure the test is marked as fails if there is an error
            Assertions.assertEquals(0, 1);
        }

    }

    @Test
    void testAPIinfoAgain() {
        cnf.changeAPIConfig("org-55", "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", "gpt-4o-mini");

        System.out.println(cnf.getOrg());
        Assertions.assertEquals("org-55", cnf.getOrg());

        System.out.println(cnf.getAPIKey());
        Assertions.assertEquals("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", cnf.getAPIKey());

        System.out.println(cnf.getModel());
        Assertions.assertEquals("gpt-4o-mini", cnf.getModel());

    }
    
}
