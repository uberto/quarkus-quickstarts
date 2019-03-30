import com.acme.http.FruitHandler
import com.acme.model.FruitRepository
import org.http4k.core.then
import org.http4k.filter.ResponseFilters
import org.http4k.servlet.HttpHandlerServlet
import javax.servlet.Servlet

private val reportHttpTransaction = ResponseFilters.ReportHttpTransaction {
    println("${it.request.uri} responded with ${it.response.status} in ${it.duration.toMillis()}ms")
}

private val app = FruitHandler(FruitRepository().apply {
    addFruit("Cherry")
    addFruit("Apple")
    addFruit("Banana")
})

class RestServlet : Servlet by HttpHandlerServlet(
    reportHttpTransaction.then(app)
)