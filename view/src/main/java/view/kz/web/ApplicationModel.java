package view.kz.web;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import view.kz.ejb.FileManagment;
import view.kz.ejb.SettingManagment;
import view.kz.persistence.DicFile;
import view.kz.util.Utils;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ManagedBean
@SessionScoped
public class ApplicationModel {
    @EJB
    private SettingManagment settingManagment;
    @ManagedProperty(value = "#{parentUserModule}")
    private ParentUserModule parentUserModule;
    @EJB
    private FileManagment fileManagment;
    private List<String> currentPath = new ArrayList<>();
    private DicFile selectedFile;
    private List<DicFile> parentFiles;
    private boolean renderFolderPanel = false;
    private Search search;
    private boolean isFileSizeError;

    public Search getSearch() {
        if(search==null){
            search = new Search();
        }
        return search;
    }

    public FileManagment getFileManagment() {
        return fileManagment;
    }

    public void setFileManagment(FileManagment fileManagment) {
        this.fileManagment = fileManagment;
    }

    public boolean isFileSizeError() {
        return isFileSizeError;
    }

    public void setFileSizeError(boolean fileSizeError) {
        this.isFileSizeError = fileSizeError;
    }

    public boolean isRenderFolderPanel() {
        return renderFolderPanel;
    }

    public void setRenderFolderPanel(boolean renderFolderPanel) {
        this.renderFolderPanel = renderFolderPanel;
    }

    public DicFile getSelectedFile() {
        return selectedFile;
    }

    public String getUidByPath(String path){
        return getFileManagment().getUidByPath(path);
    }

    public void setSelectedFile(DicFile selectedFile) {
        this.selectedFile = selectedFile;
    }

    public String getCurrentPath() {
        String root = getParentUserModule().getParentUser().getParrentDir();
        for(String path : currentPath){
            root = root + path + "\\";
        }
        return root;
    }

