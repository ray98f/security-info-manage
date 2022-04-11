package com.security.info.manage.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

public class DocUtils {

    public static MultipartFile saveWord(String name, String type, Map<String,Object> dataMap) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        configuration.setClassForTemplateLoading(DocUtils.class, "/");
        Template template = configuration.getTemplate("templates/" + type + ".xml");
        InputStreamSource streamSource = createWord(template, dataMap);
        InputStream inputStream = Objects.requireNonNull(streamSource).getInputStream();
        return new MockMultipartFile(name, name, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", inputStream);
    }

    public static InputStreamSource createWord(Template template, Map<String, Object> dataMap) {
        StringWriter out = null;
        Writer writer = null;
        try {
            out = new StringWriter();
            writer = new BufferedWriter(out, 1024);
            template.process(dataMap, writer);
            return new ByteArrayResource(out.toString().getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(writer).close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}