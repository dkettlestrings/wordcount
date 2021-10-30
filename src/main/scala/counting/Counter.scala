package counting

import org.apache.hc.client5.http.async.methods.SimpleHttpRequests
import org.apache.hc.client5.http.impl.async.{CloseableHttpAsyncClient, HttpAsyncClients}

object Counter {

  val httpclient: CloseableHttpAsyncClient  = HttpAsyncClients.createDefault()
  httpclient.start()

  def grabFile(url: String): String = httpclient.execute(SimpleHttpRequests.get(url), null)
    .get
    .getBodyText

  def extractWords(fullText: String): List[String] = fullText.split("\\s+")
    .map(word => word.filter(char => char.isLetter))
    .map(word => word.toLowerCase)
    .filterNot(word => word.isEmpty)
    .toList

  def getTopNWords(words: List[String], wordsToIgnore: Set[String], numberOfWordsToReturn: Int = 50): Set[String] = {

    val toIgnore = wordsToIgnore.map(word => word.toLowerCase)

    // Remove words we don't want and count the number of occurrences of each important word
    val wordCount: Map[String, Int] = words.filterNot(word => toIgnore.contains(word))
      .groupBy(x => x)
      .map({case (word, listOfWord) => (word, listOfWord.length)})

    wordCount.keySet
      .toList
      .sortBy(word => wordCount(word))
      .reverse
      .take(numberOfWordsToReturn)
      .toSet
  }
}
