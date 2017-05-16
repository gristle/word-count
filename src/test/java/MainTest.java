import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainTest {

    private static Path file;

    @BeforeClass
    public static void init() throws URISyntaxException, IOException {
        URL url = MainTest.class.getClassLoader().getResource("data");
        if (url != null) {
            file = Paths.get(url.toURI());
        } else {
            throw new RuntimeException("Something go wrong with file (");
        }
    }

    @Test
    public void countTest() throws IOException {
        String text = new String(Files.readAllBytes(file), Charset.defaultCharset()).toLowerCase();

        List<Main.WordWrapper> wordWrapperList = Main.countWords(file);

        for (Main.WordWrapper wordWrapper : wordWrapperList) {
            String regex = "\\b" + wordWrapper.getWord() + "\\b";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            int matchCount = 0;
            while (matcher.find()) {
                matchCount++;
            }
            Assert.assertTrue("Doesn't match for word " + wordWrapper.getWord(),
                    wordWrapper.getCounter() == matchCount);
        }
    }

    @Test
    public void sortTest() throws IOException {
        List<Main.WordWrapper> wordWrapperList = Main.sortWords(Main.countWords(file));
        for (int previous = 0, next = 1; next < wordWrapperList.size(); previous++, next++) {
            Main.WordWrapper previousWord = wordWrapperList.get(previous);
            Main.WordWrapper nextWord = wordWrapperList.get(next);
            Assert.assertTrue("Next counter can't be greater then previous " + previousWord + " " + nextWord,
                    previousWord.getCounter() >= nextWord.getCounter() );
            Assert.assertTrue("If counter equals then should be in  alphabetic order " + previousWord + " " + nextWord,
                    previousWord.getCounter() != nextWord.getCounter() ||
                            previousWord.getWord().compareTo(nextWord.getWord()) < 0);
        }
    }


}
