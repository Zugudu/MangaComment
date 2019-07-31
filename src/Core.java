import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Core
{
    public static void main(String args[]) throws MalformedURLException, IOException
    {
        String[]token=getToken();
        String id="28730";
        byte[]mes=("{\"post_id\":\""+id+"\",\"post_type\":\"chapter\",\"post_page\":1,\"parent_comment\":null,\"comment\":\"Переводчик. Молодец. Молодец.<br>\"}").getBytes("utf-8");
        HttpURLConnection con=(HttpURLConnection) new URL("https://mangalib.me/api/comments").openConnection();
        
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
        con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        con.setRequestProperty("X-CSRF-TOKEN", token[0]);
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        con.setRequestProperty("X-XSRF-TOKEN", token[1]);
        con.setRequestProperty("Cookie", "mangalib_session="+token[2]);
        con.setRequestProperty("Content-Length", mes.length+"");
        
        con.setDoOutput(true);
        
        OutputStream out=con.getOutputStream();
        
        out.write(mes, 0, mes.length);
        out.flush();
        out.close();
        
        System.out.println("Status:"+con.getResponseCode()+'\n'+con.getResponseMessage());
    }
    public static String[] getToken() throws IOException
    {
        String[]token=new String[3];
        try {
            FileReader in=new FileReader(new File("dat"));
            StringBuilder sb=new StringBuilder();
            char x;
            while((x=(char)in.read())!=10)
                sb.append(x);
            token[0]=sb.toString();
            sb=new StringBuilder();
            while((x=(char)in.read())!=10)
                sb.append(x);
            token[1]=sb.toString();
            sb=new StringBuilder();
            while((x=(char)in.read())!=10)
                sb.append(x);
            token[2]=sb.toString();
            in.close();
            return token;
        } catch (FileNotFoundException ex) {
            System.out.println("FNF 'dat'");
            System.exit(0);
        }
        return null;
    }
}