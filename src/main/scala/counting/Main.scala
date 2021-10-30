package counting

import Counter._

object Main {

  def main(args: Array[String]): Unit = {

    val url = "http://www.gutenberg.org/files/2701/2701-0.txt"

    val wordsToIgnore = Set("the", "of", "to", "and", "a", "in", "is", "it",
      "you", "that", "he", "was", "for", "on", "are", "with", "as", "I", "his",
      "they", "be", "at", "one", "have", "this", "from", "or", "had", "by",
      "not", "word", "but", "what", "some", "we", "can", "out", "other", "were",
      "all", "there", "when", "up", "use", "your", "how", "said", "an", "each",
      "she")

    val numberOfWordsToReturn = 50

    val topWords: List[String] => Set[String] = getTopNWords(wordsToIgnore, numberOfWordsToReturn, _)

    val pipeline: String => Set[String] = grabFile _ andThen extractWords andThen topWords

    pipeline(url).foreach(println)
  }
}
