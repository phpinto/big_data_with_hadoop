# Exporing input files to HDFS

hadoop fs -put ../input /

# Compiling the WordCount Program:

hadoop com.sun.tools.javac.Main WordCount.java
jar cf wc.jar WordCount*.class

# Running WordCount with the lyrics of American Pie by Don McLean
hadoop jar wc.jar WordCount /input/american_pie.txt /output_word_count_american_pie

# Running WordCount with the book Hamlet by William Shakespeare
hadoop jar wc.jar WordCount /input/hamlet.txt /output_word_count_hamlet

# Running WordCount with hundreds of BBC technology news stories
hadoop jar wc.jar WordCount /input/bbc_tech_news /output_word_count_bbc_tech_news
