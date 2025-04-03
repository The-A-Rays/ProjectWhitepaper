package com.project.aicomics.vignette;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VignetteFileReader {

     /**
     * - {@link #readSchemas(String, int)} creates a list of schemas from an input file
     * @param filename String name for the file
     * @param maxLines defines how many schemas can be read from the file
     * @return list of schemas from file
     */
    public static List<VignetteSchema> readSchemas(String fileName, int maxLines) throws IOException {
        List<VignetteSchema> schemas = new ArrayList<>();

        try (InputStream inputStream = VignetteFileReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + fileName);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null && i < maxLines) {
                    String[] fields = line.split("\t");
                    if (fields.length == 5) {
                        schemas.add(new VignetteSchema(Arrays.asList(fields)));
                    }
                    i++;
                }
            }
        }

        return schemas;
    }
}
