package view.kz.web;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import view.kz.ejb.FileManagment;
import view.kz.ejb.SettingManagment;
import view.kz.ejb.UserManagment;
import view.kz.persistence.City;
import view.kz.persistence.Contry;
import view.kz.persistence.DicFile;
import view.kz.persistence.SystemUser;
import view.kz.util.Utils;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.util.*;

@ManagedBean
@SessionScoped
public class ApplicationModel {
    @EJB
    private SettingManagment settingManagment;
    @ManagedProperty(value = "#{parentUserModule}")
    private ParentUserModule parentUserModule;
    @EJB
    private FileManagment fileManagment;
    @EJB
    public UserManagment managment;
    private List<String> currentPath = new ArrayList<>();
    private DicFile selectedFile;
    private List<DicFile> parentFiles;
    private boolean renderFolderPanel = false;
    private Search search;
    private boolean isFileSizeError;

    private Setting setting;

    public Setting getSetting() {
        if(setting == null){
            setting = new Setting(getParentUserModule().getParentUser());
        }
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public Search getSearch() {
        if(search==null){
            search = new Search();
        }
        return search;
    }

    public List<String> topPathPanelList(){
        String[] top = getCurrentPath().replace("\\\\","#").split("#");
        List<String> topList = new ArrayList<>();
        topList.add(" Облако / ");
        boolean checked = true;
        for(int i=1;i<top.length;i++){
            if (checked){
                if(top[i].equals("USERFILES")){
                    i++;
                    checked = false;
                }
            }else{
                topList.add(Utils.handleFilename(" "+top[i])+" / ");
            }
        }
        return topList;
    }

    public void rename() throws IOException {
        String[] paths = getSelectedFile().getFile().getPath().split("\\\\");
        String newFile = getParentUserModule().getParentUser().getParrentDir();
        for(int i=3;i<paths.length-1;i++){
            newFile+= paths[i]+"\\";
        }
        File file = new File(newFile+"/" + getSelectedFile().getFileName());
        File oldFile = getSelectedFile().getFile();
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(Files.readAllBytes(oldFile.toPath()));
        fos.flush();
        fos.close();
        oldFile.delete();
        selectedFile.setFile(file);
        selectedFile.setPath(file.getPath());
        selectedFile.setUid(UUID.randomUUID().toString().replace("-",""));
        fileManagment.addFile(getSelectedFile());
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Не достаточно памяти", null));
            return;
        }
        if(!newFile.exists()){
            try {
                DicFile file = new DicFile();
                file.setUser(getParentUserModule().getParentUser());
                file.setPath(newFile.getPath());
                file.setCreate(new Date());
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
            if(file.getFile().isDirectory()){

            }else{
                if(file.isSelected()){
                    File f = new File(parentUserModule.getParentUser().getParrentDir()+"\\basket\\"+file.getFile().getName());
                    if(!f.exists()){ f.createNewFile();}
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.write(Files.readAllBytes(file.getFile().toPath()));
                    fos.flush();
                    fos.close();
                    DicFile df = new DicFile();
                    df.setFile(f);
                    df.setCreate(new Date());
                    df.setBasket(true);
                    df.setUid(UUID.randomUUID().toString().replace("-",""));
                    df.setPath(f.getPath());
                    fileManagment.addFile(df);
                    file.getFile().delete();
                }
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
        DicFile df = new DicFile();
        df.setFile(f);
        df.setCreate(new Date());
        df.setBasket(true);
        df.setUid(UUID.randomUUID().toString().replace("-",""));
        df.setPath(f.getPath());
        fileManagment.addFile(df);
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
        return getImagePathMethod(file);
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
            if(f.getPath().replace("\\","").equals((getParentUserModule().getParentUser().getParrentDir()+"basket").replace("\\",""))){
                continue;
            }
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
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "  search.xhtml?faces-redirect=true");
        }
    }

    public class Setting{

        private SystemUser user;
        private City city;
        private Contry contry;

        private String password;
        private String securityPass;

        public Setting(SystemUser user) {
            this.user = new SystemUser();
            this.user.setId(user.getId());
            this.user.setCity(user.getCity());
            this.user.setType(user.getType());
            this.user.setEmail(user.getEmail());
            this.user.setFirstname(user.getFirstname());
            this.user.setLastname(user.getLastname());
            this.user.setLogin(user.getLogin());
            this.user.setPassword(user.getPassword());
            this.user.setParrentDir(user.getParrentDir());
            this.user.setParrentDir(user.getParrentDir());
            this.user.setUid(user.getUid());

            this.city = user.getCity();
            this.contry = user.getCity().getContry();
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSecurityPass() {
            return securityPass;
        }

        public void setSecurityPass(String securityPass) {
            this.securityPass = securityPass;
        }

        public SystemUser getUser() {
            return user;
        }

        public void setUser(SystemUser user) {
            this.user = user;
        }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

        public Contry getContry() {
            return contry;
        }

        public void setContry(Contry contry) {
            this.contry = contry;
        }
        public void save(){
            city.setContry(contry);
            user.setCity(city);
            getParentUserModule().setParentUser(managment.updateUser(user));
            FacesContext.getCurrentInstance().addMessage("main:saveinfo", new FacesMessage(FacesMessage.SEVERITY_INFO, "Данные успешно изменены!", null));
            return;
        }
        public void resetPassword(){
           if(password.length()<6){
               FacesContext.getCurrentInstance().addMessage("mainPass:pass", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Введите минимум 6 смволов!", null));
               return;
           }
           if(!password.equals(securityPass)){
               FacesContext.getCurrentInstance().addMessage("mainPass:pass2", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Парольи не совпадают!", null));
               return;
           }
           user.setPassword(password);
           getParentUserModule().setParentUser(managment.updateUser(user));
            FacesContext.getCurrentInstance().addMessage("mainPass:savepass", new FacesMessage(FacesMessage.SEVERITY_INFO, "Пароль успешно изменен!", null));
            return;
        }
    }

    public String getImagePathMethod(DicFile file){
        String image = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath() + "/resources/img/";
        String link  = "/download?uid=";
        if(file==null){
            return null;
        }
        if(file.getFile().isFile()){
            if(file.getFileFormat().equals("jpg")){
                image+="jpg.png";
                link+=fileManagment.getUidByPath(file.getFile().getPath());
                return link;
            }else if(file.getFileFormat().equals("png")){
                image+="png.png";
                link+=fileManagment.getUidByPath(file.getFile().getPath());
                return link;
            }
            else if(file.getFileFormat().equals("pdf")){
                image+="pdf.png";
            }
            else if(file.getFileFormat().equals("zip")){
                image+="zip.png";
            }
            else if(file.getFileFormat().equals("xls")){
                image+="xls.png";
            }
            else if(file.getFileFormat().equals("gif")){
                image+="gif.png";
            }
            else if(file.getFileFormat().equals("rar")){
                image+="rar.png";
            }
            else if (file.getFileFormat().equals("wav")){
                image+="wav.png";
            }
            else if (file.getFileFormat().equals("wmv")){
                image+="wmv.png";
            }
            else if (file.getFileFormat().equals("proj")){
                image+="proj.png";
            }
            else if (file.getFileFormat().equals("rtf")){
                image+="rtf.png";
            }
            else if(file.getFileFormat().equals("exe")){
                image+="exe.png";
            }
            else if(file.getFileFormat().equals("apk")){
                image+="apk.png";
            }
            else if(file.getFileFormat().equals("doc") || file.getFileFormat().equals("docx")){
                image+="office.png";
            }
            else if(file.getFileFormat().equals("txt")){
                image+="text.png";
            }
            else if(file.getFileFormat().equals("jar") || file.getFileFormat().equals("java") ){
                image+="java.png";
            }
            else if(file.getFileFormat().equals("ini")){
                image+="conf.jpg";
            }
            else if(file.getFileFormat().equals("html")||file.getFileFormat().equals("xhtml")||file.getFileFormat().equals("php")){
                image+="web.png";
            }
            else if(file.getFileFormat().equals("css")){
                image+="css.png";
            }
            else{
                image+="file.png";
            }
        }else{
            image+="folder.png";
        }
        return image;
    }
    private EditTextModel editTextModel;

    public EditTextModel getEditTextModel() {
        if(editTextModel==null){
            editTextModel = new EditTextModel();
        }
        return editTextModel;
    }

    public class EditTextModel{
        private String text;
        private String charset = "UTF-8"; // UTF-8, CP-1251

        public boolean editable(){
            if(selectedFile!=null){
                if(selectedFile.getFileFormat().equals("txt")||
                        selectedFile.getFileFormat().equals("ask") ||
                        selectedFile.getFileFormat().equals("ini") ||
                        selectedFile.getFileFormat().equals("cfg") ||
                        selectedFile.getFileFormat().equals("php") ||
                        selectedFile.getFileFormat().equals("xhtml") ||
                        selectedFile.getFileFormat().equals("java") ||
                        selectedFile.getFileFormat().equals("c++") ||
                        selectedFile.getFileFormat().equals("cs") ||
                        selectedFile.getFileFormat().equals("pass") ||
                        selectedFile.getFileFormat().equals("html")){
                    return true;
                }
            }
            return false;
        }
        public String getText() throws IOException {
            if(text==null && editable()){
                if(selectedFile == null || selectedFile.getFile()==null){
                    return "";
                }else{
                    List<String> lines = null;
                    try {
                        lines = Files.readAllLines(selectedFile.getFile().toPath(), Charset.forName(charset));
                    }catch (MalformedInputException ex){
                        ex.printStackTrace();
                        return "";
                    }
                    StringBuilder sb = new StringBuilder();
                    for(String s : lines){
                        sb.append(s);
                        sb.append("\n");
                    }
                    text = sb.toString();
                }
            }
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
        public void clear(){
            text=null;
        }
        public void save() throws IOException {
            FileWriter fw = new FileWriter(selectedFile.getFile());
            fw.write(text);
            fw.flush();
            fw.close();
        }
    }
}
