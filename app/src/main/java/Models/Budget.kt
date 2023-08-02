package Models

/*class Budget(title: String, description: String, budget: Double, category_id: String, id: String) {
    var id: String = ""
    var title: String = ""
    var description: String = ""
    var budget: Double = 0.00
    var category_id: String = ""

    init{
        this.id = id
        this.title = title
        this.description = description
        this.budget = budget
        this.category_id = category_id
    }
}*/

data class Budget(val id: String, val title: String, val description: String, val value: Double, val categoryId: String)