package cn.devit.util.proguard;

import static org.springframework.util.StringUtils.hasText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.ws.rs.core.Response;

import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.google.common.base.Charsets;
import com.google.common.io.CountingOutputStream;

import proguard.retrace.ReTrace;

@WebServlet(name = "Proguard Retrace", urlPatterns = "/retrace")
@MultipartConfig
public class Generator extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Support POST.");
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {
        StandardMultipartHttpServletRequest request = new StandardMultipartHttpServletRequest(
                req);
        MultipartFile file = request.getFile("mapping_file");
        String stacktrace = request.getParameter("stacktrace");
        if (file != null && hasText(stacktrace)) {
            File tmp = Files.createTempFile("mapping", ".txt").toFile();
            file.transferTo(tmp);
            String plain = convert(tmp, stacktrace);
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(plain);
            response.flushBuffer();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Missing text and type.");
        }
    }

    public String convert(File mappingFile, String stackstrace)
            throws IOException {

        LineNumberReader reader = new LineNumberReader(
                new StringReader(stackstrace));

        StringWriter stringWriter = new StringWriter();
        new ReTrace(ReTrace.STACK_TRACE_EXPRESSION, false,
                mappingFile).retrace(reader,
                        new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public void convert(String mapping, String stackstrace,
            HttpServletResponse response) throws IOException {
        Path file = Files.createTempFile("mapping", ".txt");
        com.google.common.io.Files.write(mapping, file.toFile(),
                Charsets.UTF_8);

        LineNumberReader reader = new LineNumberReader(
                new StringReader(stackstrace));

        StringWriter stringWriter = new StringWriter();
        new ReTrace(ReTrace.STACK_TRACE_EXPRESSION, false,
                file.toFile()).retrace(reader,
                        new PrintWriter(stringWriter));
        Files.delete(file);
        System.out.println(stringWriter.toString());
    }

    @Override
    public String getServletInfo() {
        return "Proguard Retrace Online";
    }

}
