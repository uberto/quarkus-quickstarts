import com.acme.model.Fruit
import com.acme.model.toFruit
import com.acme.model.toJson
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import javax.json.Json
import java.io.StringWriter


@QuarkusTest
open class FruitTests {

    private val appleJson = """{"id":123,"name":"apple"}"""

    private val fruitsJson = """[{"id":32,"name":"apple"},{"id":33,"name":"pear"},{"id":34,"name":"banana"}]"""

    @Test
    fun `test single fruit to json conversion`() {

        val apple = Fruit(123, "apple")

        assertEquals(appleJson, apple.toJson())


    }

    @Test
    fun `test list of fruits to json conversion`() {

        val apple = Fruit(32, "apple")
        val pear = Fruit(33, "pear")
        val banana = Fruit(34, "banana")

        val fruits = listOf(apple, pear, banana)
        assertEquals(fruitsJson, fruits.toJson())


    }

    @Test
    fun `test json to fruit conversion`() {

        val apple = appleJson.toFruit()

        assertEquals(Fruit(123, "apple"), apple)

    }

}