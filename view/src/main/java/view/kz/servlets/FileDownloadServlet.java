package view.kz.servlets;

import view.kz.ejb.FileManagment;
import view.kz.security.VulnerabilitiesDefender;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

public class FileDownloadServlet extends HttpServlet {

    @EJB
    private FileManagment fileManagment;

    public FileManagment getFileManagment() {
        return fileManagment;
    }

    public void setFileManagment(FileManagment fileManagment) {
        this.fileManagment = fileManagment;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            req.setCharacterEncoding("UTF-8");
            String uid = req.getParameter("uid");
            String path = getFileManagment().getPathByUID(uid);
            File file = new File(path);
            if(file.exists()) {
                resp.setCharacterEncoding("utf-8");
                resp.setContentType("*/*");
                String fileName = file.getName();
                String disposition = "attachment; filename*=utf-8''";
                fileName = fileName.replace(" ", "%20");
                String url = URLEncoder.encode(fileName, "UTF-8");
                url = url.replace("%2520", "%20");
                if (req.getHeader("user-agent").contains("MSIE"))
                    disposition = "attachment; filename=";
                disposition += url;
                resp.setHeader("Content-Disposition", VulnerabilitiesDefender.restrictHttpResponseSplitting(disposition));
                BufferedOutputStream bos = new BufferedOutputStream(resp.getOutputStream());
                bos.write(Files.readAllBytes(file.toPath()));
                resp.getOutputStream().close();
            }
            else {
                throw new Exception("File for download not found!");
            }
        }
        catch(Throwable t) {
            if(t.getClass().getSimpleName().equals("ClientAbortException")) {
                System.err.println("FileDownloadServlet ClientAbortException");
            }
            else {
                t.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