    public void uploadFile(FileUploadEvent event){
        UploadedFile item = event.getUploadedFile();
        File newFile = new File(getCurrentPath()+"/"+checkFileName(new File(getCurrentPath()),item.getName()));
        if(item.getData().length>getSizeFree(false)){
            isFileSizeError = true;
            FacesContext.getCurrentInstance().addMessage("main:loadBtn", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не достаточно памяти", null));
            return;
        }
        if(!newFile.exists()){
            try {
                DicFile file = new DicFile();
                file.setUser(getParentUserModule().getParentUser());
                file.setPath(newFile.getPath());
                file.setUid(UUID.randomUUID().toString().replace("-",""));
                getFileManagment().addFile(file);
                isFileSizeError = false;
                newFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(newFile);
                fos.write(item.getData());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String checkFileName(File file, String fileName){
        for(File f : file.listFiles()){
            if(f.getName().equals(fileName)){
                String[]name = fileName.replace(".","#").split("#");
                return checkFileName(file, fileName+"-COPY."+name[name.length-1]);
            }
        }
        return fileName;
    }

    private String newFolder;

    public String getNewFolder() {
        return newFolder;
    }

    public void setNewFolder(String newFolder) {
        this.newFolder = newFolder;
    }

    public void createFolder(){
        if(newFolder!=null && !newFolder.isEmpty()){
            File file = new File(getCurrentPath()+"/"+getNewFolder());
            if(!file.exists()){
                file.mkdirs();
                newFolder = null;
                hasError = false;
            }else{
                hasError = true;
                FacesContext.getCurrentInstance().addMessage("main:createFolders", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Папка с таким именем уже существует", null));
            }
        }else{
            hasError = true;
            FacesContext.getCurrentInstance().addMessage("main:createFolders", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Название папки не может быть пустым", null));
        }
    }

    private boolean hasError;

    public boolean isHasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public List<DicFile> getFilesRoot(){
        parentFiles = new ArrayList<>();
        getSearch().setUnicalCode(null);
        File root = new File(getCurrentPath());
        for(File f : root.listFiles()){
            if(f.isDirectory()){
                DicFile dicFile = new DicFile();
                dicFile.setFile(f);
                parentFiles.add(dicFile);
            }
        }
        for(File f : root.listFiles()){
            if(f.isFile()){
                DicFile dicFile = new DicFile();
                dicFile.setFile(f);
                parentFiles.add(dicFile);
            }
        }
        return parentFiles;
    }

    public void delete() throws IOException {
        for(DicFile file : parentFiles){
            if(file.isSelected()){
                File f = new File(parentUserModule.getParentUser().getParrentDir()+"\\basket\\"+file.getFile().getName());
                if(!f.exists()){ f.createNewFile();}
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(Files.readAllBytes(file.getFile().toPath()));
                fos.flush();
                fos.close();
                file.getFile().delete();
            }
        }
    }
    public void deleteSelectedFile(File file) throws IOException {
        File f = new File(parentUserModule.getParentUser().getParrentDir()+"\\basket\\"+file.getName());
        if(!f.exists()){ f.createNewFile();}
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(Files.readAllBytes(file.toPath()));
        fos.flush();
        fos.close();
        file.delete();
    }

    public void updateCurrentPath(String fname){
       currentPath.add(fname);
    }

    public void backPage(){
        List<String> paths = new ArrayList<>();
        for(int i=0;i<currentPath.size()-1;i++){
            paths.add(currentPath.get(i));
        }
        currentPath = paths;
    }

    public String getImagePath(DicFile file){
        return Utils.getImagePath(file);
    }

    public String handleFileName(File file){
        return Utils.handleFilename(file);
    }

    public String getSize(boolean a){
        long size = 0;
        File file = new File(getParentUserModule().getParentUser().getParrentDir());
        if(a){
            String string = ((getFileSizeByFolder(file)/1024)/1024) + "Mb";
            return string;
        }else{
            String string = ((((getParentUserModule().getParentUser().getType().getSize()) - (getFileSizeByFolder(file)))/1024)/1024)+"Mb";
            return string;
        }
    }

    public long getSizeFree(boolean a){
        long size = 0;
        File file = new File(getParentUserModule().getParentUser().getParrentDir());
        if(a){
            return getFileSizeByFolder(file);
        }else{
            return (getParentUserModule().getParentUser().getType().getSize() - (getFileSizeByFolder(file)));
        }
    }

    public long getFileSizeByFolder(File file){
        long size = 0;
        for(File f : file.listFiles()){
            if(f.isFile()){
                size+=f.length();
            }else{
                size+=getFileSizeByFolder(f);
            }
        }
        return size;
    }

    public ParentUserModule getParentUserModule() {
        return parentUserModule;
    }

    public void setParentUserModule(ParentUserModule parentUserModule) {
        this.parentUserModule = parentUserModule;
    }

    public SettingManagment getSettingManagment() {
        return settingManagment;
    }

    public void setSettingManagment(SettingManagment settingManagment) {
        this.settingManagment = settingManagment;
    }

    public List<DicFile> getBasketFiles(){
        List<DicFile> files = new ArrayList<>();
        File file = new File(parentUserModule.getParentUser().getParrentDir()+"\\basket");
        for(File f : file.listFiles()){
            if(f.isDirectory()){
                DicFile dicFile = new DicFile();
                dicFile.setFile(f);
                files.add(dicFile);
            }
        }
        for(File f : file.listFiles()){
            if(f.isFile()){
                DicFile dicFile = new DicFile();
                dicFile.setFile(f);
                files.add(dicFile);
            }
        }
        return files;
    }

    public class Search{
        private List<DicFile> searchFiles;
        private String unicalCode;

        public List<DicFile> getFiles(){
            return getFiles(new File(getParentUserModule().getParentUser().getParrentDir()));
        }

        public List<DicFile> getFiles(File file){
            ArrayList<DicFile> list = new ArrayList<>();
            getFiles(file,list);
            return list;
        }

        public List<DicFile> getFiles(File file, List<DicFile> list){
            for(File f : file.listFiles()){
                if(f.isFile()){
                    DicFile df = new DicFile();
                    df.setFile(f);
                    list.add(df);
                }else{
                    getFiles(f,list);
                }
            }
            return list;
        }

        public List<DicFile> getFillter(){
            List<DicFile> list = new ArrayList<>();
            if(unicalCode!=null){
                for(DicFile file : getFiles()){
                    if(file.getFileName().contains(unicalCode)){
                        list.add(file);
                    }
                }
            }
            return list;
        }

        public List<DicFile> getSearchFiles() {
            return searchFiles;
        }

        public void setSearchFiles(List<DicFile> searchFiles) {
            this.searchFiles = searchFiles;
        }

        public String getUnicalCode() {
            return unicalCode;
        }

        public void setUnicalCode(String unicalCode) {
            this.unicalCode = unicalCode;
        }

        public void goToSearchResult() throws IOException {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "search.xhtml?faces-redirect=true");
        }
    }
}
