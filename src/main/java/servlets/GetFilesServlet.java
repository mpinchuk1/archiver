package servlets;

import entities.FileEntity;
import utils.Util;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/download")
public class GetFilesServlet extends HttpServlet {

    List<String> fileNames = InputFilesServlet.getFileNames();
    private Util util = InputFilesServlet.getUtil();
    private EntityManager em = util.getEm();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileName = req.getParameter("fileName");
        List<FileEntity> files = new ArrayList<>();
        for(String file: fileNames){
            if(fileName.equals(file)){
                files.add(selectFileByName(fileName));
                break;
            } else if(fileName.equals("ALL")){
                files = selectAllFiles();
                break;
            }else {
                System.out.println("No such file for download");
                resp.setStatus(404);
            }
        }

        for (FileEntity fileEntity: files){
            File file = new File("D:\\Max\\projects\\Arhivator\\downloadedFiles", fileEntity.getFileName()+".zip");
            writeBytes(file, fileEntity.getData());
        }

    }

    public List<FileEntity> selectAllFiles(){
        TypedQuery<FileEntity> query = em.createQuery("SELECT f FROM FileEntity f", FileEntity.class);
        return query.getResultList();
    }

    public FileEntity selectFileByName(String fileName){
        TypedQuery<FileEntity> query = em.createQuery("SELECT f FROM FileEntity f WHERE f.fileName = :name", FileEntity.class);
        query.setParameter("name", fileName);
        return query.getSingleResult();
    }

    public void writeBytes(File file, byte[] dataToWrite){
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(dataToWrite);
            System.out.println("Successfully" + " byte inserted");
            os.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
