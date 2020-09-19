# Compiling the WordCount Program:
  
hadoop com.sun.tools.javac.Main Top100Words.java
jar cf top100.jar Top100Words*.class

# Running Top100Words on Charles Dickens' 20 published books
hadoop jar top100.jar Top100Words /input/charles_dickens /output/top_100_charles_dickens

# Running Top100Words with 401 BBC technology news stories
hadoop jar top100.jar Top100Words /input/bbc_tech_news /output/top_100_bbc_tech_news

# Running Top100Words on the Pop song lyrics
hadoop jar top100.jar Top100Words /input/song_lyrics/pop /output/top_100_pop

# Running Top100Words on the Rock song lyrics
hadoop jar top100.jar Top100Words /input/song_lyrics/rock /output/top_100_rock

# Running Top100Words on the Folk/Country song lyrics
hadoop jar top100.jar Top100Words /input/song_lyrics/folk-country /output/top_100_folk-country

# Running Top100Words on the Hip-Hop song lyrics
hadoop jar top100.jar Top100Words /input/song_lyrics/hip-hop /output/top_100_hip-hop
