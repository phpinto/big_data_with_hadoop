# Compiling the WordCount Program:
  
hadoop com.sun.tools.javac.Main Top100Words.java
jar cf top100.jar Top100Words*.class

# Running Top100Words on Charles Dickens' 20 published books
hadoop jar top100.jar Top100Words /input/charles_dickens /output/top_100_charles_dickens

# Running Top100Words with 401 BBC technology news stories
hadoop jar top100.jar Top100Words /input/bbc_tech_news /output/top_100_bbc_tech_news

