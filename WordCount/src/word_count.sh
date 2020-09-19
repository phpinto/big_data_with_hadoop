# Compiling the WordCount Program:

hadoop com.sun.tools.javac.Main WordCount.java
jar cf wc.jar WordCount*.class

# Running WordCount with the lyrics of American Pie by Don McLean
hadoop jar wc.jar WordCount /input/american_pie.txt /output/word_count_american_pie

# Running WordCount with the book Hamlet by William Shakespeare
hadoop jar wc.jar WordCount /input/hamlet.txt /output/word_count_hamlet

# Running WordCount on Charles Dickens' 20 published books
hadoop jar wc.jar WordCount /input/charles_dickens /output/word_count_charles_dickens

# Running WordCount with 401 BBC technology news stories
hadoop jar wc.jar WordCount /input/bbc_tech_news /output/word_count_bbc_tech_news

# Running WordCount on the Song Lyrics Dataset
hadoop jar wc.jar WordCount /input/song_lyrics/all_genres /output/word_count_song_lyrics
