package servlets;

import entities.FileEntity;
import utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@WebServlet("/input")
@MultipartConfig
public class InputFilesServlet extends HttpServlet {

    private static Util util = new Util();
    private static List<String> fileNames = new ArrayList<>();
    private EntityManager em = util.getEm();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Part> fileParts = req.getParts().stream().
                filter(part -> "file".equals(part.getName()) && part.getSize() > 0).collect(Collectors.toList()); // Retrieves <input type="file" name="file" multiple="true">

        for (Part filePart : fileParts) {
            Path path = Paths.get(filePart.getSubmittedFileName());
            byte[] data = getByteFromFile(filePart.getInputStream());
            FileEntity file = new FileEntity(path.getFileName().toString(), filePart.getContentType(),
                    filePart.getSize(), zipBytes(path.getFileName().toString(), data) );

            addNewFile(file);
            fileNames.add(path.getFileName().toString());
            //System.out.println(file);
        }

        resp.setStatus(200);
        resp.sendRedirect("uploadResult.jsp");

    }

    public void addNewFile(FileEntity file) {

        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            em.persist(file);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction.isActive())
                transaction.rollback();
            ex.printStackTrace();
        }

        System.out.println("File " + file.getFileName() + " has added to DB!");
    }

    public static byte[] zipBytes(String filename, byte[] input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        ZipEntry entry = new ZipEntry(filename);
        entry.setSize(input.length);
        zos.putNextEntry(entry);
        zos.write(input);
        zos.closeEntry();
        zos.close();
        return baos.toByteArray();
    }

    private byte[] getByteFromFile(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();
        return buffer.toByteArray();
    }

    public static List<String> getFileNames(){
        return fileNames;
    }

    public static Util getUtil() {
        return util;
    }

}
