import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class Core
{
    public static void main(String args[]) throws MalformedURLException, IOException
    {
        Console con=System.console();
        System.out.println("Name?");
        String name=con.readLine();
        System.out.println("Vol?");
        int vol=Integer.parseInt(con.readLine());
        System.out.println("Cp min?");
        int cmin=Integer.parseInt(con.readLine());
        System.out.println("Cp max?");
        int cmax=Integer.parseInt(con.readLine());
        getList(name, vol, cmin, cmax);
        
    }
    public static String[] getToken() throws IOException
    {
        String[]token=new String[2];
        try {
            FileReader in=new FileReader(new File("dat"));
            StringBuilder sb=new StringBuilder();
            char x;
            while((x=(char)in.read())!=13)
                sb.append(x);
            token[0]=sb.toString();
            sb=new StringBuilder();
            in.read();
            while((x=(char)in.read())!=13)
                sb.append(x);
            token[1]=sb.toString();
            in.close();
            return token;
        } catch (FileNotFoundException ex) {
            System.out.println("FNF 'dat'");
            System.exit(0);
        }
        return null;
    }
    public static void getList(String name,int v,int cmin,int cmax) throws IOException
    {
        File f=new File("id");
        if(!f.exists())
            f.createNewFile();
        while(cmin<=cmax)
        {
            String s=findId("https://mangalib.me/"+name+"/v"+v+"/c"+(cmin++));
            System.out.println(s);
            Files.write(f.toPath(), (s+"\n").getBytes(), StandardOpenOption.APPEND);
        }
    }
    public static void getCon(String id,int page) throws IOException
    {
        String[]token=getToken();
        Clipboard clip=Toolkit.getDefaultToolkit().getSystemClipboard();
        clip.setContents(new StringSelection(
                "await fetch(\"https://mangalib.me/api/comments\", {\n" +
"    \"credentials\": \"include\",\n" +
"    \"headers\": {\n" +
"        \"User-Agent\": \"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0\",\n" +
"        \"Accept\": \"application/json, text/plain, */*\",\n" +
"        \"Accept-Language\": \"ru,en-US;q=0.7,en;q=0.3\",\n" +
"        \"Content-Type\": \"application/json;charset=utf-8\",\n" +
"        \"X-CSRF-TOKEN\": \""+token[0]+"\",\n" +
"        \"X-Requested-With\": \"XMLHttpRequest\",\n" +
"        \"X-XSRF-TOKEN\": \""+token[1]+"\"\n" +
"    },\n" +
"    \"referrer\": \"https://mangalib.me/remonster/v6/c53/21\",\n" +
"    \"body\": \"{\\\"post_id\\\":\\\""+id+"\\\",\\\"post_type\\\":\\\"chapter\\\",\\\"post_page\\\":"+page+
",\\\"parent_comment\\\":null,\\\"comment\\\":\\\"Переводчик. Молодец. Молодец.<br>\\\"}\",\n" +
"    \"method\": \"POST\",\n" +
"    \"mode\": \"cors\"\n" +
"});"
        ), null);
    }
    public static String findId(String urls) throws IOException
    {
        URLConnection url=new URL(urls).openConnection();
        url.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64)");
        InputStreamReader in=new InputStreamReader(url.getInputStream(), Charset.forName("UTF8"));
        StringBuilder sb=new StringBuilder();
        char[]src="data-post-id".toCharArray();
        char counter=0;
        while(true)
        {
            if((char)in.read()==src[counter])
            {
                counter++;
                if(counter>=src.length)
                    break;
            }else{
            counter=0;
            }
        }
        in.read();in.read();
        char x;
        while((x=(char)in.read())!='\"')
        {
            sb.append(x);
        }
        return sb.toString();
    }
}