import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.Sortable
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter


static void main(String[] args) {

    List<User> users = readDataFromUrl();
    List<User> filteredUsers = users.findAll { it.salary > 3500 }

    sortList(filteredUsers)
    writeDataToCsv(filteredUsers)
}

static writeDataToCsv(List<User> users) {
    FileWriter fw = new FileWriter("users.csv")

    CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
            .setHeader("id", "name", "email", "salary")
            .build()

    try (final CSVPrinter printer = new CSVPrinter(fw, csvFormat)) {
        users.forEach(user -> {
            try {
                printer.printRecord(user.id, user.name, user.email, user.salary)
            } catch (IOException ignored) {
                println("CSV creation error")
            }
        })
    }
}

static sortList(List<User> users) {
    users.sort { c1, c2 ->
        c1.name <=> c2.name
    }
}

static List<User> readDataFromUrl() {
    def data = 'https://eltex-co.ru/test/users.php'.toURL().text

    ObjectMapper objectMapper = new ObjectMapper()
    List<User> users = objectMapper.readValue(data.toString(), List<User>.class)
    return users
}

@Sortable(includes = "name")
class User {
    String id
    String name
    String email
    Long salary
}




