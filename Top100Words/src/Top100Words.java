import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Character;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import java.util.Comparator;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Top100Words {

    public static class CountMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private IntWritable word_count = new IntWritable();
        private Text clean_word = new Text();
        String word = new String();
        Map<String, Integer> word_list = new HashMap<String, Integer>();

        @Override
        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word = itr.nextToken();
                String pattern = "[^\\-'A-Za-z]+";
                word = word.replaceAll(pattern, "");
                if (word.length() > 0) {
                    int start = 0;
                    while (start < word.length()) {
                        if (!Character.isLetter(word.charAt(start))) ++start;
                        else break;
                    }
                    int end = Character.isLetter(word.charAt(word.length() - 1)) ? word.length() : word.length() - 1;
                    if (end < start) end = start;
                    word = word.substring(start, end);
                    if (word.length() > 0) {
                        word = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();

                        Integer count = word_list.get(word);

                        if (count == null) word_list.put(word, 1);
                        else word_list.put(word, count + 1);
                    }
                }
            }
        }
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (String key : word_list.keySet()) {
                word_count.set(word_list.get(key));
                clean_word.set(key);
                context.write(clean_word, word_count);
            }
        }
    }


    public static class Top100Reducer
            extends Reducer<Text, IntWritable, Text, Text> {

        public class WordTuple implements Comparable<WordTuple> {

            private int id = 1;
            private String word;
            private int document_count;
            private int total_count;

            WordTuple(String word, int document_count, int total_count) {
                this.word = word;
                this.document_count = document_count;
                this.total_count = total_count;
            }

            public int getDocCount() {
                return this.document_count;
            }

            public int getTotalCount() {
                return this.total_count;
            }

            public String getWord() {
                return this.word;
            }

            @Override
            public int compareTo(WordTuple o) {
                return this.id - o.id;
            }
        }

        private Text final_result = new Text();
        ArrayList<WordTuple> master_list = new ArrayList<>();

        @Override
        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int doc_count = 0;
            int total_count = 0;

            for (IntWritable val : values) {
                total_count += val.get();
                doc_count++;
            }


            String[] common_words = {"The", "Of", "And", "A", "To", "In", "Is", "You", "That", "It", "He", "Was",
                    "For", "On", "Are", "As", "With", "His", "They", "I", "At", "Be", "This", "Have", "From", "Or",
                    "One", "Had", "By", "Word", "But", "Not", "What", "All", "Were", "We", "When", "Your", "Can",
                    "Said", "There", "Use", "An", "Each", "Which", "She", "Do", "How", "Their", "If", "Will", "Up",
                    "Other", "About", "Out", "Many", "Then", "Them", "These", "So", "Some", "Her", "Would", "Make",
                    "Like", "Him", "Into", "Time", "Has", "Look", "Two", "More", "Write", "Go", "See", "Number", "No",
                    "Way", "Could", "People", "My", "Than", "First", "Water", "Been", "Call", "Who", "Oil", "Its",
                    "Now", "Find", "Long", "Down", "Day", "Did", "Get", "Come", "Made", "May", "Part"};

            Set<String> common_words_set = new HashSet<>(Arrays.asList(common_words));

            if (!common_words_set.contains(key.toString())) master_list.add(new WordTuple(key.toString(), doc_count, total_count));
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {

            Comparator<WordTuple> comparison = Comparator
                    .comparing(WordTuple::getDocCount)
                    .thenComparing(WordTuple::getTotalCount)
                    .thenComparing(WordTuple::getWord);

            master_list.sort(comparison.reversed());

            int counter = 0;

            for (int i = 0; i < master_list.size(); i++) {
                if (counter++ > 99) break;
                String word = master_list.get(i).getWord();
                int occ = master_list.get(i).getDocCount();
                int tot = master_list.get(i).getTotalCount();
                final_result.set(Integer.toString(occ) + "\t" + Integer.toString(tot));
                context.write(new Text(word), final_result);
            }
        }
    }

        public static void main(String[] args) throws Exception {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf, "top 100 words");
            job.setJarByClass(Top100Words.class);
            job.setMapperClass(CountMapper.class);
            job.setReducerClass(Top100Reducer.class);
            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));
            System.exit(job.waitForCompletion(true) ? 0 : 1);
        }
}