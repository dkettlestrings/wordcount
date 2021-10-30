package counting

import Counter._
import org.scalatest.{FreeSpec, Matchers}

class CounterSpec extends FreeSpec with Matchers {

  "Counter should" - {

    "grab files from the internet" in {

      val mobyDick = grabFile("http://www.gutenberg.org/files/2701/2701-0.txt")

      mobyDick.length shouldBe 1276235

      val exampleDotCom = grabFile("http://example.com")

      exampleDotCom.length shouldBe 1256
    }

    "extract words" in {

      val taleOfTwoCitiesWords = extractWords("It was the best of times, it was the worst of times")

      taleOfTwoCitiesWords shouldBe List("it", "was", "the", "best", "of", "times", "it", "was", "the", "worst", "of", "times")


      val mobyDickWords = extractWords(grabFile("http://www.gutenberg.org/files/2701/2701-0.txt"))

      mobyDickWords.length shouldBe 215436
    }

    "grab the most frequent words" in {

      val sillyWords = getTopNWords(Set(), 2, extractWords("a a a a b b b c c "))

      sillyWords shouldBe Set("a", "b")

      val topMobyDickWords = getTopNWords(Set(), 5, extractWords(grabFile("http://www.gutenberg.org/files/2701/2701-0.txt")))

      topMobyDickWords shouldBe Set("a", "to", "of", "and", "the")
    }
  }

}
