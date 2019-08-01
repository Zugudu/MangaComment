import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Core
{
    public static HttpURLConnection con;
    public static byte[]mes;
    public static OutputStream out;
    public static String[]token,id;
    public static void main(String args[]) throws MalformedURLException, IOException, InterruptedException
    {
        getId();
        token=getToken();
        
        for(byte i=0;i<id.length;)
        {
        mes=("{\"post_id\":\""+id[i]+"\",\"post_type\":\"chapter\",\"post_page\":1,\"parent_comment\":null,\"comment\":\"Ну что же, начнем<br>\"}").getBytes("utf-8");
        connect();
        out.write(mes, 0, mes.length);
        out.flush();
        out.close();
        con.disconnect();
        
        System.out.println("Status "+id[i]+":"+con.getResponseCode()+con.getResponseMessage());
        if(con.getResponseCode()==200)
            i++;
        Thread.sleep(41000);
        }
    }
    public static void getId() throws IOException
    {
        try {
            FileReader in=new FileReader(new File("id"));
            StringBuilder sb=new StringBuilder();
            char x;
            int index=0,cx=0;
            while((x=(char)in.read())!='\uffff')
            {
                sb.append(x);
                if(x=='\n')
                    cx++;
            }
            in.close();
            id=new String[cx];
            char[]ids=sb.toString().toCharArray();
            for(short i=0;i<cx;i++)
            {
                sb=new StringBuilder();
                while((x=ids[index++])!='\n')
                    sb.append(x);
                id[i]=sb.toString();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FNF id");
            System.exit(0);
        }
    }
    public static void connect() throws ProtocolException, IOException
    {
        con=(HttpURLConnection) new URL("https://mangalib.me/api/comments").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0");
        con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        con.setRequestProperty("X-CSRF-TOKEN", token[0]);
        con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        con.setRequestProperty("X-XSRF-TOKEN", token[1]);
        con.setRequestProperty("Cookie", "mangalib_session="+token[2]);
        con.setRequestProperty("Content-Length", mes.length+"");
        con.setDoOutput(true);
        out=con.getOutputStream();
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