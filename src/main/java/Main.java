

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    private static final String SPLIT_REG_EXP = "[\\s\\t\\n.,]+";

    public static void main(String[] args) throws URISyntaxException, IOException {
        if (args.length < 2) {
            throw new RuntimeException("Filename and Number of words are mandatory parameters");
        }

        Path file = Paths.get(args[0]);

        List<WordWrapper> wordWrapperList = countWords(file);

        List<WordWrapper> sortedList = sortWords(wordWrapperList);

        int n = Integer.valueOf(args[1]);
        sortedList.subList(0, n >= sortedList.size() ? sortedList.size() : n).forEach(System.out::println);
    }

    static List<WordWrapper> countWords(Path file) throws IOException {
        String[] words = obtainWordsArray(file);

        List<WordWrapper> wordList = new ArrayList<>();
        for (String word : words) {
            Optional<WordWrapper> searchedWord =
                    wordList.stream().filter(wordWrapper -> word.equals(wordWrapper.getWord())).findFirst();
            if (searchedWord.isPresent()) {
                searchedWord.get().increment();
            } else {
                wordList.add(new WordWrapper(word, 1));
            }
        }
        return wordList;
    }

    static List<WordWrapper> sortWords(List<WordWrapper> unsortedList) {
        Comparator<WordWrapper> valueKeyComparator =
                Comparator.comparing(WordWrapper::getCounter).reversed().thenComparing(WordWrapper::getWord);
        return unsortedList.stream().sorted(valueKeyComparator).collect(Collectors.toList());
    }

    private static String[] obtainWordsArray(Path file) throws IOException {
        String text = new String(Files.readAllBytes(file), Charset.defaultCharset()).toLowerCase();
        return text.split(SPLIT_REG_EXP);
    }

    static class WordWrapper {

        private String word;
        private int counter;

        WordWrapper(String word, int counter) {
            this.word = word;
            this.counter = counter;
        }

        String getWord() {
            return word;
        }

        void setWord(String word) {
            this.word = word;
        }

        int getCounter() {
            return counter;
        }

        void setCounter(int counter) {
            this.counter = counter;
        }

        @Override
        public String toString() {
            return word + " = " + counter;
        }

        void increment() {
            counter++;
        }
    }

}
