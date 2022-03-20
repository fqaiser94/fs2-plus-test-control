package fq

import cats.effect._
import cats.effect.testkit.TestControl
import cats.implicits._
import fs2.Stream
import munit.CatsEffectSuite

import scala.concurrent.duration._

class HelloSuite extends CatsEffectSuite {
  test("return hello every 5 seconds for 2 iterations") {
    val helloStream: Stream[IO, String] =
      Stream.constant("hello").covary[IO].metered(5.second).take(2)

    val expected = Vector(
      5.second -> "hello",
      10.second -> "hello"
    )

    assertStreamOutputsWithTime(helloStream, expected)
  }

  def assertStreamOutputsWithTime[A](
      stream: Stream[IO, A],
      expected: Vector[(FiniteDuration, A)]
  ): IO[Unit] = {
    val streamWithTime: Stream[IO, (FiniteDuration, A)] =
      Stream.eval(IO.monotonic).flatMap { t0 =>
        stream.evalMap(IO.monotonic.map(t1 => t1 - t0).tupleRight)
      }

    val ioProgram: IO[Vector[(FiniteDuration, A)]] = streamWithTime.compile.toVector

    // ioProgram.assertEquals(expected) // <- uses default `IORuntime`
    TestControl.executeEmbed(ioProgram).assertEquals(expected) // <- uses mock IO runtime
  }
}
