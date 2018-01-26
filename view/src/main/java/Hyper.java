import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

public class Hyper {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(new File("F:\\hyper\\request.ask").toPath(), Charset.defaultCharset());
        StringBuilder sb = new StringBuilder();
        for(String s : lines){
            if(s!=null && !s.equals("")){
                if(s.charAt(0) == '+'){
                    sb.append(s + "                                                                                                                                                                                .");
                }else{
                    sb.append(s);
                }
                sb.append("\n");
            }
        }
        FileWriter fos = new FileWriter(new File("F:\\hyper\\responce.ask"));
        fos.write(sb.toString());
        fos.flush();
        fos.close();
    }
}
