package umsl.edu;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.HashSet;
public class WebCrawler
{

    private HashSet<String> links;

    public WebCrawler() {
        links = new HashSet< >();
    }

    public void getPageLinks(String URL) //goes through a link to find other links within it and repeat with each new link
    {

        if (!links.contains(URL)&&links.size()<1000) {
            try {
                Document document = Jsoup.connect(URL).get();
                String title = document.title();
                Element body = document.body();

                try{
                    Thread.sleep(100);
                }
                catch(InterruptedException e)
                {

                }
                if (links.add(URL)) {
                    System.out.println("Title: "+title+"\tLink: "+URL+ " Current size is: "+ links.size());
                    countWords(body.text());
                    System.out.println();
                }

                Elements linksOnPage = document.select("a[href]");

                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }

    }

    public void countWords(String input) //logs in each word and the number of times it appears in each link.
    {
        Map <String, String> map = new HashMap < > ();

        if (input != null) {
            String[] separatedWords = input.split(" ");
            for (String str: separatedWords) {
                if (map.containsKey(str)) {
                    int count = Integer.parseInt(map.get(str));
                    map.put(str, String.valueOf(count + 1));
                } else {
                    map.put(str, "1");
                }
            }
        }

        System.out.println("Words and occurrences : " + map);
    }


    public static void main(String[] args) {

        new WebCrawler().getPageLinks("https://en.wikipedia.org/wiki/Vietnam_War");
    }
}
